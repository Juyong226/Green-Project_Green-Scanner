package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garb.gbcollector.web.dao.BoardDAO;
import com.garb.gbcollector.web.vo.BoardPageNationVO;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;
import com.garb.gbcollector.util.Log;

@Service
public class BoardService {

    private Log log = new Log();
	@Autowired
    BoardDAO boardDAO;

    public List<BoardVO> listPostB(){
        return boardDAO.listPostB();
    }

    public List<BoardVO> listPostBAll(BoardPageNationVO page){
        return boardDAO.listPostBAll(page);
    }

    public List<BoardVO> listPostQ(){
        return boardDAO.listPostQ();
    }

    public List<BoardVO> listPostQAll(BoardPageNationVO page) {
        return boardDAO.listPostQAll(page);
    }

    public void insertPost(BoardVO post) {
    	log.TraceLog("insertPost service 진입");
        boardDAO.insertPost(post);
    }

    public BoardVO viewPost(int postno) {
        return boardDAO.viewPost(postno);
    }

    public void updatePost(BoardVO boardVO) {
        boardDAO.updatePost(boardVO);

    }

    public void deletePost(int postno) {
        boardDAO.deletePost(postno);
    }


    @Transactional
    public void insertBoardReply(BoardReplyVO boardReplyVO) {
        if(boardReplyVO.getReno()==null) {
            boardDAO.insertBoardReply(boardReplyVO);
            boardDAO.updateReplyCnt(boardReplyVO.getPostno(), 1);
        }else {
            boardDAO.updateReply(boardReplyVO);
        }

    }

    public List<BoardReplyVO> viewReply(int postno) {
        return boardDAO.viewReply(postno);
    }
    
    public BoardReplyVO getCommentDetail(int reno) {
    	return boardDAO.getCommentDetail(reno);
    }
    

    @Transactional
    public void deleteReply(int reno) {
        int postno = boardDAO.getPostno(reno);
        boardDAO.deleteReply(reno);
        boardDAO.updateReplyCnt(postno, -1);
    }

	public int getTotalBoardCnt(String boardname) {
		return boardDAO.selectTotalBoardCnt(boardname);
	}

	public BoardReplyVO updateComment(BoardReplyVO comment) {
		int queryResult = 0;
		queryResult = boardDAO.updateComment(comment);
		BoardReplyVO registered_comment;
		if(queryResult == 1) {
			registered_comment = boardDAO.getCommentDetail(comment.getReno());
		}else {
			registered_comment = null;
		}
		return registered_comment;
	}
}