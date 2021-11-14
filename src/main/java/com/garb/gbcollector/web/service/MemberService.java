package com.garb.gbcollector.web.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.garb.gbcollector.web.dao.MemberDAO;
import com.garb.gbcollector.web.vo.DeleteMemberVO;
import com.garb.gbcollector.web.vo.MemberVO;

@Service
public class MemberService {

	@Autowired
	MemberDAO memberDAO;
	
	public void resetPassword(DeleteMemberVO d) throws Exception{
		memberDAO.resetPassword(d);
	}
	
	public Map checkEmailForPw(MemberVO m) throws Exception{
		return memberDAO.checkEmailForPw(m);
	}
	
	public void signOutNaverMember(DeleteMemberVO m) throws Exception{
		memberDAO.signOutNaverMember(m);
	}
	
	public void signOutGoogleMember(DeleteMemberVO m) throws Exception{
		memberDAO.signOutGoogleMember(m);
	}
	
	public void signOutMember(DeleteMemberVO m) throws Exception{
		memberDAO.signOutMember(m);
	}
	
	public void memberInsert(MemberVO m) throws Exception{
		memberDAO.memberInsert(m);
	}

	public Map login(MemberVO m) {
		return memberDAO.login(m);
	}
	
	public int emailChk(MemberVO m) {
		return memberDAO.emailChk(m);
	}

	public int checkNickname(MemberVO m) {
		return memberDAO.checkNickname(m);
	}
}
