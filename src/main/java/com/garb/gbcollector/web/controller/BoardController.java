package com.garb.gbcollector.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.garb.gbcollector.web.service.BoardService;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;
import com.garb.gbcollector.web.vo.BoardPageNationVO;
import com.garb.gbcollector.util.Log;

@RequestMapping("board")
@Controller

public class BoardController {

	@Autowired
	BoardService boardService;
	private Log log = new Log();

	/* 전체게시판 조회 */
	@GetMapping(value = "/boardlist")
	public ModelAndView boardList(ModelAndView mav, HttpSession session) {
		log.TraceLog("board/list진입");
		List<BoardVO> b_boards;
		b_boards = boardService.listPostB();
		List<BoardVO> q_boards;
		q_boards = boardService.listPostQ();
		mav.addObject("b_boards", b_boards);
		mav.addObject("q_boards", q_boards);
		mav.setViewName("board/list");

		return mav;
	}

	/* 자유게시판 조회 - 최신순 4개 */
	@GetMapping(value = "/bulletin_boardlist")
	public ModelAndView boardListB(BoardPageNationVO page, ModelAndView mav, HttpSession session) {
		log.TraceLog("board/bulletin_boardlist진입");
		
		page.setStartIdx("1");
		page.setEndIdx("4");
		
		int totalBoardCnt = boardService.getTotalBoardCnt();
		List<BoardVO> boards = boardService.listPostBAll(page);
		
		mav.addObject("totalBoardCnt", totalBoardCnt);
		mav.addObject("boards", boards);
		
		// 조회된 게시글객체에서 게시판이름을 얻어 화면에 전달 
		String boardname = null;
		for(BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);
		mav.setViewName("board/board");
		return mav;
	}
	
	/* 자유게시판 조회 - 더보기 */
	@PostMapping(value = "/bulletin_boardlist/more_post")
	public ModelAndView morePost(BoardPageNationVO page, ModelAndView mav, HttpServletRequest request) {
		log.TraceLog("더보기 진입");
	
		page.setStartIdx((String)request.getParameter("startIdx"));
		page.setEndIdx((String)request.getParameter("EndIdx"));
		
		List<BoardVO> boards = boardService.listPostBAll(page);
		
		mav.addObject("boards", boards);
		
		// 조회된 게시글객체에서 게시판이름을 얻어 화면에 전달 
		String boardname = null;
		for(BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);
		mav.setViewName("board/partial-board  :: more-post");
		return mav;
		
	}
	
	
//	/* 자유게시판 조회 */
//	@GetMapping(value = "/bulletin_boardlist")
//	public ModelAndView boardListB(ModelAndView mav, HttpSession session) {
//		log.TraceLog("board/bulletin_boardlist진입");
//		List<BoardVO> boards = boardService.listPostBAll();
//		mav.addObject("boards", boards);
//		String boardname = null;
//		for(BoardVO board : boards) {
//			boardname = board.getBoardname();
//		}
//		mav.addObject("boardname", boardname);
//		mav.setViewName("board/board");
//		return mav;
//	}
	

	/* 질문게시판 조회 */
	@GetMapping(value = "/question_boardlist")
	public ModelAndView boardListQ(ModelAndView mav, HttpSession session) {
		log.TraceLog("board/question_boardlist진입");
		List<BoardVO> boards;
		boards = boardService.listPostQAll();
		mav.addObject("boards", boards);
		String boardname = null;
		for(BoardVO board : boards) {
			boardname = board.getBoardname();
		}
		mav.addObject("boardname", boardname);
		mav.setViewName("board/board");
		return mav;
	}

