package com.garb.gbcollector.web.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.MemberDAO;
import com.garb.gbcollector.web.vo.MemberVO;

@Service
public class GoogleMemberService {

	 @Autowired
	 MemberDAO memberDAO;
	 
    public String googlelogin(MemberVO g) {
    	return memberDAO.googlelogin(g);
    }
	 
	public void googlememberInsert(MemberVO m) throws Exception{
			memberDAO.googlememberInsert(m);
		}
	 
	 public Map googleIdChk(MemberVO m) throws Exception{
		 return memberDAO.googleIdChk(m);
	 }
}
