package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.SeckillStatus;
import com.changgou.pay.feign.WxPayFeign;
import com.changgou.seckill.config.RabbitMQConfig;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description: 订单超时监听
 * @Create: 2020-03-13 08:36:36
 * @Modified By:
 */
@Component
public class OrderTimeoutListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private WxPayFeign wxPayFeign;

    /**
     * 该方法监听订单超时/用户未支付/支付失败等因素
     * 是在响应的订单超时时间后监听的消息
     * @param message SeckillStatus
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_TIMEOUT_QUEUE)
    public void closeOrder(String message){
        System.out.println("监听到订单超时消息：" + message);
        //获取消息
        try {
            SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);
            this.rollbackOrder(seckillStatus);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 订单回滚操作
     * @param seckillStatus
     */
    public void rollbackOrder(SeckillStatus seckillStatus){
        //获取redis中订单信息
        String username = seckillStatus.getUsername();
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        //如果redis中有订单，说明用户未支付
        if(seckillOrder != null){
            //关闭微信支付订单
            Result result = wxPayFeign.closeOrder(String.valueOf(seckillStatus.getOrderId()));
            Map<String, String> map = (Map) result.getData();
            if(map.get("return_code").equalsIgnoreCase("success") &&
                    map.get("result_code").equalsIgnoreCase("success")){
                // 删除订单
                redisTemplate.boundHashOps("SeckillOrder").delete(username);
                // 回滚库存
                //1)从redis中获取该商品
                SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(seckillStatus.getGoodsId());
                //2) 如果redis中没有，从数据库中获取
                if (seckillGoods == null) {
                    seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
                }
                //3) 数量+1
                Long count = redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillStatus.getGoodsId(), 1);
                seckillGoods.setStockCount(count.intValue());
                redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).leftPush(seckillStatus.getGoodsId());
                //4) 数据同步redis
                redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).put(seckillStatus.getGoodsId(), seckillGoods);

                //5) 清理排队队列
                redisTemplate.boundHashOps("UserQueueCount").delete(username);
                //6) 清理抢单标识
                redisTemplate.boundHashOps("UserQueueStatus").delete(username);
            }
        }
    }

}
