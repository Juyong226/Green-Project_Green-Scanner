package com.garb.gbcollector.web.vo;

import java.util.Date;

public class FeedCommentVO {

	private Integer idx;
	private Integer feedNo;
	private String content;
	private String writer;
	private String deleteYn;
	private Date insertTime;
	private Date updateTime;
	private Date deleteTime;
	private Integer cClass;
	private Integer cOrder;
	private Integer cGroup;
	
	public FeedCommentVO() {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getcClass() {
		return cClass;
	}

	public void setcClass(Integer cClass) {
		this.cClass = cClass;
	}

	public Integer getcOrder() {
		return cOrder;
	}

	public void setcOrder(Integer cOrder) {
		this.cOrder = cOrder;
	}

	public Integer getcGroup() {
		return cGroup;
	}

	public void setcGroup(Integer cGroup) {
		this.cGroup = cGroup;
	}

	@Override
	public String toString() {
		return "FeedCommentVO [idx=" + idx + ", feedNo=" + feedNo + ", content=" + content + ", writer=" + writer
				+ ", deleteYn=" + deleteYn + ", insertTime=" + insertTime + ", updateTime=" + updateTime
				+ ", deleteTime=" + deleteTime + ", cClass=" + cClass + ", cOrder=" + cOrder + ", cGroup=" + cGroup
				+ "]";
	}
}
