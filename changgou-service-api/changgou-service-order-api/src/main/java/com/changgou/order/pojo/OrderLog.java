package com.changgou.order.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
@Table(name="tb_order_log")
@Data
@ToString
public class OrderLog implements Serializable{

	@Id
    @Column(name = "id")
	private String id;//ID

    @Column(name = "operater")
	private String operater;//操作员

    @Column(name = "operate_time")
	private Date operateTime;//操作时间

    @Column(name = "order_id")
	private String orderId;//订单ID

    @Column(name = "order_status")
	private String orderStatus;//订单状态

    @Column(name = "pay_status")
	private String payStatus;//付款状态

    @Column(name = "consign_status")
	private String consignStatus;//发货状态

    @Column(name = "remarks")
	private String remarks;//备注
}
