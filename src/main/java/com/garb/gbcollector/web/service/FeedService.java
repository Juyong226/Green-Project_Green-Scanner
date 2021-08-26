package com.garb.gbcollector.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.util.GSCalendar;
import com.garb.gbcollector.web.dao.ChallengeDAO;
import com.garb.gbcollector.web.dao.FeedCommentDAO;
import com.garb.gbcollector.web.dao.FeedDAO;
import com.garb.gbcollector.web.vo.FeedCommentVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

@Service
public class FeedService {

	@Autowired
	private FeedDAO feedDAO;
	@Autowired
	private ChallengeDAO challengeDAO;
	@Autowired
	private FeedCommentDAO	feedCommentDAO;
	
	GSCalendar gsCalendar = GSCalendar.getInstance();
	
	public boolean registerFeed(FeedVO params, String challengeNum) {
		int queryResult = 0;
		System.out.println("========================================");
		System.out.println("피드 params = " + params.toString());
		System.out.println("========================================");
		if(params.getFeedNo() == 0) {
			if(duplicateCheck(challengeNum) == true) {
				queryResult = feedDAO.insertFeed(params);
				if(queryResult == 1) {
					PersonalChallengeVO pc = challengeDAO.getPersonalChallenge(params.getChallengeNum());
					pc.setCalendar(gsCalendar.updateCalander(pc.getStartDate(), params.getPostDate(), pc.getCalendar()));
					pc.setExecutionNum(pc.getExecutionNum() + 1);
					pc.calculateAchievementRate();
					queryResult = challengeDAO.updateChallengeVO(pc);
				}
			}
		} else {
			queryResult = feedDAO.updateFeed(params);
		}
		return (queryResult == 1) ? true : false;
	}
	
	public FeedVO getFeedDetail(int feedNo) {		
		return feedDAO.selectFeedDetail(feedNo);
	}
	
	public boolean duplicateCheck(String challengeNum) {
		List<FeedVO> feedList = feedDAO.duplicateCheck(challengeNum);
		boolean result = true;		
		if(feedList.isEmpty() == false) {
			String toDay = gsCalendar.getCurrentTime();
			for(FeedVO feed : feedList) {
				if(feed.getPostDate().equals(toDay)) {
					result = false;
				}
			}
		}
		return result;
	}
	
	public boolean deleteFeed(int feedNo, String challengeNum) {
		FeedVO feed = feedDAO.selectFeedDetail(feedNo);
		int queryResult = 0;
		if(feed != null) {
			String postDate = feed.getPostDate();
			queryResult = feedDAO.deleteFeed(feedNo);
			if(queryResult == 1) {
				PersonalChallengeVO pc = challengeDAO.getPersonalChallenge(challengeNum);
				pc.setCalendar(gsCalendar.updateCalander(pc.getStartDate(), postDate, pc.getCalendar()));
				pc.setExecutionNum(pc.getExecutionNum() - 1);
				pc.calculateAchievementRate();
				queryResult = challengeDAO.updateChallengeVO(pc);
			}
		}
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
