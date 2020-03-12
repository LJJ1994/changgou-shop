package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OrderFeign orderFeign;

    /**
     * 订单详情页（提交页面）
     * @param model
     * @return
     */
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

        //默认收件人信息
        for (Address address : addressList) {
            if ("1".equals(address.getIsDefault())){
                //默认收件人
                model.addAttribute("deAddr",address);
                break;
            }
        }

        return "order";
    }

    /**
     * 添加商品数据
     * @param order
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        return orderFeign.add(order);
    }

    /**
     * 跳转到支付页
     * @param orderId
     * @param model
     * @return
     */
    @GetMapping("/toPayPage")
    public String toPayPage(String orderId, Model model){
        Result<Order> result = orderFeign.findById(orderId);
        Order order = result.getData();
        model.addAttribute("orderId", orderId);
        model.addAttribute("payMoney", order.getPayMoney());
        return "pay";
    }
}
