package com.garb.gbcollector.web.vo;

import java.util.Date;

public class BoardReplyVO {
	
	private Integer postno, reno;
	private String rewriter, rememo, nickname;
	private Date redate;
	
	
	public BoardReplyVO(Integer postno, Integer reno, 
			String rewriter, String rememo, String nickname,
			Date redate) {
		super();
		setPostno(postno);
		setReno(reno);
		setRewriter(rewriter);
		setRememo(rememo);
		setNickname(nickname);
		setRedate(redate);
	}
	
	public BoardReplyVO(Integer postno,  
			String rememo, String nickname) {
		super();
		setPostno(postno);
		setRewriter(nickname);
		setRememo(rememo);
	}
	
	
	@Override
	public String toString() {
		return "BoardReplyVO [postno=" + postno + ", reno=" + reno + ", rewriter=" + rewriter + ", rememo=" + rememo
				+ ", nickname=" + nickname + ", redate=" + redate + "]";
	}

	public BoardReplyVO() {
		super();
	}
	
	public Integer getPostno() {
		return postno;
	}
	public void setPostno(Integer postno) {
		this.postno = postno;
	}
	public Integer getReno() {
		return reno;
	}
	public void setReno(Integer reno) {
		this.reno = reno;
	}
	public String getRewriter() {
		return rewriter;
	}
	public void setRewriter(String rewriter) {
		this.rewriter = rewriter;
	}
	public String getRememo() {
		return rememo;
	}
	public void setRememo(String rememo) {
		this.rememo = rememo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getRedate() {
		return redate;
	}
	public void setRedate(Date redate) {
		this.redate = redate;
	}
	
	

}
