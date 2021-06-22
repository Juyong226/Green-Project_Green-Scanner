package com.garb.gbcollector.web.vo;

import com.garb.gbcollector.util.GbcException;

// Setter 미사용

public class TrashCanVO {
	private String address;
	private float latitude,longitude;
	
	
	public TrashCanVO(String address, float latitude, float longitude) throws GbcException{
		super();
		setAddress(address);
		setLatitude(latitude);
		setLongitude(longitude);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) throws GbcException{
		if(address==null) {
			throw new GbcException("이름이 입력되지 않았습니다.");
		}else {
			this.address = address;
		}
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) throws GbcException{
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) throws GbcException{
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "TrashCanVO [address=" + address + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
