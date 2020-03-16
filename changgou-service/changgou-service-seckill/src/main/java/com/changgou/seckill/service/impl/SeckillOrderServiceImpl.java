package com.changgou.seckill.service.impl;

import com.changgou.entity.SeckillStatus;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.task.MultiThreadingCreateOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:LJJ
 * @Description: SeckillOrder业务层接口实现类
 * @Date 2020/3/3 10:18
 *****/

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            // 主键
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            // 秒杀商品ID
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            // 支付金额
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            // 用户
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            // 商家
            if(!StringUtils.isEmpty(seckillOrder.getSellerId())){
                    criteria.andEqualTo("sellerId",seckillOrder.getSellerId());
            }
            // 创建时间
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            // 支付时间
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            // 收货人
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            // 交易流水
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /***
     * 抢单状态查询
     * @param username
     */
    @Override
    public SeckillStatus queryStatus(String username){
        return (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
    }

    /**
     * 更新订单状态
     * @param out_trade_no
     * @param transactionId
     * @param username
     */
    @Override
    public void updatePayStatus(String out_trade_no, String transactionId, String username) {
        // 从redis查询订单数据
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        if(seckillOrder != null){
            seckillOrder.setStatus("1"); //支付状态，0未支付，1已支付
            seckillOrder.setPayTime(new Date()); //支付时间
            seckillOrderMapper.insertSelective(seckillOrder); //同步到MySQL

            //清空redis缓存
            redisTemplate.boundHashOps("SeckillOrder").delete(username);
            //清空排队信息
            redisTemplate.boundHashOps("UserQueueCount").delete(username);
            //清空抢单标识
            redisTemplate.boundHashOps("UserQueueStatus").delete(username);
        }
    }

    /**
     * 下单
     * @param id
     * @param time
     * @param username
     */
    @Override
    public void add(Long id, String time, String username){
        //判断是否有库存
        Long size = redisTemplate.boundListOps("SeckillGoodsCountList_" + id).size();
        if(size == null || size <=0 ){
            //没有库存
            throw new RuntimeException("101");
        }
        /**
         * 防止重复抢单
         * 1.用户每次抢单的时候，一旦排队，我们设置一个自增值，让该值的初始值为1
         * 2.每次进入抢单的时候，对它进行递增，如果值>1，则表明已经排队,不允许重复排队
         * 3.则对外抛出异常，并抛出异常信息100 表示已经正在排队。
         */
        Long count = redisTemplate.boundHashOps("UserQueueCount").increment(username, 1);
        if(count == null || count > 1){
            throw new RuntimeException("100");
        }
        //排队信息封装
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
        //将秒杀抢单信息存入到Redis中,这里采用List方式存储,List本身是一个队列
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);
        ////将抢单状态存入到Redis中
        redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
        multiThreadingCreateOrder.createOrder();
    }

    /**
     * 根据ID查询SeckillOrder
     * @param username
     * @return
     */
    @Override
    public SeckillOrder findById(String username){
        return (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
    }

    /**
     * 查询SeckillOrder全部数据
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
}
