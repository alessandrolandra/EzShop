package it.polito.ezshop.model;

public class CreditCardObj {

	private String number;
	private double money;
	
	public CreditCardObj(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
}
