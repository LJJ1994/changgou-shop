package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
public interface SkuMapper extends Mapper<Sku> {
    //扣减库存并增加销量
    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id=#{skuId} and num>=#{num}")
    int decrCount(OrderItem orderItem);

    //回滚库存(增加库存并扣减销量)
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
    void resumeStockNum(@Param("skuId") Long skuId, @Param("num")Integer num);
}
