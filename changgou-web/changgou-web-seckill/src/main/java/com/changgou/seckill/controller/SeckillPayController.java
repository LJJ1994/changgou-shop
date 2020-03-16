package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.feign.WxPayFeign;
import com.changgou.seckill.config.TokenDecode;
import com.changgou.seckill.feign.SecKillOrderFeign;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 20:21:21
 * @Modified By:
 */
@Controller
@RequestMapping("/wxpay")
public class SeckillPayController {
    @Autowired
    private WxPayFeign wxPayFeign;

    @Autowired
    private SecKillOrderFeign secKillOrderFeign;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 生成微信支付二维码
     */
    @GetMapping
    public String wxPay(@RequestParam("orderId") String orderId, Model model){
        String username = tokenDecode.getUserInfo().get("username");
        Result<SeckillOrder> orderResult = secKillOrderFeign.findById(username);
        SeckillOrder order = orderResult.getData();
        // 订单不存在
        if(order == null){
            return "fail";
        }
        // 不是未支付状态
        if(!"0".equals(order.getStatus())){
            return "fail";
        }
        Integer money = order.getMoney().intValue();
        Result<Map> mapResult = wxPayFeign.nativePay(String.valueOf(order.getId()), money);
        Map payMap = mapResult.getData();
        if (payMap == null) {
            return "fail";
        }
        payMap.put("payMoney", money);
        payMap.put("orderId", String.valueOf(order.getId()));
        model.addAllAttributes(payMap);
        return "wxpay";
    }

    /**
     * 支付结果成功页面
     */
    @RequestMapping("/toPaySuccess")
    public String toPaySuccess(Integer payMoney, Model model){
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }
}
