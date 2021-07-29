package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class SaleTransactionObj implements SaleTransaction{

	public enum State{
		open, closed, payed
	}
	
	private Integer ticketNumber;
	private List<TicketEntry> entries;
	//TODO map containing the barCode as key and a list of RFIDs as value
	private Map<String,List<String>> RFIDs;
	private double discountRate,price;
	private State state;
	
	public SaleTransactionObj() {
		entries=new ArrayList<>();
		RFIDs = new TreeMap<>();
		this.setState(State.open);
	}
	
	public void setState(State state) {
		this.state=state;
	}
	
	public void setStateStr(String state) {
		if(state.equals(State.open.name())) {
			this.state = State.open;
		}else if(state.equals(State.payed.name())) {
			this.state = State.payed;
		}else if(state.equals(State.closed.name())){
			this.state = State.closed;
		}
	}
	
	public State getState() {
		return state;
	}
	
	@Override
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber=ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return entries;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		this.entries=entries;
	}
	
	public void addEntry(TicketEntry entry) {
		entries.add(entry);
	}
	
	/**
	 * remove specified entry from the list
	 * @param barCode
	 */
	public void removeEntry(String barCode) {
		entries.removeIf(entry->entry.getBarCode().equals(barCode));
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate=discountRate;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price=price;
	}
	
	/**
	 * compute the real sale price: original price - discount
	 * @return final price
	 */
	public double getFinalPrice() {
		return price-price*discountRate;
	}
	
	public Map<String,List<String>> getRFIDs() {
		return RFIDs;
	}
	
	public void addRFID(String barCode,String RFID) {
		if(RFIDs.containsKey(barCode)) {
			RFIDs.get(barCode).add(RFID);
		}else {
			List<String> list = new LinkedList<>();
			list.add(RFID);
			RFIDs.put(barCode, list);
		}
	}
	
	public void removeRFID(String barCode,String RFID) {
		//TODO (Ivan you can also change the parameters, is just a template)
	}
}
