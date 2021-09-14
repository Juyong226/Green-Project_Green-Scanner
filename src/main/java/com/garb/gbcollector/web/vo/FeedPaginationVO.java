package com.garb.gbcollector.web.vo;

public class FeedPaginationVO {

	private String startIdx;
	private String endIdx;
	private String challengeNum;
	
	public FeedPaginationVO() {
		setStartIdx("1");
		setEndIdx("5");
	}
	
	public FeedPaginationVO(String startIdx, String endIdx, String challengeNum) {
		super();
		setStartIdx(startIdx);
		setEndIdx(endIdx);
		setChallengeNum(challengeNum);
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

	public String getChallengeNum() {
		return challengeNum;
	}

	public void setChallengeNum(String challengeNum) {
		this.challengeNum = challengeNum;
	}	
}
