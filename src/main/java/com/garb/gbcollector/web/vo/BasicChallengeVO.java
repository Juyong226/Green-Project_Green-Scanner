package com.garb.gbcollector.web.vo;

public class BasicChallengeVO {

	private String challengeName, challengeCode, toDo;
	private int period;
	private boolean isAvailable;
	
	

	public BasicChallengeVO(String challengeName, String challengeCode, String toDo, int period, boolean isAvailable) {
		super();
		setChallengeName(challengeName);
		setChallengeCode(challengeCode);
		setToDo(toDo);
		setPeriod(period);
		setAvailable(isAvailable);
	}

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getChallengeCode() {
		return challengeCode;
	}

	public void setChallengeCode(String challengeCode) {
		this.challengeCode = challengeCode;
	}

	public String getToDo() {
		return toDo;
	}

	public void setToDo(String toDo) {
		this.toDo = toDo;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	
	
	
}
