package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.garb.gbcollector.util.GbcException;
import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.vo.BasicChallengeVO;
import com.garb.gbcollector.web.vo.MemberVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

@Controller
@RequestMapping("challenge")
public class ChallengeController {

	@Autowired
	ChallengeService challengeService;
	
	List<BasicChallengeVO> bcList; 
	
	@RequestMapping(value = "/main", method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView main(ModelAndView mav, HttpServletRequest request) {
		
		bcList = challengeService.selectBasicChallenge();
		HttpSession session = request.getSession(false);
		
		mav.addObject("bcList", bcList);
		
		// 로그인 하지 않았을 때(세션에 로그인 정보가 담겨있지 않을 때)
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다.");
			
		// 로그인 했을 때(세션에 로그인 정보가 담겨있을 때)
		} else {
			try {
				MemberVO member = (MemberVO)session.getAttribute("member");
				List<PersonalChallengeVO> tempList = challengeService.selectChallengeList(member.getMememail());
					
				List<ArrayList> cList = challengeService.isCompleted(tempList);
				
				mav.addObject("proceeding", cList.get(0));
				mav.addObject("completed", cList.get(1));
				mav.addObject("proceedingNum", Integer.toString(cList.get(0).size()));
				mav.addObject("completedNum", Integer.toString(cList.get(1).size()));
				mav.setViewName("challenge/main");			
				
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}
		}		
		return mav;
	}
	
	@RequestMapping(value = "/my-challenge", method = {RequestMethod.GET}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView myChallenge(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.setViewName(null); // 로그인 요청 페이지
			
		} else {
			try {				
				MemberVO member = (MemberVO)session.getAttribute("member");
				List<PersonalChallengeVO> tempList = challengeService.selectChallengeList(member.getMememail());
					
				List<ArrayList> cList = challengeService.isCompleted(tempList);
				
				mav.addObject("proceeding", cList.get(0));
				mav.addObject("completed", cList.get(1));
				mav.setViewName("challenge/my-challenge");
				
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}			
		}
		return mav;
	}	
	
	@RequestMapping(value = "/my-challenge/{challengeNum}", method = {RequestMethod.GET}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView pcDetail2(@PathVariable("challengeNum") String challengeNum, ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/main");
			
		} else {
			PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
			mav.addObject("personalChallenge", pc);
			mav.setViewName("challenge/my-challenge-detail");
			
		}
		return mav;
	}
	
	@RequestMapping(value = "/basic/{challengeCode}", method = {RequestMethod.GET}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView bcDetail(@PathVariable("challengeCode") String code, ModelAndView mav, HttpServletRequest request) {
		System.out.println("요청들어옴: GET /{challengeCode}");
		BasicChallengeVO bc = challengeService.getBasicChallenge(code);
		
		mav.setViewName("challenge/basic-detail");
		mav.addObject("basicChallenge", bc);
		
		return mav;
	}
	
	@RequestMapping(value = "/duplicate_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String codeDuplicateCheck(HttpServletRequest request) {
		System.out.println("요청들어옴: POST /duplicate_check with code = " + request.getParameter("challengeCode"));
		JSONObject resJson = new JSONObject();
		String code = request.getParameter("challengeCode");
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			resJson.put("msg", "로그인 후 이용할 수 있습니다.");
			
		} else {
			MemberVO member = (MemberVO) session.getAttribute("member");
			String result = challengeService.duplicateCheck(code, member.getMememail());
			
			if(result == null) {
				resJson.put("msg", "OK");
				
			} else {
				resJson.put("msg", "이미 같은 챌린지가 진행 중입니다.\n다른 챌린지에 도전해보세요!");
				
			}
		}
		return resJson.toJSONString();
	}
	
	@RequestMapping(value = "/basic/{challengeCode}/set", method = {RequestMethod.GET}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView setForm(@PathVariable("challengeCode") String code, ModelAndView mav, HttpServletRequest request) {
		System.out.println("요청들어옴: GET /set/{challengeCode}");

		HttpSession session = request.getSession(false);
		BasicChallengeVO bc = challengeService.getBasicChallenge(code);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/basic/" + code);
			
		} else {		
			mav.addObject("basicChallenge", bc);
			mav.setViewName("challenge/new-setting");
			
		}
		return mav;
	}
	
