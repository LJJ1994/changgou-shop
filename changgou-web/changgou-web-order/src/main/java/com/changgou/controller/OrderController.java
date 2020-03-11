package com.changgou.controller;

import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-11 19:32:32
 * @Modified By:
 */
@Controller
@RequestMapping("/worder")
@CrossOrigin
public class OrderController {
    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private AddressFeign addressFeign;

    @GetMapping("/ready/order")
    public String readyOrder(Model model){
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address", addressList);

        Map map = cartFeign.list().getData();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");
        model.addAttribute("carts", orderItemList);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalNum", totalNum);

        return "order";
    }
}
