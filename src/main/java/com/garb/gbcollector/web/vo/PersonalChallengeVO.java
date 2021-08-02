package com.garb.gbcollector.web.vo;

import java.util.Date;

public class PersonalChallengeVO {

	private String challengeCode, challengeName, challengeNum, email, thumbnailURL, colorCode, period,
				startDate, endDate, isCompleted, isSucceeded;
	private int executionNum;
	private long achievementRate;
 
	
	public PersonalChallengeVO(String challengeCode, String challengeName, String challengeNum, String email, String thumbnailURL, String colorCode, 
			String period, String startDate, String endDate, int executionNum, long achievementRate, 
			String isCompleted, String isSucceeded) {
		super();
		setChallengeCode(challengeCode);
		setChallengeName(challengeName);
		setChallengeNum(challengeNum);
		setEmail(email);
		setThumbnailURL(thumbnailURL);
		setColorCode(colorCode);
		setPeriod(period);
		setExecutionNum(executionNum);
		setStartDate(startDate);
		setEndDate(endDate);
		setAchievementRate(achievementRate);
		setCompleted(isCompleted);
		setSucceeded(isSucceeded);
	}
	
	public String getChallengeCode() {
		return challengeCode;
	}
	
	public void setChallengeCode(String challengeCode) {
		this.challengeCode = challengeCode;
	}
	
	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getChallengeNum() {
		return challengeNum;
	}

	public void setChallengeNum(String challengeNum) {
		this.challengeNum = challengeNum;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
	
	public String getColorCode() {
		return colorCode;
	}
	
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public int getExecutionNum() {
		return executionNum;
	}
	
	public void setExecutionNum(int executionNum) {
		this.executionNum = executionNum;
	}
	
	public long getAchievementRate() {
		return achievementRate;
	}
	
	public void setAchievementRate(long achievementRate) {
		this.achievementRate = achievementRate;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String isCompleted() {
		return isCompleted;
	}
	
	public void setCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	public String isSucceeded() {
		return isSucceeded;
	}
	
	public void setSucceeded(String isSucceeded) {
		this.isSucceeded = isSucceeded;
	}
	
	
	
	
}
