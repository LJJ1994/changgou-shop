package com.changgou.order.task;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description: 任务定时器
 * @Create: 2020-03-12 09:56:56
 * @Modified By:
 */
@Component
public class QueryPointTask {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/2 * * * * ?")
    public void queryTask(){
        // 1.获取小于当前系统时间的任务
        Date currentTime = new Date();
        List<Task> taskList = taskMapper.findTaskLessThanCurrentTime(currentTime);
        if (taskList != null && taskList.size() > 0) {
            for(Task task : taskList) {
                String msg = JSON.toJSONString(task);
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER, RabbitMQConfig.CG_BUYING_ADDPOINT_KEY, msg);
                System.out.println("订单服务向添加积分队列发送了一条消息");
            }
        }
    }
}
