package com.garb.gbcollector.web.vo;

import java.util.Date;
import com.garb.gbcollector.util.GbcException;

public class DeleteMemberVO {
	private String mememail,deletedmememail, mempw;
	private Integer navermemid, deletednavermemid;
	private Double googlememid, deletedgooglememid;
	
	public DeleteMemberVO() {
		super();
	}
	
	public DeleteMemberVO(String mememail, String mempw) throws GbcException{
		super();
		setMememail(mememail);
		setMempw(mempw);
	}
	
	public DeleteMemberVO(String mememail, String deletedmememail, Double googlememid, Double deletedgooglememid) throws GbcException{
		super();
		setMememail(mememail);
		setDeletedMememail(deletedmememail);
		setGooglememid(googlememid);
		setDeletedGooglememid(deletedgooglememid);
	}
	
	public DeleteMemberVO(String mememail, String deletedmememail, Integer navermemid, Integer deletednavermemid) throws GbcException{
		super();
		setMememail(mememail);
		setDeletedMememail(deletedmememail);
		setNavermemid(navermemid);
		setDeletednavermemid(deletednavermemid);
	}
	
	public DeleteMemberVO(String mememail,String deletedmememail, String mempw) throws GbcException{
		super();
		setMememail(mememail);
		setDeletedMememail(deletedmememail);
		setMempw(mempw);
	}
	
	public void setMememail(String mememail) {
		this.mememail = mememail;
	}
	
	public void setDeletedMememail(String deletedmememail) {
		this.deletedmememail = deletedmememail;
	}
	
	public void setMempw(String mempw) {
		this.mempw = mempw;
	}
	
	public void setNavermemid(Integer navermemid) {
		this.navermemid = navermemid;
	}
	public void setDeletednavermemid(Integer deletednavermemid) {
		this.deletednavermemid = deletednavermemid;
	}
	public void setGooglememid(Double googlememid) {
		this.googlememid = googlememid;
	}
	
	public void setDeletedGooglememid(Double deletedgooglememid) {
		this.deletedgooglememid = deletedgooglememid;
	}
	
	
}
