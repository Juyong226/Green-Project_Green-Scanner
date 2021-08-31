package com.garb.gbcollector.web.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.FeedCommentDAO;
import com.garb.gbcollector.web.vo.FeedCommentVO;

@Service
public class FeedCommentService {

	@Autowired
	private FeedCommentDAO feedCommentDAO;
	
	public boolean registerComment(FeedCommentVO params) {
		int queryResult = 0;
		if(params.getIdx() == null) {
			queryResult = feedCommentDAO.insertComment(params);
		} else {
			queryResult = feedCommentDAO.updateComment(params);
		}
		return (queryResult == 1) ? true : false;
	}
	
	public boolean deleteComment(Integer idx) {
		int queryResult = 0;
		FeedCommentVO comment = feedCommentDAO.selectCommentDetail(idx);
		if(comment != null && "N".equals(comment.getDeleteYn())) {
			queryResult = feedCommentDAO.deleteComment(idx);
		}
		return (queryResult == 1) ? true : false;
	}
	
	public List<FeedCommentVO> getCommentPreview(FeedCommentVO params) {
		List<FeedCommentVO> commentList = Collections.emptyList();
		int commentTotalCount = feedCommentDAO.selectCommentTotalCount(params);
		if(commentTotalCount > 0) {
			commentList = feedCommentDAO.selectCommentPreview(params);
		}
		return commentList;
	}
	
	public List<FeedCommentVO> getCommentList(FeedCommentVO params) {
		List<FeedCommentVO> commentList = Collections.emptyList();
		int commentTotalCount = feedCommentDAO.selectCommentTotalCount(params);
		if(commentTotalCount > 0) {
			commentList = feedCommentDAO.selectCommentList(params);
		}
		return commentList;
	}
}
