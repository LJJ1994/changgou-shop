package com.changgou.pay;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-12 11:39:39
 * @Modified By:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WXPayTest {

    @Test
    public void testPay(){
        MyConfig myConfig = new MyConfig();
        WXPay wxPay = null;
        try {
            wxPay = new WXPay(myConfig);
        } catch (Exception e){
            e.printStackTrace();
        }

        Map<String,String> map= new HashMap();
        map.put("body","畅购测试01");//商品描述
        map.put("out_trade_no","152124545454");//订单号
        map.put("total_fee","1");//金额, 以分为单位
        map.put("spbill_create_ip","127.0.0.1");//终端IP
        map.put("notify_url","https://www.baidu.com");//回调地址
        map.put("trade_type","NATIVE");//交易类型
        Map<String, String> result = null;
        try {
            result = wxPay.unifiedOrder( map );
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
