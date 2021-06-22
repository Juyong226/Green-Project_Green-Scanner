package com.garb.gbcollector.web.vo;

import com.garb.gbcollector.util.GbcException;

// Setter - 서버에서 Insert 하는 과정이 없음

public class GarbageVO {
	
	private String garbagetype, garbagename, garbagedm;
	
	public GarbageVO(String garbagetype, String garbagename, String garbagedm) throws GbcException {
		this(garbagename, garbagedm);
		setGarbagetype(garbagetype);
	}

	public GarbageVO(String garbagename, String garbagedm) throws GbcException {
		this(garbagename);
		setGarbagedm(garbagedm);		
	}
	
	public GarbageVO(String garbagename) throws GbcException{
		super();
		setGarbagename(garbagename);
	}
	
	public GarbageVO() {
		super();
	}
	
	public String getGarbagetype() {
		return garbagetype;
	}

	public void setGarbagetype(String garbagetype) throws GbcException{		
		if(garbagetype!=null) {
			this.garbagetype = garbagetype;
		}else {
			throw new GbcException("garbagetype이 입력되지 않았습니다.");
		}
	}

	public String getGarbagename() {
		return garbagename;
	}

	public void setGarbagename(String garbagename) throws GbcException {
		if(garbagename!=null) {
			this.garbagename = garbagename;
		}else {
			throw new GbcException("garbagename이 입력되지 않았습니다.");
		}
	}

	public String getGarbagedm() {
		return garbagedm;
	}

	public void setGarbagedm(String garbagedm) throws GbcException {
		if(garbagedm!=null) {
			this.garbagedm = garbagedm;
		}else {
			throw new GbcException("garbagedm이 입력되지 않았습니다.");
		}
	}

	@Override
	public String toString() {
		return "[garbagename=" + garbagename + ", garbagedm=" + garbagedm + "]";
	}
	
}
