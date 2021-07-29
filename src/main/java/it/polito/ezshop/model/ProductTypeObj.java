package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.ezshop.data.ProductType;

public class ProductTypeObj implements ProductType{
	
	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;
	
	public ProductTypeObj(Integer id, String description, String productCode, double pricePerUnit, String note){
		this.setId(id);
		this.setProductDescription(description);
		this.setBarCode(productCode);
		this.setPricePerUnit(pricePerUnit);
		this.setNote(note);
		this.setQuantity(0);
		this.setLocation("");
	}
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		this.quantity=quantity;
	}

	@Override
	public String getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(String location) {
		this.location=location;
	}

	@Override
	public String getNote() {
		return this.note;
	}

	@Override
	public void setNote(String note) {
		this.note=note;
	}

	@Override
	public String getProductDescription() {
		return this.productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.productDescription=productDescription;
	}

	@Override
	public String getBarCode() {
		return this.barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.barCode=barCode;
	}

	@Override
	public Double getPricePerUnit() {
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit=pricePerUnit;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
	}
	
	public static ProductType searchProductByBarCode(TreeMap<Integer, ProductType> inventory, String barCode) {
		for(ProductType value : inventory.values()) {
			if(value.getBarCode().equals(barCode))
            {
                return value;
            }
		}
        return null;
	}
	public static List<ProductType> searchProductDescription(TreeMap<Integer, ProductType> inventory, String description) {
		List<ProductType> p =  new ArrayList<>();
		for(ProductType value : inventory.values()) {
			if(value.getProductDescription().contains(description))
            {
                p.add(value);
            }
		}
		return p;
		
	}
	
	public static boolean verifyString(String position){
		String[] p = position.split("-",3);
		if(p.length == 3 && p[0].matches("[0-9]+") && p[1].toLowerCase().matches("[a-z]+") && p[2].matches("[0-9]+")) {
			return false;
		}
		return true;
	}
	
	public static boolean verifyPosition(Map<Integer, ProductType> inventory, String p){
		for(ProductType value : inventory.values()) {
			if(value.getLocation().equals(p))
            {
                return true;
            }
		}
        return false;
    }

}
