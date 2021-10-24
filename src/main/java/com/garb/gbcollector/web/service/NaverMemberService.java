package com.garb.gbcollector.web.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.MemberDAO;
import com.garb.gbcollector.web.vo.MemberVO;

@Service
public class NaverMemberService {
	
	@Autowired
	MemberDAO memberDAO;
	
	public Map naverlogin(MemberVO n) {
		return memberDAO.naverlogin(n);
	}

	public Map naverIdChk(MemberVO m) throws Exception{
		return memberDAO.naverIdChk(m);
	}
	
	public void navermemberInsert(MemberVO m) throws Exception{
		memberDAO.navermemberInsert(m);
	}
	
}
