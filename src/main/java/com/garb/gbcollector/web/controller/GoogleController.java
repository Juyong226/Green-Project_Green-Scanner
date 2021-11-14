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

import com.garb.gbcollector.web.service.GoogleMemberService;
import com.garb.gbcollector.web.vo.MemberVO;
import com.garb.gbcollector.util.Log;

@Controller
public class GoogleController {
	
	private Log log = new Log();
	@Autowired
	GoogleMemberService googleMemberService;
	
	@RequestMapping(value="googleMemberInsert.do",
			method= {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String googleMemberInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Double googlememid = (Double) session.getAttribute("googlememid");
		log.TraceLog("googlememid"+googlememid);
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
		
		JSONObject snsJson = new JSONObject();
		
		try {
			MemberVO m =new MemberVO(mememail, mempw, memname,memnickname,googlememid);
			log.TraceLog("googleid"+m.getGooglememid());
			googleMemberService.googlememberInsert(m);
			snsJson.put("success", memnickname+ "님 회원가입을 축하합니다!");
			
			return snsJson.toJSONString();			
		}catch(Exception e) {
			snsJson.put("failed", e.getMessage());
			return snsJson.toJSONString();
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
		
		session.setAttribute("googlememid", googlememid);
		JSONObject snsloginjson = new JSONObject();
		
		try {
			MemberVO m = new MemberVO(googlememid);
			Map returngoogledata = googleMemberService.googleIdChk(m);
		
			if(returngoogledata == null) {
				snsloginjson.put("snsredirect","/html/snsMemberInsert.html");
				snsloginjson.put("snsuseremail", googleuseremail);
				snsloginjson.put("oauth", "google");
				snsloginjson.put("returnsnsid", 0);
				return snsloginjson.toJSONString();
			}else {
				try {
					String mememail = (String) returngoogledata.get("EMAIL");
					String memnickname = (String) returngoogledata.get("NICKNAME");
					String googleusername = (String) returngoogledata.get("NAME");
					Integer forgooglemememail = 200000;
					String logout = "<span id=\"logoutBtn\">로그아웃</span>";
					
					MemberVO g = new MemberVO(mememail, forgooglemememail);
					
					session.setAttribute("member", g);
					session.setAttribute("email", mememail);
					session.setAttribute("memnickname", memnickname);
					
					snsloginjson.put("memnickname",memnickname);
					snsloginjson.put("mememail", mememail);
					snsloginjson.put("memname", googleusername);
					snsloginjson.put("oauth", "google");
					snsloginjson.put("snsredirect","/" );
					snsloginjson.put("logout", logout);

					
					return snsloginjson.toJSONString();
				}catch(Exception e) {
					
				}
			}
		}catch(Exception e) {
			snsloginjson.put("failed", e.getMessage());
			return snsloginjson.toJSONString();
		}
		return snsloginjson.toJSONString();
	}
	
}
