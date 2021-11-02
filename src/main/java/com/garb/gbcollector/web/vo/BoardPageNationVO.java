package com.garb.gbcollector.web.vo;

public class BoardPageNationVO {
	
	String startIdx;
	String endIdx;
	String postno;
	
	public BoardPageNationVO(String startIdx, String endIdx, String postno) {
		super();
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		this.postno = postno;
	}
	public BoardPageNationVO() {
		super();
	}
	
	public String getStartIdx() {
		return startIdx;
	}
	public void setStartIdx(String startIdx) {
		this.startIdx = startIdx;
	}
	public String getEndIdx() {
		return endIdx;
	}
	public void setEndIdx(String endIdx) {
		this.endIdx = endIdx;
	}
	public String getPostno() {
		return postno;
	}
	public void setPostno(String postno) {
		this.postno = postno;
	}
}
