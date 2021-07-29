package it.polito.ezshop.data;

import it.polito.ezshop.DBManagement.DBFactory;
import it.polito.ezshop.DBManagement.IDBManager;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;
import it.polito.ezshop.validator.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EZShop implements EZShopInterface {

	private User loggedInUser;
	private IDBManager dbManager;
	
	private TreeMap<Integer,User> users;
	private TreeMap<Integer,ProductType> inventory;
	private TreeMap<String,List<String>> RFIDinventory; // RFIDinventory is a map containing the barCode as key and the list of RFIDs as value
	private TreeMap<Integer,SaleTransactionObj> sales;
	private TreeMap<Integer,ReturnTransactionObj> returnTransactions;
	private TreeMap<Integer, OrderObj> orders;
	private TreeMap<Integer, CustomerObj> customers;
	private TreeMap<String, LoyaltyCard> loyaltyCards;
	private TreeMap<String, CreditCardObj> cards;
	private TreeMap<Integer,BalanceOperation> balanceOperations;
	
	private static final String creditCardPath = "src/creditCards.txt";
	
	public EZShop() {
		dbManager = DBFactory.createManager();
		init();
	}
	
	/**
	 * initialize the collections, loading data from the DB (and from file in case of credit cards)
	 */
	private void init() {
		users = new TreeMap<> ();
		List<User> us = dbManager.getAllUsers();
		if(us!=null)
			us.forEach((u)-> {users.put(u.getId(), u);});
		
		inventory = new TreeMap<>();
		List<ProductType> pt =dbManager.getAllProducts();
		if(pt!=null)
			pt.forEach((p)->{inventory.put(p.getId(), p);});
		
		balanceOperations = new TreeMap<>();
		List<BalanceOperation> bo =	dbManager.getAllBalanceOperations();
		if(bo!=null)
			bo.forEach((op)->{balanceOperations.put(op.getBalanceId(), op);});
		
		sales = new TreeMap<>();
		List<SaleTransactionObj> st=dbManager.getAllSaleTransactions();
		if(st != null)
			st.forEach(s->{sales.put(s.getTicketNumber(), s);});
		
		returnTransactions = new TreeMap<>();
		List<ReturnTransactionObj> rt=dbManager.getAllReturnTransactions();
		if(rt != null)
			rt.forEach(r->{returnTransactions.put(r.getId(), r);});
		
		customers = new TreeMap<>();
		loyaltyCards = new TreeMap<>();
		List<CustomerObj> cu = dbManager.getAllCustomers();
		if(cu!=null) {
			cu.forEach((c) -> {
				customers.put(c.getId(), c);System.out.println(c.getCustomerName());
			});
		}
		
		List<LoyaltyCard> lo = dbManager.getAllLoyaltyCards();
		if(lo != null) {
			lo.forEach((l) -> {
				loyaltyCards.put(l.getCardId(), l);		// Update loyaltyCards map and update points on related customer
				Optional<CustomerObj> o = customers.values().stream().filter((c) -> {
					if(c.getCustomerCard() != null) 
						if(c.getCustomerCard().equals(l.getCardId()))
							return true;
					return false;
				}).findFirst();
				if(!o.isEmpty())
					o.get().setPoints(l.getPoints());
			});
		}
		
		orders = new TreeMap<>();
		List<Order> or = dbManager.getAllOrders();
		if(or!=null) {
			or.forEach(o -> orders.put(o.getOrderId(), (OrderObj) o));
		}
		
		RFIDinventory = new TreeMap<>();
		Map<String, String> rfids = dbManager.getAllRFIDs();
		if(rfids != null) {
			for(Entry<String, String> e: rfids.entrySet()) {
				if(!RFIDinventory.containsKey(e.getValue()))
					RFIDinventory.put(e.getValue(), new ArrayList<String>());
				RFIDinventory.get(e.getValue()).add(e.getKey());
			}
		}
		
		readCreditCards();		
	}

	/**
	 * load credit cards from the given file (in creditCardPath), following the format: '<creditCardNumber>;<balance>'
	 */
	private void readCreditCards(){
		cards = new TreeMap<>();
		
		File f = new File(creditCardPath);
		if(f!=null) {
			BufferedReader br;
			String line;
			Matcher m;
			CreditCardObj c;
			Pattern p = Pattern.compile("([0-9]{16})\\;([0-9]+\\.[0-9]+)");
			try {
				br = new BufferedReader(new FileReader(f));
				while((line=br.readLine())!=null){
					if(line.charAt(0)!='#') {//skip lines which start with '#'
						m = p.matcher(line);
						m.find();
						c = new CreditCardObj(m.group(1));
						c.setMoney(Double.parseDouble(m.group(2)));
						cards.put(c.getNumber(),c);
						System.out.println("CARD READ: "+c.getNumber()+", "+c.getMoney());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
    @Override
    public void reset() {
    	balanceOperations.clear();
    	sales.clear();
    	returnTransactions.clear();
    	inventory.clear();
    	users.clear();
    	orders.clear();
    	customers.clear();
    	loyaltyCards.clear();
    	cards.clear();
    	RFIDinventory.clear();
    	dbManager.resetStoreInfo();
    }

    @Override
   public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	if(password == null || password.isEmpty()) {
    		throw new InvalidPasswordException();
    	}
    	if(role == null || !(role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))){
    		throw new InvalidRoleException();
    	}
    	if(username == null || username.isEmpty()) {
    		throw new InvalidUsernameException();
    	}
    	if(UserObj.verifyUsername(users, username)){
    		return -1;
    	}
    	Integer id = 1;
    	if(!users.isEmpty()) {
    		id = users.lastKey()+1; 
    	}

    	UserObj user = new UserObj(id,username,password,role);
    	if(dbManager.addUser(user)) {
    		users.put(id, user);
    		return id;
    	}
    	return -1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator"))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id<=0 ) {
    		throw new InvalidUserIdException();
    	}
    	if(users.containsKey(id)) {
    		if(dbManager.deleteUser(id)) {
	    		users.remove(id);
	            return true;
            }
    	}
        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator"))) {
    		throw new UnauthorizedException();
    	}
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator"))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id<=0 ) {
    		throw new InvalidUserIdException();
    	}
        return users.get(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator"))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id<=0 ) {
    		throw new InvalidUserIdException();
    	}
    	if(role == null || !(role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))){
    		throw new InvalidRoleException();
    	}
    	if(users.containsKey(id)) {
    		if(dbManager.updateUserRights(id,role)) {
	    		users.get(id).setRole(role);
	            return true;
            }
    	}
        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	if(password == null || password.isEmpty()) {
    		throw new InvalidPasswordException();
    	}
    	if(username == null || username.isEmpty()) {
    		throw new InvalidUsernameException();
    	}
    	User u = dbManager.getUserByUsername(username);
    	if(u != null) {
	    	if(u.getPassword().equals(password)) {
	    		loggedInUser = u;
	    		return u;
	    	}
    	}
        return null;
    }

    @Override
    public boolean logout() {
    	if(loggedInUser == null)
    		return false;
    	loggedInUser = null;
        return true;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(description == null || description.isEmpty()) {
    		throw new InvalidProductDescriptionException();
    	}
    	if(productCode == null || productCode.isEmpty() || !BarCodeValidator.validate(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(pricePerUnit <= 0.) {
    		throw new InvalidPricePerUnitException();
    	}
    	if(ProductTypeObj.searchProductByBarCode(inventory, productCode)!=null) {
    		return -1;
    	}
    	if(note ==null) {
    		note="";
    	}
    	Integer id = 1;
    	if(!inventory.isEmpty()) {
    		id = inventory.lastKey()+1; 
    	}
    	ProductTypeObj product = new ProductTypeObj(id, description, productCode, pricePerUnit, note);
    	if(dbManager.addProduct(product)) {
	    	inventory.put(id, product);
	    	return id;
	    }
    	return -1;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id<=0 ) {
    		throw new InvalidProductIdException();
    	}
    	if(newDescription == null || newDescription.isEmpty()) {
    		throw new InvalidProductDescriptionException();
    	}
    	if(newCode == null || newCode.isEmpty() || !BarCodeValidator.validate(newCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(newPrice <= 0.) {
    		throw new InvalidPricePerUnitException();
    	}
    	if(!inventory.containsKey(id)) {
    		return false;
    	}
    	ProductType pT = ProductTypeObj.searchProductByBarCode(inventory, newCode);
    	if(pT != null) {
    		if(!pT.getId().equals(id)) {
    			return false;
    		}
    	}
    	if(newNote ==null) {
    		newNote="";
    	}
    	if(dbManager.updateProduct(id, newDescription, newCode, newPrice, newNote)) {
	    	inventory.get(id).setProductDescription(newDescription);
	    	inventory.get(id).setBarCode(newCode);
	    	inventory.get(id).setPricePerUnit(newPrice);
	    	inventory.get(id).setNote(newNote);
	    	return true;
    	}
    	return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id<=0 ) {
    		throw new InvalidProductIdException();
    	}
    	if(inventory.containsKey(id)) {
    		if(dbManager.deleteProduct(id)) {
	    		inventory.remove(id);
	    		return true;
    		}
    	}
    	return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	return inventory.values().stream().collect(Collectors.toList());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(barCode == null || barCode.isEmpty() || !BarCodeValidator.validate(barCode)) {
    		throw new InvalidProductCodeException();
    	}
    	return ProductTypeObj.searchProductByBarCode(inventory, barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(description == null) {
    		description = "";
    	}
        return ProductTypeObj.searchProductDescription(inventory, description);
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(productId == null || productId<=0 ) {
    		throw new InvalidProductIdException();
    	}
    	if(!inventory.containsKey(productId) || (inventory.get(productId).getQuantity()+toBeAdded<=0) || inventory.get(productId).getLocation().isEmpty()) {
    		return false;
    	}
    	if(dbManager.updateProductQuantity(productId,inventory.get(productId).getQuantity()+toBeAdded)) {
    		inventory.get(productId).setQuantity(inventory.get(productId).getQuantity()+toBeAdded);
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	if(productId == null || productId<=0 ) {
    		throw new InvalidProductIdException();
    	}
    	if( (newPos == null || newPos.isEmpty() ) && inventory.containsKey(productId)) {
    		newPos = "";
    		inventory.get(productId).setLocation(newPos);
    		return true;
    	}
    	if(ProductTypeObj.verifyString(newPos)) {
    		throw new InvalidLocationException();
    	}
    	if(ProductTypeObj.verifyPosition(inventory, newPos) || !inventory.containsKey(productId)) {
            return false;
    	}
    	//to have data consistency between db and local memory if there are problems with the db the function will return false
    	if(dbManager.updateProductPosition(productId, newPos)) {
	    	inventory.get(productId).setLocation(newPos);
	    	return true;
    	}
    	return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        Integer id = 1;
        if(loggedInUser == null || (!loggedInUser.getRole().equals("Administrator") && !loggedInUser.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
        if(productCode == null || !BarCodeValidator.validate(productCode) || productCode.isEmpty())
        	throw new InvalidProductCodeException();
        if(quantity <= 0)
        	throw new InvalidQuantityException();
        if(pricePerUnit <= 0)
        	throw new InvalidPricePerUnitException();
        
        if(inventory.values().stream().filter((p) -> p.getBarCode().equals(productCode)).findAny().isEmpty())
        	return -1;
        
        if(!orders.isEmpty())
        	id = orders.lastKey() + 1;
    	OrderObj o = new OrderObj(id, productCode, quantity, pricePerUnit);
    	
    	if(dbManager.addOrder(o)) {
    		orders.put(id, o);
    		return id;
    	}
    	return -1;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	Integer id = 1;
        if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")))) {
    		throw new UnauthorizedException();
    	}
        if(productCode == null || !BarCodeValidator.validate(productCode) || productCode.isEmpty())
        	throw new InvalidProductCodeException();
        if(quantity <= 0)
        	throw new InvalidQuantityException();
        if(pricePerUnit <= 0)
        	throw new InvalidPricePerUnitException();
        if(this.computeBalance() < (quantity * pricePerUnit) || inventory.values().stream().filter((p) -> p.getBarCode().equals(productCode)).findAny().isEmpty())
        	return -1;
        if(!orders.isEmpty())
        	id = orders.lastKey() + 1;
    	OrderObj o = new OrderObj(id, productCode, quantity, pricePerUnit);
    	o.setStatus("payed");
    	if(dbManager.addOrder(o)) {
    		orders.put(id, o);
        	this.recordBalanceUpdate((-1) * o.getPricePerUnit() * o.getQuantity());
    		return id;
    	}
    	return -1;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")))) {
    		throw new UnauthorizedException();
    	}
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	if(orders.containsKey(orderId)) {
    		Order o = orders.get(orderId);
    		if(this.computeBalance() < (o.getQuantity() * o.getPricePerUnit()))
            	return false;
    		if(o.getStatus().equals("issued")) {
    			o.setStatus("payed");
    			if(dbManager.updateOrder(o.getOrderId(), o.getProductCode(), o.getQuantity(), o.getPricePerUnit(), o.getStatus())) {
    				this.recordBalanceUpdate((-1) * o.getPricePerUnit() * o.getQuantity());
    				return true;
    			}
    		}
    	}
    	return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")))) {
    		throw new UnauthorizedException();
    	}
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	if(orders.containsKey(orderId)) {
    		Order o = orders.get(orderId);
    		ProductType p;
    		try {
    			p = this.getProductTypeByBarCode(o.getProductCode());
    		} catch(Exception e) {
    			return false;
    		}
    		if(p.getLocation().equals(""))
    			throw new InvalidLocationException();
    		if(o.getStatus().equals("payed")) {
    			if(dbManager.updateOrder(o.getOrderId(), o.getProductCode(), o.getQuantity(), o.getPricePerUnit(), "completed")) {
    				o.setStatus("completed");
    				if(dbManager.updateProductQuantity(p.getId(), p.getQuantity() + o.getQuantity())) {
    		    			p.setQuantity(o.getQuantity());
    		    			return true;
    				}
    			}
    		}else if(o.getStatus().equals("completed"))
    			return true;
    	}
    	return false;
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
InvalidLocationException, InvalidRFIDException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")))) {
    		throw new UnauthorizedException();
    	}
    	if(orderId == null || orderId <= 0) {
    		throw new InvalidOrderIdException();
    	}
    	if(RFIDfrom == null || RFIDfrom.isEmpty() || !RFIDValidator.validate(RFIDfrom) || RFIDinventory.containsKey(RFIDfrom)) {
    		throw new InvalidRFIDException();
    	}
    	if(orders.containsKey(orderId)) {
    		Order o = orders.get(orderId);
    		ProductType p;
    		try {
    			p = this.getProductTypeByBarCode(o.getProductCode());
    		} catch(Exception e) {
    			return false;
    		}
    		if(p.getLocation().equals(""))
    			throw new InvalidLocationException();
    		long RFIDstart = Long.parseLong(RFIDfrom);
    		List<String> RFIDlist = new LinkedList<>();
    		String currentRFID;
    		for(int i=0;i<o.getQuantity();i++) {
    			currentRFID = String.format("%012d",(RFIDstart+i));
    			if(RFIDinventory.containsKey(o.getProductCode())) {
    				if(RFIDinventory.get(o.getProductCode()).contains(currentRFID))
    					throw new InvalidRFIDException();
    			}
    			RFIDlist.add(currentRFID);
    		}
    		if(o.getStatus().equals("payed")) {
    			if(dbManager.updateOrder(o.getOrderId(), o.getProductCode(), o.getQuantity(), o.getPricePerUnit(), "completed")) {
    				o.setStatus("completed");
    				if(dbManager.updateProductQuantity(p.getId(), p.getQuantity() + o.getQuantity())) {
    		    			p.setQuantity(o.getQuantity());
    		    			boolean success = true;
    		    			for(int i=0;i<RFIDlist.size();i++){
    		    				if(!dbManager.addRFID(RFIDlist.get(i),p.getBarCode()))
    		    					success = false;
    		    			}
    		    			if(success) {
    	    			   		if(RFIDinventory.containsKey(p.getBarCode())){
    	    						RFIDinventory.get(p.getBarCode()).addAll(RFIDlist);
    	    					}else {
    	    						RFIDinventory.put(p.getBarCode(), RFIDlist);
    	    					}
    	    			   		return true;
    	    				}
    				}    				
    			}
    		}else if(o.getStatus().equals("completed"))
    			return true;
    	}
    	return false;
    }
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")))) {
    		throw new UnauthorizedException();
    	}
    	return orders.values().stream().collect(Collectors.toList());
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	Integer id = 1;
        if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
        if(customerName == null || customerName.isEmpty())
        	throw new InvalidCustomerNameException();
        if(!customers.values().stream().filter((c) -> c.getCustomerName().equals(customerName)).findAny().isEmpty())
        	return -1;
        if(!customers.isEmpty())
        	id = customers.lastKey() + 1;
        CustomerObj c = new CustomerObj(id, customerName);
        if(dbManager.addCustomer(c)) {
        	customers.put(id, c);
        	return id;
        }
        return -1;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	if(newCustomerName == null || newCustomerName.equals(""))
    		throw new InvalidCustomerNameException();
    	
    	if(newCustomerCard == null || !LoyaltyCardCodeValidator.validate(newCustomerCard))
    		throw new InvalidCustomerCardException();
    	
    	CustomerObj c;
		if(customers.containsKey(id))
        	c = customers.get(id);
		else 
			throw new InvalidCustomerIdException();
		
		// We decided not to delete or reset removed loyaltyCards (the old ones)
		Optional<CustomerObj> otherCustomer = customers.values().stream().filter(cu -> {
				if(cu.getCustomerCard() != null)
					if(cu.getCustomerCard().equals(newCustomerCard))
						return true;
				return false;
			}).findAny();
		
		// Card already assigned!
		if(otherCustomer.isPresent() && (otherCustomer.get().getId() != id))
			return false;
		
		if(!customers.values().stream().filter((cu) -> cu.getCustomerName().equals(newCustomerName)).findAny().isEmpty())
        	return false;
		
		// Reset loyalty card if "", update the customer on db and delete card from db.
		if(newCustomerCard.equals("")) {
			if(c.getCustomerCard() != null) {
				if(dbManager.updateCustomer(c.getId(), newCustomerName, null)) {
					c.setCustomerName(newCustomerName);
					c.setCustomerCard(newCustomerCard);
					return true;
				} else
					return false;
			}
		}
		
		if(newCustomerCard.isEmpty())
    		throw new InvalidCustomerCardException();
		
        if(!LoyaltyCardCodeValidator.validate(newCustomerCard))
        	throw new InvalidCustomerCardException();
        
        // Update Customer and LoyaltyCard
        // We decided not to delete or reset removed loyaltyCards (the old ones)
        if(dbManager.updateCustomer(c.getId(), newCustomerName, newCustomerCard)) {
        	c.setCustomerCard(newCustomerCard);
        	c.setCustomerName(newCustomerName);
        	LoyaltyCard l;
        	if(loyaltyCards.containsKey(newCustomerCard))
        		l = loyaltyCards.get(newCustomerCard);
       		else
        		l = new LoyaltyCard(newCustomerCard);
        	loyaltyCards.put(c.getCustomerCard(), l);
        	return true;
        } 
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	if(!customers.containsKey(id))
			return false;
    	if(dbManager.deleteCustomer(id))	// We decided not to delete loyalty cards locally and from db
    		customers.remove(id);
    	else
    		return false;
    	return true;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	if(!customers.containsKey(id))
			return null;
    	return this.customers.get(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	return this.customers.values().stream().collect(Collectors.toList());
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	String card;
    	do {	// Generate a valid loyalty card code untill is already used
    		card = LoyaltyCard.generate();
    	} while(loyaltyCards.containsKey(card));
    	LoyaltyCard l = new LoyaltyCard(card);
    	if(dbManager.addLoyaltyCard(l)) {
    		loyaltyCards.put(l.getCardId(), l);
    		return l.getCardId();
    	}
    	return "";
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	if(customerId == null || customerId <= 0)
    		throw new InvalidCustomerIdException();
    	if(customerCard == null || customerCard.isEmpty() || !LoyaltyCardCodeValidator.validate(customerCard))
    		throw new InvalidCustomerCardException();
    	if(!customers.containsKey(customerId) || customers.values().stream().map((x) -> x.getCustomerCard()).filter((x) -> {
    			if(x != null)
    				if(x.equals(customerCard))
    					return true;
    			return false;
    		}).count() > 0)
    		return false;
    	Customer c = customers.get(customerId);
    	LoyaltyCard l;
    	if(loyaltyCards.containsKey(customerCard))
    		l = loyaltyCards.get(customerCard);
    	else  {
    		l = new LoyaltyCard(customerCard);
    		if(!dbManager.addLoyaltyCard(l))
    			return false;
    		else
    			loyaltyCards.put(l.getCardId(), l);
    	}
    	if(dbManager.updateCustomer(c.getId(), c.getCustomerName(), l.getCardId())) {
    		c.setCustomerCard(l.getCardId());
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	if(loggedInUser == null || (!(loggedInUser.getRole().equals("Administrator")) && !(loggedInUser.getRole().equals("ShopManager")) && !(loggedInUser.getRole().equals("Cashier")))) {
    		throw new UnauthorizedException();
    	}
    	if(customerCard == null || customerCard.isEmpty() || !LoyaltyCardCodeValidator.validate(customerCard))
    		throw new InvalidCustomerCardException();
    	if(!loyaltyCards.containsKey(customerCard))
    		return false;
    	LoyaltyCard l = loyaltyCards.get(customerCard);
    	Customer c = customers.values().stream().filter((a) -> a.getCustomerCard().equals(customerCard)).findFirst().orElseThrow(InvalidCustomerCardException::new);
    	if((c.getPoints() + pointsToBeAdded) < 0)
    		return false;
    	if(dbManager.updateLoyaltyCard(l.getCardId(), l.getPoints())) {
    		c.setPoints(c.getPoints() + pointsToBeAdded);
        	l.setPoints(l.getPoints() + pointsToBeAdded);
        	return true;
    	} 
    	return false;
    }
    
    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	Integer id;
    	try{
    		id = sales.lastKey()+1;
    	}catch(NoSuchElementException e) {
    		id = 1;
    	}
    	SaleTransactionObj s = new SaleTransactionObj();
    	s.setTicketNumber(id);
        sales.put(id, s);
    	return id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(productCode == null || productCode.isEmpty() || !BarCodeValidator.validate(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(amount<0) {
    		throw new InvalidQuantityException();
    	}
    	int quantity;
    	ProductType p;
    	if(!sales.containsKey(transactionId) || (p=getProductTypeByBarCode(productCode))==null || !sales.get(transactionId).getState().equals(SaleTransactionObj.State.open) || (quantity = p.getQuantity())<amount) {
        	return false;
        }
    	
    	SaleTransactionObj s = sales.get(transactionId);
    	
    	//ADDED: check if the product has already been added to the sale
    	if(s.getEntries().stream().anyMatch(e->e.getBarCode().equals(productCode))) {
    		s.getEntries().forEach(e->{
    			if(e.getBarCode().equals(productCode)) {
    				e.setAmount(e.getAmount()+amount);
    			}
    		});
    	}else {    	
    		TicketEntry t = new TicketEntryObj(productCode,p.getProductDescription());
    		t.setPricePerUnit(p.getPricePerUnit());
    		t.setAmount(amount);
        
    		s.addEntry(t);    		
    	}
    	p.setQuantity(quantity-amount);
		//if we comment the quantity update, it seems to be correct but is no more compliant with the API...
        s.setPrice(s.getEntries().stream().map(e->(e.getPricePerUnit()-e.getPricePerUnit()*e.getDiscountRate())*e.getAmount()).collect(Collectors.summingDouble(e->e)));
    	return true;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier")))
        	throw new UnauthorizedException(); 
    	if(transactionId == null || transactionId <=0)
        	throw new InvalidTransactionIdException(); 
        if(RFID == null || RFID.isEmpty() || !RFIDValidator.validate(RFID))
        	throw new InvalidRFIDException(); 
    	String barcode = "";
    	
    	
    	for(int i=0;i<RFIDinventory.entrySet().size();i++) {
    		Iterator<Entry<String,List<String>>> it = RFIDinventory.entrySet().iterator();
    		while(it.hasNext()) {
    			Entry<String,List<String>> entry = it.next();
    			if(entry.getValue().contains(RFID) && barcode.isEmpty()) {
    				barcode=barcode.concat(entry.getKey());
    			}
    		}
    	}
    	
    	SaleTransactionObj s = sales.get(transactionId); 
    	
    	
    	if(s == null || barcode.isEmpty() || !s.getState().equals(SaleTransactionObj.State.open))
    		return false;
    	String success ="";
    	s.getRFIDs().forEach((key,value)->{if(value.contains(RFID)) success.concat(key); });
    	if(!success.isEmpty())
    		return false;
    	
    	
    	s.addRFID(barcode, RFID);
    	
    	//if we comment the quantity update, it seems to be correct but is no more compliant with the API...
    	s.setPrice(s.getPrice() + ProductTypeObj.searchProductByBarCode(inventory, barcode).getPricePerUnit());
    	
    	return true;
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(productCode == null || productCode.isEmpty() || !BarCodeValidator.validate(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(amount<0) {
    		throw new InvalidQuantityException();
    	}
    	SaleTransactionObj s;
    	TicketEntry t;    	
    	if(!sales.containsKey(transactionId) || (s=sales.get(transactionId)).getEntries().stream().map(te->te.getBarCode()).noneMatch(barCode->barCode.equals(productCode)) || !s.getState().equals(SaleTransactionObj.State.open) || (t=s.getEntries().stream().filter(te->te.getBarCode().equals(productCode)).findFirst().get()).getAmount()<amount) {
        	return false;
        }
    	if(t.getAmount() == amount) {
    		s.removeEntry(productCode);
        }else{
        	t.setAmount(t.getAmount()-amount);
        }
    	ProductType p = getProductTypeByBarCode(productCode);
        int quantity = p.getQuantity();
        p.setQuantity(quantity+amount);
        
        s.setPrice(s.getEntries().stream().map(e->(e.getPricePerUnit()-e.getPricePerUnit()*e.getDiscountRate())*e.getAmount()).collect(Collectors.summingDouble(e->e)));
    	return true;
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier")))
        	throw new UnauthorizedException(); 
    	if(transactionId == null || transactionId <=0)
        	throw new InvalidTransactionIdException(); 
        if(RFID == null || RFID.isEmpty() || !RFIDValidator.validate(RFID))
        	throw new InvalidRFIDException(); 
        
    	String Barcode = "";
		
    	SaleTransactionObj s = sales.get(transactionId);
    	
    	for(int i=0;i<s.getRFIDs().entrySet().size();i++) {
    		Iterator<Entry<String,List<String>>> it = RFIDinventory.entrySet().iterator();
    		while(it.hasNext()) {
    			Entry<String,List<String>> entry = it.next();
    			if(entry.getValue().contains(RFID) && Barcode.isEmpty()) {
    				Barcode=Barcode.concat(entry.getKey());
    			}
    		}
    	}
    	
    	
    	if(s == null || Barcode.isEmpty() || !s.getState().equals(SaleTransactionObj.State.open))
    		 return false;
    	s.removeRFID(Barcode, RFID);
    	
    	//if we comment the quantity update, it seems to be correct but is no more compliant with the API...
    	s.setPrice(s.getPrice() - ProductTypeObj.searchProductByBarCode(inventory, Barcode).getPricePerUnit());
    	
    	return true;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(productCode == null || productCode.isEmpty() || !BarCodeValidator.validate(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(discountRate<0 || discountRate>=1) {
    		throw new InvalidDiscountRateException();
    	}
    	SaleTransactionObj s;
    	if(!sales.containsKey(transactionId) || (s=sales.get(transactionId)).getEntries().stream().map(te->te.getBarCode()).noneMatch(barCode->barCode.equals(productCode)) || s.getState().equals(SaleTransactionObj.State.closed)) {
        	return false;
        }
    	TicketEntry t = s.getEntries().stream().filter(te->te.getBarCode().equals(productCode)).findFirst().get();
        t.setDiscountRate(discountRate);
        
        s.setPrice(s.getEntries().stream().map(e->(e.getPricePerUnit()-e.getPricePerUnit()*e.getDiscountRate())*e.getAmount()).collect(Collectors.summingDouble(e->e)));
    	return true;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(discountRate<0 || discountRate>=1) {
    		throw new InvalidDiscountRateException();
    	}
    	SaleTransactionObj s;
    	if(!sales.containsKey(transactionId) || (s=sales.get(transactionId)).getState().equals(SaleTransactionObj.State.payed)) {
        	return false;//payed condition added to return false, as written at the beginning
        }
    	s.setDiscountRate(discountRate);
    	s.setPrice(s.getPrice()-(s.getPrice()*discountRate));
    	return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(!sales.containsKey(transactionId)) {
        	return -1;
        }
    	SaleTransaction s = sales.get(transactionId);
    	return (int)Math.floor((s.getPrice()-s.getPrice()*s.getDiscountRate())/10);
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	SaleTransactionObj s;
    	if(!sales.containsKey(transactionId) || (s=sales.get(transactionId)).getState().equals(SaleTransactionObj.State.closed)) {
        	return false;
        }
    	boolean success = true;
    	ProductType p;
    	List<TicketEntry> list = s.getEntries();
    	for(int i=0;i<list.size();i++) {
    		try {
				p = getProductTypeByBarCode(list.get(i).getBarCode());//quantity already updated locally
				if(!dbManager.updateProductQuantity(p.getId(),p.getQuantity())) {
	        		success = false;
	        	}
			} catch (InvalidProductCodeException | UnauthorizedException e) {
				success = false;
			}
    	}
    	if(!success)
    		return false;
    	SaleTransactionObj.State oldState = s.getState();
    	s.setState(SaleTransactionObj.State.closed);
    	
    	
    	if(!dbManager.addSaleTransaction(s)) {
    		s.setState(oldState);//rollback
    		return false;
    	}
    	
    	String dbSuccess= "";
    	s.getRFIDs().forEach((k,v)->   
			v.forEach((rfid)->{	if(dbManager.deleteRFID(rfid))
									RFIDinventory.get(k).remove(rfid);
								else
									dbSuccess.concat("false");}));
    	if(!dbSuccess.isEmpty()) {
    		System.out.println("Problem with deleteRFID");
    		return false;
    	}
    	
    	return true;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(saleNumber == null || saleNumber<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(!sales.containsKey(saleNumber) || sales.get(saleNumber).getState().equals(SaleTransactionObj.State.payed)) {
        	return false;
        }
    	if(dbManager.deleteSaleTransaction(saleNumber)) {
    		//restore products or not? Not requested in the API....
    		/*sales.get(saleNumber).getEntries().forEach(entry -> {
    			try {
    				ProductType p = getProductTypeByBarCode(entry.getBarCode());System.out.println(p.getQuantity());
					p.setQuantity(p.getQuantity()+entry.getAmount());
				} catch (InvalidProductCodeException e) {
					e.printStackTrace();
				} catch (UnauthorizedException e) {
					e.printStackTrace();
				}    			
    		});*/
    		
    		sales.remove(saleNumber);
    		return true;
    	}
    	return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();
        }
    	if(transactionId == null || transactionId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	SaleTransactionObj s;
    	if(!sales.containsKey(transactionId) || !(s=sales.get(transactionId)).getState().equals(SaleTransactionObj.State.closed)) {
        	return null;
        }
    	return s;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	if(saleNumber==null || saleNumber<=0) {
    		throw new InvalidTransactionIdException();
    	}    	
    	if(!sales.containsKey(saleNumber) || !sales.get(saleNumber).getState().equals(SaleTransactionObj.State.payed)) {
    		return -1;//payed condition added as written in the method description
    	}
    	Integer id;
    	try{
    		id = returnTransactions.lastKey()+1;
    	}catch(NoSuchElementException e) {
    		id = 1;//id starts from 1 due to InvalidTransactionIdException conditions in returnProduct and endReturnTransaction
    	}
    	ReturnTransactionObj r = new ReturnTransactionObj();
    	r.setId(id);
    	r.setSale(sales.get(saleNumber));    	
        returnTransactions.put(id, r);
    	return id;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(productCode == null || productCode.isEmpty() || !BarCodeValidator.validate(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	if(amount<=0) {
    		throw new InvalidQuantityException();
    	}
    	if(!returnTransactions.containsKey(returnId) || getProductTypeByBarCode(productCode)==null) {
        	return false;
        }
    	ReturnTransactionObj r = returnTransactions.get(returnId);
    	List<TicketEntry> saleEntries = r.getSale().getEntries();
    	TicketEntry saleProduct;
    	//check product presence in the sale and check if the amount to be returned is realistic 
    	if(!saleEntries.stream().anyMatch(te->te.getBarCode().equals(productCode)) || (saleProduct = saleEntries.stream().filter(te->te.getBarCode().equals(productCode)).findFirst().get()).getAmount()<amount) {
        	return false;
        }
    	//missing in the API: check that the returnTransaction is open..
    	TicketEntry toBeReturned = new TicketEntryObj(saleProduct.getBarCode(),saleProduct.getProductDescription());
    	toBeReturned.setPricePerUnit(saleProduct.getPricePerUnit());
    	toBeReturned.setDiscountRate(saleProduct.getDiscountRate());
    	toBeReturned.setAmount(amount);    	
        r.addEntry(toBeReturned);
    	return true;
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(RFID == null || RFID.isEmpty() || !RFIDValidator.validate(RFID)) {
    		throw new InvalidRFIDException();
    	}
    	//TODO the product is no more present in the inventoryRFID, we cannot check...
    	if(!returnTransactions.containsKey(returnId)) {
        	return false;
        }
    	ReturnTransactionObj r = returnTransactions.get(returnId);
    	List<TicketEntry> saleEntries = r.getSale().getEntries();
    	Map<String,List<String>> saleRFIDs = r.getSale().getRFIDs();
    	Stream<String> streamOfRFIDs = saleRFIDs.values().stream().flatMap(Collection::stream);
    	//check product presence in the sale 
    	if(!streamOfRFIDs.anyMatch(s->s.equals(RFID))) {
        	return false;
        }
    	String barCode = saleRFIDs.entrySet().stream().filter(entry->entry.getValue().contains(RFID)).map(entry->entry.getKey()).findFirst().get();
    	TicketEntry saleProduct = saleEntries.stream().filter(te->te.getBarCode().equals(barCode)).findFirst().get();

    	TicketEntry toBeReturned = new TicketEntryObj(saleProduct.getBarCode(),saleProduct.getProductDescription());
    	toBeReturned.setPricePerUnit(saleProduct.getPricePerUnit());
    	toBeReturned.setDiscountRate(saleProduct.getDiscountRate());
    	//1 RFID passed -> 1 product item
    	toBeReturned.setAmount(1);
        r.addEntry(toBeReturned);
        r.addRFID(toBeReturned.getBarCode(), RFID);
    	return true;
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	ReturnTransactionObj r;
    	//active return transactions means open, so this is checked
    	if(!returnTransactions.containsKey(returnId) || !(r=returnTransactions.get(returnId)).getState().equals(ReturnTransactionObj.State.open)) {
        	return false;
        }
    	
    	if(commit) {
	    	ProductType p;
	    	TicketEntry saleTE,returnTE;
	    	List<TicketEntry> list = r.getEntries();
	    	int newQuantity;
	    	double priceDifference;
	    	//update quantity of each product in the inventory
	    	for(int i=0;i<list.size();i++) {
	    		try {
	    			returnTE=list.get(i);
	    			
	    			priceDifference = returnTE.getAmount()*(returnTE.getPricePerUnit()-returnTE.getPricePerUnit()*returnTE.getDiscountRate());
					r.getSale().setPrice(r.getSale().getPrice()-priceDifference);					
	    			
	    			String barCode = returnTE.getBarCode();					
					saleTE = r.getSale().getEntries().stream().filter(e->e.getBarCode().equals(barCode)).findFirst().get();
					saleTE.setAmount(saleTE.getAmount()-returnTE.getAmount());		
					p = getProductTypeByBarCode(returnTE.getBarCode());
					newQuantity = p.getQuantity()+returnTE.getAmount();
					
					if(!dbManager.updateProductQuantity(p.getId(),newQuantity)) {
		        		return false;
		        	}					
					p.setQuantity(newQuantity);
				} catch (InvalidProductCodeException | UnauthorizedException e) {
					return false;
				}
	    	}
	    	//update RFID inventory, adding new ones
	    	boolean error = false;
	    	Iterator<Entry<String,List<String>>> it = r.getRFIDs().entrySet().iterator();
	    	while(it.hasNext()) {
	    		Entry<String,List<String>> entry = it.next();
	    		RFIDinventory.get(entry.getKey()).addAll(entry.getValue());
    			for(int j=0;j<entry.getValue().size();j++){
    				if(!dbManager.addRFID(entry.getValue().get(j),entry.getKey())) {
    					error = true;
    				}
    			}
	    	}
	    	if(error) {
	    		return false;
	    	}
	    	if(!dbManager.updateSaleTransactionAfterReturn(r.getSale(), r)) {
				return false;
			}
	    	ReturnTransactionObj.State oldState = r.getState();
	    	r.setState(ReturnTransactionObj.State.closed);
	    	if(!dbManager.addReturnTransaction(r)) {
	    		r.setState(oldState);//rollback
	    		return false;
	    	}
    	}
    	return true;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	ReturnTransactionObj r;
    	if(!returnTransactions.containsKey(returnId) || (r = returnTransactions.get(returnId)).getState().equals(ReturnTransactionObj.State.payed)) {
        	return false;
        }
    	if(dbManager.deleteReturnTransaction(returnId)) {
    		SaleTransactionObj s = r.getSale();
    		r.getEntries().forEach(e->{
    			try {
    				TicketEntry saleEntry = s.getEntries().stream().filter(te->te.getBarCode().equals(e.getBarCode())).findFirst().get();
    				saleEntry.setAmount(saleEntry.getAmount()+e.getAmount());    				
					
    				ProductType p = getProductTypeByBarCode(e.getBarCode());
					p.setQuantity(p.getQuantity()-e.getAmount());
				} catch ( InvalidProductCodeException | UnauthorizedException e1) {
					e1.printStackTrace();
				}
    		});
    		//remove the RFIDs from the inventory
    		r.getRFIDs().entrySet().forEach(e->{
    			RFIDinventory.get(e.getKey()).removeAll(e.getValue());
    		});
    		s.setPrice(s.getEntries().stream().map(e->(e.getPricePerUnit()-e.getPricePerUnit()*e.getDiscountRate())*e.getAmount()).collect(Collectors.summingDouble(e->e)));
    		returnTransactions.remove(returnId);
    		return true;
    	}
    	return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(ticketNumber == null || ticketNumber<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(cash<=0) {
    		throw new InvalidPaymentException();
    	}
    	SaleTransactionObj s;
    	if(!sales.containsKey(ticketNumber) || (s=sales.get(ticketNumber)).getFinalPrice()>cash) {
        	return -1;
    	}
    	if(recordBalanceUpdate(s.getFinalPrice())) {
    		System.out.println("Cash payment received: "+cash+" to pay: "+s.getFinalPrice());
    		s.setState(SaleTransactionObj.State.payed);
    		return cash-s.getFinalPrice();
    	}
    	return -1;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(ticketNumber == null || ticketNumber<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(creditCard==null || creditCard.isEmpty() || !CreditCardValidator.validate(creditCard)) {
    		throw new InvalidCreditCardException();
    	}
    	SaleTransactionObj s;
    	CreditCardObj c;
    	if(!sales.containsKey(ticketNumber) || !cards.containsKey(creditCard) || (c=cards.get(creditCard)).getMoney()<(s=sales.get(ticketNumber)).getFinalPrice()) {
        	return false;
        }    	
    	if(recordBalanceUpdate(s.getFinalPrice())) {
    		c.setMoney(c.getMoney()-s.getFinalPrice());
    		s.setState(SaleTransactionObj.State.payed);
    		return true;
    	}    	
    	return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	ReturnTransactionObj r;
    	if(!returnTransactions.containsKey(returnId) || (r=returnTransactions.get(returnId)).getState()!=ReturnTransactionObj.State.closed) {
        	return -1;
        }
    	double dueMoney = r.calculateDueMoney();
    	if(recordBalanceUpdate(-1*dueMoney)) {
    		r.setState(ReturnTransactionObj.State.payed);    		
    		return dueMoney;
    	}    	
    	return -1;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager") || loggedInUser.getRole().equals("Cashier"))) {
        	throw new UnauthorizedException();        	
        }
    	if(returnId == null || returnId<=0) {
    		throw new InvalidTransactionIdException();
    	}
    	if(creditCard==null || creditCard.isEmpty() || !CreditCardValidator.validate(creditCard)) {
    		throw new InvalidCreditCardException();
    	}    	
    	ReturnTransactionObj r;
    	if(!returnTransactions.containsKey(returnId) || !cards.containsKey(creditCard) || (r=returnTransactions.get(returnId)).getState()!=ReturnTransactionObj.State.closed) {
        	return -1;
        }
    	CreditCardObj c = cards.get(creditCard);
    	double dueMoney = r.calculateDueMoney();
    	if(recordBalanceUpdate(-1*dueMoney)) {
    		c.setMoney(c.getMoney()+dueMoney);
    		r.setState(ReturnTransactionObj.State.payed);
    		return dueMoney;
    	}    	
    	return -1;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator") || loggedInUser.getRole().equals("ShopManager"))) {
        	throw new UnauthorizedException();        	
        }
    	if(computeBalance() + toBeAdded < 0 ) {
    		return false;
    	}
    	Integer id = 1;
    	if(!balanceOperations.isEmpty()) {
    		id = balanceOperations.lastKey()+1; 
    	}
    	BalanceOperationObj op = new BalanceOperationObj(id,toBeAdded);
    	if(dbManager.recordBalanceUpdate(op)) {
    		balanceOperations.put(id, op);
    		return true;
    	}
    	return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	if(loggedInUser == null || !(loggedInUser.getRole().equals("Administrator"))) {
    		throw new UnauthorizedException();
    	}
    	if(from!= null && to != null)
    	{
	    	if(to.isBefore(from)) {
	    		LocalDate c = to;
	    		to = from;
	    		from = c;
	    	}
    	}
    	final LocalDate f = from;
    	final LocalDate t = to;
    	List<BalanceOperation> op =  balanceOperations.values().stream().collect(Collectors.toList());
    	if(f != null) {
    		op.removeIf((e)->e.getDate().compareTo(f) < 0);
    	}
    	if(t != null) {
    		op.removeIf((e)->e.getDate().compareTo(t) > 0);
    	}
    	return op;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
    	if(loggedInUser == null) {
        	throw new UnauthorizedException();        	
        }
    	double balance = 0;
    	for(BalanceOperation op : balanceOperations.values()) {
    		balance += op.getMoney();
    	}
        return balance;
    }
}
