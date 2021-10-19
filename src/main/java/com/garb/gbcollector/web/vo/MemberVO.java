package com.garb.gbcollector.web.vo;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.garb.gbcollector.util.GbcException;

public class MemberVO {
	private String mememail, mempw, memname, memnickname;
	private Integer navermemid;
	private Double googlememid;
	private Date memdate;
	private Integer fornavermememail;
	
	public MemberVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MemberVO(Double googlememid) throws GbcException {
		super();
		setGooglememid(googlememid);
	}
	

	public MemberVO(Integer navermemid) throws GbcException {
		super();
		setNavermemid(navermemid);
	}

	public MemberVO(String memnickname) throws GbcException {
		super();
		setMemnickname(memnickname);
	}
	
	public MemberVO(Integer navermemid, String mememail) throws GbcException{
		super();
		setNavermemid(navermemid);
		setGooglememid(getGooglememid());
	}
	
	public MemberVO(String mememail, Integer fornavermememail) throws GbcException {
		super();
		setMememail(mememail);
	}
	
	public MemberVO(String mememail, String mempw) throws GbcException {
		super();
		setMememail(mememail);
		setMempw(mempw);
	}
	
	public MemberVO(String mememail, String mempw, String memname, String memnickname, Double googlememid) throws GbcException {
		super();
		setMememail(mememail);
		setMempw(mempw);
		setMemname(memname);
		setMemnickname(memnickname);
		setGooglememid(googlememid);
	}
	
	public MemberVO(String mememail, String mempw, String memname, String memnickname, Integer navermemid) throws GbcException {
		super();
		setMememail(mememail);
		setMempw(mempw);
		setMemname(memname);
		setMemnickname(memnickname);
		setNavermemid(navermemid);
	}
	
	public MemberVO(String mememail, String mempw, String memname, String memnickname) throws GbcException {
		super();
		setMememail(mememail);
		setMempw(mempw);
		setMemname(memname);
		setMemnickname(memnickname);
	}
	
	
	public MemberVO(String mememail, String mempw, String memname, String memnickname, Date memdate) throws GbcException {
		super();
		setMememail(mememail);
		setMempw(mempw);
		setMemname(memname);
		setMemnickname(memnickname);
		setMemdate(memdate);
	}
	
	public Double getGooglememid() {
		return googlememid;
	}
	
	public void setGooglememid(Double googlememid) {
		this.googlememid = googlememid;
	}
	
	
	public Integer getNavermemid() {
		return navermemid;
	}
	
	public void setNavermemid(Integer navermemid) {
		this.navermemid = navermemid;
	}

	public String getMememail() {
		return mememail;
	}

	public void setMememail(String mememail) throws GbcException{
		if(mememail==null) {
			throw new GbcException("이메일을 입력해주세요.");
		}else {
			// 이메일 형식 검증
			String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(mememail);
			if(m.matches()){
				this.mememail = mememail;
			}else {
				throw new GbcException("올바른 이메일 형식이 아닙니다.");
			}
		}
	}

	public String getMempw() {
		return mempw;
	}

	public void setMempw(String mempw) throws GbcException{
		if(mempw==null) {
			throw new GbcException("비밀번호를 입력해주세요.");	
		}else if(mempw.length()<6){
			throw new GbcException("비밀번호는 6자리 이상이어야 합니다.\n비밀번호를 다시 확인해주세요.");
		}else {
			this.mempw = mempw;
		}
	}

	public String getMemname(){
			return memname;
	}

	public void setMemname(String memname) throws GbcException{
		if(memname==null) {
			throw new GbcException("이름을 입력해주세요.");
		}else if(memname.length()<2){
			throw new GbcException("이름은 두글자 이상이어야 합니다.");
		}else if(memname.contains(" ")){
			throw new GbcException("이름에는 공백이 포함될 수 없습니다.");
		}else {
			this.memname = memname;
		}
	}

	public String getMemnickname(){
		return memnickname;
	}

	public void setMemnickname(String memnickname) throws GbcException {
		if(memnickname==null) {
			throw new GbcException("닉네임을 입력해주세요.");
		}else if(memnickname.length()<2){
			throw new GbcException("닉네임은 두글자 이상이어야 합니다.");
		}else if(memnickname.contains(" ")){
			throw new GbcException("닉네임에는 공백이 포함될 수 없습니다.");
		}else {
			this.memnickname = memnickname;
		}
	}

	public Date getMemdate() {
		return memdate;
	}

	public void setMemdate(Date memdate) {
		this.memdate = memdate;
	}

	@Override
	public String toString() {
		return "MemberVO [mememail=" + mememail + ", mempw=" + mempw + ", memname=" + memname + ", memnickname=" + memnickname
				+ ", memdate=" + memdate + "]";
	}
}	
