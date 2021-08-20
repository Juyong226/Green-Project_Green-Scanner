package com.garb.gbcollector.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.FeedDAO;
import com.garb.gbcollector.web.vo.FeedVO;

@Service
public class FeedService {

	@Autowired
	private FeedDAO feedDAO;
	
	public boolean registerFeed(FeedVO params) {
		int queryResult = 0;
		System.out.println("========================================");
		System.out.println("피드 번호 = " + params.getFeedNo());
		System.out.println("========================================");
		if(params.getFeedNo() == 0) {
			queryResult = feedDAO.insertFeed(params);
		} else {
			queryResult = feedDAO.updateFeed(params);
		}
		return (queryResult == 1) ? true : false;
	}
	
	public FeedVO getFeedDetail(int feedNo) {
		return feedDAO.selectFeedDetail(feedNo);
	}
	
	public boolean deleteFeed(int feedNo) {
		int queryResult = feedDAO.deleteFeed(feedNo);
		return (queryResult == 1) ? true : false;
	}
	
	public List<FeedVO> getAllFeedList() {
		List<FeedVO> feedList = new ArrayList<FeedVO>();
		feedList = feedDAO.selectAllFeedList();
		return feedList;
	}
	
	public List<FeedVO> getMyFeedList(String challengeNum) {
		List<FeedVO> feedList = new ArrayList<FeedVO>();
		feedList = feedDAO.selectMyFeedList(challengeNum);
		return feedList;
	}
}
