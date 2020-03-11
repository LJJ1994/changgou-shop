package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-11 09:32:32
 * @Modified By:
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 添加购物车
     * @param num
     * @param id
     */
    @Override
    public void add(Integer num, Long id) {
        /**
         * 1）查询redis中的数据
         * 2）如果redis中已经有了，则追加数量，重新计算金额
         * 3）如果没有，将商品添加到缓存
         */
        String username = tokenDecode.getUserInfo().get("username");
        if (StringUtils.isEmpty(username)){
            throw new RuntimeException("用户未登录");
        }
        OrderItem one = (OrderItem) redisTemplate.boundHashOps("Cart_" + username).get(id);
        if (one != null) {
            // 存在购物车
            one.setNum(one.getNum() + num);
            one.setMoney(one.getNum() * one.getPrice());
            one.setPayMoney(one.getNum() * one.getPrice());
            if (num <= 0) {
                // 移除购物车该商品
                redisTemplate.boundHashOps("Cart_" + username).delete(id);
                Long size = redisTemplate.boundHashOps("Cart_" + username).size();
                if (size == null || size <=0 ) {
                    redisTemplate.delete("Cart_" + username);
                }
                return;
            }
        } else {
            // 如果当前商品在redis中不存在,将商品添加到redis中
            Result<Sku> skuResult = skuFeign.findById(id);
            Sku sku = skuResult.getData();
            Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
            Spu spu = spuResult.getData();

            one = createOrderItem(num, id, sku, spu);
        }
        redisTemplate.boundHashOps("Cart_" + username).put(id, one);
    }

    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    @Override
    public Map list(String username) {
        List<OrderItem> orderItemList = redisTemplate.boundHashOps("Cart_" + username).values();
        // 总数量和总价格
        Integer totalNum = 0;
        Integer totalMoney = 0;
        for(OrderItem orderItem : orderItemList) {
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderItemList", orderItemList);
        map.put("totalNum", totalNum);
        map.put("totalMoney", totalMoney);
        return map;
    }

    private OrderItem createOrderItem(Integer num, Long id, Sku sku, Spu spu) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(orderItem.getPrice()*num);
        orderItem.setPayMoney(orderItem.getPrice()*num);
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);
        //分类信息
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());

        return orderItem;
    }
}
