package com.example.study.apply.model;

import java.io.Serializable;

/**
 * OrderModel
 * 订单模型类
 */
public class OrderModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderType;		// 订单类型
	private String userId;		// 用户ID
	private String orderName;	// 订单名称
	private String orderNo;		// 订单编号
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "OrderModel [orderType=" + orderType + ", userId=" + userId + ", orderName=" + orderName + ", orderNo="
				+ orderNo + "]";
	}
	
}

