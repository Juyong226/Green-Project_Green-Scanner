package com.garb.gbcollector.web.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.garb.gbcollector.util.FileUtils;
import com.garb.gbcollector.util.GSCalendar;
import com.garb.gbcollector.util.UploadFileException;
import com.garb.gbcollector.web.dao.ChallengeDAO;
import com.garb.gbcollector.web.dao.FeedCommentDAO;
import com.garb.gbcollector.web.dao.FeedDAO;
import com.garb.gbcollector.web.dao.FeedImageDAO;
import com.garb.gbcollector.web.vo.FeedCommentVO;
import com.garb.gbcollector.web.vo.FeedPaginationVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;
import com.garb.gbcollector.web.vo.UploadImageVO;

@Service
public class FeedService {

	@Autowired
	private FeedDAO feedDAO;
	@Autowired
	private ChallengeDAO challengeDAO;
	@Autowired
	private FeedCommentDAO feedCommentDAO;
	@Autowired
	private FeedImageDAO feedImageDAO;
	@Autowired
	private FileUtils fileUtils;
	
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
			System.out.println("========================================================================================");
			System.out.println("registerFeed()의 params.getChangeYn(): " + params.getChangeYn());
			System.out.println("registerFeed()의 params.getFeedNo(): " + params.getFeedNo());
			System.out.println("========================================================================================");
			if("Y".equals(params.getChangeYn())) {
				feedImageDAO.deleteFeedImage(params.getFeedNo());
				if(params.getImageIdxs() != null && params.getImageIdxs().isEmpty() == false) {
					feedImageDAO.undeleteFeedImage(params.getImageIdxs());
				}
			}
		}
		System.out.println("========================================================================================");
		System.out.println("registerFeed()의 queryResult: " + queryResult);
		System.out.println("========================================================================================");
		return (queryResult == 1) ? true : false;
	}
	
	public boolean registerFeed(FeedVO params, String challengeNum, MultipartFile[] images) throws UploadFileException {
		int queryResult = 1;
		if(registerFeed(params, challengeNum) == false) {
			return false;
		}
		
		List<UploadImageVO> uploadList = fileUtils.uploadFeedImages(images, params.getFeedNo());
		if(uploadList.isEmpty() == false) {
			queryResult = feedImageDAO.insertFeedImage(uploadList);
			System.out.println("========================================================================================");
			System.out.println("insertFeedImage(uploadList)_<foreach> 결과 값: " + queryResult);
			System.out.println("========================================================================================");
			if(queryResult != -1) {
				queryResult = 0;
			}
		}
		return (queryResult == -1);
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
	
	public boolean deleteFeed(Integer feedNo, String challengeNum) {
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
	
	public List<FeedVO> getAllFeedList(FeedPaginationVO params) {
		List<FeedVO> feedList = feedDAO.selectAllFeedList(params);
		return setCommentCntAndImageIdxs(feedList);
	}
	
	public List<FeedVO> getMyFeedList(FeedPaginationVO params) {
		List<FeedVO> feedList = feedDAO.selectMyFeedList(params);
		return setCommentCntAndImageIdxs(feedList);
	}
	
	public int getFeedTotalCount() {
		return feedDAO.selectFeedTotalCount();
	}

	public int getMyFeedCnt(String challengeNum) {
		return feedDAO.selectMyFeedCount(challengeNum);
	}
	
	public List<UploadImageVO> getFeedImageList(Integer feedNo) {
		int feedImageTotalCnt = feedImageDAO.selectFeedImageTotalCount(feedNo);
		if(feedImageTotalCnt < 1) {
			return Collections.emptyList();
		}
		return feedImageDAO.selectFeedImageList(feedNo);
	}

	public File getFeedImageDetail(Integer idx) {
		UploadImageVO image = feedImageDAO.selectFeedImageDetail(idx);
		return fileUtils.getImagePath(image);
	}
	
	public List<FeedVO> setCommentCntAndImageIdxs(List<FeedVO> feedList) {
		for(FeedVO feed : feedList) {
			List<UploadImageVO> imageList = getFeedImageList(feed.getFeedNo());
			List<Integer> imageIdxs = new ArrayList<>();
			for(UploadImageVO image : imageList) {
				imageIdxs.add(image.getIdx());
			}
			FeedCommentVO comment = new FeedCommentVO();
			comment.setFeedNo(feed.getFeedNo());
			feed.setCommentCnt(feedCommentDAO.selectCommentTotalCount(comment));
			feed.setImageIdxs(imageIdxs);
		}
		return feedList;
	}
}
