package com.garb.gbcollector.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.garb.gbcollector.web.dao.MemberDAO;
import com.garb.gbcollector.web.vo.MemberVO;

@Service
public class MemberService {

	@Autowired
	MemberDAO memberDAO;
	
	public void memberInsert(MemberVO m) throws Exception{

		memberDAO.memberInsert(m);
	}

	public String login(MemberVO m) {
		
		return memberDAO.login(m);
	}
	
	public int emailChk(MemberVO m) {
		return memberDAO.emailChk(m);
	}

	public int checkNickname(MemberVO m) {
		return memberDAO.checkNickname(m);
	}
}
