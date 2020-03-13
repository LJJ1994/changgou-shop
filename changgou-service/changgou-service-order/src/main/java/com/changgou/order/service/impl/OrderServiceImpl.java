package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.changgou.entity.IdWorker;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.*;
import com.changgou.order.pojo.*;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.WxPayFeign;
import com.changgou.user.feign.UserFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * @Author:LJJ
 * @Description: Order业务层接口实现类
 * @Date 2020/3/3 10:18
 *****/

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private OrderConfigMapper orderConfigMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private WxPayFeign payFeign;

    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order){
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public Example createExample(Order order){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(order!=null){
            // 订单id
            if(!StringUtils.isEmpty(order.getId())){
                    criteria.andEqualTo("id",order.getId());
            }
            // 数量合计
            if(!StringUtils.isEmpty(order.getTotalNum())){
                    criteria.andEqualTo("totalNum",order.getTotalNum());
            }
            // 金额合计
            if(!StringUtils.isEmpty(order.getTotalMoney())){
                    criteria.andEqualTo("totalMoney",order.getTotalMoney());
            }
            // 优惠金额
            if(!StringUtils.isEmpty(order.getPreMoney())){
                    criteria.andEqualTo("preMoney",order.getPreMoney());
            }
            // 邮费
            if(!StringUtils.isEmpty(order.getPostFee())){
                    criteria.andEqualTo("postFee",order.getPostFee());
            }
            // 实付金额
            if(!StringUtils.isEmpty(order.getPayMoney())){
                    criteria.andEqualTo("payMoney",order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if(!StringUtils.isEmpty(order.getPayType())){
                    criteria.andEqualTo("payType",order.getPayType());
            }
            // 订单创建时间
            if(!StringUtils.isEmpty(order.getCreateTime())){
                    criteria.andEqualTo("createTime",order.getCreateTime());
            }
            // 订单更新时间
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                    criteria.andEqualTo("updateTime",order.getUpdateTime());
            }
            // 付款时间
            if(!StringUtils.isEmpty(order.getPayTime())){
                    criteria.andEqualTo("payTime",order.getPayTime());
            }
            // 发货时间
            if(!StringUtils.isEmpty(order.getConsignTime())){
                    criteria.andEqualTo("consignTime",order.getConsignTime());
            }
            // 交易完成时间
            if(!StringUtils.isEmpty(order.getEndTime())){
                    criteria.andEqualTo("endTime",order.getEndTime());
            }
            // 交易关闭时间
            if(!StringUtils.isEmpty(order.getCloseTime())){
                    criteria.andEqualTo("closeTime",order.getCloseTime());
            }
            // 物流名称
            if(!StringUtils.isEmpty(order.getShippingName())){
                    criteria.andEqualTo("shippingName",order.getShippingName());
            }
            // 物流单号
            if(!StringUtils.isEmpty(order.getShippingCode())){
                    criteria.andEqualTo("shippingCode",order.getShippingCode());
            }
            // 用户名称
            if(!StringUtils.isEmpty(order.getUsername())){
                    criteria.andLike("username","%"+order.getUsername()+"%");
            }
            // 买家留言
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                    criteria.andEqualTo("buyerMessage",order.getBuyerMessage());
            }
            // 是否评价
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                    criteria.andEqualTo("buyerRate",order.getBuyerRate());
            }
            // 收货人
            if(!StringUtils.isEmpty(order.getReceiverContact())){
                    criteria.andEqualTo("receiverContact",order.getReceiverContact());
            }
            // 收货人手机
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",order.getReceiverMobile());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(order.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(!StringUtils.isEmpty(order.getSourceType())){
                    criteria.andEqualTo("sourceType",order.getSourceType());
            }
            // 交易流水号
            if(!StringUtils.isEmpty(order.getTransactionId())){
                    criteria.andEqualTo("transactionId",order.getTransactionId());
            }
            // 订单状态 
            if(!StringUtils.isEmpty(order.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",order.getOrderStatus());
            }
            // 支付状态 0:未支付 1:已支付
            if(!StringUtils.isEmpty(order.getPayStatus())){
                    criteria.andEqualTo("payStatus",order.getPayStatus());
            }
            // 发货状态 0:未发货 1:已发货 2:已送达
            if(!StringUtils.isEmpty(order.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",order.getConsignStatus());
            }
            // 是否删除
            if(!StringUtils.isEmpty(order.getIsDelete())){
                    criteria.andEqualTo("isDelete",order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 增加Order
     * @param order
     */
    @Override
    @GlobalTransactional(name="order_add")
    public String add(Order order){
        // 1) 获取所有购物项
        Map map = cartService.list(order.getUsername());
        List<OrderItem> orderItemList = (List<OrderItem>)map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");
        String orderId = String.valueOf(idWorker.nextId());
        //添加订单
        order.setId(orderId);
        order.setTotalMoney(totalMoney);
        order.setTotalNum(totalNum);
        order.setPayMoney(totalMoney);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        int count = orderMapper.insertSelective(order);

        //添加订单明细
        for(OrderItem item : orderItemList){
            item.setId(String.valueOf(idWorker.nextId()));
            item.setIsReturn("0"); //是否退货
            item.setOrderId(order.getId());
            orderItemMapper.insertSelective(item);
        }
        //库存减库存
        skuFeign.decrCount(order.getUsername());
        // 增加用户积分
        Integer points = 10;
        userFeign.addPoints(points);
        // int i = 10/0;
        // 添加任务表记录
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);

        Map msgMap = new HashMap();
        msgMap.put("username", order.getUsername());
        msgMap.put("orderId", order.getId());
        msgMap.put("point", order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(msgMap));
        taskMapper.insertSelective(task);

        // 向mq 延迟队列发送消息
        String orderExpired = "60000"; // 订单超时的时间间隔(60秒)
        rabbitTemplate.convertAndSend("ordercreate", (Object) orderId, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //延迟读取
                message.getMessageProperties().setExpiration(orderExpired);
                return message;
            }
        });
        System.out.println("向延迟队列发送消息: " + orderId);

        //清除redis缓存数据
        redisTemplate.delete("Cart_" + order.getUsername());

        return orderId;
    }

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 修改订单状态为已支付
     * @param orderId
     * @param transactionId
     */
    @Override
    @Transactional
    public void updatePayStatus(String orderId, String transactionId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null && "0".equals(order.getPayStatus())){
            //2.修改订单的支付状态
            order.setPayStatus("1");
            order.setOrderStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            order.setTransactionId(transactionId); //微信返回的交易流水号
            orderMapper.updateByPrimaryKeySelective(order);

            //3.记录订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("1");
            orderLog.setPayStatus("1");
            orderLog.setRemarks("交易流水号:"+transactionId);
            orderLog.setOrderId(orderId);
            orderLogMapper.insert(orderLog);
        }
    }

    /**
     * 关闭订单
     * @param orderId
     */
    @Override
    @Transactional
    public void closeOrder(String orderId) {
        /**
         * 1.根据订单id查询mysql中的订单信息,判断订单是否存在,判断订单的支付状态
         * 2. 基于微信查询订单信息(微信)
         * 2.1)如果当前订单的支付状态为已支付,则进行数据补偿(mysql)
         * 2.2)如果当前订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
         */
        System.out.println("关闭订单业务开启:"+orderId);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            throw new RuntimeException("订单不存在!");
        }
        if (!"0".equals(order.getPayStatus())){
            System.out.println("当前订单不需要关闭");
            return;
        }
        System.out.println("关闭订单校验通过:"+orderId);

        //基于微信查询订单信息
        Map wxQueryMap = (Map) payFeign.queryOrder(orderId).getData();
        System.out.println("查询微信支付订单:"+wxQueryMap);

        //如果订单的支付状态为已支付,进行数据补偿(mysql)
        if ("SUCCESS".equals(wxQueryMap.get("trade_state"))){
            this.updatePayStatus(orderId,(String) wxQueryMap.get("transaction_id"));
            System.out.println("完成数据补偿");
        }

        //如果订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
        if ("NOTPAY".equals(wxQueryMap.get("trade_state"))){
            System.out.println("执行关闭");
            order.setUpdateTime(new Date());
            order.setOrderStatus("4"); //订单已关闭
            orderMapper.updateByPrimaryKeySelective(order);

            //新增订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("4");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);

            //恢复商品的库存
            OrderItem one = new OrderItem();
            one.setOrderId(orderId);
            List<OrderItem> orderItemList = orderItemMapper.select(one);

            for (OrderItem orderItem : orderItemList) {
                skuFeign.resumeStockNum(orderItem.getSkuId(),orderItem.getNum());
            }

            //基于微信关闭订单
            payFeign.closeOrder(orderId);

        }
    }

    /**
     * 批量发货
     * @param orders
     */
    @Override
    @Transactional
    public void batchSend(List<Order> orders) {
        //1.判断运单号和物流公司是否为空
        for(Order order : orders){
            if(order.getShippingCode() == null || order.getShippingName() == null){
                throw new RuntimeException("请填写运单号或物流公司");
            }
        }

        //2. 校验订单状态
        for (Order order : orders){
            Order one = orderMapper.selectByPrimaryKey(order.getId());
            // 不是未发货状态 或 订单未支付
            if(!one.getConsignStatus().equals("0") || !one.getOrderStatus().equals("1")){
                throw new RuntimeException("订单状态有误!");
            }
        }

        //3.循环更新订单状态
        for(Order order : orders){
            order.setConsignStatus("1"); // 发货状态：已发货
            order.setOrderStatus("2"); // 订单状态：已发货
            order.setConsignTime(new Date()); //发货时间
            order.setUpdateTime(new Date()); //更新时间
            orderMapper.updateByPrimaryKeySelective(order);

            // 记录订单变动记录
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId() + "");
            orderLog.setOperater("admin");
            orderLog.setConsignStatus("1");
            orderLog.setOrderStatus("2");
            orderLog.setOperateTime(new Date());
            orderLog.setPayStatus("1");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insertSelective(orderLog);
        }
        //4.设置第三方物流信息..
    }

    /**
     * 确认收货
     * @param orderId 订单id
     * @param operator 操作人
     */
    @Override
    @Transactional
    public void confirmTask(String orderId, String operator) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order == null){
            throw new RuntimeException("订单不存在!");
        }
        if(!"1".equals(order.getConsignStatus())){
            throw new RuntimeException("订单未发货！");
        }
        // 更改订单状态
        order.setOrderStatus("3"); // 已完成
        order.setConsignStatus("2"); // 发货状态：已送达
        order.setUpdateTime(new Date());//a更新时间
        order.setEndTime(new Date());//交易结束时间
        orderMapper.updateByPrimaryKeySelective(order);

        // 订单日志
        OrderLog orderLog=new OrderLog();
        orderLog.setId(idWorker.nextId()+"");
        orderLog.setOperateTime(new Date());//当前日期
        orderLog.setOperater(operator);//系统, 管理员, 用户
        orderLog.setOrderStatus("3");
        orderLog.setOrderId(order.getId());
        orderLogMapper.insertSelective(orderLog);
    }

    /**
     * 自动确认收货
     */
    @Override
    @Transactional
    public void autoTack() {
        /**
         * 1.从订单配置表中获取到订单自动确认的时间点
         * 2. 得到当前时间节点,向前数 ( 订单自动确认的时间节点 ) 天,作为过期的时间节点
         * 3. 从订单表中获取相关符合条件的数据 (发货时间小于过期时间,收货状态为未确认 )
         * 4.循环遍历,执行确认收货
         */
        OrderConfig orderConfig = orderConfigMapper.selectByPrimaryKey(1);
        LocalDate localDate = LocalDate.now();
        // 获取过期时间点
        LocalDate date = localDate.plusDays(-orderConfig.getTakeTimeout());
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();

        // 发货时间小于过期时间点的并且状态为未收货
        criteria.andLessThan("consignTime", date);
        criteria.andEqualTo("orderStatus", "2"); // 已发货
        List<Order> orderList = orderMapper.selectByExample(example);

        for(Order order : orderList){
            this.confirmTask(order.getId(), "system");
        }
    }
}
