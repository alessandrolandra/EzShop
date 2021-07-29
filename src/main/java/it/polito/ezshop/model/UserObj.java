package it.polito.ezshop.model;

import java.util.TreeMap;
import it.polito.ezshop.data.User;

public class UserObj implements User {
	
	
	private Integer id;
	private String username;
	private String password;
	private String role;
	
	public UserObj(Integer id, String username, String password, String role) {
		this.setId(id);
		this.setUsername(username);
		this.setPassword(password);
		this.setRole(role);
	}
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setUsername(String username) {
		this.username=username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setPassword(String password) {
		this.password=password;
	}

	@Override
	public String getRole() {
		return this.role;
	}

	@Override
	public void setRole(String role) {
		this.role=role;
	}
	
	public static boolean verifyUsername(TreeMap<Integer, User> users, String username){
			for(User value : users.values()) {
				if(value.getUsername().equals(username))
	            {
	                return true;
	            }
			}
	        return false;
	    }
}
