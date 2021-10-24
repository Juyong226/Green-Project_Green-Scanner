package com.garb.gbcollector.web.controller;

import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.web.service.NaverMemberService;
import com.garb.gbcollector.web.vo.MemberVO;

@Controller
public class NaverController {

	@Autowired
	NaverMemberService naverMemberService;
	
	@RequestMapping(value="naverMemberInsert.do",
			method= {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String naverMemberInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Integer navermemid = (Integer) session.getAttribute("navermemid");
		String mememail = request.getParameter("email");
		String memname = request.getParameter("name");
		String memnickname = request.getParameter("nickname");
		
		Random random=new Random();
		String mempw="";
		for(int i =0; i<8;i++) {
			int index=random.nextInt(25)+65; //A~Z까지 랜덤 알파벳 생성
			mempw+=(char)index;
		}
		int numIndex=random.nextInt(9999)+1000; //4자리 랜덤 정수를 생성1
		mempw+=numIndex;
		
		System.out.println("음");
		JSONObject naverJson = new JSONObject();
		
		try {
			MemberVO m =new MemberVO(mememail, mempw, memname,memnickname,navermemid);
			naverMemberService.navermemberInsert(m);
			naverJson.put("success", memnickname+ "님 회원가입을 축하합니다!");
			
			return naverJson.toJSONString();			
		}catch(Exception e) {
			naverJson.put("failed", e.getMessage());
			return naverJson.toJSONString();
		}
			
	}
	
	@RequestMapping(value = "naverSignUp.do",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf-8")
	@ResponseBody
	public String naverSignUp(HttpServletRequest request, HttpServletResponse response) {		
		HttpSession session = request.getSession();
		
		Integer navermemid = Integer.parseInt(request.getParameter("navermemid"));
		String naveruseremail = request.getParameter("naveruseremail");
		String naverusername = request.getParameter("naverusername");
		session.setAttribute("navermemid",navermemid);
		JSONObject naverloginjson = new JSONObject();
		
		try {
			MemberVO m = new MemberVO(navermemid);
			Map returnnaverdata = naverMemberService.naverIdChk(m);
			if(returnnaverdata == null) {
				
				naverloginjson.put("naverredirect","https://localhost/html/naverMemberInsert.html" );
				naverloginjson.put("naveruseremail",naveruseremail);
				naverloginjson.put("naverusername",naverusername);
				naverloginjson.put("returnnaverid", 0);
				
				
				return naverloginjson.toJSONString();
			}else {
				try {
					String mememail = (String) returnnaverdata.get("EMAIL");
					String memnickname = (String) returnnaverdata.get("NICKNAME");
					Integer fornavermememail = 100000;
					
					MemberVO n = new MemberVO(mememail, fornavermememail);
					
					session.setAttribute("member",n);				
					session.setAttribute("email", mememail);
					session.setAttribute("memnickname", memnickname);
					
					naverloginjson.put("memnickname",memnickname);				
					naverloginjson.put("naverredirect","https://localhost");
					
					return naverloginjson.toJSONString();
				}catch(Exception e) {
					
				}
				
			}
			
		}catch(Exception e) {
			naverloginjson.put("failed", e.getMessage());
			return naverloginjson.toJSONString();
		}
		return naverloginjson.toJSONString();
	}
}