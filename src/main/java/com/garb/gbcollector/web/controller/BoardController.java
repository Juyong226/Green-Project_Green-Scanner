package com.garb.gbcollector.web.controller;

import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.garb.gbcollector.web.service.BoardService;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@RequestMapping("board")
@Controller

public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	/* 전체게시판 조회 */
	@GetMapping(value="/boardlist")
	public ModelAndView boardList(ModelAndView mav, HttpSession session) {
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
	@GetMapping(value="/bulletin_boardlist")
	public ModelAndView boardListB(ModelAndView mav, HttpSession session) {
		System.out.println("board/bulletin_boardlist진입");
		List<BoardVO> b_boards;
		b_boards = boardService.listPostBAll();
		mav.addObject("b_boards", b_boards);
		mav.setViewName("board/bulletin_boardlist");
		return mav;
	}
	
	/* 질문게시판 조회 */
	@GetMapping(value="/question_boardlist")
	public ModelAndView boardListQ(ModelAndView mav, HttpSession session) {
		System.out.println("board/question_boardlist진입");
		List<BoardVO> q_boards;
		q_boards = boardService.listPostQAll();
		mav.addObject("q_boards", q_boards);
		mav.setViewName("board/question_boardlist");
		return mav;
	}
	
	/* 글쓰기 화면 요청 */
	@GetMapping(value="/write")
	public ModelAndView viewWritePage(ModelAndView mav, HttpSession session) {
		
		/* 토큰값을 생성해 session과 view에 저장 */
		addTokenToSession(mav, session);
		
		mav.setViewName("board/write");
		return mav;
	}
	
	
	public void addTokenToSession(ModelAndView mav, HttpSession session) {
		session.setAttribute("CSRF_TOKEN", UUID.randomUUID().toString());
		String token = (String) session.getAttribute("CSRF_TOKEN");
		System.out.println("CSRF_TOKEN: " + token);
		mav.addObject("token", token);
	}
	
	
	/*글쓰기*/
	@PostMapping(value = "/write")
	public ModelAndView writePost(ModelAndView mav, @ModelAttribute("BoardVO") BoardVO boardVO, 
			HttpSession session, HttpServletRequest req) 
	{
		System.out.println("writePost 진입");
		String nickname = (String) session.getAttribute("memnickname");
		boardVO.setNickname(nickname);	
		
		if(boardVO.getIsanon()==null) {
			boardVO.setIsanon("0");
		}
				
		/* form으로 전송된 요청으로부터 token값을 얻어오기 */
		String token = req.getParameter("_csrf");
		System.out.println("_csrf: " + token);
		
		
		if (session.getAttribute("CSRF_TOKEN").equals(token)) {
			System.out.println("토큰 일치");
			boardService.insertPost(boardVO);
			System.out.println(boardVO);
			String bulletin_board = "자유게시판";
			String question_board = "질문게시판";
			//자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
			if(bulletin_board.equals(boardVO.getBoardname())) {
				mav.setViewName("redirect:/board/bulletin_boardlist");
			}else if(question_board.equals(boardVO.getBoardname())) {
				mav.setViewName("redirect:/board/question_boardlist");
			}
			return mav;
		}
		System.out.println("토큰값 불일치");
		String msg = "비정상적인 접근입니다.";
		mav.addObject("msg", msg);
		mav.setViewName("board/error");
		return mav;	
	}
	
	/*글+댓글 보기*/
	@GetMapping(value = "viewpost")
	public ModelAndView viewPostPage(@RequestParam("postno") int postno, HttpSession session) 
	{
		System.out.println(postno+"번 글 보기");
		ModelAndView mav = new ModelAndView();
		BoardVO boardVO = boardService.viewPost(postno);
		System.out.println(boardVO);
		mav.addObject("post", boardVO);
		
		addTokenToSession(mav, session);
		
		//현재 게시글에 대한 댓글을 모두 가져오기. 한 게시글당 댓글이 하나 이상이기 때문에 타입은 List이다.  
		List<BoardReplyVO> boardReplyVO = boardService.viewReply(postno);
		//댓글 리스트를 reply라는 이름으로 뷰페이지에 추가 
		mav.addObject("commentList", boardReplyVO);

		//세션으로부터 닉네임 얻기
		String nickname = (String) session.getAttribute("memnickname");
		mav.addObject("login_nickname", nickname);
		mav.setViewName("board/view");
		return mav;
	}
	
	/*글 수정 페이지 가져오기 */
	@GetMapping(value = "updatePost")
	public ModelAndView updatePost(@RequestParam("postno") int postno, ModelAndView mav) {
		System.out.println(postno+"번 글 수정");
		
		addTokenToSession(mav, null);
		
		BoardVO boardVO = boardService.viewPost(postno);
		mav.addObject("post", boardVO);
		mav.setViewName("board/modify");
		return mav;
	}
	
	/*글 수정 후 저장*/
	@PostMapping(value = "updatePostSave")
	public String updatePostSave(@ModelAttribute("post") BoardVO boardVO, HttpSession session, HttpServletRequest req) {
		System.out.println(boardVO.getPostno()+"번 글 수정 진입");
		String nickname = (String) session.getAttribute("memnickname");
		boardVO.setNickname(nickname);	
		if(boardVO.getIsanon()==null) {
			boardVO.setIsanon("0");
		}
		
		String token = req.getParameter("_csrf");
		System.out.println("_csrf: " + token);
		
		if (req.getSession().getAttribute("CSRF_TOKEN").equals(token)) {
			System.out.println("토큰 일치");
			boardService.updatePost(boardVO);
			return "redirect:/board/viewpost?postno="+boardVO.getPostno();
		}
		//에러페이지 반환
		return null;
		
		
		
	}
	
	/*글 삭제*/
	@PostMapping(value="deletePost")
	public ModelAndView deletePost(@RequestParam("postno") Integer postno, HttpServletRequest request, ModelAndView mav) 
	{
		System.out.println(postno+"번 글 삭제");
		boardService.deletePost(postno);
		
		String boardname = request.getParameter("boardname");
		String bulletin_board = "자유게시판";
		String question_board = "질문게시판";
		//자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
		if(bulletin_board.equals(boardname)) {
			mav.setViewName("redirect:/board/bulletin_boardlist");
		}else if(question_board.equals(boardname)) {
			mav.setViewName("redirect:/board/question_boardlist");
		}
		return mav;
	}
	
	/*댓글 등록+수정*/
	/* BoardService에서 getReno()가 null이면 댓글 insert, 번호가 있으면 update 수행 */
	@PostMapping(value = "boardReplySave")
	public String boardReplySave(BoardReplyVO boardReplyVO, HttpSession session) {
		
		String nickname = (String) session.getAttribute("memnickname");
		boardReplyVO.setNickname(nickname);
		
		boardService.insertBoardReply(boardReplyVO);
		System.out.println("요청 들어옴");
		return "redirect:/board/viewpost?postno="+boardReplyVO.getPostno();
	}
	
	/*댓글 삭제*/
	@PostMapping(value="deleteReply")
	public String deleteReply(@RequestParam("reno") Integer reno, @RequestParam("postno") int postno) 
	{		
		System.out.println(reno+"번 댓글 삭제");
		boardService.deleteReply(reno);
		return "redirect:/board/viewpost?postno="+postno;
	}
	
}