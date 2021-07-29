package it.polito.ezshop.DBManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.polito.ezshop.data.*;
import it.polito.ezshop.model.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DBManager implements IDBManager{
	
	private static final String url = "jdbc:sqlite:src/ezDb.db";
	public static final String dbPath = "src/ezDb.db";
	private static final String saleTCreationQry = "CREATE TABLE saleTransactions (ID INT PRIMARY KEY NOT NULL,price DOUBLE NOT NULL,discountRate DOUBLE DEFAULT 0,state VARCHAR(90))";
	private static final String salePCreationQry = "CREATE TABLE saleProducts (saleID INT NOT NULL,barCode VARCHAR(14) NOT NULL,amount INT NOT NULL,pricePerUnit DOUBLE NOT NULL,discountRate DOUBLE DEFAULT 0,productDescription VARCHAR(150), PRIMARY KEY(saleID, barCode), FOREIGN KEY (saleID) REFERENCES saleTransactions (ID) ON DELETE CASCADE, FOREIGN KEY (barCode) REFERENCES productType (barCode) ON DELETE CASCADE)";
	private static final String saleRCreationQry = "CREATE TABLE saleProductRFIDs (saleID INT NOT NULL, barCode VARCHAR(14) NOT NULL, rfid VARCHAR(12) NOT NULL, PRIMARY KEY(saleID, barCode, rfid), FOREIGN KEY (saleID) REFERENCES saleTransactions (ID) ON DELETE CASCADE, FOREIGN KEY (barCode) REFERENCES productType (barCode) ON DELETE CASCADE)";
	private static final String returnTCreationQry = "CREATE TABLE returnTransactions (ID INT PRIMARY KEY NOT NULL,saleID INT NOT NULL,state VARCHAR(90))";
	private static final String returnPCreationQry = "CREATE TABLE returnProducts (returnID INT NOT NULL,barCode VARCHAR(14) NOT NULL,amount INT NOT NULL,pricePerUnit DOUBLE NOT NULL,discountRate DOUBLE DEFAULT 0,productDescription VARCHAR(150), PRIMARY KEY(returnID, barCode), FOREIGN KEY (returnID) REFERENCES returnTransactions (ID) ON DELETE CASCADE, FOREIGN KEY (barCode) REFERENCES productType (barCode) ON DELETE CASCADE)";
	private static final String returnRCreationQry = "CREATE TABLE returnProductRFIDs (returnID INT NOT NULL,barCode VARCHAR(14) NOT NULL,rfid VARCHAR(12) NOT NULL, PRIMARY KEY(returnID,barCode, rfid), FOREIGN KEY (returnID) REFERENCES returnTransactions (ID) ON DELETE CASCADE, FOREIGN KEY (barCode) REFERENCES productType (barCode) ON DELETE CASCADE)";
	private static final String userTCreationQry = "CREATE TABLE users (ID INT PRIMARY KEY NOT NULL, username VARCHAR(256), password VARCHAR(256), role VARCHAR(256))";
	private static final String prodTCreationQry = "CREATE TABLE productType (ID INT PRIMARY KEY NOT NULL, productDescription VARCHAR(256), barCode VARCHAR(14) UNIQUE NOT NULL, pricePerUnit DOUBLE, note VARCHAR(256), location VARCHAR(256), quantity INT)";
	private static final String balanceOpCreationQry ="CREATE TABLE balanceOperations (ID INT PRIMARY KEY NOT NULL, money DOUBLE, type VARCHAR(7), date DATE)";
	private static final String customerCreationQry = "CREATE TABLE customers (id INT PRIMARY KEY NOT NULL, name VARCHAR(256) UNIQUE NOT NULL, loyaltyCard VARCHAR(10) UNIQUE DEFAULT NULL)";
	private static final String loyaltyCardCreationQry = "CREATE TABLE loyaltyCards (id VARCHAR(10) PRIMARY KEY NOT NULL, points INT DEFAULT 0)";
	private static final String orderCreationQry = "CREATE TABLE orders (id INT PRIMARY KEY NOT NULL, productCode VARCHAR(256) NOT NULL, pricePerUnit DOUBLE NOT NULL, quantity INT DEFAULT 0, status VARCHAR(256) NOT NULL)";
	private static final String rfidCreationQry = "CREATE TABLE rfids (RFID VARCHAR(12) PRIMARY KEY NOT NULL, productCode VARCHAR(256) NOT NULL)";
	
	private static final String saleTRemovalQry = "DELETE FROM saleTransactions";
	private static final String salePRemovalQry = "DELETE FROM saleProducts";
	private static final String returnTRemovalQry = "DELETE FROM returnTransactions";
	private static final String returnPRemovalQry = "DELETE FROM returnProducts";
	private static final String userTRemovalQry = "DELETE FROM users";
	private static final String productRemovalQry = "DELETE FROM productType";
	private static final String balanceOpRemovalQry = "DELETE FROM balanceOperations";
	private static final String customerRemovalQry = "DELETE FROM customers";
	private static final String loyaltyCardRemovalQry = "DELETE FROM loyaltyCards";
	private static final String orderRemovalQry = "DELETE FROM orders";	
	private static final String rfidRemovalQry = "DELETE FROM rfids";
	
	/**
	 * load the JDBC driver, create and setup the DB if it is not present
	 */
	public DBManager() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		File f = new File(dbPath);
		if(!f.exists()) {
			try {
				f.createNewFile();
				if(setupDB())
					System.out.println("db created successfully");
				else
					System.out.println("db created with problems on setup");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
	}
	
	/**
	 * create the connection to the local DB
	 * @return DB connection
	 * @throws SQLException
	 */
	private Connection connect() throws SQLException{
		return DriverManager.getConnection(url);		
	}

	@Override
	public boolean setupDB() {
		boolean success=true;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = connect();
			stmt = conn.createStatement();			
			stmt.executeUpdate(saleTCreationQry);
			stmt.executeUpdate(salePCreationQry);
			stmt.executeUpdate(saleRCreationQry);
			stmt.executeUpdate(returnTCreationQry);
			stmt.executeUpdate(returnPCreationQry);
			stmt.executeUpdate(returnRCreationQry);
			stmt.executeUpdate(userTCreationQry);
			stmt.executeUpdate(prodTCreationQry);
			stmt.executeUpdate(balanceOpCreationQry);
			stmt.executeUpdate(customerCreationQry);
			stmt.executeUpdate(loyaltyCardCreationQry);
			stmt.executeUpdate(orderCreationQry);
			stmt.executeUpdate(rfidCreationQry);
			stmt.close();
		}catch (SQLException ex) {
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}
	
	@Override
	public boolean resetStoreInfo() {
		boolean success=true;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = connect();
			stmt = conn.createStatement();			
			stmt.executeUpdate(saleTRemovalQry);
			stmt.executeUpdate(salePRemovalQry);
			stmt.executeUpdate(returnTRemovalQry);
			stmt.executeUpdate(returnPRemovalQry);
			stmt.executeUpdate(productRemovalQry);
			stmt.executeUpdate(balanceOpRemovalQry);			
			stmt.executeUpdate(userTRemovalQry);
			stmt.executeUpdate(productRemovalQry);
			stmt.executeUpdate(balanceOpRemovalQry);
			stmt.executeUpdate(customerRemovalQry);
			stmt.executeUpdate(loyaltyCardRemovalQry);
			stmt.executeUpdate(orderRemovalQry);
			stmt.executeUpdate(rfidRemovalQry);
			stmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<User> getAllUsers() {
		String sql = "SELECT ID,username,password,role FROM users";

		List<User> lista = new ArrayList<User>();
		Connection conn = null;
		Statement stmt = null;
		try {
			User user;
			conn = connect();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				user = new UserObj(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				lista.add(user);
			}
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			lista = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				lista = null;
			}
		}
		System.out.println("Get all User DB successful");
		return lista;
	}

	@Override
	public boolean addUser(User user) {
		String sql = "INSERT INTO users (ID,username,password,role) VALUES (?,?,?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getRole());
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		System.out.println("Add User into DB successful");
		return success;
	}

	@Override
	public boolean updateUserRights(Integer id, String role) {
		String sql = "UPDATE users SET role=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1,role);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}


	@Override
	public boolean deleteUser(Integer id) {
		String sql = "DELETE FROM users WHERE id=?";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}
	
	@Override
	public User getUserByUsername (String Username) {
		String sql = "SELECT ID,username,password,role FROM users WHERE username=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		User u = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1,Username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				 u = new UserObj(rs.getInt(1), rs.getString(2),  rs.getString(3),  rs.getString(4));
			}
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return u;
	}
	
	@Override
	public List<ProductType> getAllProducts() { 
		String sql = "SELECT ID, productDescription, barCode, pricePerUnit, note, location, quantity FROM productType";
		List<ProductType> list = new ArrayList<ProductType>();
		Connection conn = null;
		Statement stmt = null;
		try {
			ProductTypeObj product;
			conn = connect();
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				product = new ProductTypeObj(rs.getInt(1), rs.getString(2), rs.getString(3),  rs.getDouble(4), rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setQuantity(rs.getInt(7));
				list.add(product);
			}
			System.out.println("Get all Product DB successful");
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			list = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				list = null;
			}
		}
		return list;
	}


	@Override
	public boolean addProduct(ProductType product) {
		String sql = "INSERT INTO productType (ID,productDescription, barCode, pricePerUnit, note, location, quantity) VALUES (?,?,?,?,?,?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, product.getId());
			pstmt.setString(2, product.getProductDescription());
			pstmt.setString(3, product.getBarCode());
			pstmt.setDouble(4, product.getPricePerUnit());
			pstmt.setString(5, product.getNote());
			pstmt.setString(6, product.getLocation());
			pstmt.setInt(7, product.getQuantity());
			
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) {
		String sql = "UPDATE productType SET productDescription=?, barCode=?, pricePerUnit=?, note=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, newDescription);
			pstmt.setString(2, newCode);
			pstmt.setDouble(3, newPrice);
			pstmt.setString(4, newNote);
			pstmt.setInt(5, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}
	@Override
	public boolean updateProductQuantity(Integer id, int toBeAdded) {
		String sql = "UPDATE productType SET quantity=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, toBeAdded);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}
	
	@Override
	public boolean updateProductPosition(Integer id, String newPos){
		String sql = "UPDATE productType SET location=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, newPos);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean deleteProduct(Integer id) {
		String sql = "DELETE FROM productType WHERE id=?";
		String sql1 = "SELECT barCode FROM productType WHERE id=?";
		String sql2 = "DELETE FROM rfids WHERE productCode=?";
		
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			
			conn = connect();
			pstmt = conn.prepareStatement(sql1);		
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			int barCode = rs.getInt(1);
			pstmt.close();
		
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql2);		
			pstmt.setInt(1, barCode);
			pstmt.executeUpdate();
			pstmt.close();
			
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}
	
	@Override
	public List<ReturnTransactionObj> getAllReturnTransactions() {
		String sql = "SELECT r.ID,r.state,s.ID,s.state,s.price,s.discountRate FROM returnTransactions r, saleTransactions s WHERE s.ID = r.saleID";
		String sql2 = "SELECT barCode,amount,pricePerUnit,discountRate,productDescription FROM saleProducts WHERE saleID = ?";
		String sql3 = "SELECT barCode,amount,pricePerUnit,discountRate,productDescription FROM returnProducts WHERE returnID = ?";
		String sql4 = "SELECT rfid FROM returnProductRFIDs WHERE returnID = ? AND barCode = ?";
		
		List<ReturnTransactionObj> list = new LinkedList<>();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null,pstmt2 = null;
		try {
			ReturnTransactionObj r;
			TicketEntry t;
			
			conn = connect();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs2,rs3;
			while(rs.next()) {
				r=new ReturnTransactionObj();
				r.setId(rs.getInt(1));
				r.setStateStr(rs.getString(2));
				SaleTransactionObj s = new SaleTransactionObj();
				s.setTicketNumber(rs.getInt(3));
				s.setStateStr(rs.getString(4));
				s.setPrice(rs.getDouble(5));
				s.setDiscountRate(rs.getDouble(6));
				
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1,s.getTicketNumber());
				rs2 = pstmt.executeQuery();
				while(rs2.next()) {
					t=new TicketEntryObj(rs2.getString(1),rs2.getString(5));
					t.setAmount(rs2.getInt(2));
					t.setPricePerUnit(rs2.getDouble(3));
					t.setDiscountRate(rs2.getDouble(4));
					s.addEntry(t);
				}
				pstmt.close();
				
				r.setSale(s);
				
				pstmt = conn.prepareStatement(sql3);				
				pstmt.setInt(1,r.getId());
				
				pstmt2 = conn.prepareStatement(sql4);
				pstmt2.setInt(1,r.getId());				
				rs2 = pstmt.executeQuery();
				while(rs2.next()) {
					t=new TicketEntryObj(rs2.getString(1),rs2.getString(5));
					t.setAmount(rs2.getInt(2));
					t.setPricePerUnit(rs2.getDouble(3));
					t.setDiscountRate(rs2.getDouble(4));
					r.addEntry(t);
					pstmt2.setString(1,t.getBarCode());
					rs3 = pstmt2.executeQuery();
					while(rs3.next()) {
						r.addRFID(t.getBarCode(), rs3.getString(1));
					}
					pstmt2.close();
				}
				pstmt.close();
				list.add(r);
			}
			stmt.close();			
		}catch (SQLException ex) {
			list=null;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				list=null;
			}
		}
		return list;
	}

	@Override
	public boolean addReturnTransaction(ReturnTransactionObj r) {
		boolean success=true;
		String sql = "INSERT INTO returnTransactions (ID,state,saleID) VALUES (?,?,?)";
		String sql2 = "INSERT INTO returnProducts (returnID,barCode,amount,pricePerUnit,discountRate,productDescription) VALUES (?,?,?,?,?,?)";
		String sql3 = "INSERT INTO returnProductRFIDs (returnID,barCode,rfid) VALUES (?,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1,r.getId());
			pstmt.setString(2,r.getState().name());
			pstmt.setDouble(3,r.getSale().getTicketNumber());
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("ReturnTransaction added in the DB: "+r.getId()+" "+r.getState().name()+" "+r.getSale().getTicketNumber());
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1,r.getId());
			
			List<TicketEntry> entries = r.getEntries();
			TicketEntry entry;
			for(int i=0;i<entries.size();i++) {
				entry = entries.get(i);
				pstmt.setString(2,entry.getBarCode());
				pstmt.setInt(3,entry.getAmount());
				pstmt.setDouble(4,entry.getPricePerUnit());
				pstmt.setDouble(5,entry.getDiscountRate());
				pstmt.setString(6,entry.getProductDescription());
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1,r.getId());
			Iterator<Entry<String,List<String>>> it = r.getRFIDs().entrySet().iterator();
			Entry<String,List<String>> entry2;
			while(it.hasNext()){
				entry2 = it.next();
				pstmt.setString(2,entry2.getKey());
				for(int i=0;i<entry2.getValue().size();i++) {
					pstmt.setString(3,entry2.getValue().get(i));
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
		}catch (SQLException ex) {
			success=false;ex.printStackTrace();
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean deleteReturnTransaction(Integer id) {
		boolean success=true;
		String sql = "DELETE FROM returnTransactions WHERE ID = ?";
		String sql2 = "DELETE FROM returnProducts WHERE returnID = ?";
		String sql3 = "DELETE FROM returnProductRFIDs WHERE returnID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<Order> getAllOrders() {
		String sql = "SELECT id, productCode, pricePerUnit, quantity, status FROM orders";
		List<Order> list = new LinkedList<Order>();
		Connection conn = null;
		Statement stmt = null;
		try {
			OrderObj c;
			conn = connect();
			stmt = conn.createStatement();

			System.out.println("1");
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("2");
			while (rs.next()) {
				c = new OrderObj(rs.getInt(1), rs.getString(2), rs.getInt(4), rs.getDouble(3));
				c.setStatus(rs.getString(5));
				list.add(c);
			}
			System.out.println("Get all Orders from DB successful");
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			list = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				list = null;
			}
		}
		return list;
	}

	@Override
	public boolean addOrder(Order o) {
		String sql = "INSERT INTO orders (id, productCode, pricePerUnit, quantity, status) VALUES (?,?,?,?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, o.getOrderId());
			pstmt.setString(2, o.getProductCode());
			pstmt.setDouble(3, o.getPricePerUnit());
			pstmt.setInt(4, o.getQuantity());
			pstmt.setString(5, o.getStatus());
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean updateOrder(Integer id, String productCode, Integer quantity, double pricePerUnit, String status) {
		String sql = "UPDATE orders SET productCode=?, quantity=?, pricePerUnit=?, status=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, productCode);
			pstmt.setInt(2, quantity);
			pstmt.setDouble(3, pricePerUnit);
			pstmt.setString(4, status);
			pstmt.setInt(5, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<CustomerObj> getAllCustomers() {
		String sql = "SELECT id, name, loyaltyCard FROM customers";
		List<CustomerObj> list = new LinkedList<CustomerObj>();
		Connection conn = null;
		Statement stmt = null;
		try {
			CustomerObj c;
			conn = connect();
			stmt = conn.createStatement();

			System.out.println("1");
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("2");
			while (rs.next()) {
				c = new CustomerObj(rs.getInt(1), rs.getString(2));
				c.setCustomerCard(rs.getString(3));
				list.add(c);
			}
			System.out.println("Get all Customers from DB successful");
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			list = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				list = null;
			}
		}
		return list;
	}

	@Override
	public boolean addCustomer(CustomerObj c) {
		String sql = "INSERT INTO customers (id, name, loyaltyCard) VALUES (?,?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, c.getId());
			pstmt.setString(2, c.getCustomerName());
			pstmt.setString(3, c.getCustomerCard());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
		
	}

	@Override
	public boolean updateCustomer(Integer id, String name, String card) {
		String sql = "UPDATE customers SET name=?, loyaltyCard=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, name);
			pstmt.setString(2, card);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean deleteCustomer(Integer id) {
		String sql = "DELETE FROM customers WHERE id=?";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<SaleTransactionObj> getAllSaleTransactions() {
		String sql = "SELECT ID,price,discountRate,state FROM saleTransactions ORDER BY ID";
		String sql2 = "SELECT barCode,amount,pricePerUnit,discountRate,productDescription FROM saleProducts WHERE saleID = ?";
		String sql3 = "SELECT rfid FROM saleProductRFIDs WHERE saleID = ? AND barCode = ?";
		//TODO: work here
		List<SaleTransactionObj> list = new LinkedList<>();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		try {
			SaleTransactionObj s;
			TicketEntry t;
			
			conn = connect();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs2;
			ResultSet rs3;
			while(rs.next()) {
				s=new SaleTransactionObj();
				s.setTicketNumber(rs.getInt(1));
				s.setPrice(rs.getDouble(2));
				s.setDiscountRate(rs.getDouble(3));
				s.setStateStr(rs.getString(4));
				
				pstmt = conn.prepareStatement(sql2);
				pstmt2 = conn.prepareStatement(sql3);
				pstmt.setInt(1,rs.getInt(1));
				pstmt2.setInt(1, rs.getInt(1));	
				rs2 = pstmt.executeQuery();
				
				while(rs2.next()) {
					t=new TicketEntryObj(rs2.getString(1),rs2.getString(5));
					t.setAmount(rs2.getInt(2));
					t.setPricePerUnit(rs2.getDouble(3));
					t.setDiscountRate(rs2.getDouble(4));
					s.addEntry(t);
					
					// rfids
					pstmt2.setString(2, rs2.getString(1));
					rs3 = pstmt2.executeQuery();
					while(rs3.next())
						s.addRFID(rs2.getString(1), rs3.getString(1));
					pstmt2.close();
				}
				
				pstmt.close();
				list.add(s);
			}
			stmt.close();			
		}catch (SQLException ex) {
			list=null;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				list=null;
			}
		}
		return list;
	}

	@Override
	public boolean addSaleTransaction(SaleTransactionObj s) {
		boolean success=true;
		String sql = "INSERT INTO saleTransactions (ID,price,discountRate,state) VALUES (?,?,?,?)";
		String sql2 = "INSERT INTO saleProducts (saleID,barCode,amount,pricePerUnit,discountRate,productDescription) VALUES (?,?,?,?,?,?)";
		String sql3 = "INSERT INTO saleProductRFIDs (saleID, barCode, rfid) VALUES (?,?,?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1,s.getTicketNumber());
			pstmt.setDouble(2,s.getPrice());
			pstmt.setDouble(3,s.getDiscountRate());
			pstmt.setString(4,s.getState().name());
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Sale added in the DB: "+s.getTicketNumber()+" "+s.getPrice()+" "+s.getDiscountRate());
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1,s.getTicketNumber());
			
			List<TicketEntry> entries = s.getEntries();
			TicketEntry entry;
			for(int i=0;i<entries.size();i++) {
				entry = entries.get(i);
				pstmt.setString(2,entry.getBarCode());
				pstmt.setInt(3,entry.getAmount());
				pstmt.setDouble(4,entry.getPricePerUnit());
				pstmt.setDouble(5,entry.getDiscountRate());
				pstmt.setString(6,entry.getProductDescription());
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, s.getTicketNumber());
			Iterator<Entry<String,List<String>>> it = s.getRFIDs().entrySet().iterator();
			Entry<String,List<String>> entry2;
			while(it.hasNext()){
				entry2 = it.next();
				pstmt.setString(2,entry2.getKey());
				for(int i=0;i<entry2.getValue().size();i++) {
					pstmt.setString(3,entry2.getValue().get(i));
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}
	
	@Override
	public boolean updateSaleTransactionAfterReturn(SaleTransactionObj s, ReturnTransactionObj r) {
		boolean success=true;
		String sql = "UPDATE saleTransactions SET price=? WHERE ID=?";
		String sql2 = "UPDATE saleProducts SET amount=? WHERE saleID=? AND barCode=?";
		String sql3 = "DELETE FROM saleProductRFIDs WHERE saleID = ? AND barCode = ? AND rfid = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);			
			pstmt.setDouble(1,s.getPrice());
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(2,s.getTicketNumber());
			
			List<TicketEntry> entries = s.getEntries();
			TicketEntry entry;
			for(int i=0;i<entries.size();i++) {
				entry = entries.get(i);
				pstmt.setInt(1,entry.getAmount());
				pstmt.setString(3,entry.getBarCode());				
				pstmt.executeUpdate();
				System.out.println("Sale item "+ entry.getBarCode() +" amount changed in the DB: "+entry.getAmount());
			}
			pstmt.close();	
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, s.getTicketNumber());
			Iterator<Entry<String,List<String>>> it = r.getRFIDs().entrySet().iterator();
			Entry<String,List<String>> entry2;
			while(it.hasNext()){
				entry2 = it.next();
				pstmt.setString(2,entry2.getKey());
				for(int i=0;i<entry2.getValue().size();i++) {
					pstmt.setString(3,entry2.getValue().get(i));
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean deleteSaleTransaction(Integer id) {
		boolean success=true;
		String sql = "DELETE FROM saleTransactions WHERE ID = ?";
		//saleProduct should automatically be removed due to the TRUNCATE foreign key but in this version is better to do it explicitly
		String sql2 = "DELETE FROM saleProducts WHERE saleID = ?";
		String sql3 = "DELETE FROM saleProductRFIDs WHERE saleID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1,id);
			pstmt.executeUpdate();
			pstmt.close();
			
			System.out.println("Sale removed from DB: "+id);
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<BalanceOperation> getAllBalanceOperations() {
		String sql = "SELECT ID,money,date FROM balanceOperations";

		List<BalanceOperation> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			BalanceOperation op;
			conn = connect();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				op = new BalanceOperationObj(rs.getInt(1), rs.getDouble(2));
				op.setDate(rs.getDate(3).toLocalDate());
				list.add(op);
			}
			System.out.println("Get all BalanceOperation DB successful");
			stmt.close();
		} catch (SQLException ex) {
			list = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				list = null;
			}
		}
		return list;
	}

	@Override
	public boolean recordBalanceUpdate(BalanceOperation balanceOperation){
		String sql = "INSERT INTO balanceOperations (ID,money,type,date) VALUES (?,?,?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setInt(1, balanceOperation.getBalanceId());
			pstmt.setDouble(2, balanceOperation.getMoney());
			pstmt.setString(3, balanceOperation.getType());
			pstmt.setDate(4, Date.valueOf(balanceOperation.getDate()));
			pstmt.executeUpdate();
			System.out.println("Balance Operation recorded successful");
			pstmt.close();
		}catch (SQLException ex) {ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				success=false;
			}
		}
		return success;
	}

	@Override
	public List<LoyaltyCard> getAllLoyaltyCards() {
		String sql = "SELECT id, points FROM loyaltyCards";
		List<LoyaltyCard> list = new ArrayList<LoyaltyCard>();
		Connection conn = null;
		Statement stmt = null;
		try {
			LoyaltyCard l;
			conn = connect();
			stmt = conn.createStatement();

			System.out.println("1");
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("2");
			while (rs.next()) {
				l = new LoyaltyCard(rs.getString(1));
				l.setPoints(rs.getInt(2));
				list.add(l);
			}
			System.out.println("Get all LoyaltyCards DB successful");
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			list = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				list = null;
			}
		}
		return list;
	}

	@Override
	public boolean addLoyaltyCard(LoyaltyCard c) {
		String sql = "INSERT INTO loyaltyCards (id, points) VALUES (?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, c.getCardId());
			pstmt.setInt(2, c.getPoints());
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean updateLoyaltyCard(String id, Integer points) {
		String sql = "UPDATE loyaltyCards SET points=? WHERE id=?";
		boolean success=true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, points);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	
	@Override
	public boolean deleteLoyaltyCard(String id) {
		return false;
		/*String sql = "DELETE FROM loyaltyCards WHERE id=?";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;*/
	}

	@Override
	public Map<String, String> getAllRFIDs() {
		String sql = "SELECT RFID, productCode FROM rfids";
		Map<String, String> rfids = new HashMap<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = connect();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				rfids.put(rs.getString(1), rs.getString(2));
			}
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			rfids = null;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				rfids = null;
			}
		}
		System.out.println("Get all RFIDS DB successful");
		return rfids;
	}

	@Override
	public boolean addRFID(String rfid, String barCode) {
		String sql = "INSERT INTO rfids (RFID, productCode) VALUES (?,?)";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, rfid);
			pstmt.setString(2, barCode);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}

	@Override
	public boolean deleteRFID(String rfid) {
		String sql = "DELETE FROM rfids WHERE RFID=?";
		boolean success=true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connect();
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, rfid);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
			success=false;
		}finally {
			try {
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
				success=false;
			}
		}
		return success;
	}
}
