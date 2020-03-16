package com.changgou.seckill.feign;

import com.changgou.entity.Result;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seckill")
public interface SecKillOrderFeign {

    @RequestMapping("/seckillOrder/add")
    public Result add(@RequestParam("time") String time, @RequestParam("id") Long id);

    @GetMapping("/seckillOrder/{username}")
    public Result<SeckillOrder> findById(@PathVariable("username") String username);
}
