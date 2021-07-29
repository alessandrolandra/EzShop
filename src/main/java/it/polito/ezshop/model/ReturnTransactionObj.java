package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import it.polito.ezshop.data.TicketEntry;

public class ReturnTransactionObj{

	public enum State{
		open, closed, payed
	}
	
	private Integer Id;
	private SaleTransactionObj sale;
	private List<TicketEntry> entries;
	private Map<String,List<String>> RFIDs;
	private State state;
	
	public ReturnTransactionObj() {
		entries=new ArrayList<>();
		RFIDs = new TreeMap<>();
		this.setState(State.open);
	}
	
	public void setId(Integer Id) {
		this.Id=Id;
	}
	
	public Integer getId() {
		return Id;
	}
	
	public void setSale(SaleTransactionObj sale) {
		this.sale=sale;
	}
	
	public SaleTransactionObj getSale() {
		return sale;
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

	public List<TicketEntry> getEntries() {
		return entries;
	}
	
	public void addEntry(TicketEntry product) {		
		entries.add(product);
	}

	/**
	 * calculate the total amount of money to be given to the customer
	 * @return
	 */
	public double calculateDueMoney() {		
		return entries.stream().map(e->(e.getPricePerUnit()-e.getPricePerUnit()*e.getDiscountRate())*e.getAmount()).collect(Collectors.summingDouble(en->en));		
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
}
