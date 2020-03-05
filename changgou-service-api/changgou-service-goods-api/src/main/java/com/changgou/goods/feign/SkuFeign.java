package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-06 00:48:48
 * @Modified By:
 */
@FeignClient(name = "goods")
public interface SkuFeign {

    @GetMapping("/sku/list")
    public List<Sku> findListForSearch();
}
