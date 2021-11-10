package com.garb.gbcollector.web.controller;

import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.constant.Method;
import com.garb.gbcollector.util.UiUtils;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.web.service.FeedCommentService;
import com.garb.gbcollector.web.service.FeedService;
import com.garb.gbcollector.web.vo.FeedCommentVO;
import com.garb.gbcollector.web.vo.FeedVO;

@Controller
@RequestMapping("feed")
public class FeedCommentController extends UiUtils {

	@Autowired
	private FeedCommentService feedCommentService;
	
	@Autowired
	private FeedService feedService;
	private Log log = new Log();
	/*피드글 + 댓글 리스트 요청*/
	@GetMapping(value = "/{feedNo}/comments")
	public String getCommentList(@PathVariable("feedNo") final Integer feedNo, @RequestParam(value="challengeNum") final String challengeNum,
			FeedCommentVO params, Model model, HttpServletRequest request) {
		
		String redirectURI = "";		
		FeedVO feed = feedService.getFeedDetail(feedNo);
		if(feed != null) {
			params.setFeedNo(feedNo);
			List<FeedCommentVO> commentList = feedCommentService.getCommentList(params);
			if(commentList.isEmpty() == false) {
				feed.setCommentCnt(commentList.size());
				model.addAttribute("commentList", commentList);
			}
			/*댓글 없음*/
			model.addAttribute("feed", feed);
			model.addAttribute("challengeNum", challengeNum);
			
			HttpSession session = request.getSession(false);
			if(session != null) {
				model.addAttribute("nickname", session.getAttribute("memnickname"));
			}	
			return "challenge/feed/comment";
		}
		/*없는 글이거나 이미 삭제됨*/
		redirectURI = "/challenge/feed/" + challengeNum;
		return showMessageWithRedirection("이미 삭제되었거나 존재하지 않는 글입니다.", redirectURI, Method.GET, null, model);				
	}
	
	/*댓글 수정 폼 요청*/
	@GetMapping(value = {"/{feedNo}/comments/{idx}"})
	public String getCommentUpdateForm (@PathVariable("feedNo") final String feedNo, @PathVariable(value = "idx", required = false) final Integer idx,
			@RequestParam(value="challengeNum") final String challengeNum, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/feed/" + feedNo + "comments?challengeNum=" + challengeNum;
		if(session != null) {
			try {
				FeedCommentVO params = feedCommentService.getCommentDetail(idx);
				if(params != null) {
					model.addAttribute("params", params);
					model.addAttribute("challengeNum", challengeNum);
					model.addAttribute("nickname", session.getAttribute("memnickname"));
					return "challenge/feed/partial-content :: cmt-update-form";
				}
				/*없는 댓글이거나 이미 삭제됨*/
				return showMessageWithRedirection("이미 삭제되었거나 존재하지 않는 댓글입니다.", redirectURI, Method.GET, null, model);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return showMessageWithRedirection("데이터베이스 처리 과정에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			} catch (Exception e) {
				e.printStackTrace();
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			}
		}
		/*로그인 필요*/
		redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
	}
	
	/*댓글 작성 및 수정 요청*/
	@RequestMapping(value = { "/{feedNo}/comments", "/{feedNo}/comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH })
	public String registerComment(@PathVariable("feedNo") final String feedNo, @PathVariable(value = "idx", required = false) final Integer idx,
			@RequestParam(value="challengeNum") final String challengeNum, FeedCommentVO params, Model model, HttpServletRequest request) {
		
		log.TraceLog("=======================================================================");
		log.TraceLog("댓글 작성/수정 시 넘어오는 submit params 객체 = " + params.toString());
		log.TraceLog("=======================================================================");
		HttpSession session = request.getSession(false);
		String redirectURI = "/feed/" + feedNo + "comments?challengeNum=" + challengeNum;
		if(session != null) {
			try {
				if(idx != null) {
					params.setIdx(idx);
				}
				FeedCommentVO comment = feedCommentService.registerComment(params);
				model.addAttribute("comment", comment);
				model.addAttribute("challengeNum", challengeNum);
				model.addAttribute("nickname", session.getAttribute("memnickname"));
				return "challenge/feed/partial-content :: cmt-regi";
			} catch (DataAccessException e) {
				e.printStackTrace();
				return showMessageWithRedirection("데이터베이스 처리 과정에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			} catch (SQLException e) {
				e.printStackTrace();
				return showMessageWithRedirection("데이터베이스 처리 과정에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			} catch (Exception e) {
				e.printStackTrace();
				return showMessageWithRedirection("시스템에 문제가 발생하였습니다.", redirectURI, Method.GET, null, model);
			}
		}
		redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
	}
	
	@RequestMapping(value = "/{feedNo}/comments/{idx}", method = {RequestMethod.DELETE}, produces = "application/text; charset=utf8")
	@ResponseBody
	public String deleteComment(@PathVariable("idx") final Integer idx, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		JSONObject resJson = new JSONObject();
		if(session != null) {
			try {
				boolean isDeleted = feedCommentService.deleteComment(idx);
				resJson.put("success", "댓글이 삭제되었습니다.");
			} catch (DataAccessException e) {
				e.printStackTrace();
				resJson.put("failed", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
			} catch (Exception e) {
				e.printStackTrace();
				resJson.put("failed", "시스템에 문제가 발생하였습니다.");
			}
		} else {
			resJson.put("loginRequired", "로그인 후 이용이 가능합니다.");
		}		
		return resJson.toJSONString();
	}
}
