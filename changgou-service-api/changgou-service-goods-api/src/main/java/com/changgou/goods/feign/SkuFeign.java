package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") Long spuId);

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable Long id);
}
