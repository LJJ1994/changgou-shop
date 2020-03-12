package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
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
    private OrderService orderService;

    @RabbitListener(queues = "order_pay")
    public void updateOrderStatus(String message){
        System.out.println("接收到订单状态消息："+message);
        Map map = JSON.parseObject( message, Map.class );
        orderService.updatePayStatus( (String)map.get("orderId"), (String)map.get("transactionId") );
    }
}
