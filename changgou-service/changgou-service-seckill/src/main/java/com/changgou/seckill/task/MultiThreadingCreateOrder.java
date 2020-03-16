package com.changgou.seckill.task;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.IdWorker;
import com.changgou.entity.SeckillStatus;
import com.changgou.seckill.config.RabbitMQConfig;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description: 多线程下单
 * @Create: 2020-03-15 12:32:32
 * @Modified By:
 */
@Component
public class MultiThreadingCreateOrder {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 异步下单
     */
    @Async
    public void createOrder(){
        try {
            System.out.println("准备多线程下单..");
            Thread.sleep(10000);
            System.out.println("开始下单");
            // 多线程每次从队列中获取数据，分别获取用户名和订单商品编号以及商品秒杀时间段，进行下单操作
            SeckillStatus seckillStatus = (SeckillStatus)redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
            if(seckillStatus == null){
                return;
            }
            //从队列中获取一个商品
            Object sgoods = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
            if(sgoods == null){
                // 清理当前用户信息
                this.clearQueue(seckillStatus);
                return;
            }
            Long id = seckillStatus.getGoodsId();
            String  username = seckillStatus.getUsername();
            String time = seckillStatus.getTime();
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);
            // 如果该商品库存为0或不存在
            if(seckillGoods == null || seckillGoods.getStockCount() <= 0){
                throw new RuntimeException("商品售罄!");
            }
            // 创建秒杀订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setMoney(new BigDecimal(seckillGoods.getCostPrice()));
            seckillOrder.setStatus("0");
            seckillOrder.setSellerId(seckillGoods.getSellerId());
            seckillOrder.setUserId(username);
            seckillOrder.setSeckillId(id);
            //秒杀订单存入redis
            redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);
            //库存减少
            Long seckillGoodsCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(id, -1);//商品数量递减
            seckillGoods.setStockCount(seckillGoodsCount.intValue());

            //判断redis库存信息
            if(seckillGoods.getStockCount() <= 0){
                //如果为0，则同步数据到MySQL
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //清除redis中该商品信息
                redisTemplate.boundHashOps("SeckillGoods_" + time).delete(id);
            } else {
                //更新redis商品库存
                redisTemplate.boundHashOps("SeckillGoods_" + time).put(id, seckillGoods);
                // 抢单成功，更新redis抢单状态, 排队--->等待支付
                seckillStatus.setStatus(2);
                seckillStatus.setOrderId(seckillOrder.getId());
                seckillStatus.setMoney(seckillOrder.getMoney().floatValue());
                redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
                //发送MQ消息
                this.sendDelayMessage(seckillStatus);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清理用户排队信息
     * @param seckillStatus
     */
    private void clearQueue(SeckillStatus seckillStatus) {
        //清理排队信息
        redisTemplate.boundHashOps("UserQueueCount").delete(seckillStatus.getUsername());
        //清理抢单信息
        redisTemplate.boundHashOps("UserQueueStatus").delete(seckillStatus.getUsername());
    }

    /**
     * 下单时，发送延时消息到MQ， 订单有效期为30分钟，测试为20秒
     * @param seckillStatus
     */
    public void sendDelayMessage(SeckillStatus seckillStatus){
        String jsonString = JSON.toJSONString(seckillStatus);
        // 向mq 延迟队列发送消息
        String orderExpired = "600000"; // 测试订单超时的时间间隔(600秒)
        rabbitTemplate.convertAndSend("seckillorder", (Object) jsonString, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //延迟读取
                message.getMessageProperties().setExpiration(orderExpired);
                return message;
            }
        });
    }
}
