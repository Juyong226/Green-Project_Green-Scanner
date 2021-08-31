package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.FeedCommentVO;

@Repository
public interface FeedCommentDAO {
	
	public int insertComment(FeedCommentVO params);
	
	public FeedCommentVO selectCommentDetail(Integer idx);
	
	public int updateComment(FeedCommentVO params);
	
	public int deleteComment(Integer idx);
	
	public List<FeedCommentVO> selectCommentPreview(FeedCommentVO params);
	
	public List<FeedCommentVO> selectCommentList(FeedCommentVO params);
	
	public int selectCommentTotalCount(FeedCommentVO params);
}
