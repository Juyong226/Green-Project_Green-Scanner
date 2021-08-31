package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garb.gbcollector.web.dao.BoardDAO;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@Service
public class BoardService {

    @Autowired
    BoardDAO boardDAO;

    public List<BoardVO> listPostB(){
        return boardDAO.listPostB();
    }

    public List<BoardVO> listPostBAll(){
        return boardDAO.listPostBAll();
    }

    public List<BoardVO> listPostQ(){
        return boardDAO.listPostQ();
    }

    public List<BoardVO> listPostQAll() {
        return boardDAO.listPostQAll();
    }

    public void insertPost(BoardVO post) {
    	System.out.println("insertPost service 진입");
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

    @Transactional
    public void deleteReply(int reno) {
        int postno = boardDAO.getPostno(reno);
        boardDAO.deleteReply(reno);
        boardDAO.updateReplyCnt(postno, -1);
    }
}