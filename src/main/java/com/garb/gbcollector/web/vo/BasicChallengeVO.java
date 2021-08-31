package com.garb.gbcollector.web.vo;

public class BasicChallengeVO {

	private String challengeName, challengeCode, thumbnailURL, toDo, isAvailable;
	
	

	public BasicChallengeVO(String challengeName, String challengeCode, String thumbnailURL, String toDo, String isAvailable) {
		super();
		setChallengeName(challengeName);
		setChallengeCode(challengeCode);
		setThumbnailURL(thumbnailURL);
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
	
	public String getThumbnailURL() {
		return thumbnailURL;
	}


	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
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

