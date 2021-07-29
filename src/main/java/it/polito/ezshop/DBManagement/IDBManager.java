package it.polito.ezshop.DBManagement;

import java.util.List;
import java.util.Map;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.model.CustomerObj;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.ReturnTransactionObj;
import it.polito.ezshop.model.SaleTransactionObj;

public interface IDBManager {

	 /**
	 * create DB empty tables, with proper fields and relationships
	 * @return false if a problem has occurred; true otherwise
	 */
	public boolean setupDB();
	/**
	 * remove all products, transactions and balance operations
	 * @return false if a problem has occurred; true otherwise
	 */
	public boolean resetStoreInfo();
	
	public List<User> getAllUsers();
	public boolean addUser(User user);
	public boolean updateUserRights(Integer id, String role);
	public boolean deleteUser(Integer id);
	public User getUserByUsername (String Username);
	
	public List<ProductType> getAllProducts();
	public boolean addProduct(ProductType product);
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote);
	public boolean updateProductQuantity(Integer id, int toBeAdded);
	public boolean updateProductPosition(Integer id, String newPos);
	public boolean deleteProduct(Integer id);
	
	public Map<String, String> getAllRFIDs();	// Returns < RFID, BarCode >
	public boolean addRFID(String rfid, String barCode);
	public boolean deleteRFID(String rfid);
	
	public List<ReturnTransactionObj> getAllReturnTransactions();
	public boolean addReturnTransaction(ReturnTransactionObj r);
	public boolean deleteReturnTransaction(Integer id);
	
	public List<Order> getAllOrders();
	public boolean addOrder(Order o);
	public boolean updateOrder(Integer id, String productCode, Integer quantity, double pricePerUnit, String status);
	
	public List<CustomerObj> getAllCustomers();
	public boolean addCustomer(CustomerObj c);
	public boolean updateCustomer(Integer id, String name, String card);
	public boolean deleteCustomer(Integer id);
	
	public List<LoyaltyCard> getAllLoyaltyCards();
	public boolean addLoyaltyCard(LoyaltyCard c);
	public boolean updateLoyaltyCard(String id, Integer points);
	public boolean deleteLoyaltyCard(String id);
	
	public List<SaleTransactionObj> getAllSaleTransactions();
	public boolean addSaleTransaction(SaleTransactionObj s);
	public boolean updateSaleTransactionAfterReturn(SaleTransactionObj s, ReturnTransactionObj r);
	public boolean deleteSaleTransaction(Integer id);
	
	public List<BalanceOperation> getAllBalanceOperations();
	public boolean recordBalanceUpdate(BalanceOperation balanceOperation);
}
