package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;

public class CustomerObj implements Customer{
	
	private Integer id;
	private String name;
	private LoyaltyCard card;
	
	/**
	 * Customer constructor
	 * @param id	Integer that unique identifies a Customer
	 * @param name	Name of the Customer
	 */
	public CustomerObj(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.card = null;
	}
	
	/**
	 * @return String that is the name of the Customer
	 */
	public String getCustomerName() {
		return this.name;
	}

	/**
	 * @param customerName	sets the name of the Customer
	 */
	public void setCustomerName(String customerName) {
		this.name = customerName;
	}

	/**
	 * @return String associated with the LoyaltyCard id
	 */
	public String getCustomerCard() {
		if(this.card != null)
			return this.card.getCardId();
		else
			return null;
	}

	/**
	 * creates a LoyaltyCard associated with the Customer
	 * @param customerCard String associated to the LoyaltyCard's Customer
	 */
	public void setCustomerCard(String customerCard) {
		if(customerCard == null || customerCard.equals(""))
			this.card = null;
		else
			this.card = new LoyaltyCard(customerCard);
	}

	/**
	 * @return id of the Customer
	 */
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * Updates the id of a Customer
	 * @param id	new Customer id
	 */
	public void setId(Integer id) {
		this.id = id;	
	}

	/**
	 * @return number of points in the Customer's Loyalty Card
	 */
	public Integer getPoints() {
		if(this.card == null)
			return 0;
		return this.card.getPoints();
	}
	
	/**
	 * @param points 	# of points to be assigned to the Customer's Loyalty Card
	 */
	public void setPoints(Integer points) {
		if(this.card != null)
			this.card.setPoints(points);
	}
	
}
