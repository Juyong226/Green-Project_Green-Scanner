package com.garb.gbcollector.web.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.web.dao.FeedCommentDAO;
import com.garb.gbcollector.web.vo.FeedCommentVO;
import com.garb.gbcollector.web.vo.RequestInforVO;

@Service
public class FeedCommentService {

	@Autowired
	private FeedCommentDAO feedCommentDAO;
	
	private Log log = new Log();
	
	public FeedCommentVO registerComment(FeedCommentVO params, RequestInforVO infor) throws SQLException {
		int queryResult = 0;
		if(params.getIdx() == null) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_NEWCOMMENT));
			queryResult = feedCommentDAO.insertComment(params);
		} else {
			log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_UPDATE_COMMENT, Integer.toString(params.getIdx())));
			queryResult = feedCommentDAO.updateComment(params);
		}
		FeedCommentVO comment;
		if(queryResult == 1) {
			comment = feedCommentDAO.selectCommentDetail(params.getIdx());
		} else {
			comment = null;
		}
		return comment;
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

	public FeedCommentVO getCommentDetail(Integer idx) {
		FeedCommentVO comment = feedCommentDAO.selectCommentDetail(idx);
		return comment;
	}
}
