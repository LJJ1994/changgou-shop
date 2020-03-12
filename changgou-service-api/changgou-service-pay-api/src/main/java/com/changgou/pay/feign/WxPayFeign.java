package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "pay")
public interface WxPayFeign {
    /**
     * 远程调用pay下单
     * @param orderId
     * @param money
     * @return
     */
    @GetMapping("/wxpay/nativePay")
    public Result<Map> nativePay(@RequestParam("orderId") String orderId,
                                 @RequestParam("money") Integer money);
}
