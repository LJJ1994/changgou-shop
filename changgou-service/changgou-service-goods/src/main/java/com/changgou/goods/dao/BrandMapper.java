package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
public interface BrandMapper extends Mapper<Brand> {
    @Select(value = "select tb.* from tb_brand tb ,tb_category_brand tbc where tb.id = tbc.brand_id and tbc.category_id=#{cid}")
    List<Brand> findByCategory(Integer cid);
}
