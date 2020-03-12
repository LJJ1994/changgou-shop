package com.changgou.user.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.Task;
import com.changgou.user.config.RabbitMQConfig;
import com.changgou.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 10:16:16
 * @Modified By:
 */
@Component
public class AddPointListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_ADDPOINT)
    public void receiveAddPointMessage(String message){
        System.out.println("用户服务接收到MQ任务消息");
        Task task = JSON.parseObject(message, Task.class);
        // 消息为空直接返回
        if(task == null || StringUtils.isEmpty(task.getRequestBody())){
            return;
        }
        //判断redis是否存在任务
        Object value = redisTemplate.boundValueOps(task.getId()).get();
        if(value != null){
            return;
        }
        //更新用户积分
        int count = userService.updateUserPoints(task);
        if(count == 0){
            return;
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER, RabbitMQConfig.CG_BUYING_FINISHADDPOINT_KEY, JSON.toJSONString(task));
        System.out.println("用户服务向完成添加积分队列发送了一条消息");
    }
}
