package it.polito.ezshop.model;

import it.polito.ezshop.data.Order;

public class OrderObj implements Order {
	
	private enum Status { issued, payed, completed };
	private Integer balanceId;
	private Integer orderId;
	private String productCode;
	private double pricePerUnit;
	private int quantity;
	private Status status;
	
	public OrderObj(Integer orderId, String productCode, Integer quantity, double pricePerUnit) {
		this.balanceId = 0;
		this.orderId = orderId;
		this.productCode = productCode;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.status = Status.issued;
	}
	
	/**
	 * @return 
	 */
	@Override
	public Integer getBalanceId() {
		return this.balanceId;
	}

	/**
	 * @param balanceId
	 */
	@Override
	public void setBalanceId(Integer balanceId) {
		this.balanceId = balanceId;
	}

	/**
	 * @return
	 */
	@Override
	public String getProductCode() {
		return this.productCode;
	}

	/**
	 * @param productCode
	 */
	@Override
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return
	 */
	@Override
	public double getPricePerUnit() {
		return this.pricePerUnit;
	}

	/**
	 * @param pricePerUnit
	 */
	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * @return
	 */
	@Override
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * @param quantity
	 */
	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return 
	 */
	@Override
	public String getStatus() {
		return this.status.name();
	}

	/**
	 * Update order status
	 * @param status	new order status
	 */
	@Override
	public void setStatus(String status) {
		status = status.toLowerCase();
		if(Status.payed.name().equals(status)) {
			this.status = Status.payed;
			return;
		}
		if(Status.completed.name().equals(status)) {
			this.status = Status.completed;
			return;
		}
		this.status = Status.issued; // Become issued if there are problems
	}

	/**
	 * @return 
	 */
	@Override
	public Integer getOrderId() {
		return this.orderId;
	}

	/**
	 * @param orderId
	 */
	@Override
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
}
