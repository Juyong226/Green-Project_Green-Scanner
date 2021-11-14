package com.garb.gbcollector.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.constant.Method;
import com.garb.gbcollector.util.UiUtils;
import com.garb.gbcollector.util.UploadFileException;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.service.FeedService;
import com.garb.gbcollector.web.vo.FeedPaginationVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;
import com.garb.gbcollector.web.vo.RequestInforVO;
import com.garb.gbcollector.web.vo.UploadImageVO;

@Controller
@RequestMapping("challenge/feed")
public class FeedController extends UiUtils {

	@Autowired
	private FeedService feedService;
	@Autowired
	private ChallengeService challengeService;
	private Log log = new Log();
	
	/*피드 글 쓰기 페이지 요청*/
	@GetMapping(value = "/{challengeNum}/set")
	public String openWritePage(@RequestParam(value = "feedNo", required = false) Integer feedNo, 
			@PathVariable("challengeNum") String challengeNum, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, infor.getId() + " 님이 챌린지( CHALLENGENUM: " + challengeNum + " ) 피드 등록/수정 폼 요청");
		
		HttpSession session = request.getSession(false);
		String redirectURI = "";
		if(session == null) {
			redirectURI = "/challenge/main";
			return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
			
		} else {
			PersonalChallengeVO pc = challengeService.getPersonalChallenge(challengeNum);
			FeedVO feed;
			if(feedNo == null) {
				feed = new FeedVO();
				feed.setWriter((String)session.getAttribute("memnickname"));				
			} else {
				feed = feedService.getFeedDetail(feedNo);
				if(feed == null) {
					redirectURI = "/challenge/my-challenge/" + challengeNum;
					return showMessageWithRedirection("올바르지 않은 접근입니다.", redirectURI, Method.GET, null, model);
				}
				List<UploadImageVO> imageList = feedService.getFeedImageList(feedNo);
				model.addAttribute("imageList", imageList);
			}
			model.addAttribute("feed", feed);
			model.addAttribute("challengeName", pc.getChallengeName());
			model.addAttribute("challengeNum", challengeNum);
		}
		return "/challenge/feed/write";
	}
	
	/*피드 글 등록 요청*/
	@PostMapping(value = "/{challengeNum}")
	public String registerFeed(final FeedVO params, final MultipartFile[] images, Model model,
			@PathVariable("challengeNum") String challengeNum, @RequestParam(value = "challengeName") String challengeName, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_WRITE_FEED, infor.getId(), challengeNum));
		
		String redirectURI = "/challenge/my-challenge/" + challengeNum;
		try {
			HttpSession session = request.getSession(false);
			
			if(session == null) {
				redirectURI = "/challenge/main";
				return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
			} else {
				params.setEmail((String) session.getAttribute("email"));
				params.setChallengeName(challengeName);
				params.setPostDate(challengeService.getCurrentTime());
				boolean isRegistered = feedService.registerFeed(params, challengeNum, images, infor);
				if(isRegistered == false) {
					log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_WIRTE_FEED, infor.getId(), challengeNum));
					return showMessageWithRedirection
							("피드를 등록 또는 수정할 수 없습니다.\n1. 이 챌린지에 대한 피드를 이미 작성했는지 확인해주세요.\n\t(챌린지당 1일 1회 등록 가능)\n2. 이미 삭제된 피드인지 확인해주세요.\n\t(삭제된 피드는 수정이 불가능)", 
									redirectURI, Method.GET, null, model);
				}
				log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_WRITE_FEED, infor.getId(), challengeNum, Integer.toString(params.getFeedNo())));
				return "redirect:/challenge/my-challenge/" + challengeNum;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			return showMessageWithRedirection("데이터베이스 처리 과정에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);			
		} catch (UploadFileException e) {
			e.printStackTrace();
			return showMessageWithRedirection("이미지 업로드에 실패하였습니다.\n1. 파일의 확장자가 다음과 같은 지 확인해주세요. ( jpg, jpeg, png )\n2. 파일의 크기를 확인해주세요. (5MB 이하 파일만 업로드 가능)", redirectURI, Method.GET, null, model);
		} catch (Exception e) {
			e.printStackTrace();
			return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);			
		} 
	}
	
	/*동일 챌린지 피드 중복 체크 요청*/
	@RequestMapping(value = "/duplicate_check", method = {RequestMethod.POST}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String duplicateCheck(HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		String challengeNum = request.getParameter("challengeNum");
		log.TraceLog(infor, infor.getId() + " 님이 챌린지( CHALLENGENUM: " + challengeNum + " ) 피드 중복 체크 요청");
		
		HttpSession session = request.getSession(false);
		JSONObject resJson = new JSONObject();
		if(session != null) {
			try {
				boolean result = feedService.duplicateCheck(challengeNum);
				if(result == false) {
					resJson.put("msg", "피드 작성은 챌린지 당 하루 1회만 가능합니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resJson.put("msg", "시스템에 문제가 발생하였습니다.");
			}			
		} else {
			resJson.put("msg", "로그인 후 이용할 수 있습니다.");
		}
		return resJson.toJSONString();
	}
	
	/*피드 이미지 요청*/
	@GetMapping(value = "/{feedNo}/img/{idx}", produces = {"image/jpeg", "image/png"})
	public ResponseEntity<byte[]> loadFeedImages(@PathVariable("idx") final Integer idx, HttpServletRequest request) {
		
		File image = feedService.getFeedImageDetail(idx);
		try {
			InputStream imageStream = new FileInputStream(image);
			byte[] imageByteArray = IOUtils.toByteArray(imageStream);
			imageStream.close();
			return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
		} catch (IOException e) {
			throw new RuntimeException("이미지 로드에 실패하였습니다.");
		}
	}
	
	/*전체 피드 리스트 요청*/
	@GetMapping(value = "/")
	public String openFeedList(FeedPaginationVO params, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.ACCESS_FEEDLIST, infor.getId()));
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			model.addAttribute("nickname", session.getAttribute("memnickname"));
		}
		int totalFeedCnt = feedService.getFeedTotalCount();
		if(totalFeedCnt < Integer.parseInt(params.getEndIdx())) {
			params.setEndIdx(Integer.toString(totalFeedCnt));
		}
		List<FeedVO> feedList = feedService.getAllFeedList(params);
		log.TraceLog("getAllFeedList()에서 리턴하는 feedList: " + feedList);
		model.addAttribute("idx", params.getStartIdx());
		model.addAttribute("feedList", feedList);
		model.addAttribute("totalFeedCnt", totalFeedCnt);
		return "challenge/feed/list";
	}
	
	/*피드 더 보기 요청*/
	@PostMapping(value = "/more_feed")
	public String more_feed(FeedPaginationVO params, Model model, HttpServletRequest request) {
		
		RequestInforVO infor = new RequestInforVO(request);
		String requestPage = request.getParameter("requestPage");
		log.TraceLog(infor, infor.getId() + " 님이 " + requestPage + "에서 '더 보기' 요청");
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			model.addAttribute("nickname", session.getAttribute("memnickname"));
		}
		params.setStartIdx((String)request.getParameter("startIdx"));
		params.setEndIdx((String)request.getParameter("endIdx"));
		params.setChallengeNum((String)request.getParameter("challengeNum"));
		List<FeedVO> feedList;
		if(requestPage.equals("all")) {
			feedList = feedService.getAllFeedList(params);
		} else {
			feedList = feedService.getMyFeedList(params);
		}
		model.addAttribute("idx", (String)request.getParameter("startIdx"));
		model.addAttribute("feedList", feedList);
		return "challenge/feed/partial-content :: more-feed";		
	}
	
	/*피드 삭제 요청*/
	@DeleteMapping(value = "/{challengeNum}/{feedNo}")
	public String deleteFeed(@PathVariable("challengeNum") String challengeNum,
			@PathVariable("feedNo") String feedNo, HttpServletRequest request, Model model) {
		
		RequestInforVO infor = new RequestInforVO(request);
		log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_DELETE_FEED, infor.getId(), feedNo));
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/my-challenge/" + challengeNum;
		if(session == null) {
			redirectURI = "/challenge/main";
			return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
		} else {
			if(feedNo == null) {
				return showMessageWithRedirection("올바르지 않은 접근입니다.", redirectURI, Method.GET, null, model);
			} else {
				try {
					boolean isDeleted = feedService.deleteFeed(Integer.parseInt(feedNo), challengeNum);
					if(isDeleted == false) {
						log.TraceLog(infor, BuildDescription.get(LogDescription.FAIL_DELETE_FEED, infor.getId(), feedNo));
						return showMessageWithRedirection("피드 삭제에 실패하였습니다.", redirectURI, Method.GET, null, model);
					}
					log.TraceLog(infor, BuildDescription.get(LogDescription.SUCCESS_DELETE_FEED, infor.getId(), feedNo));
				} catch (DataAccessException e) {
					e.printStackTrace();
					return showMessageWithRedirection("데이터베이스 처리 과정에서 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
				} catch (Exception e) {
					e.printStackTrace();
					return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
				}
			}
		}
		return showMessageWithRedirection("피드가 삭제되었습니다.", redirectURI, Method.GET, null, model);
	}
}
