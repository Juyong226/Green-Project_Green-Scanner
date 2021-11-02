package com.garb.gbcollector.web.vo;

public class BasicChallengeVO {

	private String challengeName, challengeCode, thumbnailURL, mainImageURL, listImageURL, toDo, isAvailable;
	
	

	public BasicChallengeVO(String challengeName, String challengeCode, String thumbnailURL, String mainImageURL, String listImageURL, String toDo, String isAvailable) {
		super();
		setChallengeName(challengeName);
		setChallengeCode(challengeCode);
		setThumbnailURL(thumbnailURL);
		setMainImageURL(mainImageURL);
		setListImageURL(listImageURL);
		setToDo(toDo);
		setIsAvailable(isAvailable);
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



	public String getMainImageURL() {
		return mainImageURL;
	}



	public void setMainImageURL(String mainImageURL) {
		this.mainImageURL = mainImageURL;
	}



	public String getListImageURL() {
		return listImageURL;
	}



	public void setListImageURL(String listImageURL) {
		this.listImageURL = listImageURL;
	}



	public String getToDo() {
		return toDo;
	}



	public void setToDo(String toDo) {
		this.toDo = toDo;
	}



	public String getIsAvailable() {
		return isAvailable;
	}



	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}



	@Override
	public String toString() {
		return "BasicChallengeVO [challengeName=" + challengeName + ", challengeCode=" + challengeCode
				+ ", thumbnailURL=" + thumbnailURL + ", mainImageURL=" + mainImageURL + ", listImageURL=" + listImageURL
				+ ", toDo=" + toDo + ", isAvailable=" + isAvailable + "]";
	}
}

