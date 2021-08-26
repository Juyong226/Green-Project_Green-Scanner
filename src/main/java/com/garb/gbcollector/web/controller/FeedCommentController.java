package com.garb.gbcollector.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.garb.gbcollector.constant.Method;
import com.garb.gbcollector.util.UiUtils;
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
	
	/*피드글 + 댓글 리스트 요청*/
	@GetMapping(value = "/comments/{feedNo}")
	public String getCommentList(@PathVariable("feedNo") Integer feedNo, @RequestParam(value="challengeNum") String challengeNum,
			FeedCommentVO params, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		String redirectURI = "";
		if(session != null) {
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
					model.addAttribute("nickname", session.getAttribute("memnickname"));
					return "challenge/feed/comment";
				}
				/*없는 글이거나 이미 삭제됨*/
				redirectURI = "/challenge/feed/" + challengeNum;
				return showMessageWithRedirection("이미 삭제되었거나 존재하지 않는 글입니다.", redirectURI, Method.GET, null, model);				
		}
		/*로그인 필요*/
		redirectURI = "/challenge/main";
		return showMessageWithRedirection("로그인 후 이용이 가능합니다.", redirectURI, Method.GET, null, model);
	}
	
	/*댓글 작성 및 수정 요청*/
	@RequestMapping(value = { "/comments/{feedNo}", "/comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH })
	public String registerComment(@PathVariable("feedNo") String feedNo, @PathVariable(value = "idx", required = false) Integer idx,
			@RequestParam(value="challengeNum") String challengeNum, @ModelAttribute("params") FeedCommentVO params, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		String redirectURI = "/challenge/feed/" + challengeNum;
		if(session != null) {
			try {
				if(idx != null) {
					params.setIdx(idx);
				}
				boolean isRegisterd = feedCommentService.registerComment(params);
				return "redirect:/feed/comments/" + feedNo +"?challengeNum=" + challengeNum;
			} catch (DataAccessException e) {
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
	
}
