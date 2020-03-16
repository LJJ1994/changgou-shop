package com.changgou.seckill;

import com.changgou.interceptor.FeignInterceptor;
import com.changgou.seckill.config.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-14 16:03:03
 * @Modified By:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

@EnableFeignClients(basePackages = {"com.changgou.seckill.feign","com.changgou.pay.feign"})
public class WebSecKillApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSecKillApplication.class,args);
    }

    @Bean
    public FeignInterceptor feignInterceptor(){

        return new FeignInterceptor();
    }

    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }
}
