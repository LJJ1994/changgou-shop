package com.changgou;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-04 14:59:59
 * @Modified By:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCanalClient
@EnableEurekaClient
public class CanalApplication {
    public static void main(String[] args) {

        SpringApplication.run(CanalApplication.class, args);
    }
}
