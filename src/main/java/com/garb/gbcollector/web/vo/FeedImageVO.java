package com.garb.gbcollector.web.vo;

import java.util.Date;

public class FeedImageVO {
	
	private Integer idx;
	private Integer feedNo;
	private String originalName;
	private String saveName;
	private Integer imgSize;
	private Date insertTime;

	public FeedImageVO() {
		super();
	}
	
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public Integer getFeedNo() {
		return feedNo;
	}
	public void setFeedNo(Integer feedNo) {
		this.feedNo = feedNo;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	public Integer getImgSize() {
		return imgSize;
	}
	public void setImgSize(Integer imgSize) {
		this.imgSize = imgSize;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	
}
