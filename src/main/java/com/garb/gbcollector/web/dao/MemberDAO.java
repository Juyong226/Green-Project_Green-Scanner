package com.garb.gbcollector.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.MemberVO;

@Mapper
@Repository("memberDAO")
public interface MemberDAO {
	public String naverlogin(MemberVO memberVO);
	public int naverIdChk(MemberVO memberVO);
	public void navermemberInsert(MemberVO memberVO);
	public void memberInsert(MemberVO memberVO);
	public String login(MemberVO memberVO); 
	public int emailChk(MemberVO memberVO);
	public int checkNickname(MemberVO memberVO);
} 
