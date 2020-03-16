package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.config.TokenDecode;
import com.changgou.seckill.service.SeckillOrderService;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 21:31:31
 * @Modified By:
 */
@Component
public class OrderPayListener {
    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private TokenDecode tokenDecode;

    @RabbitListener(queues = "order_pay")
    public void updateOrderStatus(String message){
        System.out.println("接收到秒杀订单状态消息："+message);
        try {
            Map map = JSON.parseObject(message, Map.class);
            String orderId = (String) map.get("orderId");
            String transactionId = (String) map.get("transactionId");
            String username = tokenDecode.getUserInfo().get("username");
            seckillOrderService.updatePayStatus(orderId, transactionId, username);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("执行更新订单状态失败!");
        }
    }
}
