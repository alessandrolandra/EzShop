package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryObj implements TicketEntry {

	private String barCode;
	private String description;
	private int amount;
	private double pricePerUnit;
	private double discountRate;
	
	public TicketEntryObj(String barCode, String description) {
		this.barCode = barCode;
		this.description = description;
		this.amount = 0;
		this.pricePerUnit = 0;
		this.discountRate = 0;
	}
	
	/**
	 * @return String: TicketEntry barcode
	 */
	@Override
	public String getBarCode() {
		return this.barCode;
	}

	/**
	 * Update barCode
	 * @param barCode	barCode String to set
	 */
	@Override
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	/**
	 * @return String: the description of the TicketEntry
	 */
	public String getProductDescription() {
		return this.description;
	}

	/**
	 * Updates the description of the product
	 * @param productDescription
	 */
	@Override
	public void setProductDescription(String productDescription) {
		this.description = productDescription;
	}

	/**
	 * @return int: the amount of the Ticket Entry
	 */
	@Override
	public int getAmount() {
		return this.amount;
	}

	/**
	 * Updates the amount
	 * @param amount to be set
	 */
	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return double: price per unit
	 */
	@Override
	public double getPricePerUnit() {
		return this.pricePerUnit;
	}

	/**
	 * Updates the price per unit
	 * @param pricePerUnit	new price per unit
	 */
	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * @return double: the discount rate applied
	 */
	@Override
	public double getDiscountRate() {
		return this.discountRate;
	}

	/**
	 * Updates the discount rate
	 * @param discountRate	new rate of discount
	 */
	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	
}
