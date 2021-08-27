package com.garb.gbcollector.web.vo;

import java.util.Date;

public class BoardVO {
	
	private int postno, parentno, reply_cnt;
	private String boardname, title, content, email, nickname, isanon, filename;
	private Date postdate;
	
	public BoardVO(int postno, int parentno, String boardname, int reply_cnt,
			String title, String content, String email, String nickname, Date postdate, String filename) {
		super();
		setReply_cnt(reply_cnt);
		setBoardname(boardname);
		setPostno(postno);
		setParentno(parentno);
		setTitle(title);
		setContent(content);
		setEmail(email);
		setNickname(nickname);
		setPostdate(postdate);
		setFilename(filename);	
	}

	

	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}



	public BoardVO() {
		super();
	}
	
	public BoardVO(String boardname, String title, String nickname, String content) {
		super();
		setTitle(title);
		setNickname(nickname);
		setContent(content);
		setBoardname(boardname);
	}
	
	public BoardVO(String boardname, String title, String nickname, String content, String isanon) {
		super();
		setTitle(title);
		setNickname(nickname);
		setContent(content);
		setBoardname(boardname);
		setIsanon(isanon);
	}
	
	public BoardVO(String boardname, String title, String nickname, String content, String isanon, String filename) {
		super();
		setTitle(title);
		setNickname(nickname);
		setContent(content);
		setBoardname(boardname);
		setIsanon(isanon);
		setFilename(filename);
	}
	
	public String getIsanon() {
		return isanon;
	}

	public void setIsanon(String isanon) {
		this.isanon = isanon;
	}
	
	public int getReply_cnt() {
		return reply_cnt;
	}
	public void setReply_cnt(int reply_cnt) {
		this.reply_cnt = reply_cnt;
	}
	public int getPostno() {
		return postno;
	}
	public void setPostno(int postno) {
		this.postno = postno;
	}
	public int getParentno() {
		return parentno;
	}
	public void setParentno(int parentno) {
		this.parentno = parentno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getPostdate() {
		return postdate;
	}
	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}
	public String getBoardname() {
		return boardname;
	}
	public void setBoardname(String boardname) {
		this.boardname = boardname;
	}



	@Override
	public String toString() {
		return "BoardVO [postno=" + postno + ", parentno=" + parentno + ", reply_cnt=" + reply_cnt + ", boardname="
				+ boardname + ", title=" + title + ", content=" + content + ", email=" + email + ", nickname="
				+ nickname + ", isanon=" + isanon + ", filename=" + filename + ", postdate=" + postdate + "]";
	}




}
