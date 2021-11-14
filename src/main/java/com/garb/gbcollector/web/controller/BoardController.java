package com.garb.gbcollector.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.web.vo.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.garb.gbcollector.constant.Method;
import com.garb.gbcollector.web.service.BoardService;
import com.garb.gbcollector.util.Log;

@RequestMapping("board")
@Controller

public class BoardController {

	@Autowired
	BoardService boardService;
	private Log log = new Log();

	/* 댓글 수정 폼 요청 */
	@GetMapping(value = {"/{postno}/comments/{reno}"})
	public String getCommentUpdateForm (@PathVariable("postno") final String postno, 
			@PathVariable(value = "reno", required = false) final Integer reno,
			 Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String redirectURI = "/board/viewpost?postno="+postno;
		if(session != null) {
				BoardReplyVO comment = boardService.getCommentDetail(reno);
				if(comment != null) {
					model.addAttribute("comment", comment);
					model.addAttribute("nickname", session.getAttribute("memnickname"));
					return "board/partial-content :: cmt-update-form";
				}
		}
		return null;
	}
	
	/*댓글 수정 요청*/
	@RequestMapping(value = { "/{postno}/comments", "/{postno}/comments/{reno}" }, 
								method = { RequestMethod.POST, RequestMethod.PATCH })
	public String updateComment(@PathVariable("postno") final String postno, 
			@PathVariable(value = "reno", required = false) final Integer reno,
			BoardReplyVO comment, Model model, HttpServletRequest req) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_BOARD_COMMENT, info.getId(), postno, Integer.toString(reno)));
		HttpSession session = req.getSession(false);
		String redirectURI = "/board/viewpost?postno="+postno;
		if(session != null) {
			try {
				if(reno != null) {
					comment.setReno(reno);
				}
				BoardReplyVO updated_comment = boardService.updateComment(comment);
				model.addAttribute("comment", updated_comment);
				model.addAttribute("nickname", session.getAttribute("memnickname"));
				log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_BOARD_COMMENT, info.getId(), postno, Integer.toString(reno)));
				return "board/partial-content :: cmt-regi";
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.TraceLog(info, BuildDescription.get(LogDescription.FAIL_BOARD_COMMENT, info.getId(), postno, Integer.toString(reno)));
				return "데이터베이스 처리 과정에 문제가 발생하였습니다.";
			} catch (Exception e) {
				e.printStackTrace();
				log.TraceLog(info, BuildDescription.get(LogDescription.FAIL_BOARD_COMMENT, info.getId(), postno, Integer.toString(reno)));
				return "시스템에 문제가 발생하였습니다.";
			}
		}
		return "로그인 후 이용이 가능합니다.";
	}

	/* 전체게시판 조회 */
	@GetMapping(value = "/boardlist")
	public ModelAndView boardList(HttpServletRequest req, ModelAndView mav, HttpSession session) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_BOARDMAIN, info.getId()));
		List<BoardVO> b_boards = boardService.listPostB();
		List<BoardVO> q_boards = boardService.listPostQ();
		mav.addObject("b_boards", b_boards);
		mav.addObject("q_boards", q_boards);
		mav.setViewName("board/list");
		return mav;
	}

	/* 자유게시판 조회 - 최신순 4개 */
	@GetMapping(value = "/bulletin_boardlist")
	public ModelAndView boardListB(HttpServletRequest req, ModelAndView mav, HttpSession session) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_BULLETIN, info.getId()));
		BoardPageNationVO page = new BoardPageNationVO();
		page.setStartIdx("1");
		page.setEndIdx("4");
		List<BoardVO> boards = boardService.listPostBAll(page);
		mav.addObject("boards", boards);
		String boardname = null;
		for (BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		int totalBoardCnt = boardService.getTotalBoardCnt(boardname);
		mav.addObject("totalBoardCnt", totalBoardCnt);
		mav.addObject("boardname", boardname);
		mav.setViewName("board/board");
		return mav;
	}

	/* 자유게시판 조회 - 더보기 */
	@PostMapping(value = "/bulletin_boardlist/more_post")
	public ModelAndView morePost(ModelAndView mav, HttpServletRequest request) {
		BoardPageNationVO page = new BoardPageNationVO();
		page.setStartIdx((String)request.getParameter("startIdx"));
		page.setEndIdx((String)request.getParameter("endIdx"));
		List<BoardVO> boards = boardService.listPostBAll(page);
		mav.addObject("boards", boards);
		// 조회된 게시글객체에서 게시판이름을 얻어 화면에 전달
		String boardname = null;
		for (BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);
		mav.setViewName("board/partial-board  :: more-post");
		return mav;
	}


	/* 질문게시판 조회 */
	@GetMapping(value = "/question_boardlist")
	public ModelAndView boardListQ(HttpServletRequest req, ModelAndView mav, HttpSession session) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_QUESTION, info.getId()));
		BoardPageNationVO page = new BoardPageNationVO();
		page.setStartIdx("1");
		page.setEndIdx("4");

		List<BoardVO> boards = boardService.listPostQAll(page);

		mav.addObject("boards", boards);
		String boardname = null;
		for (BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);

		int totalBoardCnt = boardService.getTotalBoardCnt(boardname);
		mav.addObject("totalBoardCnt", totalBoardCnt);

		mav.setViewName("board/board");
		return mav;
	}

	/* 질문게시판 조회 - 더보기 */
	@PostMapping(value = "/question_boardlist/more_post")
	public ModelAndView morePost_q(ModelAndView mav, HttpServletRequest request) {
		BoardPageNationVO page = new BoardPageNationVO();
		page.setStartIdx((String)request.getParameter("startIdx"));
		page.setEndIdx((String)request.getParameter("endIdx"));
		List<BoardVO> boards = boardService.listPostQAll(page);
		mav.addObject("boards", boards);
		// 조회된 게시글객체에서 게시판이름을 얻어 화면에 전달
		String boardname = null;
		for (BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);
		mav.setViewName("board/partial-board  :: more-post");
		return mav;
	}

	/* 글쓰기 화면 요청 */
	@GetMapping(value = "/write")
	public ModelAndView viewWritePage(HttpServletRequest req, ModelAndView mav, HttpSession session) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_WRITE_POST, info.getId()));
		/* 세션 확인하여 nickname이 존재하지 않으면 boardlist요청 전송 */
		String nickname = (String) session.getAttribute("memnickname");
		if(nickname == null) {
			mav.setViewName("redirect:/board/boardlist");
			return mav;
		}
		/* 토큰값을 생성해 session과 view에 저장 */
		addTokenToSession(mav, session);
		mav.setViewName("board/write");
		return mav;
	}

	public void addTokenToSession(ModelAndView mav, HttpSession session) {
		session.setAttribute("CSRF_TOKEN", UUID.randomUUID().toString());
		String token = (String) session.getAttribute("CSRF_TOKEN");
		log.TraceLog("CSRF_TOKEN: " + token);
		mav.addObject("token", token);
	}

	/* 글쓰기 */
	@PostMapping(value = "/write")
	public ModelAndView writePost(ModelAndView mav, @ModelAttribute("BoardVO") BoardVO boardVO, HttpSession session,
			MultipartHttpServletRequest req) throws IllegalStateException, IOException {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_WRITE_POST, info.getId()));
		String toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uploadPath = Paths.get("C:", "GreenScanner", "upload", "board", toDay, "/").toString();
		MultipartFile file = req.getFile("file");
		if (file != null && !"".equals(file.getOriginalFilename())) {
			String fileName = file.getOriginalFilename().toLowerCase();
			if (fileName != null) {
				// 확장자 유효성 검사 
				if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif")
						|| fileName.endsWith("jpeg")) {
					String saveName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
					log.TraceLog("파일 이름: " + saveName);
					log.TraceLog(saveName);
					File target = new File(uploadPath, saveName);
					// 폴더 경로
					File Folder = new File(uploadPath);
					if (!Folder.exists()) {
						try {
							Folder.mkdir(); // 폴더 생성
							log.TraceLog("폴더 생성");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					file.transferTo(target);
					boardVO.setFilename(saveName);
				}
			}
		}
		String nickname = (String) session.getAttribute("memnickname");
		boardVO.setNickname(nickname);
		if (boardVO.getIsanon() == null) {
			boardVO.setIsanon("0");
		}
		/* form으로 전송된 요청으로부터 token값을 얻어오기 */
		String token = req.getParameter("_csrf");
		log.TraceLog("_csrf: " + token);
		if (session.getAttribute("CSRF_TOKEN").equals(token)) {
			log.TraceLog("토큰 일치");
			boardService.insertPost(boardVO);
			String bulletin_board = "자유게시판";
			String question_board = "질문게시판";
			// 자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
			if (bulletin_board.equals(boardVO.getBoardname())) {
				mav.setViewName("redirect:/board/bulletin_boardlist");
			} else if (question_board.equals(boardVO.getBoardname())) {
				mav.setViewName("redirect:/board/question_boardlist");
			}
			log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_WRITE_POST, info.getId()));
			return mav;
		}
		log.TraceLog("토큰값 불일치");
		String msg = "비정상적인 접근입니다.";
		mav.addObject("msg", msg);
		mav.setViewName("board/error");
		return mav;
	}

	/* 글+댓글 보기 */
	@GetMapping(value = "viewpost")
	public ModelAndView viewPostPage(@RequestParam("postno") int postno, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_POST, info.getId(), Integer.toString(postno)));
		ModelAndView mav = new ModelAndView();
		BoardVO boardVO = boardService.viewPost(postno);
		mav.addObject("post", boardVO);
		addTokenToSession(mav, session);
		List<BoardReplyVO> boardReplyVO = boardService.viewReply(postno);
		mav.addObject("commentList", boardReplyVO);
		String nickname = (String) session.getAttribute("memnickname");
		mav.addObject("login_nickname", nickname);
		mav.setViewName("board/view");
		return mav;
	}

	/* 이미지 출력 */
	@GetMapping(value = "display")
	public ResponseEntity<Resource> display(@RequestParam("filename") String filename) {
		String toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String path = Paths.get("C:", "GreenScanner", "upload", "board", toDay, "/").toString();;
		Resource resource = new FileSystemResource(path +"/"+ filename);
		if (!resource.exists())
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try {
			filePath = Paths.get(path + filename);
			header.add("Content-Type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	/* 글 수정 페이지 가져오기 */
	@GetMapping(value = "updatePost")
	public ModelAndView updatePost(@RequestParam("postno") int postno, ModelAndView mav, HttpSession session, HttpServletRequest req) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.ACCESS_UPDATE_POST, info.getId(), Integer.toString(postno)));
		/* 세션 확인하여 nickname이 존재하지 않으면 boardlist요청 전송 */
		String nickname = (String) session.getAttribute("memnickname");
		if(nickname == null) {
			mav.setViewName("redirect:/board/boardlist");
			return mav;
		}
		addTokenToSession(mav, session);
		BoardVO boardVO = boardService.viewPost(postno);
		mav.addObject("post", boardVO);
		mav.addObject("title", boardVO.getTitle());
		mav.setViewName("board/modify");
		return mav;
	}

	/* 글 수정 후 저장 */
	@PostMapping(value = "updatePostSave")
	public String updatePostSave(@ModelAttribute("post") BoardVO boardVO, HttpSession session,
			MultipartHttpServletRequest req) throws IllegalStateException, IOException {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_UPDATE_POST, info.getId(), Integer.toString(boardVO.getPostno())));
		String toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uploadPath = Paths.get("C://GreenScanner", "upload", "board", toDay, "/").toString();
		String orgFileName = req.getParameter("orgFile");
		String prevFileName = req.getParameter("prevFile");
		MultipartFile newFile = req.getFile("file");
		try {
			//새로운 파일을 업로드한 경우
			if ("".equals(newFile.getOriginalFilename())) {
				boardVO.setFilename(prevFileName);
			}else {
				String newFile_saveName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FilenameUtils.getExtension(newFile.getOriginalFilename());
				boardVO.setFilename(newFile_saveName);
				File removeFile = new File(uploadPath + orgFileName);
				removeFile.delete();
				File new_target = new File(uploadPath, newFile_saveName);
				newFile.transferTo(new_target);
			}
			String nickname = (String) session.getAttribute("memnickname");
			boardVO.setNickname(nickname);
			if (boardVO.getIsanon() == null) {
				boardVO.setIsanon("0");
			}
			String token = req.getParameter("_csrf");
			if (req.getSession().getAttribute("CSRF_TOKEN").equals(token)) {
				boardService.updatePost(boardVO);
				log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_UPDATE_POST, info.getId(), Integer.toString(boardVO.getPostno())));
				return "redirect:/board/viewpost?postno=" + boardVO.getPostno();
			}
			return "redirect:/board/error";
		}catch (Exception e){
			return "redirect:/board/error";
		}
	}

	/* 글 삭제 */
	@PostMapping(value = "deletePost")
	public ModelAndView deletePost(@RequestParam("postno") Integer postno, HttpServletRequest req,
			ModelAndView mav) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_DELETE_POST, info.getId(), Integer.toString(postno)));
		try {
			boardService.deletePost(postno);
			String boardname = req.getParameter("boardname");
			String bulletin_board = "자유게시판";
			String question_board = "질문게시판";
			// 자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
			if (bulletin_board.equals(boardname)) {
				mav.setViewName("redirect:/board/bulletin_boardlist");
			} else if (question_board.equals(boardname)) {
				mav.setViewName("redirect:/board/question_boardlist");
			}
			log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_DELETE_POST, info.getId(), Integer.toString(postno)));
			return mav;
		} catch (Exception e){
			log.TraceLog(info, BuildDescription.get(LogDescription.FAIL_DELETE_POST, info.getId(), Integer.toString(postno)));
			mav.setViewName("/board/error");
			return mav;
		}
	}

	/* 댓글 등록+수정 */
	/* BoardService에서 getReno()가 null이면 댓글 insert, 번호가 있으면 update 수행 */
	@PostMapping(value = "boardReplySave")
	public String boardReplySave(BoardReplyVO boardReplyVO, HttpSession session, HttpServletRequest req) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_BOARD_COMMENT, info.getId(), Integer.toString(boardReplyVO.getPostno())));
		String nickname = (String) session.getAttribute("memnickname");
		System.out.println(nickname);
		boardReplyVO.setNickname(nickname);
		try{
			boardService.insertBoardReply(boardReplyVO);
			log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_BOARD_COMMENT, info.getId(), Integer.toString(boardReplyVO.getPostno())));
			return "redirect:/board/viewpost?postno=" + boardReplyVO.getPostno();
		}catch (Exception e){
			System.out.println(e);
			log.TraceLog(info, BuildDescription.get(LogDescription.FAIL_BOARD_COMMENT, info.getId(), Integer.toString(boardReplyVO.getPostno())));
			return "redirect:/board/error";
		}
	}

	/* 댓글 삭제 */
	@PostMapping(value = "deleteReply")
	public String deleteReply(@RequestParam("reno") Integer reno, @RequestParam("postno") int postno, HttpServletRequest req) {
		RequestInforVO info = new RequestInforVO(req);
		log.TraceLog(info, BuildDescription.get(LogDescription.REQUEST_DELETE_BOARD_COMMENT, info.getId(), Integer.toString(postno), Integer.toString(reno)));
		try {
			boardService.deleteReply(reno);
			log.TraceLog(info, BuildDescription.get(LogDescription.SUCCESS_DELETE_BOARD_COMMENT, info.getId(), Integer.toString(postno), Integer.toString(reno)));
			return "redirect:/board/viewpost?postno=" + postno;
		}catch (Exception e){
			log.TraceLog(info, BuildDescription.get(LogDescription.FAIL_DELETE_BOARD_COMMENT, info.getId(), Integer.toString(postno), Integer.toString(reno)));
			return "redirect:/board/error";
		}
	}

	@GetMapping(value = "error")
	public ModelAndView errorPage(ModelAndView mav){
		mav.setViewName("board/error");
		return mav;
	}
}