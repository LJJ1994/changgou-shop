package com.changgou.pay.service.impl;

import com.changgou.pay.service.WxPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 19:48:48
 * @Modified By:
 */
@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WXPay wxPay;

    @Value("${wxpay.notify_url}")
    private String notify_url;

    /**
     * 生成微信二维码
     * @param orderId 订单id
     * @param money 金额
     * @return
     */
    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            //1. 封装请求参数
            Map<String,String> map = new HashMap<>();
            map.put("body","畅购商城");
            map.put("out_trade_no",orderId);

            BigDecimal payMoney = new BigDecimal("0.01");
            BigDecimal fen = payMoney.multiply(new BigDecimal("100")); //1.00
            fen = fen.setScale(0,BigDecimal.ROUND_UP); // 1
            map.put("total_fee",String.valueOf(fen));

            map.put("spbill_create_ip","127.0.0.1");
            map.put("notify_url",notify_url);
            map.put("trade_type","NATIVE");

            //2.基于wxpay完成统一下单接口的调用,并获取返回结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    @Override
    public Map queryOrder(String orderId) {
        try{
            Map<String ,String> map = new HashMap();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.orderQuery(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭订单
     * @param orderId
     * @return
     */
    @Override
    public Map closeOrder(String orderId) {
        try{
            Map<String,String> map = new HashMap();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.closeOrder(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
