package com.changgou.order.listener;

import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-13 11:51:51
 * @Modified By:
 */
@Component
public class OrderTackListener {
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "order_tack")
    public void autoTack(String message){
        System.out.println("收到自动确认收货消息: " + message);
        orderService.autoTack(); //自动确认收货
    }
}