	/* 글쓰기 화면 요청 */
	@GetMapping(value = "/write")
	public ModelAndView viewWritePage(ModelAndView mav, HttpSession session) {

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
			MultipartHttpServletRequest req) {
		log.TraceLog("writePost 진입");

		String uploadPath = System.getProperty("user.home") + "/files/";
		log.TraceLog(uploadPath);
		MultipartFile file = req.getFile("file");
		if (file != null && !"".equals(file.getOriginalFilename())) {
			String fileName = file.getOriginalFilename().toLowerCase();

			if (fileName != null) {
				if (fileName.endsWith(".jpg") 
					|| fileName.endsWith(".png") 
					|| fileName.endsWith(".gif")
					|| fileName.endsWith("jpeg")) {

					log.TraceLog("파일 이름: " + fileName);
					File uploadFile = new File(uploadPath + fileName);

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
					if (uploadFile.exists()) {
						fileName = new Date().getTime() + fileName;
						uploadFile = new File(uploadPath + fileName);
						log.TraceLog("파일 업로드: " + uploadPath + fileName);
					}
					try {
						file.transferTo(uploadFile);
					} catch (Exception e) {
						log.TraceLog("업로드 에러");
						log.TraceLog(e);
					}
					boardVO.setFilename(fileName);
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
			return mav;
		}
		log.TraceLog("토큰값 불일치");
		String msg = "비정상적인 접근입니다.";
		mav.addObject("msg", msg);
		mav.setViewName("board/error");
		return mav;
	}

//	/*글쓰기*/
//	@PostMapping(value = "/write")
//	public ModelAndView writePost(ModelAndView mav, @ModelAttribute("BoardVO") BoardVO boardVO, 
//			HttpSession session, HttpServletRequest req) 
//	{
//		log.TraceLog("writePost 진입");
//		String nickname = (String) session.getAttribute("memnickname");
//		boardVO.setNickname(nickname);	
//		
//		if(boardVO.getIsanon()==null) {
//			boardVO.setIsanon("0");
//		}
//				
//		/* form으로 전송된 요청으로부터 token값을 얻어오기 */
//		String token = req.getParameter("_csrf");
//		log.TraceLog("_csrf: " + token);
//		
//		
//		if (session.getAttribute("CSRF_TOKEN").equals(token)) {
//			log.TraceLog("토큰 일치");
//			boardService.insertPost(boardVO);
//			log.TraceLog(boardVO);
//			String bulletin_board = "자유게시판";
//			String question_board = "질문게시판";
//			//자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
//			if(bulletin_board.equals(boardVO.getBoardname())) {
//				mav.setViewName("redirect:/board/bulletin_boardlist");
//			}else if(question_board.equals(boardVO.getBoardname())) {
//				mav.setViewName("redirect:/board/question_boardlist");
//			}
//			return mav;
//		}
//		log.TraceLog("토큰값 불일치");
//		String msg = "비정상적인 접근입니다.";
//		mav.addObject("msg", msg);
//		mav.setViewName("board/error");
//		return mav;	
//	}

	/* 글+댓글 보기 */
	@GetMapping(value = "viewpost")
	public ModelAndView viewPostPage(@RequestParam("postno") int postno, HttpServletResponse res, HttpSession session) {
		log.TraceLog(postno + "번 글 보기");
		ModelAndView mav = new ModelAndView();
		BoardVO boardVO = boardService.viewPost(postno);
		log.TraceLog(boardVO.getFilename());
		mav.addObject("post", boardVO);

		addTokenToSession(mav, session);

		// 현재 게시글에 대한 댓글을 모두 가져오기. 한 게시글당 댓글이 하나 이상이기 때문에 타입은 List이다.
		List<BoardReplyVO> boardReplyVO = boardService.viewReply(postno);
		// 댓글 리스트를 reply라는 이름으로 뷰페이지에 추가
		mav.addObject("commentList", boardReplyVO);

		// 세션으로부터 닉네임 얻기
		String nickname = (String) session.getAttribute("memnickname");
		mav.addObject("login_nickname", nickname);
		mav.setViewName("board/view");
		return mav;
	}

	/* 이미지 출력 */
	@GetMapping(value = "display")
	public ResponseEntity<Resource> display(@RequestParam("filename") String filename) {
		String path = "C:\\Users\\user\\files\\";
		Resource resource = new FileSystemResource(path + filename);

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
	public ModelAndView updatePost(@RequestParam("postno") int postno, ModelAndView mav, HttpSession session) {
		log.TraceLog(postno + "번 글 수정");

		addTokenToSession(mav, session);

		BoardVO boardVO = boardService.viewPost(postno);
		mav.addObject("post", boardVO);
		mav.setViewName("board/modify");
		return mav;
	}

	/* 글 수정 후 저장 */
	@PostMapping(value = "updatePostSave")
	public String updatePostSave(@ModelAttribute("post") BoardVO boardVO, HttpSession session,
			MultipartHttpServletRequest req) {
		log.TraceLog(boardVO.getPostno() + "번 글 수정 진입");

		String uploadPath = System.getProperty("user.home") + "/files/";
		String orgFileName = req.getParameter("orgFile");

		MultipartFile newFile = req.getFile("file");
		if (newFile != null) {
			String newFileName = newFile.getOriginalFilename();
			boardVO.setFilename(orgFileName);

			// if: when want to change upload file
			if (newFile != null && !newFileName.equals("")) {
				if (orgFileName != null || !orgFileName.equals("")) {
					// remove uploaded file
					File removeFile = new File(uploadPath + orgFileName);
					removeFile.delete();
					//
				}
				// create new upload file
				File newUploadFile = new File(uploadPath + newFileName);
				try {
					newFile.transferTo(newUploadFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				boardVO.setFilename(newFileName);
			}
		}

		String nickname = (String) session.getAttribute("memnickname");
		boardVO.setNickname(nickname);
		if (boardVO.getIsanon() == null) {
			boardVO.setIsanon("0");
		}

		String token = req.getParameter("_csrf");
		log.TraceLog("_csrf: " + token);

		if (req.getSession().getAttribute("CSRF_TOKEN").equals(token)) {
			log.TraceLog("토큰 일치");
			boardService.updatePost(boardVO);
			return "redirect:/board/viewpost?postno=" + boardVO.getPostno();
		}
		// 에러페이지 반환
		return null;

	}

	/* 글 삭제 */
	@PostMapping(value = "deletePost")
	public ModelAndView deletePost(@RequestParam("postno") Integer postno, HttpServletRequest request,
			ModelAndView mav) {
		log.TraceLog(postno + "번 글 삭제");
		boardService.deletePost(postno);

		String boardname = request.getParameter("boardname");
		String bulletin_board = "자유게시판";
		String question_board = "질문게시판";
		// 자유게시판에 쓴 글이면 자유게시판 리스트로 이동, 질문게시판에 쓴 글이면 질문게시판 리스트로 이동
		if (bulletin_board.equals(boardname)) {
			mav.setViewName("redirect:/board/bulletin_boardlist");
		} else if (question_board.equals(boardname)) {
			mav.setViewName("redirect:/board/question_boardlist");
		}
		return mav;
	}

	/* 댓글 등록+수정 */
	/* BoardService에서 getReno()가 null이면 댓글 insert, 번호가 있으면 update 수행 */
	@PostMapping(value = "boardReplySave")
	public String boardReplySave(BoardReplyVO boardReplyVO, HttpSession session) {

		String nickname = (String) session.getAttribute("memnickname");
		boardReplyVO.setNickname(nickname);

		boardService.insertBoardReply(boardReplyVO);
		log.TraceLog("요청 들어옴");
		return "redirect:/board/viewpost?postno=" + boardReplyVO.getPostno();
	}

	/* 댓글 삭제 */
	@PostMapping(value = "deleteReply")
	public String deleteReply(@RequestParam("reno") Integer reno, @RequestParam("postno") int postno) {
		log.TraceLog(reno + "번 댓글 삭제");
		boardService.deleteReply(reno);
		return "redirect:/board/viewpost?postno=" + postno;
	}

}