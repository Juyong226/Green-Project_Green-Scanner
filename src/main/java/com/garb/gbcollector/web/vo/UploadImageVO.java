package com.garb.gbcollector.web.vo;

import java.util.Date;

public class UploadImageVO {
	
	private Integer idx;
	private Integer feedNo;
	private String originalName;
	private String saveName;
	private long imgSize;
	private String deleteYn;
	private Date insertTime;
	private Date deleteTime;

	public UploadImageVO() {
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

	public long getImgSize() {
		return imgSize;
	}

	public void setImgSize(long imgSize) {
		this.imgSize = imgSize;
	}

	public String getDeleteYn() {
		return deleteYn;
	}

	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	public String toString() {
		return "FeedImageVO [idx=" + idx + ", feedNo=" + feedNo + ", originalName=" + originalName + ", saveName="
				+ saveName + ", imgSize=" + imgSize + ", deleteYn=" + deleteYn + ", insertTime=" + insertTime
				+ ", deleteTime=" + deleteTime + "]";
	}	
}
