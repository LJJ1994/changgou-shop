package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WxPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class PayController {
    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private WxPayFeign wxPayFeign;

    /**
     * 生成微信支付二维码
     */
    @GetMapping
    public String wxPay(String orderId, Model model){
        Result<Order> orderResult = orderFeign.findById(orderId);
        Order order = orderResult.getData();
        // 订单不存在
        if(order == null){
            return "fail";
        }
        // 不是未支付状态
        if(!"0".equals(order.getPayStatus())){
            return "fail";
        }
        Result<Map> mapResult = wxPayFeign.nativePay(orderId, order.getPayMoney());
        Map payMap = (Map) mapResult.getData();
        if (payMap == null) {
            return "fail";
        }
        payMap.put("payMoney", order.getPayMoney());
        payMap.put("orderId", orderId);
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
