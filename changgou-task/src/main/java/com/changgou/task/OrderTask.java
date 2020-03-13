package com.changgou.task;

import com.changgou.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-13 11:47:47
 * @Modified By:
 */
@Component
public class OrderTask {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 自动收货定时任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoTake(){
        System.out.println("收货时间: " + new Date());
        rabbitTemplate.convertAndSend("", RabbitMQConfig.ORDER_TACK, "-");
    }
}
