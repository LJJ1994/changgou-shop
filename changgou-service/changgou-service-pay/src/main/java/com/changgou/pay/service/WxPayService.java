package com.changgou.pay.service;

import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 19:46:46
 * @Modified By:
 */
public interface WxPayService {
    /**
     * 生成微信二维码
     * @param orderId
     * @param money
     * @return
     */
    Map nativePay(String orderId, Integer money);

    Map queryOrder(String out_trade_no);
}