	@RequestMapping(value = "/basic/{challengeCode}", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView create(@PathVariable("challengeCode") String code, ModelAndView mav, HttpServletRequest request) {
		System.out.println("요청들어옴: POST /set/{challengeCode}");

		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/basic/" + code);
			
		} else {
			try {
				BasicChallengeVO bc = challengeService.getBasicChallenge(code);
				MemberVO m = (MemberVO) session.getAttribute("member");
								
				String email = m.getMememail();
				String challengeCode = code;
				String challengeName = bc.getChallengeName();
				String challengeNum = UUID.randomUUID().toString().replace("-", "").substring(0, 11);
				String colorCode = request.getParameter("colorCode");
				String period = request.getParameter("period");
				String thumbnailURL = 	bc.getThumbnailURL();			
				String startDate = challengeService.getCurrentTime();
				String endDate = challengeService.getEndDate(startDate, period);
				String calendar = challengeService.createCalendar(period);
				
				PersonalChallengeVO pc = new PersonalChallengeVO(challengeCode, challengeName, challengeNum, email, 
						thumbnailURL, colorCode, period, startDate, endDate, calendar);
			
				int result = challengeService.createChallenge(pc);
				
				if(result == 1) {
					mav.addObject("insert_succeeded", "챌린지를 성공적으로 생성하였습니다.");
					mav.setViewName("redirect:/challenge/main");
					
				} else {
					mav.addObject("insert_failed", "챌린지 생성에 실패하였습니다.");
					mav.setViewName("redirect:/challenge/basic/" + code);
					
				}
							
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}
		}
		return mav;
	}
	
	@RequestMapping(value = "/my-challenge/{challengeNum}/edit", method = {RequestMethod.GET}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView updatePage(@PathVariable("challengeNum") String challengeNum, ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/main");
			
		} else {
			PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
			mav.addObject("personalChallenge", pc);
			mav.setViewName("challenge/edit-my-challenge");
		}
		return mav;
	}
	
	@RequestMapping(value = "/period_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String periodCheck(HttpServletRequest request) {
		System.out.println("요청들어옴: POST /period_check with newPeriod/challengeNum = " + request.getParameter("newPeriod") + "/" + request.getParameter("cNum"));
		JSONObject resJson = new JSONObject();
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			resJson.put("msg", "로그인 후 이용할 수 있습니다.");
			
		} else {
			if(!challengeService.periodCheck(request.getParameter("cNum"), request.getParameter("newPeriod"))) {
				resJson.put("msg", "챌린지가 선택한 기간 이상으로 진행되었습니다.\n더 긴 기간을 선택해주세요.");
			}
			
		}
		return resJson.toJSONString();
	}

	@RequestMapping(value = "/my-challenge/{challengeNum}", method = {RequestMethod.PUT}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView update(@PathVariable("challengeNum") String challengeNum, ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/main");
			
		} else {
			try {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
				
				String newPeriod = request.getParameter("period");
				pc.setColorCode(request.getParameter("colorCode"));
				
				int result = challengeService.updateChallenge(pc, newPeriod);
				
				if(result == 1) {
					mav.addObject("update_succeeded", "챌린지를 수정했습니다.");
					mav.setViewName("redirect:/challenge/my-challenge");
					
				} else {
					mav.addObject("update_failed", "챌린지 수정에 실패했습니다.");
					mav.setViewName("redirect:/challenge/my-challenge/" + challengeNum);	
					
				}
				
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/my-challenge/{challengeNum}", method = {RequestMethod.DELETE}, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView delete(@PathVariable("challengeNum") String challengeNum, ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			mav.addObject("login_required", "로그인 후 이용할 수 있습니다");
			mav.setViewName("redirect:/challenge/main");
			
		} else {
			int result = challengeService.deleteChallenge(challengeNum);
			
			if(result == 1) {
				mav.addObject("delete_succeeded", "챌린지가 삭제되었습니다.");
				mav.setViewName("redirect:/challenge/my-challenge");
			} else {
				mav.addObject("delete_failed", "챌린지 삭제에 실패하였습니다.");
				mav.setViewName("redirect:/challenge/my-challenge/" + challengeNum);
			}
		}
		
		return mav;
	}
	
}

