package com.garb.gbcollector.web.vo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.garb.gbcollector.util.GSCalendar;

public class PersonalChallengeVO {
	GSCalendar gsCalendar = GSCalendar.getInstance();
	
	private String challengeCode, challengeName, challengeNum, email, thumbnailURL, colorCode, period,
				startDate, endDate, calendar;
	private int isCompleted, isSucceeded, executionNum, achievementRate, daysRemained;
	private List<ArrayList<Character>> calList;

	public PersonalChallengeVO() {
		super();
	}
	
	public PersonalChallengeVO(String challengeCode, String challengeName, String challengeNum, String email, String thumbnailURL, String colorCode, 
			String period, String startDate, String endDate, String calendar) {
		super();
		setChallengeCode(challengeCode);
		setChallengeName(challengeName);
		setChallengeNum(challengeNum);
		setEmail(email);
		setThumbnailURL(thumbnailURL);
		setColorCode(colorCode);
		setPeriod(period);
		setStartDate(startDate);
		setEndDate(endDate);
		setCalendar(calendar);
		
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
		countDatesToEnd();
	}


	public String getCalendar() {
		return calendar;
	}


	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}


	public int getIsCompleted() {
		return isCompleted;
	}


	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}


	public int getIsSucceeded() {
		return isSucceeded;
	}


	public void setIsSucceeded(int isSucceeded) {
		this.isSucceeded = isSucceeded;
	}


	public int getExecutionNum() {
		return executionNum;
	}


	public void setExecutionNum(int executionNum) {
		this.executionNum = executionNum;
	}


	public int getAchievementRate() {
		return achievementRate;
	}


	public void setAchievementRate(int achievementRate) {
		this.achievementRate = achievementRate;
	}
	
	public int getDaysRemained() {
		return daysRemained;
	}

	public void setDaysRemained(int daysRemained) {
		this.daysRemained = daysRemained;
	}

	public List<ArrayList<Character>> getCalList() {
		return calList;
	}

	public void setCalList(List<ArrayList<Character>> calList) {
		this.calList = calList;
	}
	
	@Override
	public String toString() {
		return "PersonalChallengeVO [challengeCode=" + challengeCode + ", challengeName=" + challengeName
				+ ", challengeNum=" + challengeNum + ", email=" + email + ", thumbnailURL=" + thumbnailURL
				+ ", colorCode=" + colorCode + ", period=" + period + ", startDate=" + startDate + ", endDate="
				+ endDate + ", calendar=" + calendar + ", isCompleted=" + isCompleted + ", isSucceeded=" + isSucceeded
				+ ", executionNum=" + executionNum + ", achievementRate=" + achievementRate + ", calList=" + calList
				+ "]";
	}

	public void calculateAchievementRate() {
		double p = Double.parseDouble(period);
		double result = Math.round((executionNum/p)*100);
		setAchievementRate((int) result);
	}
	
	public void countDatesToEnd() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate toDay = LocalDate.now();
		LocalDate endDate = LocalDate.parse(this.endDate, format);
		if(toDay.isEqual(endDate) || toDay.isAfter(endDate)) {
			setDaysRemained(0);
		} else {
			setDaysRemained(gsCalendar.between2Dates(endDate, toDay));
		}
	}
}
