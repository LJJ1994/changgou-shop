package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-03 08:41:41
 * @Modified By:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
