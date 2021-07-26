package com.garb.gbcollector.web.vo;

public class BasicChallengeVO {

	private String challengeName, challengeCode, toDo, isAvailable;
	
	

	public BasicChallengeVO(String challengeName, String challengeCode, String toDo, String isAvailable) {
		super();
		setChallengeName(challengeName);
		setChallengeCode(challengeCode);
		setToDo(toDo);
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

	public String isAvailable() {
		return isAvailable;
	}

	public void setAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	
	
	
}

