package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.ManagerEsService;
import com.changgou.search.service.ManagerEsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsUpListener {

    @Autowired
    private ManagerEsService managerEsService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_ADD_QUEUE)
    public void receiveMessage(Long spuId){
        System.out.println("接收到的消息为:   "+spuId);

        //查询skulist,并导入到索引库
        managerEsService.importDataBySpuId(spuId);
    }
}
