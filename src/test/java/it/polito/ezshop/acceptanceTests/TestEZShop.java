package it.polito.ezshop.acceptanceTests;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import it.polito.ezshop.model.*;
import it.polito.ezshop.validator.*;
import it.polito.ezshop.DBManagement.DBManager;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class TestEZShop {
	
	public static final String dbBackupPath = "src/ezDbBackup.db";
	public static final String dbBackupPathTEST = "src/ezDbBackup2.db";
	public static UserObj root,cashier,shopManager;	
	
	private BalanceOperationObj Test1=new BalanceOperationObj(1,12.23);
	private CreditCardObj Test2=new CreditCardObj("1234asdFFGf12");
	private CustomerObj Test3=new CustomerObj(12,"qwdEFeiub123^$");
	private LoyaltyCard Test4=new LoyaltyCard("qwDAeiub123^$");
	private OrderObj Test5=new OrderObj(12,"iusd!@#baci23AD1",94,0.123);
	private ProductTypeObj Test6=new ProductTypeObj(123,"fuiADqtrg1234!#$","oifntg1928",32.250,"203FF#1r1a");
	private SaleTransactionObj Test7=new SaleTransactionObj();
	private TicketEntryObj Test8=new TicketEntryObj("qdiwo!@#n19284QEW","fuiADqtrg1234!#$");
	private UserObj Test9=new UserObj(31,"oainoqf","oav!@#","oab2r$");
	
	private EZShop ez = new EZShop();
	
	/*
	 * parameter setting
	 */
	private int a = 1223;
	private int b = -1223;
	Integer A = Integer.valueOf(a);
	Integer B = Integer.valueOf(b);
	
	List<TicketEntry> entrylst = new LinkedList<TicketEntry>();
	TreeMap<Integer, ProductType> inventorymap = new TreeMap<>();
	TreeMap<Integer,User> usermap = new TreeMap<>();
	
	/*
	 * Black box unit test
	 */
	@Test
	public void testBanlanceOperationObjSetBalanced() {		
		Test1.setBalanceId(-1223);
		Assert.assertEquals(Test1.getBalanceId(),-1223);
		
		Test1.setBalanceId(1223);
		Assert.assertEquals(Test1.getBalanceId(), 1223);
	}
	
	@Test
	public void testBanlanceOperationObjSetDate() {
		LocalDate Date=LocalDate.of(2007, 4, 12);
		Test1.setDate(Date);
		
		Assert.assertEquals(Test1.getDate(), Date);
	}
	
	@Test
	public void testBanlanceOperationObjSetMoney() {
		Test1.setMoney(-1.223);
		Assert.assertEquals(Test1.getMoney(),-1.223, 0.001);
		
		Test1.setMoney(1.223);
		Assert.assertEquals(Test1.getMoney(),1.223, 0.001);
	}
	
	@Test
    public void testBanlanceOperationObjSetType() {
		Test1.setType("qweqd");
		Assert.assertEquals(Test1.getType(),"DEBIT");
		
		Test1.setType("DEBIT");
		Assert.assertEquals(Test1.getType(),"DEBIT");
		
		Test1.setType("CREDIT");
		Assert.assertEquals(Test1.getType(),"CREDIT");
	}

	@Test
    public void testCreditCardObjSetMoney() {
		Test2.setMoney(-1.223);
		Assert.assertEquals(Test2.getMoney(),-1.223,0.001);
		
		Test2.setMoney(1.223);
		Assert.assertEquals(Test2.getMoney(),1.223,0.001);
	}

	@Test
    public void testCustomerObjSetCustomerName() {
		Test3.setCustomerName("ASd2a2");
		Assert.assertEquals(Test3.getCustomerName(),"ASd2a2");
		
		Test3.setCustomerName("");
		Assert.assertNotNull(Test3.getCustomerName());
	}
	
	@Test
    public void testCustomerObjSetCustomerCard() {
		Test3.setCustomerCard("ASd2a2");
		Assert.assertEquals(Test3.getCustomerCard(),"ASd2a2");
		
		Test3.setCustomerCard("");
		Assert.assertNull(Test3.getCustomerCard());
	}
	
	@Test
    public void testCustomerObjSetId() {
		Test3.setId(B);
		Assert.assertEquals(Test3.getId(),B);
		
		Test3.setId(A);
		Assert.assertEquals(Test3.getId(),A);
	}
	
	@Test
    public void testCustomerObjSetPoints() {
		Test3.setPoints(B);
		Assert.assertEquals(Test3.getPoints(), Integer.valueOf(0));
		
		Test3.setPoints(A);
		Assert.assertEquals(Test3.getPoints(), Integer.valueOf(0));
	}

	@Test
    public void testLoyaltyCardSetCardId() {
		Test4.setCardId("ASd2a2");
		Assert.assertEquals(Test4.getCardId(),"ASd2a2");
		
		Test4.setCardId("");
		Assert.assertNotNull(Test4.getCardId());	
	}
	
	@Test
    public void testLoyaltyCardSetPoints() {
		Test4.setPoints(1223);
		Assert.assertEquals(Test4.getPoints(), 1223);
		
		Test4.setPoints(-1223);
		Assert.assertEquals(Test4.getPoints(), -1223);
	}

	@Test
    public void testOrderObjSetBalanceId() {
		Test5.setBalanceId(B);
		Assert.assertEquals(Test5.getBalanceId(), B);
		
		Test5.setBalanceId(A);
		Assert.assertEquals(Test5.getBalanceId(), A);
	}
	
	@Test
    public void testOrderObjSetProductCode() {
		Test5.setProductCode("ASd2a2");
		Assert.assertEquals(Test5.getProductCode(),"ASd2a2");
		
		Test5.setProductCode("");
		Assert.assertNotNull(Test5.getProductCode());	
	}
	
	@Test
    public void testOrderObjSetPricePerUnit() {
		Test5.setPricePerUnit(-1.223);
		Assert.assertEquals(Test5.getPricePerUnit(),-1.223,0.001);
		
		Test5.setPricePerUnit(1.223);
		Assert.assertEquals(Test5.getPricePerUnit(),1.223,0.001);
	}
	
	@Test
    public void testOrderObjSetQuantity() {
		Test5.setQuantity(-1223);
		Assert.assertEquals(Test5.getQuantity(), -1223);
		
		Test5.setQuantity(1223);
		Assert.assertEquals(Test5.getQuantity(), 1223);
	}
	
	@Test
    public void testOrderObjSetStatus() {
		Test5.setStatus("payed");
		Assert.assertEquals(Test5.getStatus(),"payed");
		
		Test5.setStatus("completed");
		Assert.assertEquals(Test5.getStatus(),"completed");
		
		Test5.setStatus("issued");
		Assert.assertEquals(Test5.getStatus(),"issued");
		
		Test5.setStatus("dqkdq4!@k13");
		Assert.assertEquals(Test5.getStatus(),"issued");
	}
	
	
	@Test
    public void testOrderObjSetOrderId() {
		Test5.setOrderId(B);
		Assert.assertEquals(Test5.getOrderId(), B);
		
		Test5.setOrderId(A);
		Assert.assertEquals(Test5.getOrderId(), A);
	}

	@Test
    public void testProductTypeObjSetQuantity() {
		Test6.setQuantity(B);
		Assert.assertEquals(Test6.getQuantity(), B);
		
		Test6.setQuantity(A);
		Assert.assertEquals(Test6.getQuantity(), A);
	}
	
	@Test
    public void testProductTypeObjSetLocation() {
		Test6.setLocation("ASd2a2");
		Assert.assertEquals(Test6.getLocation(),"ASd2a2");
		
		Test6.setLocation("");
		Assert.assertNotNull(Test6.getLocation());	
	}
	
	@Test
    public void testProductTypeObjSetNote() {
		Test6.setNote("ASd2a2");
		Assert.assertEquals(Test6.getNote(),"ASd2a2");
		
		Test6.setNote("");
		Assert.assertNotNull(Test6.getNote());
	}
	
	@Test
    public void testProductTypeObjSetProductDescription() {
		Test6.setProductDescription("ASd2a2");
		Assert.assertEquals(Test6.getProductDescription(),"ASd2a2");
		
		Test6.setProductDescription("");
		Assert.assertNotNull(Test6.getProductDescription());
	}
	
	@Test
    public void testProductTypeObjSetBarCode() {
		Test6.setBarCode("ASd2a2");
		Assert.assertEquals(Test6.getBarCode(),"ASd2a2");
		
		Test6.setBarCode("");
		Assert.assertNotNull(Test6.getBarCode());
	}
	
	@Test
    public void testProductTypeObjSetPricePerUnit() {
		Test6.setPricePerUnit(-1.223);
		Assert.assertEquals(Test6.getPricePerUnit(),-1.223, 0.001);
		
		Test6.setPricePerUnit(1.223);
		Assert.assertEquals(Test6.getPricePerUnit(),1.223, 0.001);
	}
	
	@Test
    public void testProductTypeObjSetId() {
		Test6.setId(B);
		Assert.assertEquals(Test6.getId(), B);
		
		Test6.setId(A);
		Assert.assertEquals(Test6.getId(), A);
	}
	
	@Test
    public void testProductTypeObjVerifyString() {
		Assert.assertTrue(ProductTypeObj.verifyString("12-d"));
		Assert.assertTrue(ProductTypeObj.verifyString("12-d-e1ad-dqw"));
		Assert.assertTrue(ProductTypeObj.verifyString("121231-d-e1ad"));
		Assert.assertTrue(ProductTypeObj.verifyString("asd-ddiqn-213124"));
		Assert.assertTrue(ProductTypeObj.verifyString("!@3-ddiqn-213124"));
		Assert.assertTrue(ProductTypeObj.verifyString("123--213124"));
		Assert.assertTrue(ProductTypeObj.verifyString("123-123d-213124"));
		Assert.assertTrue(ProductTypeObj.verifyString("123-d!@#-213124"));
		Assert.assertTrue(ProductTypeObj.verifyString("123-ddiqn-123asd"));
		Assert.assertTrue(ProductTypeObj.verifyString("123-ddiqn-!@$123"));

		Assert.assertFalse(ProductTypeObj.verifyString("123-ddiqn-213124"));
	}
	
	@Test
    public void testProductTypeObjVerifyPosition() {
		inventorymap.put(1, Test6);
		Test6.setLocation("asdqwhvufqq123");
		Assert.assertTrue(ProductTypeObj.verifyPosition(inventorymap, "asdqwhvufqq123"));
		Assert.assertFalse(ProductTypeObj.verifyPosition(inventorymap, "Turin999"));
	}
	
	@Test
	public void testProductTypeObjSearchProductByBarCode() {
		inventorymap.put(1, Test6);
		Assert.assertEquals(ProductTypeObj.searchProductByBarCode(inventorymap, "oifntg1928"), Test6);
		Assert.assertNotEquals(ProductTypeObj.searchProductByBarCode(inventorymap, "ASd2a2"), Test6);
		
	}
	
	@Test
	public void testProductTypeObjSearchProductByDescription() {
		inventorymap.put(1, Test6);
		Test6.setProductDescription("iodnqwoidqnidqd");
		Assert.assertTrue(ProductTypeObj.searchProductDescription(inventorymap, "iodnqwoidqnidqd").contains(Test6));
		Assert.assertFalse(ProductTypeObj.searchProductDescription(inventorymap, "ASd2a2").contains(Test6));
		
	}
	
	@Test
    public void testTickeyEntryObjsetBarCode() {
		Test8.setBarCode("1.2231dsaas!@#");
		Assert.assertEquals(Test8.getBarCode(),"1.2231dsaas!@#");
		
		Test8.setBarCode("");
		Assert.assertNotNull(Test8.getBarCode());
	}
	
	@Test
    public void testTickeyEntryObjsetProductDescription() {
		Test8.setProductDescription("ASd2a2");
		Assert.assertEquals(Test8.getProductDescription(),"ASd2a2");
		
		Test8.setProductDescription("");
		Assert.assertNotNull(Test8.getProductDescription());	
	}
	
	@Test
    public void testTickeyEntryObjsetAmount() {
		Test8.setAmount(-1223);
		Assert.assertEquals(Test8.getAmount(), -1223);
		
		Test8.setAmount(1223);
		Assert.assertEquals(Test8.getAmount(), 1223);
	}
	
	@Test
    public void testTickeyEntryObjsetPricePerUnit() {
		Test8.setPricePerUnit(-1.223);
		Assert.assertEquals(Test8.getPricePerUnit(),-1.223,0.001);
		
		Test8.setPricePerUnit(1.223);
		Assert.assertEquals(Test8.getPricePerUnit(),1.223,0.001);		
	}
	
	@Test
    public void testTickeyEntryObjsetDiscountRate() {
		Test8.setDiscountRate(-1.223);
		Assert.assertEquals(Test8.getDiscountRate(),-1.223,0.001);
		
		Test8.setDiscountRate(1.223);
		Assert.assertEquals(Test8.getDiscountRate(),1.223,0.001);
	}
	
	@Test
    public void testUserObjVerifyUsername() {
		usermap.put(1, Test9);
		Assert.assertTrue(UserObj.verifyUsername(usermap, "oainoqf"));
		Assert.assertFalse(UserObj.verifyUsername(usermap, "Adam"));
	}
	@Test
    public void testUserObjsetId() {
		Test9.setId(B);
		Assert.assertEquals(Test9.getId(),B);
		
		Test9.setId(A);
		Assert.assertEquals(Test9.getId(),A);
	}
	@Test
    public void testUserObjsetUsername() {
		Test9.setUsername("1.2231dsaas!@#");
		Assert.assertEquals(Test9.getUsername(),"1.2231dsaas!@#");
		
		Test9.setUsername("");
		Assert.assertNotNull(Test9.getUsername());	
	}
	@Test
    public void testUserObjsetPassword() {
		Test9.setPassword("1.2231dsaas!@#");
		Assert.assertEquals(Test9.getPassword(),"1.2231dsaas!@#");
		
		Test9.setPassword("");
		Assert.assertNotNull(Test9.getPassword());	
	}
	@Test
    public void testUserObjsetRole() {
		Test9.setRole("1.2231dsaas!@#");
		Assert.assertEquals(Test9.getRole(),"1.2231dsaas!@#");
		
		Test9.setRole("");
		Assert.assertNotNull(Test9.getRole());	
	}

	@Test
    public void testCreditCardValidatorValidate() {
		Assert.assertFalse(CreditCardValidator.validate("123"));
		Assert.assertFalse(CreditCardValidator.validate("51002939910530091"));
		
		Assert.assertTrue(CreditCardValidator.validate("5100293991053009"));
	}
	
	/*
	 * White box unit test
	 */	
	
	@Test
    public void testBarCodeValidatorValidate() {
		Assert.assertFalse(BarCodeValidator.validate("5600182087295"));
		Assert.assertFalse(BarCodeValidator.validate("123123123"));
		Assert.assertFalse(BarCodeValidator.validate("123123123123123123"));
		Assert.assertFalse(BarCodeValidator.validate("1231ascac232"));
		Assert.assertFalse(BarCodeValidator.validate("1231!@$!1231"));
		
		Assert.assertTrue(BarCodeValidator.validate("5600182087995"));
	}
	
	@Test
    public void testLoyaltyCardCodeValidatorValidate() {
		Assert.assertFalse(LoyaltyCardCodeValidator.validate("123"));
		Assert.assertFalse(LoyaltyCardCodeValidator.validate("12345678901"));
		Assert.assertFalse(LoyaltyCardCodeValidator.validate("asdfghjklz"));
		
		Assert.assertTrue(LoyaltyCardCodeValidator.validate("1234567890"));
	}
	
	/*
	 * Step 2, tests to cover classes with dependencies
	 */		
	
	@Test
	public void testSaleTransactionObjSetState() {
		Test7.setState(SaleTransactionObj.State.open);
		Assert.assertEquals(Test7.getState(),SaleTransactionObj.State.open);
		Test7.setState(SaleTransactionObj.State.closed);
		Assert.assertEquals(Test7.getState(),SaleTransactionObj.State.closed);
		Test7.setState(SaleTransactionObj.State.payed);
		Assert.assertEquals(Test7.getState(),SaleTransactionObj.State.payed);
	}
	
	@Test
	public void testSaleTransactionObjSetTicketNumber() {
		Test7.setTicketNumber(B);
		Assert.assertEquals(Test7.getTicketNumber(), B);
		
		Test7.setTicketNumber(A);
		Assert.assertEquals(Test7.getTicketNumber(), A);
	}	
	
	@Test
	public void testSaleTransactionObjSetDiscountRate() {
		Test7.setDiscountRate(-1.223);
		Assert.assertEquals(Test7.getDiscountRate(),-1.223,0.001);
		
		Test7.setDiscountRate(0.223);
		Assert.assertEquals(Test7.getDiscountRate(),0.223,0.001);
		
		Test7.setDiscountRate(1.223);
		Assert.assertEquals(Test7.getDiscountRate(),1.223,0.001);
	}
	
	@Test
	public void testSaleTransactionObjSetPrice() {
		Test7.setPrice(-1.223);
		Assert.assertEquals(Test7.getPrice(),-1.223,0.001);
		
		Test7.setPrice(1.223);
		Assert.assertEquals(Test7.getPrice(),1.223,0.001);
	}
	
	@Test
    public void testSaleTransactionObjSetStateStr() {
		SaleTransactionObj a=new SaleTransactionObj();
		a.setStateStr("open");
		Assert.assertEquals(a.getState(),SaleTransactionObj.State.open);
		a.setStateStr("closed");
		Assert.assertEquals(a.getState(),SaleTransactionObj.State.closed);
		a.setStateStr("payed");
		Assert.assertEquals(a.getState(),SaleTransactionObj.State.payed);
	}
	
	@Test
	public void testSaleTransactionObjSetEntries() {
		entrylst.add(Test8);
		Test7.setEntries(entrylst);
		Assert.assertEquals(Test7.getEntries(), entrylst);
	}
	
	@Test
	public void testSaleTransactionObjAddEntry() {
		TicketEntryObj te = new TicketEntryObj("aaa","bbb");
		te.setPricePerUnit(10.5);
		te.setAmount(5);
		te.setDiscountRate(10);
		SaleTransactionObj s = new SaleTransactionObj();
		s.addEntry(te);
		Assert.assertEquals(s.getEntries().size(),1);
		Assert.assertEquals(s.getEntries().get(0).getBarCode(),"aaa");
		Assert.assertEquals(s.getEntries().get(0).getProductDescription(),"bbb");
		Assert.assertEquals(s.getEntries().get(0).getPricePerUnit(),10.5,0.001);
		Assert.assertEquals(s.getEntries().get(0).getAmount(),5);
		Assert.assertEquals(s.getEntries().get(0).getDiscountRate(),10,0.001);
	}
	
	@Test
	public void testSaleTransactionObjRemoveEntry() {
		TicketEntryObj te = new TicketEntryObj("aaa","bbb");
		SaleTransactionObj s = new SaleTransactionObj();
		s.addEntry(te);
		s.removeEntry(te.getBarCode());
		Assert.assertEquals(s.getEntries().size(),0);
	}
	
	@Test
	public void testSaleTransactionObjGetEntries() {
		TicketEntryObj te = new TicketEntryObj("aaa","bbb");
		TicketEntryObj te2 = new TicketEntryObj("ccc","ddd");
		SaleTransactionObj s = new SaleTransactionObj();
		s.addEntry(te);
		s.addEntry(te2);
		Assert.assertEquals(s.getEntries().size(),2);
	}
	
	@Test
	public void testSaleTransactionGetFinalPrice() {
		SaleTransactionObj s = new SaleTransactionObj();
		s.setPrice(150);
		s.setDiscountRate(0.1);
		Assert.assertEquals(s.getFinalPrice(),150-15,0.001);
	}
	
	
	
	@Test
	public void testReturnTransactionObjSetId() {
		ReturnTransactionObj r = new ReturnTransactionObj();
		r.setId(123);
		Assert.assertEquals(r.getId(), Integer.valueOf(123));
		r.setId(984);
		Assert.assertEquals(r.getId(), Integer.valueOf(984));
	}
	
	@Test
    public void testReturnTransactionObjSetState() {
		ReturnTransactionObj a=new ReturnTransactionObj();
		a.setState(ReturnTransactionObj.State.open);
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.open);
		a.setState(ReturnTransactionObj.State.closed);
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.closed);
		a.setState(ReturnTransactionObj.State.payed);
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.payed);
	}
	
	@Test
    public void testReturnTransactionObjSetStateStr() {
		ReturnTransactionObj a=new ReturnTransactionObj();
		a.setStateStr("open");
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.open);
		a.setStateStr("closed");
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.closed);
		a.setStateStr("payed");
		Assert.assertEquals(a.getState(),ReturnTransactionObj.State.payed);
	}
	
	@Test
	public void testReturnTransactionObjSetSale() {
		SaleTransactionObj s = new SaleTransactionObj();
		ReturnTransactionObj r = new ReturnTransactionObj();
		r.setSale(s);
		Assert.assertEquals(r.getSale(), s);
	}
	
	@Test
	public void testReturnTransactionObjAddEntry() {
		TicketEntryObj te = new TicketEntryObj("aaa","bbb");
		te.setPricePerUnit(10.5);
		te.setAmount(5);
		te.setDiscountRate(10);
		ReturnTransactionObj r = new ReturnTransactionObj();		
		r.addEntry(te);
		Assert.assertEquals(r.getEntries().size(),1);
		Assert.assertEquals(r.getEntries().get(0).getBarCode(),"aaa");
		Assert.assertEquals(r.getEntries().get(0).getProductDescription(),"bbb");
		Assert.assertEquals(r.getEntries().get(0).getPricePerUnit(),10.5,0.001);
		Assert.assertEquals(r.getEntries().get(0).getAmount(),5);
		Assert.assertEquals(r.getEntries().get(0).getDiscountRate(),10,0.001);
	}
	
	@Test
	public void testReturnTransactionObjCalculateDueMoney() {
		TicketEntryObj te = new TicketEntryObj("aaa","bbb");
		te.setPricePerUnit(10.5);
		te.setAmount(5);
		te.setDiscountRate(0.1);
		TicketEntryObj te2 = new TicketEntryObj("aaa","bbb");
		te2.setPricePerUnit(5);
		te2.setAmount(2);
		ReturnTransactionObj r = new ReturnTransactionObj();		
		r.addEntry(te);
		r.addEntry(te2);
		Assert.assertEquals(r.calculateDueMoney(),(10.5-1.05)*5+5*2,0.001);
	}
	
	
	/*
	 * Integration tests - API
	 */
	
	
	/**
	 * save DB backup copy, to start from an empty one and restore the saved one after the test execution
	 * create a new user inside the test DB
	 */
	@BeforeClass
	public static void setup() {
		try {
			Files.copy(Path.of(DBManager.dbPath), Path.of(dbBackupPath),StandardCopyOption.REPLACE_EXISTING);
			Files.delete(Path.of(DBManager.dbPath));
			EZShop ez = new EZShop();
			root = new UserObj(1,"root","root","Administrator");
			cashier = new UserObj(2,"cashier","cashier","Cashier");
			shopManager = new UserObj(3, "shopManager", "shopManager", "ShopManager");
			root.setId(ez.createUser(root.getUsername(), root.getPassword(), root.getRole()));
			cashier.setId(ez.createUser(cashier.getUsername(), cashier.getPassword(), cashier.getRole()));
			shopManager.setId(ez.createUser(shopManager.getUsername(), shopManager.getPassword(), shopManager.getRole()));
		} catch (IOException | InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
			e.printStackTrace();
		}
	}
	private void copyAndDeleteTestDB() {
		try {
			Files.copy(Path.of(DBManager.dbPath), Path.of(dbBackupPathTEST),StandardCopyOption.REPLACE_EXISTING);
			Files.delete(Path.of(DBManager.dbPath));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	private void restoreTestDB() {
		try {
			Files.copy(Path.of(dbBackupPathTEST), Path.of(DBManager.dbPath),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Integration test defineCustomer
	 */
	@Test
    public void testDefineCustomer() {
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.defineCustomer("Jack"));
		int id1=-1,id2=-1;
		try {
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidCustomerNameException.class, () -> ez.defineCustomer(null));
			Assert.assertThrows(InvalidCustomerNameException.class, () -> ez.defineCustomer(""));			
			Assert.assertTrue((id1=ez.defineCustomer("Jack"))>0);
			Assert.assertEquals(ez.getCustomer(id1).getCustomerName(),"Jack");
			Assert.assertTrue((id2=ez.defineCustomer("Nick"))>0);
			Assert.assertEquals(ez.getCustomer(id2).getCustomerName(),"Nick");
			copyAndDeleteTestDB();
			Assert.assertEquals(ez.defineCustomer("Jerry"),Integer.valueOf(-1));
			restoreTestDB();
		} catch (InvalidCustomerNameException | InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidCustomerIdException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteCustomer(id1);
				ez.deleteCustomer(id2);
			} catch (UnauthorizedException | InvalidCustomerIdException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Integration test deleteCustomer
	 */
	@Test
    public void testDeleteCustomer() {
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteCustomer(1));
		try {
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.deleteCustomer(-2));
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.deleteCustomer(0));
				
			int id = ez.defineCustomer("Jack");
			Assert.assertFalse(ez.deleteCustomer(id+1));
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.deleteCustomer(id));
			restoreTestDB();
			Assert.assertTrue(ez.deleteCustomer(id));			
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidCustomerIdException | InvalidCustomerNameException e) {
			e.printStackTrace();			
		}
	}
	/**
	 * Integration test getCreditsAndDebits
	 */
	@Test
    public void testGetCreditsAndDebits() {		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getCreditsAndDebits(LocalDate.of(2021, 04, 01), LocalDate.of(2021, 06, 01)));
		int sid=-1,pid=-1;
		try {
			ez.login(root.getUsername(),root.getPassword());
			LocalDate from = LocalDate.now(), to = LocalDate.now();
			from = from.minusMonths(1);
			to = to.plusMonths(1);
			Assert.assertEquals(ez.getCreditsAndDebits(from, to).size(),0);
			ez.recordBalanceUpdate(10);
			Assert.assertEquals(ez.getCreditsAndDebits(null,null).size(),1);
			Assert.assertEquals(ez.getCreditsAndDebits(from, null).size(),1);
			Assert.assertEquals(ez.getCreditsAndDebits(null, to).size(),1);
			Assert.assertEquals(ez.getCreditsAndDebits(to, from).size(),1);
			Assert.assertEquals(ez.getCreditsAndDebits(from, to).size(),1);
			Assert.assertEquals(ez.getCreditsAndDebits(from.minusMonths(2), to.minusMonths(2)).size(),0);
			sid = ez.startSaleTransaction();
			pid = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(pid, "0-x-0");
			ez.updateQuantity(pid, 2);
			ez.addProductToSale(sid, "8023883019015", 2);
			ez.endSaleTransaction(sid);
			ez.receiveCashPayment(sid, 20);
			List<BalanceOperation> l = ez.getCreditsAndDebits(null, null).stream().filter(e->e.getType().equals("CREDIT")).collect(Collectors.toList());
			Assert.assertEquals(l.get(l.size()-1).getMoney(), 11.98, 0.001);
			ez.recordBalanceUpdate(28.02);
			ez.payOrderFor("8023883019015", 10, 4);
			l = ez.getCreditsAndDebits(null, null).stream().filter(e->e.getType().equals("DEBIT")).collect(Collectors.toList());
			Assert.assertEquals(l.get(l.size()-1).getMoney(), -40, 0.001);
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException | InvalidTransactionIdException | InvalidQuantityException | InvalidPaymentException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(sid);
				ez.deleteProductType(pid);			
			} catch (InvalidTransactionIdException | UnauthorizedException | InvalidProductIdException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 	Integration Test getCustomer
	 */
	@Test
    public void testGetCustomer() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getCustomer(1));
		try {
			ez.login("root", "root");
			Integer id = 0;
			Customer c;
			String s = "Amy";
			id = ez.defineCustomer(s);
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.getCustomer(null));
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.getCustomer(0));
			
			c = ez.getCustomer(id);
			Assert.assertEquals(id, c.getId());
			Assert.assertEquals(s, c.getCustomerName());
			Assert.assertNull(ez.getCustomer(id+1));
			
			//restore the previous DB state
			if(id>0) {
				ez.deleteCustomer(id);
			}			
		} catch (InvalidCustomerIdException | UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidCustomerNameException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Integration Test getAllOrders
	 */
	@Test
	public void testGetAllOrders() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getAllOrders());
		int idp=-1;
		try {
			ez.login("root", "root");
			idp = ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			ez.issueOrder("978020137962", 23, 25);
			ez.issueOrder("978020137962", 24, 26);
			List<Order> orders = ez.getAllOrders();
			orders = orders.stream().filter((o) -> o.getProductCode().equals("978020137962")).collect(Collectors.toList());
			for(Order o: orders) {
				if(o.getPricePerUnit() == 25)
					Assert.assertEquals(23, o.getQuantity());
				if(o.getPricePerUnit() == 26)
					Assert.assertEquals(24, o.getQuantity());
			}
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * 	Integration Test modifyCustomer
	 */
	@Test
    public void testModifyCustomer() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getCustomer(1));
		Customer c;
		String s = "Jack";
		try {
			Assert.assertThrows(UnauthorizedException.class, () ->ez.modifyCustomer(123, "cdcdcd", "32477"));
			ez.login("root", "root");
			final Integer id1 = ez.defineCustomer(s);
			final Integer id2 = ez.defineCustomer("Andrea");
			String card = LoyaltyCard.generate();
			String card2 = LoyaltyCard.generate();
			Assert.assertThrows(InvalidCustomerNameException.class, () -> ez.modifyCustomer(id1, "", card));
			Assert.assertThrows(InvalidCustomerNameException.class, () -> ez.modifyCustomer(id1, null, card));
			
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyCustomer(id1, "Frank", ""));
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyCustomer(id1, "Frank", null));
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyCustomer(id1, "Frank", "32477"));
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyCustomer(id1, "Frank", "sduuhsiudf"));
			
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.modifyCustomer(123, "Boomer", card));
			
			Assert.assertFalse(ez.modifyCustomer(id1, "Andrea", card)); 	// Already used name
			Assert.assertFalse(ez.modifyCustomer(id2, s, card));	// Already used name
			Assert.assertTrue(ez.modifyCustomer(id2, "Mia", card2));
			Assert.assertTrue(ez.modifyCustomer(id1, "Jerry", card));
			c = ez.getCustomer(id1);
			Assert.assertEquals(card, c.getCustomerCard());
			Assert.assertEquals("Jerry", c.getCustomerName());
			Assert.assertTrue(ez.modifyCustomer(id2, "Frank", ""));
			
			//restore the previous DB state
			if(id1>0) {
				ez.deleteCustomer(id1);
			}		
			if(id2>0) {
				ez.deleteCustomer(id2);
			}		
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidCustomerNameException | InvalidCustomerIdException | InvalidCustomerCardException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Integration Test recordBalanceUpdate
	 */
	@Test
	public void testRecordBalanceUpdate() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.recordBalanceUpdate(100.0));
		try {
			ez.login("root", "root");
			double toBeAdded1 = -10000.0;
			double toBeAdded2 = 100.0;
			Assert.assertFalse(ez.recordBalanceUpdate(toBeAdded1));
			Assert.assertTrue(ez.recordBalanceUpdate(toBeAdded2));
			// Assert.assertEquals(toBeAdded2, ez.computeBalance(), 0);	// Only right at first execution with empty db
			
			// Restore 0 balance on DB
			ez.recordBalanceUpdate(-toBeAdded2);
			
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Integration Test createCard
	 */
	@Test
	public void testCreateCard() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.createCard());
		try {
			ez.login("root", "root");
			String s = ez.createCard();
			Assert.assertTrue(LoyaltyCardCodeValidator.validate(s));
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Integration Test updateQuantity
	 */
	@Test
	public void testUpdateQuantity() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.updateQuantity(123, 5));
		try {
			ez.login("root", "root");
			
			Integer id = ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			
			ez.updatePosition(id, "0-x-1");
			Assert.assertTrue(ez.updateQuantity(id, 5));
			Assert.assertFalse(ez.updateQuantity(id+1, 5));
			Assert.assertFalse(ez.updateQuantity(id, -5));
			Assert.assertFalse(ez.updateQuantity(id, -10));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updateQuantity(null, 5));
			
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidLocationException e) {
			e.printStackTrace();
		} finally {
			// Restore db
			try {
				ez.deleteProductType(ez.getProductTypeByBarCode("978020137962").getId());
			} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Integration Test applyDiscountRateToSale
	 */
	@Test
	public void testApplyDiscountRateToSale() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.applyDiscountRateToSale(1, 20.0));
		try {
			ez.login("root", "root");
			final Integer id = ez.startSaleTransaction();
			ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			ez.getProductTypeByBarCode("978020137962").setQuantity(10);
			Boolean b = ez.addProductToSale(id, "978020137962", 5);	// Price per unit 32.25
			if(b) {
				Assert.assertThrows(InvalidDiscountRateException.class, () -> ez.applyDiscountRateToSale(id, 1));
				Assert.assertThrows(InvalidDiscountRateException.class, () -> ez.applyDiscountRateToSale(id, 20));
				Assert.assertThrows(InvalidDiscountRateException.class, () -> ez.applyDiscountRateToSale(id, -1));
				Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.applyDiscountRateToSale(0, 0.2));
				Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.applyDiscountRateToSale(null, 0.2));
				Assert.assertFalse(ez.applyDiscountRateToSale(id+1, 0.25));	// Transaction not exists
				Assert.assertTrue(ez.applyDiscountRateToSale(id, 0.25));
				ez.endSaleTransaction(id);
				Assert.assertEquals(0.25, ez.getSaleTransaction(id).getDiscountRate(), 0);
				Assert.assertTrue(ez.applyDiscountRateToSale(id, 0.25));	// If transaction is closed discountRate can be modified
			}
			
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidTransactionIdException | InvalidQuantityException | InvalidDiscountRateException e) {
			e.printStackTrace();
		} finally {
			// Restore db
			try {
				ez.deleteProductType(ez.getProductTypeByBarCode("978020137962").getId());
			} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Integration Test updateUserRights
	 */
	@Test
	public void testUpdateUserRights() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.updateUserRights(2, "Cashier"));
		try {
			ez.login("root", "root");
			Integer id = ez.createUser("ale", "ale", "Cashier");
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.updateUserRights(0, "ShopManager"));
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.updateUserRights(-1, "ShopManager"));
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.updateUserRights(null, "ShopManager"));
			Assert.assertThrows(InvalidRoleException.class, () -> ez.updateUserRights(id, ""));
			Assert.assertThrows(InvalidRoleException.class, () -> ez.updateUserRights(id, null));
			Assert.assertThrows(InvalidRoleException.class, () -> ez.updateUserRights(id, "SomeRole"));
			Assert.assertFalse(ez.updateUserRights(id+100, "ShopManager"));
			Assert.assertTrue(ez.updateUserRights(id, "ShopManager"));
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidRoleException | InvalidUserIdException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Integration Test deleteSaleTransaction
	 */
	@Test
	public void testDeleteSaleTransaction() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteSaleTransaction(1));
		try {
			ez.login("root", "root");
			Integer id = ez.startSaleTransaction();
			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteSaleTransaction(0));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteSaleTransaction(-1));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteSaleTransaction(null));
			
			boolean state = ez.endSaleTransaction(id);
			if(state) {
				ez.receiveCashPayment(id, 10);
				Assert.assertFalse(ez.deleteSaleTransaction(id));	// Already Payed
			}
			
			id = ez.startSaleTransaction();
			Assert.assertFalse(ez.deleteSaleTransaction(id+100));	// Transaction doesn't exist
			Assert.assertTrue(ez.deleteSaleTransaction(id));
			Assert.assertNull(ez.getSaleTransaction(id));
			
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidTransactionIdException | InvalidPaymentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Integration Test deleteReturnTransaction
	 */
	@Test
	public void testDeleteReturnTransaction() {	
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteReturnTransaction(1));
		try {
			ez.login("root", "root");
			Integer id = ez.startSaleTransaction();
			ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			ez.getProductTypeByBarCode("978020137962").setQuantity(10);
			ez.addProductToSale(id, "978020137962", 3);	// Price per unit 32.25
			ez.endSaleTransaction(id);
			ez.receiveCashPayment(id, 45);
			
			Integer returnId = ez.startReturnTransaction(id);
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteReturnTransaction(0));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteReturnTransaction(-1));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteReturnTransaction(null));
			Assert.assertFalse(ez.deleteReturnTransaction(returnId+1000)); // It doesn't exist
			ez.returnProduct(returnId, "978020137962", 3);
			ez.endReturnTransaction(returnId, true);
			Assert.assertTrue(ez.deleteReturnTransaction(returnId));
			Assert.assertEquals(7, ez.getProductTypeByBarCode("978020137962").getQuantity(), 0.001);
			
			returnId = ez.startReturnTransaction(id);
			ez.endReturnTransaction(returnId, true);
			ez.returnCashPayment(returnId);
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidTransactionIdException | InvalidPaymentException | InvalidProductCodeException | InvalidQuantityException | InvalidProductDescriptionException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(ez.getProductTypeByBarCode("978020137962").getId());
			} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Integration Test startReturnTransaction
	 */
	@Test
	public void testStartReturnTransaction() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.startReturnTransaction(1));
		int returnId=-1,saleId=-1;
		try {
			ez.login("root", "root");
			saleId = ez.startSaleTransaction();
			ez.endSaleTransaction(saleId);
			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.startReturnTransaction(0));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.startReturnTransaction(-1));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.startReturnTransaction(null));
			
			Assert.assertTrue(ez.startReturnTransaction(saleId+1000) == -1);
			Assert.assertTrue(ez.startReturnTransaction(saleId) == -1);//sale not yet payed
			ez.receiveCashPayment(saleId, 10);
			returnId = ez.startReturnTransaction(saleId);
			Assert.assertTrue(returnId > 0);
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidTransactionIdException | InvalidPaymentException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteReturnTransaction(returnId);
				ez.deleteSaleTransaction(saleId);
			} catch (InvalidTransactionIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Integration Test issueOrder
	 */
	@Test
	public void testIssueOrder() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.issueOrder(null, 1, 1));
		try {
			ez.login("root", "root");
			ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.issueOrder("", 1, 1));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.issueOrder(null, 1, 1));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.issueOrder("fakeProduct", 1, 1));
			
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.issueOrder("978020137962", 0, 1));
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.issueOrder("978020137962", -1, 1));
			
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.issueOrder("978020137962", 2, 0));
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.issueOrder("978020137962", 2, -1));
			
			Assert.assertTrue(ez.issueOrder("978020137962", 2, 2.2) > 0);
			Assert.assertTrue(ez.issueOrder("9780201379723", 2, 2.2) == -1); // Product doesn't exist
			
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductDescriptionException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(ez.getProductTypeByBarCode("978020137962").getId());
			} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e) {
				e.printStackTrace();
			}			
		}
	}

	/**
	 * Integration Test getProductTypeByBarCode
	 */
	@Test
	public void testGetProductTypeByBarCode() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getProductTypeByBarCode("978020137962"));
		try {
			ez.login("root", "root");
			
			Assert.assertNull(ez.getProductTypeByBarCode("9780201379723")); // Does not exist but is valid
			Integer pid = ez.createProductType("FakeSomething", "978020137962", 10, "A cool product");
			ProductType p = ez.getProductTypeByBarCode("978020137962");
			Assert.assertEquals("978020137962", p.getBarCode());
			Assert.assertEquals(pid, p.getId());
			
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.getProductTypeByBarCode(""));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.getProductTypeByBarCode(null));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.getProductTypeByBarCode("fakeProduct"));
			
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductDescriptionException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(ez.getProductTypeByBarCode("978020137962").getId());
			} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Integration Test returnCreditCardPayment
	 */
	@Test
	public void testReturnCreditCardPayment() {
		ez.logout();	//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.returnCreditCardPayment(null, null));
		int rid=-1,sid=-1;
		try {
			ez.login("root", "root");
			
			sid = ez.startSaleTransaction();		
			ez.endSaleTransaction(sid);
			ez.receiveCashPayment(sid, 10);
			rid = ez.startReturnTransaction(sid);
			Integer returnId = rid;
			Assert.assertEquals(ez.returnCashPayment(returnId), -1, 0.001);
			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnCreditCardPayment(-1, "5100293991053009"));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnCreditCardPayment(0, "5100293991053009"));
			Assert.assertThrows(InvalidCreditCardException.class, () -> ez.returnCreditCardPayment(returnId, ""));
			Assert.assertThrows(InvalidCreditCardException.class, () -> ez.returnCreditCardPayment(returnId, null));
			Assert.assertThrows(InvalidCreditCardException.class, () -> ez.returnCreditCardPayment(returnId, "4382984839"));
			
			Assert.assertEquals(-1, ez.returnCreditCardPayment(returnId, "5100293991053009"), 0); // returnTransaction not ended
			Assert.assertEquals(-1, ez.returnCreditCardPayment(returnId+1000, "5100293991053009"), 0); // Does not exist
			Assert.assertEquals(-1, ez.returnCreditCardPayment(returnId, "5100293991053009"), 0); 
			
			ez.endReturnTransaction(returnId, true);
			Assert.assertEquals(0, ez.returnCreditCardPayment(returnId, "5100293991053009"), 0.001);
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidTransactionIdException | UnauthorizedException | InvalidCreditCardException | InvalidPaymentException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteReturnTransaction(rid);
				ez.deleteSaleTransaction(sid);
			} catch (InvalidTransactionIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Integration test updatePosition
	 */
	@Test
    public void testUpdatePosition() {		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.updatePosition(1,"123-ddiqn-213124"));
		int id1=-1,id2=-1;
		try {
			ez.login(cashier.getUsername(),cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.updatePosition(1,"123-ddiqn-213124"));
			ez.logout();			
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updatePosition(-2,"123-ddiqn-213124"));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updatePosition(0,"123-ddiqn-213124"));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updatePosition(null,"123-ddiqn-213124"));
			id1 = ez.createProductType("desc", "8023883019015", 5.99, "");
			int id=id1;
			Assert.assertThrows(InvalidLocationException.class, () -> ez.updatePosition(id,"invalid"));
			Assert.assertFalse(ez.updatePosition(id1+1,"123-ddiqn-213124"));
			Assert.assertTrue(ez.updatePosition(id1,"123-ddiqn-213124"));
			ProductType p = ez.getProductTypeByBarCode("8023883019015");
			Assert.assertEquals(p.getLocation(),"123-ddiqn-213124");
			id2 = ez.createProductType("desc2", "6953029710723", 6.99, "");
			Assert.assertFalse(ez.updatePosition(id2,"123-ddiqn-213124"));//position already assigned
			Assert.assertTrue(ez.updatePosition(id1,null));
			p = ez.getProductTypeByBarCode("8023883019015");
			Assert.assertEquals(p.getLocation(),"");
			Assert.assertTrue(ez.updatePosition(id1,""));
			p = ez.getProductTypeByBarCode("8023883019015");
			Assert.assertEquals(p.getLocation(),"");
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.updatePosition(id1,"123-ddiqn-213125"));//no productType table
			restoreTestDB();
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(id1);
				ez.deleteProductType(id2);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Integration test attachCardToCustomer
	 */
	@Test
	public void testAttachCardToCustomer(){
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.attachCardToCustomer("1234567890", 1));
		int c1=-1,c2=-1;
		try {
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.attachCardToCustomer(null, 1));
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.attachCardToCustomer("", 1));
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.attachCardToCustomer("invalid", 1));			
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.attachCardToCustomer("1234567890", null));
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.attachCardToCustomer("1234567890", -2));
			Assert.assertThrows(InvalidCustomerIdException.class, () -> ez.attachCardToCustomer("1234567890", 0));
			
			c1 = ez.defineCustomer("Jack");
			c2 = ez.defineCustomer("Nick");
			String card1 = ez.createCard();
			String card2 = ez.createCard();
			Assert.assertFalse(ez.attachCardToCustomer(card1, c2+1));//no customer present with that id
			Assert.assertTrue(ez.attachCardToCustomer("1234567890", c1));
			Assert.assertTrue(ez.attachCardToCustomer(card1, c1));			
			Assert.assertFalse(ez.attachCardToCustomer(card1, c2));//already assigned to another customer
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.attachCardToCustomer("1234567891", c1));//no loyaltycard table
			Assert.assertFalse(ez.attachCardToCustomer(card2, c1));//no customers table
			restoreTestDB();
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidCustomerNameException | InvalidCustomerIdException | InvalidCustomerCardException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteCustomer(c1);
				ez.deleteCustomer(c2);				
			} catch (InvalidCustomerIdException | UnauthorizedException e) {
				e.printStackTrace();
			}			
		}
	}
	/**
	 * Integration test returnCashPayment
	 */
	@Test
	public void testReturnCashPayment(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.returnCashPayment(15));
		int saleId=-1,returnId=-1,returnId2=-1,idp=-1;
		try {
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnCashPayment(-2));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnCashPayment(0));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnCashPayment(null));			
						
			Assert.assertEquals(ez.returnCashPayment(15),-1,0.001);
			saleId = ez.startSaleTransaction();
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.updateQuantity(idp, 2);
			ez.addProductToSale(saleId, "8023883019015", 2);
			ez.endSaleTransaction(saleId);
			ez.receiveCashPayment(saleId, 20);
			returnId = ez.startReturnTransaction(saleId);
			Assert.assertEquals(ez.returnCashPayment(returnId),-1,0.001);//still to be ended
			Assert.assertTrue(ez.endReturnTransaction(returnId, true));
			Assert.assertEquals(ez.returnCashPayment(returnId),0,0.001);
			returnId2 = ez.startReturnTransaction(saleId);
			ez.returnProduct(returnId2, "8023883019015", 1);
			Assert.assertTrue(ez.endReturnTransaction(returnId2, true));
			ez.recordBalanceUpdate(5.99);
			double balance = ez.computeBalance();
			copyAndDeleteTestDB();
			Assert.assertEquals(ez.returnCashPayment(returnId2),-1,0.001);
			restoreTestDB();
			Assert.assertEquals(ez.returnCashPayment(returnId2),5.99,0.001);
			Assert.assertEquals(ez.computeBalance(),balance-5.99,0.001);
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidTransactionIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidQuantityException | InvalidProductIdException | InvalidLocationException | InvalidPaymentException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(saleId);
				ez.deleteReturnTransaction(returnId);
				ez.deleteReturnTransaction(returnId2);
				ez.deleteProductType(idp);
			} catch (UnauthorizedException | InvalidTransactionIdException | InvalidProductIdException e) {
				e.printStackTrace();
			}			
		}
	}
	/**
	 * Integration test getProductTypesByDescription
	 */
	@Test
	public void testGetProductTypesByDescription(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getProductTypesByDescription("desc"));
		int id1=-1,id2=-1,id3=-1;
		try {
			ez.login(cashier.getUsername(),cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.getProductTypesByDescription("desc"));
			ez.logout();			
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertEquals(ez.getProductTypesByDescription("desc").size(),0);
			id1 = ez.createProductType("desc", "8023883019015", 5.99, "");
			Assert.assertEquals(ez.getProductTypesByDescription(null).size(),1);
			Assert.assertEquals(ez.getProductTypesByDescription("").size(),1);			
			Assert.assertEquals(ez.getProductTypesByDescription("desc").size(),1);
			Assert.assertEquals(ez.getProductTypesByDescription("desc2").size(),0);
			id2 = ez.createProductType("desc2", "6953029710723", 6.99, "");
			Assert.assertEquals(ez.getProductTypesByDescription("desc2").size(),1);
			id3 = ez.createProductType("desc", "6973443180087", 1.99, "");
			Assert.assertEquals(ez.getProductTypesByDescription("desc").size(),3);
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(id1);
				ez.deleteProductType(id2);
				ez.deleteProductType(id3);
			} catch (UnauthorizedException | InvalidProductIdException e) {
				e.printStackTrace();
			}			
		}
	}
	/**
	 * Integration test payOrderFor
	 */
	@Test
	public void testPayOrderFor(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.payOrderFor("8023883019015",20,10.5));
		int id1=-1;		
		try {
			ez.login(cashier.getUsername(),cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.payOrderFor("8023883019015",20,10.5));
			ez.logout();
			ez.login(root.getUsername(),root.getPassword());			
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.payOrderFor("8023883019015",20,-2));
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.payOrderFor("8023883019015",20,0));
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.payOrderFor("8023883019015",-2,10.5));
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.payOrderFor("8023883019015",0,10.5));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.payOrderFor(null,20,10.5));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.payOrderFor("",20,10.5));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.payOrderFor("8023883019014",20,10.5));			
			
			Assert.assertEquals(ez.payOrderFor("8023883019015",20,10),Integer.valueOf(-1));//no product type
			id1 = ez.createProductType("desc", "8023883019015", 5.99, "");			
			Assert.assertEquals(ez.payOrderFor("8023883019015",20,ez.computeBalance()+1),Integer.valueOf(-1));//not enough money
			ez.recordBalanceUpdate(220);
			int oid = ez.payOrderFor("8023883019015",20,10);
			Assert.assertTrue(oid>0);
			Assert.assertTrue(ez.getAllOrders().stream().filter(o->o.getOrderId()==oid).findFirst().get().getStatus().equals("payed"));
			copyAndDeleteTestDB();
			Assert.assertEquals(ez.payOrderFor("8023883019015",2,10),Integer.valueOf(-1));
			restoreTestDB();			
			int oid2 = ez.payOrderFor("8023883019015",2,10);
			Assert.assertTrue(oid2>0);
			Assert.assertTrue(ez.getAllOrders().stream().filter(o->o.getOrderId()==oid2).findFirst().get().getStatus().equals("payed"));
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(id1);
			} catch (UnauthorizedException | InvalidProductIdException e) {
				e.printStackTrace();
			}			
		}
	}
	/**
	 * Integration test updateProduct
	 */
	@Test
	public void testUpdateProduct(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.updateProduct(1,"desc","8023883019015",10.5,""));
		int id1=-1,id2=-1;		
		try {
			ez.login(cashier.getUsername(),cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.updateProduct(1,"desc","8023883019015",10.5,""));
			ez.logout();
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.updateProduct(1,"desc","8023883019015",-2,""));
			Assert.assertThrows(InvalidPricePerUnitException.class, () -> ez.updateProduct(1,"desc","8023883019015",0,""));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updateProduct(null,"desc","8023883019015",10.5,""));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updateProduct(-2,"desc","8023883019015",10.5,""));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.updateProduct(0,"desc","8023883019015",10.5,""));
			Assert.assertThrows(InvalidProductDescriptionException.class, () -> ez.updateProduct(1,null,"8023883019015",0,""));
			Assert.assertThrows(InvalidProductDescriptionException.class, () -> ez.updateProduct(1,"","8023883019015",0,""));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.updateProduct(1,"desc",null,10.5,""));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.updateProduct(1,"desc","",10.5,""));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.updateProduct(1,"desc","8023883019014",10.5,""));
			Assert.assertFalse(ez.updateProduct(1,"desc","8023883019015",10.5,""));//product not present
			id1 = ez.createProductType("desc", "8023883019015", 5.99, "");
			id2 = ez.createProductType("desc2", "6953029710723", 6.99, "");
			Assert.assertTrue(ez.updateProduct(id1,"desc123","8023883019015",10.5,"aaa"));
			Assert.assertEquals(ez.getProductTypeByBarCode("8023883019015").getProductDescription(),"desc123");
			Assert.assertEquals(ez.getProductTypeByBarCode("8023883019015").getPricePerUnit(),10.5,0.001);
			Assert.assertEquals(ez.getProductTypeByBarCode("8023883019015").getNote(),"aaa");
			Assert.assertFalse(ez.updateProduct(id2,"desc","8023883019015",10.5,null));
			Assert.assertTrue(ez.updateProduct(id2,"desc","6953029710723",10.5,null));
			Assert.assertEquals(ez.getProductTypeByBarCode("6953029710723").getPricePerUnit(),10.5,0.001);
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.updateProduct(id2,"desc","6953029710723",10.5,"nn"));
			restoreTestDB();
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductDescriptionException | InvalidProductIdException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(id1);
				ez.deleteProductType(id2);
			} catch (UnauthorizedException | InvalidProductIdException e) {
				e.printStackTrace();
			}			
		}
	}
	
	@Test
	public void testDeleteProductFromSale() {
		int idp=-1,trans_id=-1;
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteProductFromSale(1,"8023883019015",8001));
			ez.login(root.getUsername(),root.getPassword());
			
			trans_id = ez.startSaleTransaction();
			Integer ID = Integer.valueOf(trans_id);
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.updateQuantity(idp, 99);
			ez.addProductToSale(trans_id, "8023883019015", 99);
			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteProductFromSale(-1,"8023883019015",98));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.deleteProductFromSale(null,"8023883019015",98));
			
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.deleteProductFromSale(ID,"!@#ASD12",98));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.deleteProductFromSale(ID,null,98));

			Assert.assertThrows(InvalidQuantityException.class, () -> ez.deleteProductFromSale(ID,"8023883019015",-80));
			
			Assert.assertFalse(ez.deleteProductFromSale(ID,"5434521019015",98));
			Assert.assertFalse(ez.deleteProductFromSale(ID,"8023883019015",198));
			Assert.assertTrue(ez.deleteProductFromSale(ID,"8023883019015",2));
			Assert.assertTrue(ez.deleteProductFromSale(ID,"8023883019015",97));
			
			ez.endSaleTransaction(ID);
			
		}
		catch(InvalidLocationException|InvalidProductDescriptionException|InvalidProductIdException|InvalidPricePerUnitException|InvalidTransactionIdException|InvalidQuantityException|InvalidUsernameException|UnauthorizedException|InvalidPasswordException|InvalidProductCodeException e) {
			e.printStackTrace();
		}finally {
			try {
				ez.deleteSaleTransaction(trans_id);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		}
		
		
	
	}
	
	@Test
	public void testGetAllUsers() {
		int idu=-1;
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(UnauthorizedException.class, () -> ez.getAllUsers());
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.getAllUsers());
			ez.logout();
			ez.login(root.getUsername(),root.getPassword());
			
			Assert.assertEquals(ez.getAllUsers().size(), 3);//root,cashier and shopManager
			idu=ez.createUser("Jerry", "Jerry", "Administrator");
			Assert.assertEquals(ez.getAllUsers().size(), 4);			
		}	
		catch(InvalidPasswordException|InvalidUsernameException|UnauthorizedException | InvalidRoleException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteUser(idu);
			} catch (InvalidUserIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testPayOrder() {
		int id1=-1;
		try {
			ez.logout();
			Assert.assertThrows(UnauthorizedException.class, () -> ez.payOrder(A));
			ez.login(root.getUsername(), root.getPassword());
			Assert.assertThrows(InvalidOrderIdException.class, () -> ez.payOrder(-A));
			
			id1=ez.createProductType("product1", "8023883019015", 2.4, "asd123qweQWE");
			Integer orderID1=ez.issueOrder("8023883019015",999,2.4);

			ez.recordBalanceUpdate(99.0);
			
			Assert.assertFalse(ez.payOrder(A));
			Assert.assertFalse(ez.payOrder(orderID1));			
		}
		catch(InvalidOrderIdException|InvalidProductDescriptionException|InvalidProductCodeException|UnauthorizedException|InvalidPricePerUnitException|InvalidPasswordException|InvalidUsernameException|InvalidQuantityException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(id1);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testGetAllProductTypes() {
		int idp1=-1,idp2=-1;
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(UnauthorizedException.class, () -> ez.getAllProductTypes());

			ez.logout();
			ez.login(root.getUsername(),root.getPassword());

			idp1 = ez.createProductType("desc", "8023883019015", 5.99, "");
			Assert.assertEquals(1,ez.getAllProductTypes().size());
			idp2 = ez.createProductType("desc", "6953029710723", 5.99, "");
			Assert.assertEquals(2,ez.getAllProductTypes().size());
		}	
		catch(InvalidPasswordException|InvalidUsernameException|UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(idp1);
				ez.deleteProductType(idp2);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testCreateUser() {
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(InvalidPasswordException.class, ()->ez.createUser("new_user",null,"Cashier"));
			Assert.assertThrows(InvalidPasswordException.class, ()->ez.createUser("new_user","","Cashier"));
			
			Assert.assertThrows(InvalidRoleException.class, () -> ez.createUser("new_user","123456","Boss"));
			
			Assert.assertThrows(InvalidUsernameException.class, () -> ez.createUser("","123456","Cashier"));
			Assert.assertThrows(InvalidUsernameException.class, () -> ez.createUser(null,"123456","Cashier"));
			
			Integer i = Integer.valueOf(-1);
			Assert.assertEquals(i, ez.createUser("cashier","123456","Cashier"));
			Integer j = Integer.valueOf(5);
			Assert.assertEquals(j, ez.createUser("new_user","123456","Cashier"));
			} catch(InvalidRoleException|InvalidPasswordException|InvalidUsernameException e) {
				e.printStackTrace();
			}

	}
	
	@Test
	public void testApplyDiscountRateToProduct() {
		int idp=-1,trans_id=-1;
		try {
			ez.logout();//no logged in user
			ez.login(root.getUsername(), root.getPassword());
			trans_id=ez.startSaleTransaction();			
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.updateQuantity(idp, 99);
			ez.addProductToSale(trans_id,"8023883019015",99);
			
			ez.logout();
			int tid = trans_id;
			Assert.assertThrows(UnauthorizedException.class, () -> ez.applyDiscountRateToProduct(tid,"8023883019015",0.9));
			
			ez.logout();
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.applyDiscountRateToProduct(null,"8023883019015",0.9));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.applyDiscountRateToProduct(-2,"8023883019015",0.9));
			
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.applyDiscountRateToProduct(tid,null,0.9));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.applyDiscountRateToProduct(tid,"",0.9));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.applyDiscountRateToProduct(tid,"dq1u!@#A",0.9));
			
			Assert.assertThrows(InvalidDiscountRateException.class, () -> ez.applyDiscountRateToProduct(tid,"8023883019015",-0.9));
			Assert.assertThrows(InvalidDiscountRateException.class, () -> ez.applyDiscountRateToProduct(tid,"8023883019015",1.9));

			Assert.assertTrue(ez.applyDiscountRateToProduct(trans_id,"8023883019015",0.9));
			Assert.assertFalse(ez.applyDiscountRateToProduct(trans_id,"1233883019015",0.9));
			Assert.assertFalse(ez.applyDiscountRateToProduct(123,"8023883019015",0.9));

		}catch(InvalidDiscountRateException|InvalidQuantityException|InvalidPasswordException|UnauthorizedException|InvalidProductCodeException|InvalidTransactionIdException|InvalidUsernameException | InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException e) {
			e.printStackTrace();
		}finally {
			try {
				ez.login(root.getUsername(), root.getPassword());				
				ez.deleteSaleTransaction(trans_id);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException | InvalidUsernameException | InvalidPasswordException e) {
				e.printStackTrace();
			}			
		}
	}
	

	@Test
	public void testReturnProduct() {
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(UnauthorizedException.class, () -> ez.returnProduct(12,"8023883019015",99));
			ez.login(root.getUsername(), root.getPassword());
			final Integer trans_id=ez.startSaleTransaction();
			ez.addProductToSale(trans_id,"8023883019015",99);
			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnProduct(null,"8023883019015",99));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.returnProduct(-12,"8023883019015",99));
			
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(trans_id,"",99));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(trans_id,null,99));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(trans_id,"aisdn!@#4341aAS",99));
			
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.returnProduct(trans_id,"8023883019015",-99));
			
			Assert.assertFalse(ez.returnProduct(4,"8023883019015",99));//no product added
			ez.addProductToSale(trans_id,"8023883019015",99);

		}catch(InvalidTransactionIdException|InvalidProductCodeException|InvalidQuantityException|InvalidPasswordException|InvalidUsernameException|UnauthorizedException e) {
			e.printStackTrace();
		}				
	}
	
	
	@Test
	public void testEndSaleTransaction() {
		int idp = -1;
		try {
			ez.logout();//no logged in user
			Assert.assertThrows(UnauthorizedException.class, () -> ez.endSaleTransaction(1));
			
			ez.login(root.getUsername(), root.getPassword());
			int trasId=ez.startSaleTransaction();
			Assert.assertThrows(InvalidTransactionIdException.class, () ->ez.endSaleTransaction(null));
			Assert.assertThrows(InvalidTransactionIdException.class, () ->ez.endSaleTransaction(-1));
			
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.recordBalanceUpdate(600);
			int ido = ez.issueOrder("8023883019015", 20, 25);
			int ido2 = ez.issueOrder("8023883019015", 10, 10);
			ez.payOrder(ido);
			ez.payOrder(ido2);
			ez.recordOrderArrivalRFID(ido, "000000000010");//RFIDS from 10 to 29 added
			ez.addProductToSaleRFID(trasId, "000000000010");
			
			Assert.assertFalse(ez.endSaleTransaction(123));
			Assert.assertTrue(ez.endSaleTransaction(trasId));
			Assert.assertEquals(25.0, ez.getSaleTransaction(trasId).getPrice(), 0);
			
			ez = new EZShop();//call the init method to read data from DB
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertTrue(ez.recordOrderArrivalRFID(ido2, "000000000010"));
			
		}catch(InvalidTransactionIdException|InvalidPasswordException|InvalidUsernameException|UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException | InvalidQuantityException | InvalidOrderIdException | InvalidRFIDException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}				
	

	
	/**
	 * Integration test reset
	 */
	@Test
	public void testReset() {
		ez.logout();//no logged in user
		try {
			ez.login(root.getUsername(),root.getPassword());
			int sale = ez.startSaleTransaction();			
			ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.recordBalanceUpdate(100);
			ez.reset();
			ez = new EZShop();//call again the init method to load data from DB
			//add again users
			root.setId(ez.createUser(root.getUsername(), root.getPassword(), root.getRole()));
			cashier.setId(ez.createUser(cashier.getUsername(), cashier.getPassword(), cashier.getRole()));
			shopManager.setId(ez.createUser(shopManager.getUsername(), shopManager.getPassword(), shopManager.getRole()));
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertNull(ez.getSaleTransaction(sale));
			Assert.assertEquals(ez.getAllProductTypes().size(),0);
			Assert.assertEquals(ez.getAllCustomers().size(),0);
			Assert.assertEquals(ez.getAllOrders().size(),0);
			Assert.assertEquals(ez.getAllUsers().size(),3);
			Assert.assertEquals(ez.getCreditsAndDebits(null, null).size(),0);
		} catch (UnauthorizedException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductDescriptionException | InvalidTransactionIdException | InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Integration test recordOrderArrival
	 */
	@Test
	public void testRecordOrderArrival(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.recordOrderArrival(1));
		int idp=-1;		
		try {
			ez.login(cashier.getUsername(),cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.recordOrderArrival(1));
			ez.logout();
			ez.login(root.getUsername(),root.getPassword());			
			Assert.assertThrows(InvalidOrderIdException.class, () -> ez.recordOrderArrival(null));
			Assert.assertThrows(InvalidOrderIdException.class, () -> ez.recordOrderArrival(-2));
			Assert.assertThrows(InvalidOrderIdException.class, () -> ez.recordOrderArrival(0));
			
						
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			int ido = ez.issueOrder("8023883019015", 20, 25);
			Assert.assertFalse(ez.recordOrderArrival(idp+35));//order doesn't exist
			Assert.assertThrows(InvalidLocationException.class, () -> ez.recordOrderArrival(ido));
			ez.updatePosition(idp, "0-x-0");
			Assert.assertFalse(ez.recordOrderArrival(ido));//order not yet payed/completed
			ez.recordBalanceUpdate(500);
			ez.payOrder(ido);
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.recordOrderArrival(ido));
			restoreTestDB();
			Assert.assertTrue(ez.recordOrderArrival(ido));
			Assert.assertTrue(ez.getAllOrders().stream().filter(o->o.getOrderId()==ido).findFirst().get().getStatus().equals("completed"));
			Assert.assertTrue(ez.recordOrderArrival(ido));//order already completed
			Assert.assertTrue(ez.getAllOrders().stream().filter(o->o.getOrderId()==ido).findFirst().get().getStatus().equals("completed"));			
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductDescriptionException | InvalidProductIdException | InvalidOrderIdException | InvalidLocationException | InvalidQuantityException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(idp);
			} catch (UnauthorizedException | InvalidProductIdException e) {
				e.printStackTrace();
			}			
		}
	}
	/**
	 * Integration test login
	 */
	@Test
	public void testLogin() {
		ez.logout();//no logged in user
		try {
			Assert.assertThrows(InvalidUsernameException.class, () -> ez.login(null,root.getPassword()));
			Assert.assertThrows(InvalidUsernameException.class, () -> ez.login("",root.getPassword()));
			Assert.assertThrows(InvalidPasswordException.class, () -> ez.login(root.getUsername(),null));
			Assert.assertThrows(InvalidPasswordException.class, () -> ez.login(root.getUsername(),""));
			Assert.assertNull(ez.login(root.getUsername(),"wrong_password"));
			Assert.assertNull(ez.login("wrong_user",root.getPassword()));
			User u = ez.login(root.getUsername(), root.getPassword());
			Assert.assertNotNull(u);
			Assert.assertEquals(u.getUsername(),root.getUsername());
			Assert.assertEquals(u.getPassword(),root.getPassword());
			Assert.assertEquals(u.getRole(),root.getRole());
			Assert.assertEquals(u.getId(),root.getId());
			copyAndDeleteTestDB();
			Assert.assertNull(ez.login(root.getUsername(), root.getPassword()));
			restoreTestDB();
		} catch ( InvalidUsernameException | InvalidPasswordException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Integration test endReturnTransaction
	 */
	@Test
	public void testEndReturnTransaction(){		
		ez.logout();//no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.endReturnTransaction(1,true));
		int idp=-1,saleId=-1,returnId=-1,returnId2=-1,returnId3=-1, trasId=-1, returnId4=-1;		
		try {
			ez.login(root.getUsername(),root.getPassword());			
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.endReturnTransaction(-2,true));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.endReturnTransaction(0,true));
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.endReturnTransaction(null,true));
			
			Assert.assertFalse(ez.endReturnTransaction(1, true));//transaction not present
			saleId = ez.startSaleTransaction();
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.updateQuantity(idp, 2);
			ez.addProductToSale(saleId, "8023883019015", 2);			
			ez.endSaleTransaction(saleId);			
			ez.receiveCashPayment(saleId, 20);
			
			// Test DB RFIDS
			trasId = ez.startSaleTransaction();
			int ido = ez.issueOrder("8023883019015", 20, 25);
			int ido2 = ez.issueOrder("8023883019015", 10, 10);
			ez.payOrder(ido);
			ez.payOrder(ido2);
			ez.recordOrderArrivalRFID(ido, "000000000010");//RFIDS from 10 to 29 added
			ez.addProductToSaleRFID(trasId, "000000000010");
			ez.endSaleTransaction(saleId);
			ez.receiveCashPayment(saleId, 30);
			
			Assert.assertTrue(ez.endSaleTransaction(trasId));
			Assert.assertEquals(25.0, ez.getSaleTransaction(trasId).getPrice(), 0);
			Assert.assertFalse(ez.recordOrderArrivalRFID(ido2, "000000000010"));
			
			returnId4 = ez.startReturnTransaction(trasId);
			ez.returnProductRFID(returnId4, "000000000010");
			ez.endReturnTransaction(returnId4, true);
			
			ez = new EZShop();//call the init method to read data from DB
			ez.login(root.getUsername(),root.getPassword());
			Assert.assertFalse(ez.recordOrderArrivalRFID(ido2, "000000000010"));
			// END Test DB RFIDS
			
			returnId = ez.startReturnTransaction(saleId);
			ez.returnProduct(returnId, "8023883019015", 1);
			Assert.assertTrue(ez.endReturnTransaction(returnId, false));			
			Assert.assertEquals(ez.getProductTypeByBarCode("8023883019015").getQuantity(),Integer.valueOf(0));//no change
			returnId2 = ez.startReturnTransaction(saleId);		
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.endReturnTransaction(returnId2, true));//no returnTransactions table
			restoreTestDB();
			ez.returnProduct(returnId2, "8023883019015", 1);
			Assert.assertTrue(ez.endReturnTransaction(returnId2, true));
			Assert.assertEquals(ez.getProductTypeByBarCode("8023883019015").getQuantity(),Integer.valueOf(1));//quantity in the inventory increased
			Assert.assertFalse(ez.endReturnTransaction(returnId2, true));//already closed
			returnId3 = ez.startReturnTransaction(saleId);
			ez.returnProduct(returnId3, "8023883019015", 1);
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.endReturnTransaction(returnId3, true));//no returnProducts table
			restoreTestDB();
			
			
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductDescriptionException | InvalidProductIdException | InvalidLocationException | InvalidQuantityException | InvalidTransactionIdException | InvalidPaymentException | InvalidOrderIdException | InvalidRFIDException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteReturnTransaction(returnId);
				ez.deleteReturnTransaction(returnId2);
				ez.deleteReturnTransaction(returnId3);
				ez.deleteSaleTransaction(saleId);
				ez.deleteProductType(idp);				
			} catch (UnauthorizedException | InvalidProductIdException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}			
		}
	}
	/*
	 * Integration test ReceiveCashPayment
	 */
	@Test
	public void testReceiveCashPayment() {
		ez.logout();// no logged in user
		Integer testNull = null;
		Integer idp = null;
		Integer ids = null;
		String validBarcode1 = "8005235155961";
		Assert.assertThrows(UnauthorizedException.class, () -> ez.receiveCashPayment(1, 1));

		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.receiveCashPayment(testNull, 1));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.receiveCashPayment(-10, 1));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			idp = ez.createProductType("test1", validBarcode1, 10, "noteTest");
			ez.updatePosition(idp, "1-a-1");
			ez.updateQuantity(idp, 100);
			Integer ids1 = ez.startSaleTransaction();
			ids = ids1;
			ez.addProductToSale(ids1, validBarcode1, 1);
			ez.endSaleTransaction(ids1);

			Assert.assertThrows(InvalidPaymentException.class, () -> ez.receiveCashPayment(ids1, -10));
			Assert.assertFalse(ez.receiveCashPayment(ids1 + 1, 10) >= 0);
			Assert.assertFalse(ez.receiveCashPayment(ids1, 1) >= 0);

			copyAndDeleteTestDB();
			Assert.assertFalse(ez.receiveCashPayment(ids, 100) >= 0);
			restoreTestDB();
			Assert.assertTrue(ez.receiveCashPayment(ids, 100) >= 0);

		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException
				| InvalidTransactionIdException | InvalidProductDescriptionException | InvalidProductCodeException
				| InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException
				| InvalidQuantityException | InvalidPaymentException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(ids);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}

		}

	}
	/*
	 * Integration test receiveCreditCardPayment
	 */
	@Test
	public void testReceiveCreditCardPayment() {
		ez.logout();// no logged in user
		Integer idp = null;
		Integer ids = null;
		Integer testIntegerNull = null;
		String testStringNull = null;
		String validBarcode1 = "8005235155961";
		String fullCard = "4485370086510891";
		String emptyCard = "4716258050958645";
		String notSavedCard = "1358954993914435";
		Assert.assertThrows(UnauthorizedException.class, () -> ez.receiveCreditCardPayment(1, fullCard));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class,
					() -> ez.receiveCreditCardPayment(testIntegerNull, fullCard));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.receiveCreditCardPayment(-10, fullCard));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			idp = ez.createProductType("test1", validBarcode1, 1, "noteTest");
			ez.updatePosition(idp, "1-a-1");
			ez.updateQuantity(idp, 100);
			Integer ids1 = ez.startSaleTransaction();
			ids = ids1;
			ez.addProductToSale(ids1, validBarcode1, 1);
			ez.endSaleTransaction(ids1);

			Assert.assertThrows(InvalidCreditCardException.class, () -> ez.receiveCreditCardPayment(ids1, ""));
			Assert.assertThrows(InvalidCreditCardException.class,
					() -> ez.receiveCreditCardPayment(ids1, testStringNull));
			Assert.assertThrows(InvalidCreditCardException.class,
					() -> ez.receiveCreditCardPayment(ids1, "nonExistentCard"));

			Assert.assertFalse(ez.receiveCreditCardPayment(ids1, notSavedCard));
			Assert.assertFalse(ez.receiveCreditCardPayment(ids1, emptyCard));
			Assert.assertFalse(ez.receiveCreditCardPayment(ids1 + 1, fullCard));
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.receiveCreditCardPayment(ids, fullCard));
			restoreTestDB();
			Assert.assertTrue(ez.receiveCreditCardPayment(ids, fullCard));

		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException
				| InvalidTransactionIdException | InvalidProductDescriptionException | InvalidProductCodeException
				| InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException
				| InvalidQuantityException | InvalidCreditCardException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(ids);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}

		}
	}
	/*
	 * Integration test computePointsForSale
	 */
	@Test
	public void testComputePointForSale() {
		Integer testIntegerNull = null;
		Integer idp = null;
		Integer ids = null;
		String validBarcode1 = "8005235155961";
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.computePointsForSale(1));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.computePointsForSale(testIntegerNull));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.computePointsForSale(-10));
			ez.logout();

			ez.login(root.getUsername(), root.getPassword());
			idp = ez.createProductType("test1", validBarcode1, 10, "noteTest");
			ez.updatePosition(idp, "1-a-1");
			ez.updateQuantity(idp, 100);
			Integer ids1 = ez.startSaleTransaction();
			ids = ids1;
			ez.addProductToSale(ids1, validBarcode1, 20);
			Assert.assertFalse(ez.computePointsForSale(ids + 1) >= 0);
			Assert.assertTrue(ez.computePointsForSale(ids) >= 0);
			ez.endSaleTransaction(ids1);
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException
				| InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException
				| InvalidProductIdException | InvalidLocationException | InvalidTransactionIdException
				| InvalidQuantityException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(ids);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * Integration test addProductToSale
	 */
	@Test
	public void testAddProductToSale() {
		Integer testIntegerNull = null;
		String testStringNull = null;
		Integer idp = null;
		Integer ids = null;
		String validBarcode1 = "8005235155961";
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.addProductToSale(1, validBarcode1, 1));

		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class,
					() -> ez.addProductToSale(testIntegerNull, validBarcode1, 1));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.addProductToSale(-1, validBarcode1, 1));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			Integer ids1 = ez.startSaleTransaction();
			ids = ids1;
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.addProductToSale(ids1, testStringNull, 1));
			Assert.assertThrows(InvalidProductCodeException.class, () -> ez.addProductToSale(ids1, "", 1));
			Assert.assertThrows(InvalidProductCodeException.class,
					() -> ez.addProductToSale(ids1, "invalidBarcode", 1));
			Assert.assertThrows(InvalidQuantityException.class, () -> ez.addProductToSale(ids1, validBarcode1, -1));
			Assert.assertFalse(ez.addProductToSale(ids1 + 1, validBarcode1, 1));
			Assert.assertFalse(ez.addProductToSale(ids1, validBarcode1, 1));
			idp = ez.createProductType("test1", validBarcode1, 10, "noteTest");
			ez.updatePosition(idp, "1-a-1");
			ez.updateQuantity(idp, 100);
			Assert.assertFalse(ez.addProductToSale(ids1, validBarcode1, 1000));
			Assert.assertTrue(ez.addProductToSale(ids1, validBarcode1, 1));
			Assert.assertTrue(ez.addProductToSale(ids1, validBarcode1, 1));
			ez.endSaleTransaction(ids1);
			Assert.assertFalse(ez.addProductToSale(ids1, validBarcode1, 1));
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException
				| InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
				| InvalidProductDescriptionException | InvalidPricePerUnitException | InvalidProductIdException
				| InvalidLocationException e) {

			e.printStackTrace();
		} finally {
			try {
				ez.deleteSaleTransaction(ids);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		}

	}
	/*
	 * Integration test computeBalance
	 */
	@Test
	public void testComputeBalance() {
		ez.logout();
		Assert.assertThrows(UnauthorizedException.class, () -> ez.computeBalance());
		try {			
			ez.login(root.getUsername(), root.getPassword());
			double initialBalance = ez.computeBalance();
			ez.recordBalanceUpdate(100);
			ez.recordBalanceUpdate(100);
			Assert.assertTrue(ez.computeBalance() == initialBalance+200);
			ez.recordBalanceUpdate(-200);
		} catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Integration test deleteProductType
	 */
	@Test
	public void testDeleteProductType() {
		Integer testNull = null;
		Integer id = null;
		String validBarcode1 = "8005235155961";
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteProductType(10));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteProductType(10));
			ez.logout();

			ez.login(root.getUsername(), root.getPassword());
			id = ez.createProductType("test1", validBarcode1, 10, "noteTest");
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.deleteProductType(-10));
			Assert.assertThrows(InvalidProductIdException.class, () -> ez.deleteProductType(testNull));

			Assert.assertFalse(ez.deleteProductType(id + 1));
			Assert.assertTrue(ez.deleteProductType(id));

		} catch (InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Integration test getPassword
	 */
	@Test
	public void testStartSaleTransaction() {
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.startSaleTransaction());
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			List <Integer> ids = new ArrayList<>();
			ids.add(ez.startSaleTransaction());
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			ids.add(ez.startSaleTransaction());
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			ids.add(ez.startSaleTransaction());
			ids.forEach((i)->Assert.assertTrue(i>0));
			 
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Integration test getSaleTransaction
	 */
	@Test
	public void testGetSaleTransaction() {
		Integer ids=0;
		Integer testIntegerNull = null;
		Integer idp = null;
		String validBarcode1 = "8005235155961";
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getSaleTransaction(1));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.getSaleTransaction(testIntegerNull));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidTransactionIdException.class, () -> ez.getSaleTransaction(-1));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			idp = ez.createProductType("test1", validBarcode1, 10, "noteTest");
			ez.updatePosition(idp, "1-a-1");
			ez.updateQuantity(idp, 100);
			Integer ids1 = ez.startSaleTransaction();
			ids = ids1;
			ez.addProductToSale(ids1, validBarcode1, 20);
			Assert.assertNull(ez.getSaleTransaction(ids1));
			Assert.assertNull(ez.getSaleTransaction(ids1+1));
			ez.endSaleTransaction(ids1);
			Assert.assertNotNull(ez.getSaleTransaction(ids1));
			 
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException | InvalidTransactionIdException | InvalidQuantityException e) {
			e.printStackTrace();
		}finally {
			try {
				ez.deleteSaleTransaction(ids);
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * Integration test modifyPointsOnCard
	 */
	@Test
	public void testModifyPointsOnCard() {
		String testStringNull = null;
		Integer idCustomer = 0;
		String idCard = "";
		ez.logout();// no logged in user
		String validCard = "1234567893";
		Assert.assertThrows(UnauthorizedException.class, () -> ez.modifyPointsOnCard("test",10));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyPointsOnCard(testStringNull,1));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyPointsOnCard("",1));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			idCustomer = ez.defineCustomer("Test");
			idCard = ez.createCard();
			ez.attachCardToCustomer(idCard, idCustomer);
			//Assert.assertThrows(InvalidCustomerCardException.class, () -> ez.modifyPointsOnCard("test",1));
			Assert.assertFalse(ez.modifyPointsOnCard(validCard,1));
			Assert.assertFalse(ez.modifyPointsOnCard(idCard,-100));
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.modifyPointsOnCard(idCard,1));
			restoreTestDB();
			Assert.assertTrue(ez.modifyPointsOnCard(idCard,1));
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidCustomerNameException | UnauthorizedException | InvalidCustomerIdException | InvalidCustomerCardException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteCustomer(idCustomer);
				//can't delete the loyalty card
			} catch (InvalidCustomerIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
		
	}
	/*
	 * Integration test getUser
	 */
	@Test
	public void testGetUser() {
		Integer testNull = null;
		Integer id = null;
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getUser(10));

		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.getUser(10));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.getUser(-10));
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.getUser(testNull));

			id = ez.createUser("test", "test", "Administrator");
			Assert.assertNull(ez.getUser(id + 1));
			Assert.assertTrue(ez.getUser(id).getUsername().equals("test"));

		} catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException | InvalidUserIdException | UnauthorizedException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteUser(id);
			} catch (InvalidUserIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}

	}
	/*
	 * Integration test createProductType
	 */
	@Test
	public void testCreateProductType() {
		ez.logout();// no logged in user
		String testNull = null;
		String validBarcode1 = "8005235155961";
		String validBarcode2 = "9788891907820";

		Assert.assertThrows(UnauthorizedException.class,
				() -> ez.createProductType("test", validBarcode1, 10, "noteTest"));
		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class,
					() -> ez.createProductType("test", validBarcode1, 10, "noteTest"));
			ez.logout();
			ez.login(shopManager.getUsername(), shopManager.getPassword());
			Assert.assertThrows(InvalidProductDescriptionException.class,
					() -> ez.createProductType("", validBarcode1, 10, "noteTest"));
			ez.logout();

			ez.login(root.getUsername(), root.getPassword());

			Assert.assertThrows(InvalidProductDescriptionException.class,
					() -> ez.createProductType(testNull, validBarcode1, 10, "noteTest"));

			Assert.assertThrows(InvalidProductCodeException.class,
					() -> ez.createProductType("test", "", 10, "noteTest"));
			Assert.assertThrows(InvalidProductCodeException.class,
					() -> ez.createProductType("test", testNull, 10, "noteTest"));
			Assert.assertThrows(InvalidProductCodeException.class,
					() -> ez.createProductType("test", "54541254", 10, "noteTest"));

			Assert.assertThrows(InvalidPricePerUnitException.class,
					() -> ez.createProductType("test", validBarcode1, -10, "noteTest"));
			Integer id1 = 0;
			Integer id2 = 0;
			Assert.assertTrue((id1 = ez.createProductType("test1", validBarcode1, 10, "noteTest")) > 0);
			Assert.assertTrue((id2 = ez.createProductType("test2", validBarcode2, 10, testNull)) > 0);
			Assert.assertFalse(ez.createProductType("test3", validBarcode1, 10, "noteTest") > 0);
			ez.deleteProductType(id1);
			ez.deleteProductType(id2);
			copyAndDeleteTestDB();
			Assert.assertFalse(ez.createProductType("test2", validBarcode2, 10, "noteTest") >= 0);
			restoreTestDB();
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException
				| InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException
				| InvalidProductIdException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Integration test deleteUser
	 */
	@Test
	public void testDeleteUser() {
		Integer testNull = null;
		Integer id = null;
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteUser(10));

		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			Assert.assertThrows(UnauthorizedException.class, () -> ez.deleteUser(10));
			ez.logout();

			ez.login(root.getUsername(), root.getPassword());
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.deleteUser(-10));
			Assert.assertThrows(InvalidUserIdException.class, () -> ez.deleteUser(testNull));

			id = ez.createUser("test", "test", "Administrator");

			Assert.assertFalse(ez.deleteUser(id + 1));
			Assert.assertTrue(ez.deleteUser(id));

		} catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException | InvalidUserIdException
				| UnauthorizedException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteUser(id);
			} catch (InvalidUserIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}

	}
	/*
	 * Integration test getAllCustomers
	 */
	@Test
	public void testGetAllCustomer() {
		List <Integer> idC = new ArrayList<>();
		TreeMap <Integer, Customer> customersMap = new TreeMap<>();
		ez.logout();// no logged in user
		Assert.assertThrows(UnauthorizedException.class, () -> ez.getAllCustomers());

		try {
			ez.login(cashier.getUsername(), cashier.getPassword());
			idC.add(ez.defineCustomer("test1"));
			idC.add(ez.defineCustomer("test2"));
			idC.add(ez.defineCustomer("test3"));
			ez.getAllCustomers().forEach((c)->customersMap.put(c.getId(), c));
			idC.forEach((id)->Assert.assertTrue(customersMap.containsKey(id)));
			ez.logout();
			ez.login(root.getUsername(), root.getPassword());
		} catch (InvalidUsernameException | InvalidPasswordException | InvalidCustomerNameException | UnauthorizedException e) {
			e.printStackTrace();
		} finally {
			idC.forEach((id)->{
				try {
					ez.deleteCustomer(id);
				} catch (InvalidCustomerIdException | UnauthorizedException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	/**
	 * Integeration test getAllRFIDs
	 */
	@Test
	public void testGetAllRFIDs() {
		int idp=-1;
		try {
			ez.login(root.getUsername(),root.getPassword());
			idp = ez.createProductType("desc", "8023883019015", 5.99, "");
			ez.updatePosition(idp, "0-x-0");
			ez.recordBalanceUpdate(600);
			int ido = ez.issueOrder("8023883019015", 20, 25);
			int ido2 = ez.issueOrder("8023883019015", 10, 10);
			ez.payOrder(ido);
			ez.payOrder(ido2);
			ez.recordOrderArrivalRFID(ido, "000000000010");//RFIDS from 10 to 29 added
			ez = new EZShop();//call the init method to read data from DB
			ez.login(root.getUsername(),root.getPassword());			
			Assert.assertThrows(InvalidRFIDException.class,()->ez.recordOrderArrivalRFID(ido2, "000000000020"));//RFIDS from 20 to 29 already present
		} catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidQuantityException | InvalidProductIdException | InvalidLocationException | InvalidOrderIdException | InvalidRFIDException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(idp);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	@Test
	public void testRFIDValidatorRFIDValidator() {		
		Assert.assertFalse(RFIDValidator.validate(null));
		Assert.assertFalse(RFIDValidator.validate("!@#$!@$!@$!@"));
		Assert.assertFalse(RFIDValidator.validate("iqjsmfurjdue"));
		Assert.assertFalse(RFIDValidator.validate("123"));
		Assert.assertFalse(RFIDValidator.validate("12303947561833"));
		Assert.assertTrue(RFIDValidator.validate("038401738492"));
	}
	
	@Test
	public void testRecordOrderArrivalRFIDtest() {
		//ProductTypeObj newProductType=new ProductTypeObj(1,"fuiADqtrg1234!#$","11233211233123",32.250,"203FF#1r1a");
		OrderObj newOrder1=new OrderObj(13,"11233211233123",94,0.123);
		//SaleTransactionObj newTransaction=new SaleTransactionObj();
		int pid2 = -1;
		try {
			ez.logout();
			Assert.assertThrows(UnauthorizedException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("9910239102391", ez.createProductType("qind12313!@#", "9910239102391", 12.23, "some notes"), 12.23),"000000000000"));
			ez.login(cashier.getUsername(), cashier.getPassword());
			
			Assert.assertThrows(UnauthorizedException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("9910239102391", ez.createProductType("qind12313!@#", "9910239102391", 12.23, "some notes"), 12.23),"000000000000"));
			ez.login(root.getUsername(), root.getPassword());
			pid2=ez.createProductType("qind12313!@#", "2343243000009", 12.23, "some notes");
			
			final int productID2 = pid2;
			Assert.assertThrows(InvalidRFIDException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("2343243000009",productID2, 12.23),"aASabcabcavs"));
			Assert.assertThrows(InvalidRFIDException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("2343243000009",productID2, 12.23),"!@#$!@#$#@!$"));
			Assert.assertThrows(InvalidRFIDException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("2343243000009",productID2, 12.23),"123123343"));
			Assert.assertThrows(InvalidRFIDException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("2343243000009",productID2, 12.23),null));
			
			Integer errorOrderId=-1;
			Assert.assertThrows(InvalidOrderIdException.class, () -> ez.recordOrderArrivalRFID(errorOrderId,null));
			ez.getProductTypeByBarCode("2343243000009").setLocation("");
			Assert.assertThrows(InvalidLocationException.class, () -> ez.recordOrderArrivalRFID(ez.issueOrder("2343243000009",100, 12.23),"000000000000"));
			
			Integer nonexistentOrder=99;
			Assert.assertFalse(ez.recordOrderArrivalRFID(nonexistentOrder,"000000000000"));
			//order status
			ez.getProductTypeByBarCode("2343243000009").setLocation("0-A-1");
			int ido=ez.issueOrder("2343243000009",100, 12.23);
			ez.recordBalanceUpdate(1223);
			
			Assert.assertFalse(ez.recordOrderArrivalRFID(ido,"000000000100"));
			Assert.assertFalse(ez.recordOrderArrivalRFID(ido,"000000000200"));
			ez.payOrder(ido);
			Assert.assertTrue(ez.recordOrderArrivalRFID(ido,"000000000200"));

			ez.recordOrderArrivalRFID(newOrder1.getOrderId(),"000000000000");
			Assert.assertThrows(InvalidRFIDException.class, () -> ez.recordOrderArrivalRFID(newOrder1.getOrderId(),"!@#$!@#$#@!$"));
			
			
			ez.deleteProductType(productID2);
		}
		catch( InvalidUsernameException | InvalidPasswordException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException | InvalidOrderIdException | InvalidLocationException | InvalidRFIDException | InvalidQuantityException e) {
			e.printStackTrace();
		} finally {
			try {
				ez.deleteProductType(pid2);
			} catch (InvalidProductIdException | UnauthorizedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testAddProductToSaleRFIDtest() {
		SaleTransactionObj newTransaction=new SaleTransactionObj();
		int idp = -1,ids=-1;
	    try {
	      ez.logout();
	      ez.login(root.getUsername(), root.getPassword());
	      ids=ez.startSaleTransaction();
	      idp = ez.createProductType("qind12313!@#", "2343243000009", 12.23, "some notes");
	      ez.getProductTypeByBarCode("2343243000009").setLocation("0-A-1");
	      ez.recordBalanceUpdate(1223);
	      Integer orderId=ez.issueOrder("2343243000009",100, 12.23);
	      ez.payOrder(orderId);
	      ez.recordOrderArrivalRFID(orderId,"000000000500");
	      
	      Integer errorTransactionId=-1;
	      int transactionID = ids;
	      Assert.assertThrows(InvalidTransactionIdException.class, () ->ez.addProductToSaleRFID(null,"000000000501"));
	      Assert.assertThrows(InvalidTransactionIdException.class, () ->ez.addProductToSaleRFID(errorTransactionId,"000000000501"));
	      Assert.assertThrows(InvalidRFIDException.class, () ->ez.addProductToSaleRFID(transactionID,"aqwASD1232334"));
	      Assert.assertThrows(InvalidRFIDException.class, () ->ez.addProductToSaleRFID(transactionID,"!@#$##1232334"));
	      Assert.assertThrows(InvalidRFIDException.class, () ->ez.addProductToSaleRFID(transactionID,null));
	      Assert.assertThrows(InvalidRFIDException.class, () ->ez.addProductToSaleRFID(transactionID,"00001234000000"));
	      Assert.assertThrows(InvalidRFIDException.class, () ->ez.addProductToSaleRFID(transactionID,"000000"));
	      ez.logout();
	      Assert.assertThrows(UnauthorizedException.class, () ->ez.addProductToSaleRFID(newTransaction.getTicketNumber(),"000000000501"));  
	      
	      ez.login(root.getUsername(), root.getPassword());	      
	      Assert.assertTrue(ez.addProductToSaleRFID(transactionID,"000000000501"));	      
	    }
	    catch(InvalidTransactionIdException|InvalidQuantityException|InvalidUsernameException|InvalidPasswordException|InvalidOrderIdException|InvalidRFIDException|InvalidLocationException|UnauthorizedException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
	      e.printStackTrace();
	    } finally {
	      try {
			ez.deleteProductType(idp);
			ez.deleteSaleTransaction(ids);
			} catch (InvalidProductIdException | InvalidTransactionIdException | UnauthorizedException e) {
				e.printStackTrace();
		    }
	    }
	  }
	
	@Test
    public void testDeleteProductFromSaleRFIDtest() {    
        int ids=-1,idp=-1;
        try {
          ez.logout();
          ez.login(root.getUsername(), root.getPassword());
          ids=ez.startSaleTransaction();
	      idp = ez.createProductType("qind12313!@#", "2343243000009", 12.23, "some notes");
	      ez.getProductTypeByBarCode("2343243000009").setLocation("0-A-1");
	      ez.recordBalanceUpdate(1223);
	      Integer orderId=ez.issueOrder("2343243000009",100, 12.23);
	      ez.payOrder(orderId);
	      ez.recordOrderArrivalRFID(orderId,"000000001500");

	      ez.login(root.getUsername(), root.getPassword());	      
	      Assert.assertTrue(ez.addProductToSaleRFID(ids,"000000001501"));	      
          Assert.assertTrue(ez.deleteProductFromSaleRFID(ids, "000000001501"));
        } catch (InvalidOrderIdException|InvalidQuantityException|UnauthorizedException|InvalidLocationException|InvalidRFIDException|InvalidUsernameException|InvalidPasswordException|InvalidTransactionIdException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductDescriptionException e) {
          e.printStackTrace();
        } finally {
            try {
            	ez.deleteProductType(idp);
                ez.deleteSaleTransaction(ids);
            } catch (InvalidTransactionIdException | UnauthorizedException | InvalidProductIdException e) {
                e.printStackTrace();
			}
        }
      }
	
	@Test
	public void testReturnProductRFIDtest() {	
		int ids=-1,idp=-1,idr=-1;
		try {
			ez.logout();//no logged in user
			
			ez.login(root.getUsername(), root.getPassword());
			
			
			ids=ez.startSaleTransaction();
		      idp = ez.createProductType("qind12313!@#", "2343243000009", 12.23, "some notes");
		      ez.getProductTypeByBarCode("2343243000009").setLocation("0-A-1");
		      ez.recordBalanceUpdate(1223);
		      Integer orderId=ez.issueOrder("2343243000009",100, 12.23);
		      ez.payOrder(orderId);
			
			ez.recordOrderArrivalRFID(orderId,"000000004000");		    
		    ez.addProductToSale(ids, "2343243000009", 10);
		    ez.addProductToSaleRFID(ids, "000000004000");
		    ez.endSaleTransaction(ids);
		    ez.receiveCashPayment(ids, 30000);
		    idr = ez.startReturnTransaction(ids);
			Assert.assertTrue(ez.returnProductRFID(idr, "000000004000"));
			ez.endReturnTransaction(idr, false);
		}catch(InvalidTransactionIdException|InvalidProductCodeException|InvalidQuantityException|InvalidPasswordException|InvalidUsernameException|UnauthorizedException | InvalidRFIDException | InvalidPricePerUnitException | InvalidOrderIdException | InvalidLocationException | InvalidProductDescriptionException | InvalidPaymentException e) {
			e.printStackTrace();
		}finally {
			try {
				ez.deleteProductType(idp);
				ez.deleteSaleTransaction(ids);
			} catch (InvalidProductIdException | UnauthorizedException | InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * restore DB from backup
	 */
	@AfterClass
	public static void tearDown() {
		try {
			Files.copy(Path.of(dbBackupPath), Path.of(DBManager.dbPath),StandardCopyOption.REPLACE_EXISTING);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
