package com.changgou.goods.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-04 00:29:29
 * @Modified By:
 */
@Data
@ToString
public class Goods implements Serializable {
    private Spu spu;
    private List<Sku> skuList;
}
