package com.garb.gbcollector.web.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.DeleteMemberVO;
import com.garb.gbcollector.web.vo.MemberVO;

@Repository
public interface MemberDAO {
	public void changePassword(MemberVO memberVO);
	public int checkPassword(MemberVO memberVO);
	public void resetPassword(DeleteMemberVO deletememberVO);
	public Map checkEmailForPw(MemberVO memberVO);
	public void signOutNaverMember(DeleteMemberVO deletememberVO);
	public void signOutGoogleMember(DeleteMemberVO deletememberVO);
	public void signOutMember(DeleteMemberVO deletememberVO);
	public String googlelogin(MemberVO memberVO);
	public void googlememberInsert(MemberVO memberVO);
	public Map googleIdChk(MemberVO memberVO);
	public Map naverlogin(MemberVO memberVO);
	public Map naverIdChk(MemberVO memberVO);
	public void navermemberInsert(MemberVO memberVO);
	public void memberInsert(MemberVO memberVO);
	public Map login(MemberVO memberVO); 
	public int emailChk(MemberVO memberVO);
	public int checkNickname(MemberVO memberVO);
} 
