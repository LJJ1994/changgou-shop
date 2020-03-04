package com.changgou.goods;

import com.changgou.entity.IdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-03 06:19:19
 * @Modified By:
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = {"com.changgou.goods.dao"})
public class GoodsApplication {
    @Value("${idworker.workerId}")
    private Integer workerId;

    @Value("${idworker.datacenterId}")
    private Integer datacenterId;

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(workerId, datacenterId);
    }
}
