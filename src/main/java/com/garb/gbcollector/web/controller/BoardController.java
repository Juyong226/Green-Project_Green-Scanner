package com.garb.gbcollector.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.garb.gbcollector.web.service.BoardService;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@RequestMapping("board")
@Controller

public class BoardController {
	
	String boardname = null;
	String title = null;
	String content = null;
	String nickname = null;
	Date postdate = null; 
	int postno = 0;
	int parentno = 0;
	
	@Autowired
	BoardService boardService;
	
	/* 전체게시판 조회 */
	@RequestMapping(value="/boardlist", method=RequestMethod.GET)
	public ModelAndView boardList(ModelAndView mav, HttpServletRequest req, HttpServletResponse res, HttpSession sessoion) {
		System.out.println("board/list진입");
		List<BoardVO> b_boards;
		b_boards = boardService.listPostB();
		List<BoardVO> q_boards;
		q_boards = boardService.listPostQ();
		mav.addObject("b_boards", b_boards);
		mav.addObject("q_boards", q_boards);
		mav.setViewName("board/list");
		return mav;
	}
	
	/* 자유게시판 조회 */
	@RequestMapping(value="/bulletin_boardlist", method=RequestMethod.GET)
	public ModelAndView boardListB(ModelAndView mav, HttpServletRequest req, HttpServletResponse res, HttpSession sessoion) {
		System.out.println("board/bulletin_boardlist진입");
		List<BoardVO> b_boards;
		b_boards = boardService.listPostBAll();
		mav.addObject("b_boards", b_boards);
		mav.setViewName("board/bulletin_boardlist");
		return mav;
	}
	
	/* 질문게시판 조회 */
	@RequestMapping(value="/question_boardlist", method=RequestMethod.GET)
	public ModelAndView boardListQ(ModelAndView mav, HttpServletRequest req, HttpServletResponse res, HttpSession sessoion) {
		System.out.println("board/question_boardlist진입");
		List<BoardVO> q_boards;
		q_boards = boardService.listPostQAll();
		mav.addObject("q_boards", q_boards);
		mav.setViewName("board/question_boardlist");
		return mav;
	}
	
	
	

	/*글쓰기*/
	@RequestMapping(value = "registerPost.do", 
			method= {RequestMethod.POST},
			produces = "application/text; charset=utf8")	
	
	@ResponseBody
	public String registerPost(HttpServletRequest request,
			HttpServletResponse response) {

		//request로부터 게시판이름, 제목, 내용 얻기 
		boardname = request.getParameter("boardname");
		title = request.getParameter("title");
		//nickname = request.getParameter("nickname");
		content = request.getParameter("content");
		
		//session을 얻어 session으로부터 닉네임 얻기 
		HttpSession session = request.getSession();
		nickname = (String) session.getAttribute("memnickname");
		//System.out.println(nickname);		
		try {
			BoardVO post = new BoardVO(boardname, title, nickname, content);
			boardService.insertPost(post);
			return "글이 성공적으로 등록되었습니다";
		}catch (Exception e) {
			//글 등록 실패 메시지 띄우기 
			return e.getMessage();
		}
	}
	
	/*글+댓글 보기
	 *선택한 글과 그 글에 달린 댓글을 모두 가져와 한 페이지에 출력*/
	@RequestMapping(value = "viewpost", method= {RequestMethod.GET})
	@ResponseBody
	public ModelAndView viewPostPage(@RequestParam("postno") int postno, HttpServletRequest request) {
		//postno를 파라미터로 받아와서 해당 글 조회 
		System.out.println(postno+"번 글 보기");
		ModelAndView mav = new ModelAndView();
		//선택한 postno와 일치하는 게시글 조회 
		BoardVO boardVO = boardService.viewPost(postno);
		//게시글 객체를 post라는 이름으로 뷰페이지에 추가 
		mav.addObject("post", boardVO);
		//뷰 이름 지정- viewPost.jsp
		mav.setViewName("board/view");
		//System.out.println(boardVO);
		
		//현재 게시글에 대한 댓글을 모두 가져오기. 한 게시글당 댓글이 하나 이상이기 때문에 타입은 List이다.  
//		List<BoardReplyVO> boardReplyVO = boardService.viewReply(postno);
//		//댓글 리스트를 reply라는 이름으로 뷰페이지에 추가 
//		mav.addObject("reply", boardReplyVO);
		//System.out.println(boardReplyVO);
		//세션으로부터 닉네임 얻기
		HttpSession session = request.getSession();
		nickname = (String) session.getAttribute("memnickname");
		//System.out.println(nickname);
		
		//뷰페이지를 반환(viewPost.jsp호출됨)
		return mav;
	}
	
	/*글 수정 페이지 가져오기 */
	@RequestMapping(value = "updatePost")
	public String updatePost(@RequestParam("postno") int postno, ModelMap modelMap) {
		System.out.println(postno+"번 글 수정");
		BoardVO boardVO = boardService.viewPost(postno);
		modelMap.addAttribute("post", boardVO);
		return "updatePost";
	}
	
	/*글 수정 후 저장*/
	@RequestMapping(value = "updatePostSave")
	public String updatePostSave(@ModelAttribute("post") BoardVO boardVO) {
		boardService.updatePost(boardVO);
		return "redirect:/viewPost?postno="+boardVO.getPostno();
	}
	
	/*글 삭제*/
	@RequestMapping(value="deletePost", 
					method= {RequestMethod.POST})
	public String deletePost(@RequestParam("postno") Integer postno, HttpServletRequest request) {
		
		
		HttpSession session = request.getSession();
		nickname = (String) session.getAttribute("memnickname");
		//System.out.println(nickname);
		
		System.out.println(postno+"번 글 삭제");
		boardService.deletePost(postno);
		//글 삭제 후 전체 글 리스트 페이지로 이동
		return "redirect:/html/boardPage.html";
	}
	
	/*댓글 등록+수정*/
	/* BoardService에서 getReno()가 null이면 댓글 insert, 번호가 있으면 update 수행 */
	@RequestMapping(value = "boardReplySave", 
					method= {RequestMethod.POST})
	public String boardReplySave(HttpServletRequest request, BoardReplyVO boardReplyVO) {
		
		HttpSession session = request.getSession();
		nickname = (String) session.getAttribute("memnickname");
		//System.out.println(nickname);
		boardReplyVO.setNickname(nickname);
		
		boardService.insertBoardReply(boardReplyVO);
		System.out.println("요청 들어옴");
		return "redirect:/viewPost?postno="+boardReplyVO.getPostno();
	}
	
	/*댓글 삭제*/
	@RequestMapping(value="deleteReply", 
					method= {RequestMethod.POST})
	public String deleteReply(@RequestParam("reno") Integer reno, BoardReplyVO boardReplyVO) {
		System.out.println(reno+"번 댓글 삭제");
		boardService.deleteReply(reno);
		//댓글 삭제 후 페이지 새로고침 
		return "redirect:/viewPost?postno="+boardReplyVO.getPostno();
	}
	
}