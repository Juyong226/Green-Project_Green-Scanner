package com.garb.gbcollector.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.BoardPageNationVO;
import com.garb.gbcollector.web.vo.BoardReplyVO;
import com.garb.gbcollector.web.vo.BoardVO;

@Repository
public interface BoardDAO {
    public List<BoardVO> listPostB();
    public List<BoardVO> listPostBAll(BoardPageNationVO page);
    public List<BoardVO> listPostQ();
    public List<BoardVO> listPostQAll(BoardPageNationVO page);
    public void insertPost(BoardVO post);
    public BoardVO viewPost(int postno);
    public void insertBoardReply(BoardReplyVO boardReplyVO);
    public void updateReply(BoardReplyVO boardReplyVO);
    public List<BoardReplyVO> viewReply(int postno);
    public void deletePost(int postno);
    public void deleteReply(int reno);
    public void updatePost(BoardVO boardVO);
    public void likePost(int postno);
    //댓글 갯수 갱신
    public void updateReplyCnt(int postno, int amount);
    //댓글 삭제 시 해당 게시물 번호 조회
    public int getPostno(int reno);
    //전체게시글 수 조회 
	public int selectTotalBoardCnt(String boardname);
	//특정 댓글 조회
	public BoardReplyVO getCommentDetail(int reno);
	public int updateComment(BoardReplyVO comment);
}