package com.garb.gbcollector.web.vo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import com.garb.gbcollector.util.GbcException;

public class TemperatureVO {
	private String today;
	String average;
	private String lowest;
	String highest;
	
	public TemperatureVO(String today, String average, String lowest, String highest)throws GbcException {
		super();
		setToday(today);
		setAverage(average);
		setLowest(lowest);
		setHighest(highest);
	}

	public String getToday(){
		
		return today;
	}
	public void setToday(String today)throws GbcException{
		if(today==null) {
			throw new GbcException("날짜요청 되지 않음");
		}else {
			this.today = today;
		}
		
			
	}

	public String getAverage() {
		return average;
	}
	public void setAverage(String average) throws GbcException{
		this.average = average;
		
	}

	public String getLowest() {
		return lowest;
	}
	public void setLowest(String lowest) throws GbcException{
		this.lowest = lowest;
	}
	public String getHighest() {
		return highest;
	}
	public void setHighest(String highest) throws GbcException{
		this.highest = highest;
	}

	@Override
	public String toString() {
		return "TemperatureVO [today=" + today + ", average=" + average + ", lowest=" + lowest + ", highest=" + highest
				+ "]";
	}
	
}
