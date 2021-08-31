package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garb.gbcollector.web.dao.BoardDAO;
import com.garb.gbcollector.web.dao.QnABoardDAO;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@Service
public class QnABoardService {

    @Autowired
    QnABoardDAO qnaboardDAO;

    public List<BoardVO> getPostList(){
        return qnaboardDAO.getPostList();
    }

    public List<BoardVO> listPostBAll(){
        return qnaboardDAO.listPostBAll();
    }

    public List<BoardVO> listPostQ(){
        return qnaboardDAO.listPostQ();
    }

    public List<BoardVO> listPostQAll() {
        return qnaboardDAO.listPostQAll();
    }

    public void insertPost(BoardVO post) {
    	System.out.println("insertPost service 진입");
    	qnaboardDAO.insertPost(post);
    }

    public BoardVO viewPost(int postno) {
        return qnaboardDAO.viewPost(postno);
    }

    public void updatePost(BoardVO boardVO) {
    	qnaboardDAO.updatePost(boardVO);

    }

    public void deletePost(int postno) {
    	qnaboardDAO.deletePost(postno);
    }


    @Transactional
    public void insertBoardReply(BoardReplyVO boardReplyVO) {
        if(boardReplyVO.getReno()==null) {
        	qnaboardDAO.insertBoardReply(boardReplyVO);
        	qnaboardDAO.updateReplyCnt(boardReplyVO.getPostno(), 1);
        }else {
        	qnaboardDAO.updateReply(boardReplyVO);
        }

    }

    public List<BoardReplyVO> viewReply(int postno) {
        return qnaboardDAO.viewReply(postno);
    }

    @Transactional
    public void deleteReply(int reno) {
        int postno = qnaboardDAO.getPostno(reno);
        qnaboardDAO.deleteReply(reno);
        qnaboardDAO.updateReplyCnt(postno, -1);
    }
}