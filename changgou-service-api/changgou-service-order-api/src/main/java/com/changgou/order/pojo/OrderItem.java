package com.changgou.order.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
@Table(name="tb_order_item")
public class OrderItem implements Serializable{

	@Id
    @Column(name = "id")
	private String id;//ID

    @Column(name = "category_id1")
	private Integer categoryId1;//1级分类

    @Column(name = "category_id2")
	private Integer categoryId2;//2级分类

    @Column(name = "category_id3")
	private Integer categoryId3;//3级分类

    @Column(name = "spu_id")
	private Long spuId;//SPU_ID

    @Column(name = "sku_id")
	private Long skuId;//SKU_ID

    @Column(name = "order_id")
	private String orderId;//订单ID

    @Column(name = "name")
	private String name;//商品名称

    @Column(name = "price")
	private Integer price;//单价

    @Column(name = "num")
	private Integer num;//数量

    @Column(name = "money")
	private Integer money;//总金额

    @Column(name = "pay_money")
	private Integer payMoney;//实付金额

    @Column(name = "image")
	private String image;//图片地址

    @Column(name = "weight")
	private Integer weight;//重量

    @Column(name = "post_fee")
	private Integer postFee;//运费

    @Column(name = "is_return")
	private String isReturn;//是否退货

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCategoryId1() {
		return categoryId1;
	}

	public void setCategoryId1(Integer categoryId1) {
		this.categoryId1 = categoryId1;
	}

	public Integer getCategoryId2() {
		return categoryId2;
	}

	public void setCategoryId2(Integer categoryId2) {
		this.categoryId2 = categoryId2;
	}

	public Integer getCategoryId3() {
		return categoryId3;
	}

	public void setCategoryId3(Integer categoryId3) {
		this.categoryId3 = categoryId3;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getPostFee() {
		return postFee;
	}

	public void setPostFee(Integer postFee) {
		this.postFee = postFee;
	}

	public String getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}

	@Override
	public String toString() {
		return "OrderItem{" +
				"id='" + id + '\'' +
				", categoryId1=" + categoryId1 +
				", categoryId2=" + categoryId2 +
				", categoryId3=" + categoryId3 +
				", spuId=" + spuId +
				", skuId=" + skuId +
				", orderId='" + orderId + '\'' +
				", name='" + name + '\'' +
				", price=" + price +
				", num=" + num +
				", money=" + money +
				", payMoney=" + payMoney +
				", image='" + image + '\'' +
				", weight=" + weight +
				", postFee=" + postFee +
				", isReturn='" + isReturn + '\'' +
				'}';
	}
}
