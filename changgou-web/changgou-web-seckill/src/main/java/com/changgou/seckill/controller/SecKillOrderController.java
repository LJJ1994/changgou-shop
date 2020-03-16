package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.seckill.feign.SecKillOrderFeign;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-16 15:14:14
 * @Modified By:
 */
@Controller
@RequestMapping("/wseckillorder")
public class SecKillOrderController {
    @Autowired
    private SecKillOrderFeign secKillOrderFeign;

    /**
     * 跳转到支付页
     * @param orderId
     * @param money
     * @param model
     * @return
     */
    @GetMapping("/toPayPage")
    public String toPayPage(@RequestParam("orderId") String orderId,
                            @RequestParam("money") Integer money,
                            Model model){
        model.addAttribute("orderId", orderId);
        model.addAttribute("payMoney", money);
        return "pay";
    }
}
