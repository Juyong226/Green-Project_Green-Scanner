package com.garb.gbcollector.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.web.service.MemberService;
import com.garb.gbcollector.web.vo.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	@RequestMapping(value = "emailChk.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")
	@ResponseBody
	public String emailChk(HttpServletRequest request, HttpServletResponse response) {
			String mememail=request.getParameter("email");
			JSONObject checkjson=new JSONObject();
			try {
				MemberVO m = new MemberVO(mememail);
				int result = memberService.emailChk(m);
				checkjson.put("chk", result);
			} catch(Exception e) {
				
			}			
			return checkjson.toJSONString();		
	}
	
	@RequestMapping(value = "checkNickname.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkNickname(HttpServletRequest request, HttpServletResponse response) {
		String nickName = request.getParameter("nickName");
		JSONObject resJson = new JSONObject();
		try {
			MemberVO m = new MemberVO(nickName);
			int result = memberService.checkNickname(m);
			resJson.put("checked", result);
		} catch(Exception e) {
			
		}			
		return resJson.toJSONString();		
	}
	
	//세션이 Null인지 체크하는 메서드
	@RequestMapping(value = "checkSession.do",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkSession(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resJson = new JSONObject();
		HttpSession session = request.getSession(false);
		//세션이 만료되었을 경우 다시 로그인 해달라는 메세지를 리턴
		if(session == null || session.getAttribute("member") == null) {
			resJson.put("sessionNull", "로그아웃 되었습니다.\n다시 로그인 해주세요.");			
		}
		return resJson.toJSONString();
	}
	
	
	@RequestMapping(value = "logout.do", 
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) {
			JSONObject resJson = new JSONObject();
			HttpSession session = request.getSession(false);
			//로그아웃시에도 세션이 만료된 상태에서 NullPointerException이 발생하는 것을 방지하기 위해서
			//로그아웃 요청 시 먼저 세션의 만료 여부를 검사하고
			//만료 시에는 session.invalidate()를 실행하지 않고 로그아웃되었다는 메세지만 리턴
			if(session != null) {
				System.out.println("세션 존재");
				session.invalidate();
				resJson.put("success", "로그아웃 되었습니다.");
				return resJson.toJSONString();
			} else {
				System.out.println("세션 만료");
				resJson.put("sessionNull", "로그아웃 되었습니다.");
				return resJson.toJSONString();
			}		
	}

	
	@RequestMapping(value = "login.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		
		JSONObject loginjson = new JSONObject();

		try {
			MemberVO m = new MemberVO(mememail,mempw); 
			String memnickname = memberService.login(m);
			System.out.println(memnickname);

			if(memnickname!=null) {
				HttpSession session=request.getSession();				
				session.setAttribute("member", m);
				session.setAttribute("email", mememail);
				session.setAttribute("memnickname", memnickname);
				loginjson.put("memnickname", memnickname);
				System.out.println(memnickname);
			} else {
				loginjson.put("failed","회원 정보 없음");
			}
		} catch(Exception e) {
			e.printStackTrace();
			loginjson.put("denied", e.getMessage());
		}
		return loginjson.toJSONString();
	}

		
	@RequestMapping(value = "memberInsert.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String memberInsert(HttpServletRequest request, HttpServletResponse response) {
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		String memname = request.getParameter("name");
		String memnickname = request.getParameter("nickname");
		
		JSONObject resJson = new JSONObject();
		try {
			MemberVO m = new MemberVO(mememail, mempw, memname, memnickname);
			memberService.memberInsert(m);
			resJson.put("success", memname + "님 회원가입을 축하합니다!");
			return resJson.toJSONString();
		} catch(Exception e) {
			resJson.put("failed", e.getMessage());
			return resJson.toJSONString();
		}	
	}

}
