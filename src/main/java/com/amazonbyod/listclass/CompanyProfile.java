package com.amazonbyod.listclass;

import java.util.Date;

public class CompanyProfile {
	
	private String companyId;
	private String ComapanyName;
	private String companySymbol;
	private String companyAddress;
	Date company_foundedon;
	private String company_ceo;
	private String company_assets;
	private String company_revenue;
	
	public CompanyProfile(String companyId, String ComapanyName, String companySymbol, String companyAddress,
			Date company_foundedon, String company_ceo, String company_assets, String company_revenue) {
			
		super();
		
		this.companyId=companyId;
		this.ComapanyName=ComapanyName;
		this.companySymbol=companySymbol;
		this.companyAddress=companyAddress;
		this.company_foundedon=company_foundedon;
		this.company_ceo=company_ceo;
		this.company_assets=company_assets;
		this.company_revenue=company_revenue;
	}
	
	
	public String getCompanyId() {
		return companyId;
	}
	public String getComapanyName() {
		return ComapanyName;
	}
	public String getCompanySymbol() {
		return companySymbol;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public Date getCompany_foundedon() {
		return company_foundedon;
	}
	public String getCompany_ceo() {
		return company_ceo;
	}
	public String getCompany_assets() {
		return company_assets;
	}
	public String getCompany_revenue() {
		return company_revenue;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setComapanyName(String comapanyName) {
		ComapanyName = comapanyName;
	}
	public void setCompanySymbol(String companySymbol) {
		this.companySymbol = companySymbol;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public void setCompany_foundedon(Date company_foundedon) {
		this.company_foundedon = company_foundedon;
	}
	public void setCompany_ceo(String company_ceo) {
		this.company_ceo = company_ceo;
	}
	public void setCompany_assets(String company_assets) {
		this.company_assets = company_assets;
	}
	public void setCompany_revenue(String company_revenue) {
		this.company_revenue = company_revenue;
	}

}
