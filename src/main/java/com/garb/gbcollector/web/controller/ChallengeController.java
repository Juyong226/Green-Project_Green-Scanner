package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.constant.Method;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.util.UiUtils;
import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.service.FeedService;
import com.garb.gbcollector.web.vo.BasicChallengeVO;
import com.garb.gbcollector.web.vo.FeedPaginationVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.MemberVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;
import com.garb.gbcollector.web.vo.RequestInforVO;

@Controller
@RequestMapping("challenge")
public class ChallengeController extends UiUtils {

	@Autowired
	ChallengeService challengeService;
	@Autowired
	FeedService feedService;
	
	List<BasicChallengeVO> bcList; 
	private Log log = new Log();
	
	@GetMapping(value = "/main")
	public String main(Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.ACCESS_CHALLMAIN, infor.getId()));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "";
		bcList = challengeService.selectBasicChallenge();
		model.addAttribute("bcList", bcList);
		if(session != null && (MemberVO)session.getAttribute("member") != null) {
				try {
					MemberVO member = (MemberVO)session.getAttribute("member");
					List<PersonalChallengeVO> tempList = challengeService.selectChallengeList(member.getMememail());	
					List<ArrayList<PersonalChallengeVO>> cList = challengeService.isCompleted(tempList, infor);				
					model.addAttribute("proceeding", cList.get(0));
					model.addAttribute("completed", cList.get(1));
					model.addAttribute("proceedingNum", Integer.toString(cList.get(0).size()));
					model.addAttribute("completedNum", Integer.toString(cList.get(1).size()));
					return "challenge/main";
				} catch (Exception e) {
					e.printStackTrace();
					redirectURI = "/";
					return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
				}						
		}
		model.addAttribute("login_required", "비로그인");
		return "challenge/main";
	}
	
	/*
	@GetMapping(value = "/my-challenge")
	public String myChallenge(Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/main";
		if(session != null) {
			try {				
				MemberVO member = (MemberVO)session.getAttribute("member");
				List<PersonalChallengeVO> tempList = challengeService.selectChallengeList(member.getMememail());	
				List<ArrayList> cList = challengeService.isCompleted(tempList, infor);				
				model.addAttribute("proceeding", cList.get(0));
				model.addAttribute("completed", cList.get(1));
				return "challenge/my-challenge";
			} catch (Exception e) {
				e.printStackTrace();
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			}						
		} 
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
	}	
	*/
	
	@GetMapping(value = "/my-challenge/{challengeNum}")
	public String pcDetail2(@PathVariable("challengeNum") String challengeNum, FeedPaginationVO params, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.ACCESS_MYCHALL, infor.getId(), challengeNum));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/main";
		if(session != null) {
			try {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
				BasicChallengeVO bc = challengeService.getBasicChallenge(pc.getChallengeCode());
				int myFeedCnt = feedService.getMyFeedCnt(challengeNum);
				if(myFeedCnt < Integer.parseInt(params.getEndIdx())) {
					params.setEndIdx(Integer.toString(myFeedCnt));
				}
				List<FeedVO> feedList = feedService.getMyFeedList(params);
				model.addAttribute("idx", params.getStartIdx());
				model.addAttribute("nickname", session.getAttribute("memnickname"));
				model.addAttribute("personalChallenge", pc);
				model.addAttribute("basicChallenge", bc);
				model.addAttribute("feedList", feedList);
				model.addAttribute("myFeedCnt", myFeedCnt);
				return "challenge/my-challenge-detail";
			} catch (NullPointerException e) {
				e.printStackTrace();			
				return showMessageWithRedirection("이미 삭제되었거나 존재하지 않는 챌린지입니다.", redirectURI, null, null, model);
			} catch (Exception e) {
				e.printStackTrace();			
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, null, null, model);
			}
		} 		
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);		
	}
	
	@GetMapping(value = "/basic/{challengeCode}")
	public String bcDetail(@PathVariable("challengeCode") String code, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, infor.getId() + " 님이 기본 챌린지( CHALLENGECODE: " + code + " ) 상세 조회 페이지 접속");
		
		BasicChallengeVO bc = challengeService.getBasicChallenge(code);
		model.addAttribute("basicChallenge", bc);		
		return "challenge/basic-detail";
	}
	
	@RequestMapping(value = "/duplicate_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String codeDuplicateCheck(HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		String code = request.getParameter("challengeCode");
		log.TraceLog(infor, infor.getId() + " 님이 새 챌린지( CHALLENGECODE: " + code + " ) 중복 체크 요청");
		
		JSONObject resJson = new JSONObject();	
		HttpSession session = request.getSession(false);		
		if(session != null && (MemberVO)session.getAttribute("member") != null) {
			try {
				MemberVO member = (MemberVO)session.getAttribute("member");
				String result = challengeService.duplicateCheck(code, member.getMememail());		
				if(result != null) {
					resJson.put("msg", "이미 같은 챌린지가 진행 중입니다.\n다른 챌린지에 도전해보세요!");	
					return resJson.toJSONString();
				} 
				resJson.put("msg", "OK");
				return resJson.toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
				resJson.put("msg", "시스템에 문제가 발생하였습니다.");
				return resJson.toJSONString();
			}
		}
		resJson.put("msg", "로그인 후 이용할 수 있습니다.");
		return resJson.toJSONString();
	}
	
	@GetMapping(value = "/basic/{challengeCode}/set")
	public String setForm(@PathVariable("challengeCode") String code, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, infor.getId() + " 님이 새 챌린지( CHALLENGECODE: " + code + " ) 설정 폼 요청");
		
		HttpSession session = request.getSession(false);			
		if(session != null) {
			BasicChallengeVO bc = challengeService.getBasicChallenge(code);	
			model.addAttribute("basicChallenge", bc);
			return "challenge/new-setting";			
		} 
		String redirectURI = "/challenge/basic/" + code;
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
	}
	
	@PostMapping(value = "/basic/{challengeCode}")
	public String create(@PathVariable("challengeCode") String code, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_NEWCHALL, infor.getId(), code));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/basic/" + code;		
		if(session != null) {
			try {
				String cNum = challengeService.duplicateCheck(code, (String) session.getAttribute("email"));
				if(cNum != null) {
					redirectURI = "/challenge/main";
					return showMessageWithRedirection("이미 같은 챌린지가 진행 중입니다.", redirectURI, Method.GET, null, model);
				}
				BasicChallengeVO bc = challengeService.getBasicChallenge(code);
				MemberVO m = (MemberVO)session.getAttribute("member");	
				
				String email = m.getMememail();
				String challengeCode = code;
				String challengeName = bc.getChallengeName();
				String challengeNum = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
				String colorCode = request.getParameter("colorCode");
				String period = request.getParameter("period");
				String thumbnailURL = 	bc.getListImageURL();			
				String startDate = challengeService.getCurrentTime();
				String endDate = challengeService.getEndDate(startDate, period);
				String calendar = challengeService.createCalendar(period);
				
				PersonalChallengeVO pc = new PersonalChallengeVO(challengeCode, challengeName, challengeNum, email, 
						thumbnailURL, colorCode, period, startDate, endDate, calendar);
			
				int result = challengeService.createChallenge(pc);				
				if(result != 1) {
					log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_NEWCHALL, infor.getId(), code));
					return showMessageWithRedirection("챌린지 생성에 실패하였습니다.", redirectURI, Method.GET, null, model);
				}
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_NEWCHALL, infor.getId(), code, challengeNum));
				return "redirect:/challenge/main";
				
			} catch (Exception e) {
				e.printStackTrace();
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			}				
		}
		return showMessageWithRedirection("로그인 후 이용할 수 있습니다.", redirectURI, Method.GET, null, model);
	}
	
	@GetMapping(value = "/my-challenge/{challengeNum}/edit")
	public String updatePage(@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, infor.getId() + " 님이 챌린지( CHALLENGENUM: " + challengeNum + " ) 수정 폼 요청");
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
			BasicChallengeVO bc = challengeService.getBasicChallenge(pc.getChallengeCode());
			model.addAttribute("personalChallenge", pc);
			model.addAttribute("basicChallenge", bc);
			return "challenge/edit-my-challenge";						
		}
		String redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용할 수 있습니다.", redirectURI, Method.GET, null, model);		
	}
	
	@RequestMapping(value = "/period_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String periodCheck(HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		String challengeNum = request.getParameter("cNum");
		String period = request.getParameter("newPeriod");
		log.TraceLog(infor, infor.getId() + " 님의 챌린지( CHALLENGENUM: " + challengeNum + " ) 기간 체크 요청");
		
		JSONObject resJson = new JSONObject();
		HttpSession session = request.getSession(false);	
		if(session != null) {
			try {
				if(!challengeService.periodCheck(challengeNum, period)) {
					log.TraceLog(infor, "기간 체크 결과: " + period + "일로 기간 조정 불가");
					resJson.put("msg", "챌린지가 선택한 기간 이상으로 진행되었습니다.\n더 긴 기간을 선택해주세요.");
					return resJson.toJSONString();
				}
				log.TraceLog(infor, "기간 체크 결과: " + period + "일로 기간 조정 가능");
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				resJson.put("msg", "시스템에 문제가 발생하였습니다.");
				return resJson.toJSONString();
			}
		}
		resJson.put("msg", "로그인 후 이용할 수 있습니다.");
		return resJson.toJSONString();
	}

	@PutMapping(value = "/my-challenge/{challengeNum}")
	public String update(@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_UPDATE_CHALL, infor.getId(), challengeNum));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/my-challenge/" + challengeNum;
		if(session != null) {
			try {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
				String newPeriod = request.getParameter("period");
				pc.setColorCode(request.getParameter("colorCode"));
				
				int result = challengeService.updateChallenge(pc, newPeriod);				
				if(result != 1) {
					log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_UPDATE_CHALL, infor.getId(), challengeNum));
					return showMessageWithRedirection("챌린지 수정에 실패하였습니다.", redirectURI, Method.GET, null, model);				
				}
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_UPDATE_CHALL, infor.getId(), challengeNum));
				return showMessageWithRedirection("챌린지를 수정했습니다.", redirectURI, Method.GET, null, model);	
				
			} catch (Exception e) {
				e.printStackTrace();
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			}					
		} 		
		redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용할 수 있습니다.", redirectURI, Method.GET, null, model);
	}
	
	@DeleteMapping(value = "/my-challenge/{challengeNum}")
	public String delete(@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_DELETE_CHALL, infor.getId(), challengeNum));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "";
		if(session != null) {
			int result = challengeService.deleteChallenge(challengeNum);			
			if(result != 1) {
				log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_DELETE_CHALL, infor.getId(), challengeNum));
				redirectURI = "/challenge/my-challenge/" + challengeNum;
				return showMessageWithRedirection("챌린지 삭제에 실패하였습니다.", redirectURI, Method.GET, null, model);
			}
			log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_DELETE_CHALL, infor.getId(), challengeNum));
			redirectURI = "/challenge/main";
			return showMessageWithRedirection("챌린지가 삭제되었습니다.", redirectURI, Method.GET, null, model);			
		}
		redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용할 수 있습니다.", redirectURI, Method.GET, null, model);		
	}	
}

