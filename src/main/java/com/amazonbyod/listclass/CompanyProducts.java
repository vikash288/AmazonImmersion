package com.amazonbyod.listclass;

import java.util.Date;

public class CompanyProducts {
	

	String productId;
	String companyID;
	String product_name;
	String product_description;
	String product_type;
	Date product_initaldate;
	int marketvol;
	float lat;
	float lng;
	String product_loc;
	
	public CompanyProducts(String productId, String companyID, String product_name, String product_description,
			String product_type, Date product_initaldate,int marketvol,float lat,float lng,String product_loc) {
		super();
		this.productId=productId;
		this.companyID=companyID;
		this.product_name=product_name;
		this.product_description=product_description;
		this.product_type=product_type;
		this.product_initaldate=product_initaldate;
		this.marketvol=marketvol;
		this.lat=lat;
		this.lng=lng;
		this.product_loc=product_loc;
	}
	
	
	public String getProductId() {
		return productId;
	}

	public String getCompanyID() {
		return companyID;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public String getProduct_type() {
		return product_type;
	}

	public Date getProduct_initaldate() {
		return product_initaldate;
	}

	public int getMarketvol() {
		return marketvol;
	}

	public float getLat() {
		return lat;
	}

	public float getLng() {
		return lng;
	}

	public String getProduct_loc() {
		return product_loc;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public void setProduct_initaldate(Date product_initaldate) {
		this.product_initaldate = product_initaldate;
	}

	public void setMarketvol(int marketvol) {
		this.marketvol = marketvol;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public void setProduct_loc(String product_loc) {
		this.product_loc = product_loc;
	}


}
