package com.amazonbyod.listclass;

import java.util.Date;

public class WeatherData {
	
	private String stationCode;
	private String stationName;
	private float lat;
	private float lng;
	private Date date;
	private int tmax;
	private int tmin;
	private float wind;
	private float rain;
	private int snowfall;
	private int storm;
	
	public WeatherData(String stationCode, String stationName, float lat, float lng, Date date, int tmax, int tmin,
			float wind, float rain, int snowfall, int storm) {
		
		this.stationCode=stationCode;
		this.stationName=stationName;
		this.lat=lat;
		this.lng=lng;
		this.date=date;
		this.tmax=tmax;
		this.tmin=tmin;
		this.wind=wind;
		this.rain=rain;
		this.snowfall=snowfall;
		this.storm=storm;

	}
	
	public String getStationCode() {
		return stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public float getLat() {
		return lat;
	}

	public float getLng() {
		return lng;
	}

	public Date getDate() {
		return date;
	}

	public int getTmax() {
		return tmax;
	}

	public int getTmin() {
		return tmin;
	}

	public float getWind() {
		return wind;
	}

	public float getRain() {
		return rain;
	}

	public int getSnowfall() {
		return snowfall;
	}

	public int getStorm() {
		return storm;
	}



}
