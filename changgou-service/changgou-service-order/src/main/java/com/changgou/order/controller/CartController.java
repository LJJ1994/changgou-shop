package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-11 09:43:43
 * @Modified By:
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    @GetMapping("/add")
    public Result add(@RequestParam("num") Integer num,
                      @RequestParam("id") Long id){
        cartService.add(num, id);
        return new Result(true, StatusCode.OK, "添加购物车成功!");
    }

    @GetMapping("/list")
    public Result<Map> list(){
        // String username = "itheima";
        String username = tokenDecode.getUserInfo().get("username");
        Map map = cartService.list(username);
        return new Result<>(true, StatusCode.OK, "购物车列表!", map);
    }
}
