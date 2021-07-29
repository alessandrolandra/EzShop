package it.polito.ezshop.model;

import java.time.LocalDate;

import it.polito.ezshop.data.BalanceOperation;

public class BalanceOperationObj implements BalanceOperation {
	
	enum Type { CREDIT, DEBIT};
	private int balanceId;
	private LocalDate date;
	private double money;
	private Type type;
	
	public BalanceOperationObj(int id, double toBeAdded){
		this.setBalanceId(id);
		this.setMoney(toBeAdded);
		this.setDate(LocalDate.now());
		if(toBeAdded >= 0) 
			this.setType(Type.CREDIT.name());
		else
			this.setType(Type.DEBIT.name());
	}
	
	@Override
	public int getBalanceId() {
		return this.balanceId;
	}

	@Override
	public void setBalanceId(int balanceId) {
		this.balanceId=balanceId;		
	}

	@Override
	public LocalDate getDate() {
		return this.date;
	}

	@Override
	public void setDate(LocalDate date) {
		this.date=date;		
	}

	@Override
	public double getMoney() {
		return this.money;
	}

	@Override
	public void setMoney(double money) {
		this.money=money;		
	}

	@Override
	public String getType() {
		return this.type.name();
	}

	@Override
	public void setType(String type) {
		if(type.equals(Type.CREDIT.name()))
			this.type = Type.CREDIT;
		else {
			this.type = Type.DEBIT;
		}
	}	
}
