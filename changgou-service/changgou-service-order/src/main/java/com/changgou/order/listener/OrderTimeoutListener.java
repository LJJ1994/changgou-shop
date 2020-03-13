package com.changgou.order.listener;

import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private OrderService orderService;

    @RabbitListener(queues = "ordertimeout")
    public void closeOrder(String message){
        System.out.println("监听到订单超时消息：" + message);
        try {
            orderService.closeOrder(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
