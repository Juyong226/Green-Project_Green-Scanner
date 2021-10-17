package com.garb.gbcollector.web.controller;

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

import com.garb.gbcollector.web.service.GoogleMemberService;
import com.garb.gbcollector.web.vo.MemberVO;

@Controller
public class GoogleController {
	
	@Autowired
	GoogleMemberService googleMemberService;
	
	@RequestMapping(value="googleMemberInsert.do",
			method= {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String googleMemberInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Double googlememid = (Double) session.getAttribute("googlememid");
		System.out.println("googlememid"+googlememid);
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
		
		System.out.println("음!!!");
		JSONObject naverJson = new JSONObject();
		
		try {
			MemberVO m =new MemberVO(mememail, mempw, memname,memnickname,googlememid);
			System.out.println("구글아이디"+m.getGooglememid());
			googleMemberService.googlememberInsert(m);
			naverJson.put("success", memnickname+ "님 회원가입을 축하합니다!");
			
			return naverJson.toJSONString();			
		}catch(Exception e) {
			naverJson.put("failed", e.getMessage());
			return naverJson.toJSONString();
		}
		
		
	
	}
	

	@RequestMapping(value = "googleSignUp.do",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf-8")
	@ResponseBody
	public String googleSignUp(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();	

		double googlememid = Double.parseDouble(request.getParameter("googlememid"));
		String googleuseremail = request.getParameter("googleuseremail");
		
		System.out.println(googlememid);
		session.setAttribute("googlememid", googlememid);
		JSONObject googleloginjson = new JSONObject();
		
		try {
			MemberVO m = new MemberVO(googlememid);
			int returngoogleid = googleMemberService.googleIdChk(m);
		
			if(returngoogleid == 0) {
				googleloginjson.put("googleredirect","https://localhost/html/googleMemberInsert.html");
				googleloginjson.put("googleuseremail", googleuseremail);
				googleloginjson.put("returngoogleid", returngoogleid);
				
				return googleloginjson.toJSONString();
			}else if(returngoogleid == 1) {
				System.out.println("123");
				try {
					MemberVO g = new MemberVO(googlememid);
					session.setAttribute("member", g);
					String memnickname = googleMemberService.googlelogin(g);
					System.out.println(memnickname);
					session.setAttribute("memnickname", memnickname);
					googleloginjson.put("googleredirect","https://localhost" );
					googleloginjson.put("memnickname",memnickname);
					return googleloginjson.toJSONString();
				}catch(Exception e) {
					
				}
			}
		}catch(Exception e) {
			googleloginjson.put("failed", e.getMessage());
			return googleloginjson.toJSONString();
		}
		return googleloginjson.toJSONString();
	}
	
}
