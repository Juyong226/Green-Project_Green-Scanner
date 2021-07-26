package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value = "",
					method = {RequestMethod.GET})
	@ResponseBody
	public ModelAndView selectAllChallenges(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 1. BasicChallenge를 조회한다.
		 * 2. 요청이 들어오면 로그인 여부(세션 존재 여부)를 확인한다. 세션을 확인하는 메서드는 따로 작성한다.
		 * 3. 로그인 하지 않았을 시 -> BasicChallenge만 조회하여 리턴한다.
		 * 4. 로그인 했을 시 -> 세션에 진행/완료 챌린지 리스트가 존재하는 지 확인한다.
		 * 	4-1. 진행/완료 챌린지 리스트 존재하지 않을 시 -> BasicChallenge와 ChallengeList 둘 다 조회한다.
		 * 		4-1-1. 조회 후, ChallengeList 리스트 항목을 진행/완료로 구분하여 별도에 리스트에 저장하고, 이를 각각 세션에 저장한다.
		 * 			진행/완료를 구분하는 메서드는 따로 작성한다.
		 * 		4-1-2. BasicChallenge, 진행 중인 챌린지, 완료된 챌린지를 리턴한다.
		 * 	4-2. 진행/완료 챌린지 리스트가 존재할 때 -> BasicChallenge를 조회하고, 세션에서 진행/완료 챌린지 리스트를 각각 꺼낸다.
		 * 		4-2-1. 진행 중인 챌린지 리스트 중에서 완료된 챌린지를 다시 구분하여 각 리스트를 수정한다. 3-1-1에서 언급한 메서드를 사용한다.
		 * 		4-2-2. 3-1-2와 동일
		 * */
		
		bcList = challengeService.selectBasicChallenge();
		HttpSession session = request.getSession(false);
		
		mav.setViewName("challenge/mainChallenge");
		mav.addObject("bcList", bcList);
		
		// 로그인 하지 않았을 때(세션에 로그인 정보가 담겨있지 않을 때)
		if(session == null) {
			mav.addObject("loginRequired", "로그인 후 이용할 수 있습니다.");
			
		// 로그인 했을 때(세션에 로그인 정보가 담겨있을 때)
		} else {
			try {
				MemberVO m = (MemberVO) session.getAttribute("member");
				
				String email = m.getMememail();
				List<ArrayList> cList = (List<ArrayList>) session.getAttribute("challengeList");
				
				// 당일 챌린지 서비스에 첫 방문했을 시(세션에 챌린지 정보가 담겨있지 않을 때)
				if(cList == null) {
					List<PersonalChallengeVO> tempList = challengeService.selectChallengeList(email);
					
					cList = challengeService.isCompleted(tempList);
					session.setAttribute("challengeList", cList);
					
				// 당일 챌린지 서비스에 이미 방분했을 시(세션에 챌린지 정보가 담겨있을 때)
				} else {				
					cList = challengeService.isCompleted(cList.get(0), cList.get(1));
				
					//cList.set(0, tempList.get(0));
					//cList.set(1, tempList.get(1));
					session.setAttribute("challengeList", cList);

				}
				mav.addObject("proceeding", cList.get(0));
				mav.addObject("completed", cList.get(1));
				mav.addObject("proceedingNum", Integer.toString(cList.get(0).size()));
				mav.addObject("completedNum", Integer.toString(cList.get(1).size()));
				
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}
		}		
		return mav;
	}
	
	@RequestMapping(value = "/createChallenge",
					method = {RequestMethod.POST},
					produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView createChallenge(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		mav.setViewName("challenge/createChallenge");
		
		if(session == null) {
			mav.addObject("loginRequired", "로그인 후 이용할 수 있습니다");
		} else {
			try {
				MemberVO m = (MemberVO) session.getAttribute("member");
				
				String email = m.getMememail();
				String challengeCode = request.getParameter("challengeCode");
				String colorCode = request.getParameter("colorCode");
				String period = request.getParameter("period");
				String thumbnailCode = request.getParameter("thumbnailCode");				
				String startDate = challengeService.getCurrentTime();
				String endDate = challengeService.getEndDate(startDate, period);
				int executionNum = 0;
				long achievementRate = 0L;
				String isCompleted = "0";
				String isSucceeded = "0";
				
				PersonalChallengeVO pc = new PersonalChallengeVO(challengeCode, email, 
						thumbnailCode, colorCode, period, startDate, endDate, executionNum, 
						achievementRate, isCompleted, isSucceeded);
				
				challengeService.createChallenge(pc);
				mav.addObject("created", "챌린지 생성 성공");
				
			} catch (GbcException e) {
				// 클라이언트쪽으로 exception 메세지 보내기
				mav.addObject("exception", e.getMessage());
				e.printStackTrace();
			}			
		}
		return mav;
	}
	
}

