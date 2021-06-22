package com.garb.gbcollector.web.vo;

import com.garb.gbcollector.util.GbcException;

public class ZeroWasteShopVO {
	private String name,address,closeddays,contact,type;
	private float latitude,longitude;
	
	public ZeroWasteShopVO() {
		super();
	}
	public ZeroWasteShopVO(String name, String address, String closeddays, String contact, float latitude,
			float longitude,String type) throws GbcException{
		super();
		setName(name);
		setAddress(address);
		setCloseddays(closeddays);
		setContact(contact);
		setLatitude(latitude);
		setLongitude(longitude);
		setType(type);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) throws GbcException{
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) throws GbcException{
		if(name==null) {
			throw new GbcException("이름이 입력되지 않았습니다.");
		}else {
			this.name = name;
		}
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) throws GbcException{
		this.address = address;
	}
	public String getCloseddays() {
		if(closeddays==null) {
			closeddays = "X";
		}
		return closeddays;
	}
	public void setCloseddays(String closeddays) throws GbcException{
		this.closeddays = closeddays;
	}
	public String getContact() {
		if(contact==null) {
			contact = "X";
		}
		return contact;
	}
	public void setContact(String contact) throws GbcException{
		this.contact = contact;
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
		return "ZeroWasteShopVO [name=" + name + ", address=" + address + ", closeddays=" + closeddays + ", contact="
				+ contact + ", type=" + type + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
