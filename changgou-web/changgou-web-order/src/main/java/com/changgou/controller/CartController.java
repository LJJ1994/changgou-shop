package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-11 16:32:32
 * @Modified By:
 */
@Controller
@RequestMapping("/wcart")
@CrossOrigin
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    /**
     * 查询购物车列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        Map list = cartFeign.list().getData();
        model.addAttribute("items", list);
        return "cart";
    }

    /**
     * 添加购物车
     */
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(Long id, Integer num) {
        cartFeign.add(id, num);
        Map list = cartFeign.list().getData();
        return new Result<>(true, StatusCode.OK, "OK", list);
    }
}
