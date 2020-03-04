package com.changgou.business.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.business.service.AdService;
import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pojo.Ad;

import java.io.IOException;
import java.util.List;

@Component
public class AdListener {
    @Autowired
    private AdService adService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 监听广告消息队列，有数据更新消息则请求nginx, lua脚本更新redis
     * @param message
     */
    @RabbitListener(queues = "ad_update_queue")
    public void receiveMessage(String message){
        System.out.println("接收到的消息为:"+message);
        List<Ad> adList = adService.findByPosition(message);
        String jsonString = JSON.toJSONString(adList);
        // 设置最新数据到redis
        stringRedisTemplate.boundValueOps("ad_" + message).set(jsonString);
        //发起远程调用
        OkHttpClient okHttpClient = new OkHttpClient();
        // 请求openrestry-nginx, 其中的ad.lua会更新redis中的数据
        String url = "http://127.0.0.1/ad_update?position="+message;
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                System.out.println("请求成功:"+response.message());
            }
        });
    }
}
