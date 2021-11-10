package com.garb.gbcollector.web.vo;

import java.util.Date;
import java.util.List;

public class FeedVO {

	//field
	private int feedNo, commentCnt;
	private String challengeNum, challengeName, email, writer, content, postDate, changeYn;
	private Date postTime;
	private List<Integer> imageIdxs;
	
	//constructor
	public FeedVO() {
		super();
	}
	
	public FeedVO(int feedNo, int commentCnt, String challengeNum, String email, 
			String writer, String content, String postDate, Date postTime) {
		super();
		setFeedNo(feedNo);
		setCommentCnt(commentCnt);
		setChallengeNum(challengeNum);
		setEmail(email);
		setWriter(writer);
		setContent(content);
		setPostDate(postDate);
		setPostTime(postTime);
	}

	//getter&setter
	public int getFeedNo() {
		return feedNo;
	}

	public void setFeedNo(int feedNo) {
		this.feedNo = feedNo;
	}

	public int getCommentCnt() {
		return commentCnt;
	}

	public void setCommentCnt(int commentCnt) {
		this.commentCnt = commentCnt;
	}

	public String getChallengeNum() {
		return challengeNum;
	}

	public void setChallengeNum(String challengeNum) {
		this.challengeNum = challengeNum;
	}

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	
	public Date getPostTime() { 
		return postTime; 
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public String getChangeYn() {
		return changeYn;
	}

	public void setChangeYn(String changeYn) {
		this.changeYn = changeYn;
	}

	public List<Integer> getImageIdxs() {
		return imageIdxs;
	}

	public void setImageIdxs(List<Integer> imageIdxs) {
		this.imageIdxs = imageIdxs;
	}

	@Override
	public String toString() {
		return "FeedVO [feedNo=" + feedNo + ", commentCnt=" + commentCnt + ", challengeNum=" + challengeNum
				+ ", challengeName=" + challengeName + ", email=" + email + ", writer=" + writer + ", content="
				+ content + ", postDate=" + postDate + ", changeYn=" + changeYn + ", postTime=" + postTime
				+ ", imageIdxs=" + imageIdxs + "]";
	}	
}
