package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garb.gbcollector.web.dao.BoardDAO;
import com.garb.gbcollector.web.dao.FAQBoardDAO;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@Service
public class FAQBoardService {

    @Autowired
    FAQBoardDAO faqboardDAO;

    public List<BoardVO> getPostList(){
        return faqboardDAO.getPostList();
    }

    public List<BoardVO> listPostBAll(){
        return faqboardDAO.listPostBAll();
    }

    public List<BoardVO> listPostQ(){
        return faqboardDAO.listPostQ();
    }

    public List<BoardVO> listPostQAll() {
        return faqboardDAO.listPostQAll();
    }

    public void insertPost(BoardVO post) {
    	System.out.println("insertPost service 진입");
    	faqboardDAO.insertPost(post);
    }

    public BoardVO viewPost(int postno) {
        return faqboardDAO.viewPost(postno);
    }

    public void updatePost(BoardVO boardVO) {
    	faqboardDAO.updatePost(boardVO);

    }

    public void deletePost(int postno) {
    	faqboardDAO.deletePost(postno);
    }


    @Transactional
    public void insertBoardReply(BoardReplyVO boardReplyVO) {
        if(boardReplyVO.getReno()==null) {
        	faqboardDAO.insertBoardReply(boardReplyVO);
        	faqboardDAO.updateReplyCnt(boardReplyVO.getPostno(), 1);
        }else {
        	faqboardDAO.updateReply(boardReplyVO);
        }

    }

    public List<BoardReplyVO> viewReply(int postno) {
        return faqboardDAO.viewReply(postno);
    }

    @Transactional
    public void deleteReply(int reno) {
        int postno = faqboardDAO.getPostno(reno);
        faqboardDAO.deleteReply(reno);
        faqboardDAO.updateReplyCnt(postno, -1);
    }
}