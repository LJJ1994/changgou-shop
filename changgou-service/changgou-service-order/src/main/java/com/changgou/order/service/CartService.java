package com.changgou.order.service;

import java.util.Map;

public interface CartService {
    /**
     * 添加购物车
     * @param num
     * @param id
     */
    void add(Integer num, Long id);

    /***
     * 查询用户的购物车数据
     * @return
     */
    Map list(String username);
}
