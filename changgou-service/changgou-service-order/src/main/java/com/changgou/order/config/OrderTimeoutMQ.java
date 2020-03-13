package com.changgou.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description: 订单超时MQ配置
 * @Create: 2020-03-13 08:02:02
 * @Modified By:
 */
@Configuration
public class OrderTimeoutMQ {
    public static final String EX_ORDER_TIMEOUT = "ex_order_timeout";// 死信交换机
    public static final String ORDER_TIMEOUT_QUEUE = "ordertimeout"; // 死信队列
    public static final String ORDER_CREATE_QUEUE = "ordercreate"; // 延迟队列

    //声明交换机
    @Bean(EX_ORDER_TIMEOUT)
    public Exchange EX_ORDER_TIMEOUT(){
        return ExchangeBuilder.directExchange(EX_ORDER_TIMEOUT).durable(true).build();
    }

    /**
     * 定义死信队列，接收延迟队列发送来的消息
     */
    @Bean(ORDER_TIMEOUT_QUEUE)
    public Queue ORDER_TIMEOUT_QUEUE(){
        //延时队列中的消息过期了，会自动触发消息的转发，通过指定routing-key发送到指定exchange中
        return new Queue(ORDER_TIMEOUT_QUEUE, true);
    }

    /**
     * 定义订单创建队列, 该消息未被消费，会被转发到死信队列
     */
    @Bean(ORDER_CREATE_QUEUE)
    public Queue ORDER_CREATE_QUEUE(){
        return QueueBuilder
                .durable(ORDER_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", EX_ORDER_TIMEOUT)
                .withArgument("x-dead-letter-routing-key", ORDER_TIMEOUT_QUEUE)
                .build();
    }

    /**
     * 绑定死信队列和交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingQueue(@Qualifier(ORDER_TIMEOUT_QUEUE) Queue queue,
                           @Qualifier(EX_ORDER_TIMEOUT) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_TIMEOUT_QUEUE).noargs();
    }
}
