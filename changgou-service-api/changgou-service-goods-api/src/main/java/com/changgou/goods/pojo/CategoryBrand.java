package com.changgou.goods.pojo;
import javax.persistence.*;
import java.io.Serializable;
import java.lang.Integer;
/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
@Table(name="tb_category_brand")
public class CategoryBrand implements Serializable{

	@Id
    @Column(name = "category_id")
	private Integer categoryId;//分类ID

    @Column(name = "brand_id")
	private Integer brandId;//品牌ID



	//get方法
	public Integer getCategoryId() {
		return categoryId;
	}

	//set方法
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	//get方法
	public Integer getBrandId() {
		return brandId;
	}

	//set方法
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}


}
