package com.changgou.pay.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_PAY = "order_pay";

    public static final String EX_PAY_NOTIFY = "paynotify";

    @Bean
    public Queue queue(){
        return  new Queue(ORDER_PAY);
    }

    //声明交换机
    @Bean(EX_PAY_NOTIFY)
    public Exchange EX_BUYING_ADDPOINTUSER(){
        return ExchangeBuilder.fanoutExchange(EX_PAY_NOTIFY).durable(true).build();
    }
}
