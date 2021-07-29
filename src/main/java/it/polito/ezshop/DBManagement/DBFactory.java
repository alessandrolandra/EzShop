package it.polito.ezshop.DBManagement;

public class DBFactory {
	
	/**
	 * create the real implementation of the IDBManager interface
	 * @return IDBManager interface
	 */
	public static IDBManager createManager() {
		return new DBManager();
	}

}
