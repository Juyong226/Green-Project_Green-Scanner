package com.garb.gbcollector.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.service.FeedService;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

@Controller
@RequestMapping("challenge/feed")
public class FeedController {

	@Autowired
	private FeedService feedService;
	@Autowired
	private ChallengeService challengeService;
	
	/*피드 글 쓰기 페이지 요청*/
	@GetMapping(value = "/{challengeNum}/set")
	public String openWritePage(@RequestParam(value = "feedNo", required = false) Integer feedNo, 
			@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			return "redirect:/challenge/main";
			
		} else {
			PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
			if(feedNo == null) {
				model.addAttribute("feed", new FeedVO());
			} else {
				FeedVO feed = feedService.getFeedDetail(feedNo);
				if(feed == null) {
					return "redirect:/challenge/my-challenge/" + challengeNum;
				} else {
					model.addAttribute("feed", feed);
				}
			}
			model.addAttribute("nickname", session.getAttribute("memnickname"));
			model.addAttribute("challengeName", pc.getChallengeName());
			model.addAttribute("challengeNum", challengeNum);
		}
		return "/challenge/feed/write";
	}
	
	/*피드 글 등록 요청*/
	@PostMapping(value = "/{challengeNum}")
	public String registerFeed(final FeedVO params, 
			@PathVariable("challengeNum") String challengeNum, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if(session == null) {
				return "redirect:/challenge/main";
			} else {
				params.setEmail((String) session.getAttribute("email"));
				params.setPostDate(challengeService.getCurrentTime());
				boolean isRegistered = feedService.registerFeed(params);
				if(isRegistered == false) {
					// 피드 등록에 실패했다는 메세지 전달
				}	
			}
		} catch (DataAccessException e) {
			// 데이터베이스 처리 과정에서 문제가 발생했다는 메세지 전달
			e.printStackTrace();
		} catch (Exception e) {
			// 시스템에 문제가 발생했다는 메세지 전달
			e.printStackTrace();
		}
		return "redirect:/challenge/feed/" + challengeNum;
	}
	
	/*동일 챌린지 피드 중복 체크 요청*/
	@RequestMapping(value = "/duplicate_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String duplicateCheck(HttpServletRequest request) {
		System.out.println("요청들어옴: POST /duplicate_check with challengeNum = " + request.getParameter("challengeNum"));
		HttpSession session = request.getSession(false);
		JSONObject resJson = new JSONObject();
		if(session == null) {
			resJson.put("msg", "로그인 후 이용할 수 있습니다.");
		} else {
			boolean result = feedService.duplicateCheck(request.getParameter("challengeNum"));
			if(result == false) {
				resJson.put("msg", "피드 작성은 챌린지 당 하루 1회만 가능합니다.");
			}
		}
		return resJson.toJSONString();
	}
	
	/*피드 리스트 요청*/
	@GetMapping(value = "/{challengeNum}")
	public String openMyFeedList(@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return "redirect:/challenge/main";
		} else {
			List<FeedVO> feedList = feedService.getMyFeedList(challengeNum);
			model.addAttribute("nickname", session.getAttribute("memnickname"));
			model.addAttribute("feedList", feedList);
		}
		return "challenge/feed/list";
	}
	
	/*피드 삭제 요청*/
	@DeleteMapping(value = "/{challengeNum}/{feedNo}")
	public String deleteFeed(@PathVariable("challengeNum") String challengeNum,
			@PathVariable("feedNo") String feedNo, HttpServletRequest request) {
		System.out.println("요청들어옴: POST /deleteFeed with challengeNum/feedNo = " + challengeNum + "/" + feedNo);
		HttpSession session = request.getSession(false);
		if(session == null) {
			return "redirect:/challenge/main";
		} else {
			if(feedNo == null) {
				return "redirect:/challenge/feed/" + challengeNum;
			} else {
				try {
					boolean isDeleted = feedService.deleteFeed(Integer.parseInt(feedNo), challengeNum);
					if(isDeleted == false) {
						// 게시글 삭제에 실패했다는 메세지 전달
					}
					
				} catch (DataAccessException e) {
					// 데이터베이스 처리 과정에서 문제가 발생했다는 메세지 전달
					e.printStackTrace();
				} catch (Exception e) {
					// 시스템에 문제가 발생했다는 메세지 전달
					e.printStackTrace();
				}
			}
		}
		return "redirect:/challenge/feed/" + challengeNum;
	}
}
