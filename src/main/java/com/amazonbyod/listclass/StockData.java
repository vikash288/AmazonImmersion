package com.amazonbyod.listclass;

import java.util.Date;

public class StockData {
	
  private String companySymbol;
  private Date stockDate;
  private Date stocktime;
  private float stockopen;
  private float stockhigh;
  private float stockclose;
  private int stockvol;
  private float stockdiv;
  private int stocksplit;
  private float stockadjopen;
  private float stockadjhigh;
  private float stockadjlow;
  private float stockadjclose;
  private int stockadjvol;
  
	public StockData(String companySymbol,Date stockDate, Date stocktime,float stockopen, float stockhigh,float stocklow, float stockclose, int stockvol, float stockdiv,
		int stocksplit, float stockadjopen, float stockadjhigh,float stockadjlow,float stockadjclose,int stockadjvol) {
		this.companySymbol=companySymbol;
		this.stockDate=stockDate;
		this.stockopen=stockopen;
		this.stockhigh=stockhigh;
		this.stockclose=stockclose;
		this.stockvol=stockvol;
		this.stockdiv=stockdiv;
		this.stocksplit=stocksplit;
		this.stockadjopen=stockadjopen;
		this.stockadjhigh=stockadjhigh;
		this.stockadjlow=stockadjlow;
		this.stockadjclose=stockadjclose;
		this.stockadjvol=stockadjvol;
	}
	
	public String getCompanySymbol() {
		return companySymbol;
	}
	
	  public Date getStockDate() {
			return stockDate;
		}
	  
	  public Date getStockTime() {
			return stocktime;
		}

		public float getStockopen() {
			return stockopen;
		}

		public float getStockhigh() {
			return stockhigh;
		}

		public float getStockclose() {
			return stockclose;
		}

		public int getStockvol() {
			return stockvol;
		}

		public float getStockdiv() {
			return stockdiv;
		}

		public int getStocksplit() {
			return stocksplit;
		}

		public float getStockadjopen() {
			return stockadjopen;
		}

		public float getStockadjhigh() {
			return stockadjhigh;
		}

		public float getStockadjlow() {
			return stockadjlow;
		}

		public float getStockadjclose() {
			return stockadjclose;
		}

		public int getStockadjvol() {
			return stockadjvol;
		}

}
