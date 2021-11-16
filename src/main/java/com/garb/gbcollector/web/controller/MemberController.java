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

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.web.service.MailService;
import com.garb.gbcollector.web.service.MemberService;
import com.garb.gbcollector.web.vo.DeleteMemberVO;
import com.garb.gbcollector.web.vo.MemberVO;
import com.garb.gbcollector.web.vo.RequestInforVO;

@Controller
public class MemberController {
	
	private Log log = new Log();
	@Autowired
	MemberService memberService;
	
	@Autowired
	MailService mailService;
	
	@RequestMapping(value = "changePassword.do",
			method = {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String changePassword(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_PASSWORD_CHANGE, infor.getId()));
		
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		JSONObject checkjson = new JSONObject();
		
		try {
			MemberVO m = new MemberVO(mememail, mempw);
			memberService.changePassword(m);
			log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_PASSWORD_CHANGE, infor.getId()));
			checkjson.put("success", "비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
		} catch (Exception e) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_PASSWORD_CHANGE, infor.getId()));
			checkjson.put("failed", "비밀번호가 변경이 실패하였습니다. 잠시 후 다시 시도해주세요.");
			e.printStackTrace();
		}
		
		return checkjson.toJSONString();
	}
	
	@RequestMapping(value = "checkPassword.do",
			method = {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String checkPassword(HttpServletRequest request, HttpServletResponse response) {
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		
		JSONObject checkjson = new JSONObject();
		try {
			MemberVO m = new MemberVO(mememail, mempw);
			Integer checkPassword = memberService.checkPassword(m);
			if(checkPassword ==1 ) {
				checkjson.put("exist","변경하실 비밀번호를 입력해주세요.");
			}else {
				checkjson.put("notExist", "비밀번호를 잘못 입력하셨습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkjson.toJSONString();
	}
	
	
	
	@RequestMapping(value = "toFindPassword.do",
			method = {RequestMethod.POST},
			produces = "application/text; charset=utf-8")
	@ResponseBody
	public String toFindPassword(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_PASSWORD_RESET, infor.getId()));
		
		String mememail=request.getParameter("email");		
		JSONObject resJson = new JSONObject();
		try {
			MemberVO m = new MemberVO(mememail);
			Map checkEmailForPw = memberService.checkEmailForPw(m);

			if (checkEmailForPw != null) {
				String naverSignUp = (String) checkEmailForPw.get("NAVERMEMID");
				Double googleSignUp = (Double) checkEmailForPw.get("GOOGLEID");
				String normalEmail = (String) checkEmailForPw.get("EMAIL");
				if (naverSignUp != null) {
					resJson.put("snsSignUp", "네이버 아이디로 회원가입한 회원입니다. 네이버 아이디로 로그인해 주세요.");
				}
				if (googleSignUp != null) {
					resJson.put("snsSignUp", "구글 아이디로 회원가입한 회원입니다. 구글 아이디로 로그인해 주세요.");
				}
				if (normalEmail != null & naverSignUp == null & googleSignUp == null) {
					Random random=new Random();
					String mempw="";
					for(int i =0; i<8;i++) {
						int index=random.nextInt(25)+65; //A~Z까지 랜덤 알파벳 생성
						mempw+=(char)index;
					}
					int numIndex=random.nextInt(9999)+1000; //4자리 랜덤 정수를 생성1
					mempw+=numIndex;
					mailService.sendMail(mememail,mempw);
					DeleteMemberVO d = new DeleteMemberVO(mememail, mempw);
					memberService.resetPassword(d);
					log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_PASSWORD_RESET, infor.getId()));
					resJson.put("success", normalEmail+"로 발송된 비밀번호로 다시 한번 로그인 해주세요.");
					resJson.put("redirect", "/html/login.html");			
				}				
			}else {
				resJson.put("nonSignUp", "회원가입한 회원이 아닙니다.");
			}
		} catch (Exception e) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_PASSWORD_RESET, infor.getId()));
			e.printStackTrace();
		}	
		return resJson.toJSONString();
	}
	
	@RequestMapping(value = "signout.do",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf-8")
	@ResponseBody
	public String sigout(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_SIGNOUT, infor.getId()));
		
		HttpSession session = request.getSession(false);
		String navermemid = (String) session.getAttribute("navermemid");
		Double googlememid = (Double) session.getAttribute("googlememid");
		String mememail = (String)session.getAttribute("email");
		JSONObject signoutjson = new JSONObject();
		Random random = new Random();
		String memidtosignout = "  ";
		for(int i =0; i<8;i++) {
			int index=random.nextInt(25)+65; //A~Z까지 랜덤 알파벳 생성
			memidtosignout+=(char)index;
		}
		memidtosignout +="/signout";
		String deletedmememail = mememail + memidtosignout;
		if(navermemid != null) {
			try {
				String deletednavermemid = "signout";
				DeleteMemberVO m = new DeleteMemberVO(mememail, deletedmememail, navermemid, deletednavermemid);
				memberService.signOutNaverMember(m);
				signoutjson.put("redirect", "/");
				signoutjson.put("chk", "회원 탈퇴 되었습니다. ㅠㅠ 다음에 또 놀러오세요!");
				session.invalidate();
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_SIGNOUT, infor.getId()));
			} catch (Exception e) {
				log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_SIGNOUT, infor.getId()));
				e.printStackTrace();
			}
		}else if(googlememid != null) {
			try {
				Double deletedgooglememid = (double) 100;
				DeleteMemberVO m = new DeleteMemberVO(mememail, deletedmememail, googlememid, deletedgooglememid);
				memberService.signOutGoogleMember(m);
				signoutjson.put("redirect", "/");
				signoutjson.put("chk", "회원 탈퇴 되었습니다. ㅠㅠ 다음에 또 놀러오세요!");
				session.invalidate();
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_SIGNOUT, infor.getId()));
			} catch (Exception e) {
				log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_SIGNOUT, infor.getId()));
				e.printStackTrace();
			}
		}else {
			String mempw = (String)session.getAttribute("password");
			try {
				DeleteMemberVO m = new DeleteMemberVO(mememail, deletedmememail, mempw);
				memberService.signOutMember(m);
				signoutjson.put("redirect", "/");
				signoutjson.put("chk", "회원 탈퇴 되었습니다. ㅠㅠ 다음에 또 놀러오세요!");
				session.invalidate();
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_SIGNOUT, infor.getId()));
			}catch(Exception e){
				log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_SIGNOUT, infor.getId()));
				e.printStackTrace();				
			}
		}
		return signoutjson.toJSONString();
	}
	
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
			e.printStackTrace();
		}			
		return resJson.toJSONString();		
	}
	
	//세션이 Null인지 체크하는 메서드
	@RequestMapping(value = "checkSession.do",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkSession(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		JSONObject resJson = new JSONObject();
		HttpSession session = request.getSession(false);
		//세션이 만료되었을 경우 다시 로그인 해달라는 메세지를 리턴
		if(session == null) {
			log.TraceLog(infor, "사용자 세션 만료로 인한 로그아웃 처리");
			resJson.put("sessionNull", "로그아웃 되었습니다.\n다시 로그인 해주세요.");			
		} else if (session != null && session.getAttribute("member") == null) {
			session.invalidate();
			log.TraceLog(infor, "서버 문제로 인한 로그아웃 처리");
			resJson.put("sessionNull", "로그아웃 되었습니다.\n다시 로그인 해주세요.");
		}
		return resJson.toJSONString();
	}
	
	
	@RequestMapping(value = "logout.do", 
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_LOGOUT, infor.getId()));
		
		JSONObject resJson = new JSONObject();
		HttpSession session = request.getSession(false);
		//로그아웃시에도 세션이 만료된 상태에서 NullPointerException이 발생하는 것을 방지하기 위해서
		//로그아웃 요청 시 먼저 세션의 만료 여부를 검사하고
		//만료 시에는 session.invalidate()를 실행하지 않고 로그아웃되었다는 메세지만 리턴
		if(session != null) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_LOGOUT, infor.getId()));
			session.invalidate();
			resJson.put("success", "로그아웃 되었습니다.");
			return resJson.toJSONString();
		} else {
			log.TraceLog(infor, "사용자 세션 만료로 인한 로그아웃 처리");
			resJson.put("sessionNull", "로그아웃 되었습니다.");
			return resJson.toJSONString();
		}		
	}

	
	@RequestMapping(value = "login.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_LOGIN));
		
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		
		JSONObject loginjson = new JSONObject();

		try {
			MemberVO m = new MemberVO(mememail,mempw); 
			Map memdata = memberService.login(m);
			String memnickname = (String) memdata.get("NICKNAME");
			String memname = (String) memdata.get("NAME");
			if(memdata != null) {
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_LOGIN, mememail));
				HttpSession session=request.getSession();		
				String logout = "<span id=\"logoutBtn\">로그아웃</span>";
				session.setAttribute("member", m);
				session.setAttribute("email", mememail);
				session.setAttribute("password", mempw);
				session.setAttribute("memnickname", memnickname);
				
				loginjson.put("memname", memname);
				loginjson.put("mememail", mememail);
				loginjson.put("memnickname", memnickname);
				loginjson.put("logout", logout);
			} else {
				log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_LOGIN));
				loginjson.put("failed","회원 정보 없음");
			}
		} catch(Exception e) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_LOGIN));
			loginjson.put("failed","회원 정보 없음");
			loginjson.put("denied", e.getMessage());
			e.printStackTrace();
		}
		return loginjson.toJSONString();
	}

		
	@RequestMapping(value = "memberInsert.do", 
					method= {RequestMethod.POST},
					produces = "application/text; charset=utf8")			
	@ResponseBody
	public String memberInsert(HttpServletRequest request, HttpServletResponse response) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_JOIN));
		
		String mememail = request.getParameter("email");
		String mempw = request.getParameter("pw");
		String memname = request.getParameter("name");
		String memnickname = request.getParameter("nickname");
		
		JSONObject resJson = new JSONObject();
		try {
			MemberVO m = new MemberVO(mememail, mempw, memname, memnickname);
			memberService.memberInsert(m);
			log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_JOIN, mememail));
			resJson.put("success", memname + "님 회원가입을 축하합니다!" + "다시 한번 로그인 해주세요");
			resJson.put("redirect", "/html/login.html");
		} catch(Exception e) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_JOIN));
			resJson.put("failed", e.getMessage());
			e.printStackTrace();
		}	
		return resJson.toJSONString();
	}

}
