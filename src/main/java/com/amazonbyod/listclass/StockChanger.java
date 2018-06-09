package com.amazonbyod.listclass;

import java.util.Date;

public class StockChanger {
	
	String eventDate;
	int changeType;
	
	public StockChanger(String eventDate,int changeType){
		this.eventDate=eventDate;
		this.changeType=changeType;
	}
	
	
	public String getEventDate() {
		return eventDate;
	}

	public int getChangeType() {
		return changeType;
	}

	

}
