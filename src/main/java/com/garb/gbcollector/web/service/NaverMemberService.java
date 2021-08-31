package com.garb.gbcollector.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.MemberDAO;
import com.garb.gbcollector.web.vo.MemberVO;

@Service
public class NaverMemberService {
	
	@Autowired
	MemberDAO memberDAO;
	
	public String naverlogin(MemberVO n) {
		return memberDAO.naverlogin(n);
	}

	public int naverIdChk(MemberVO m) throws Exception{
		return memberDAO.naverIdChk(m);
	}
	
	public void navermemberInsert(MemberVO m) throws Exception{
		memberDAO.navermemberInsert(m);
	}
	
}
