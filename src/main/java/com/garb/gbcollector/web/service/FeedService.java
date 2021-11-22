package com.garb.gbcollector.web.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.garb.gbcollector.constant.LogDescription;
import com.garb.gbcollector.util.BuildDescription;
import com.garb.gbcollector.util.FileUtils;
import com.garb.gbcollector.util.GSCalendar;
import com.garb.gbcollector.util.UploadFileException;
import com.garb.gbcollector.util.Log;
import com.garb.gbcollector.web.dao.ChallengeDAO;
import com.garb.gbcollector.web.dao.FeedCommentDAO;
import com.garb.gbcollector.web.dao.FeedDAO;
import com.garb.gbcollector.web.dao.FeedImageDAO;
import com.garb.gbcollector.web.vo.FeedCommentVO;
import com.garb.gbcollector.web.vo.FeedPaginationVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;
import com.garb.gbcollector.web.vo.RequestInforVO;
import com.garb.gbcollector.web.vo.UploadImageVO;

@Service
public class FeedService {

	private Log log = new Log();
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
	
	public boolean registerFeed(FeedVO params, String challengeNum, RequestInforVO infor) {
		int queryResult = 0;
		if(params.getFeedNo() == 0) {
			log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_NEWFEED));
			if(duplicateCheck(challengeNum) == true) {
				queryResult = feedDAO.insertFeed(params);
				if(queryResult == 1) {
					PersonalChallengeVO pc = challengeDAO.getPersonalChallenge(params.getChallengeNum());
					pc.setCalendar(gsCalendar.updateCalander(pc.getStartDate(), params.getPostDate(), pc.getCalendar()));
					pc.setExecutionNum(pc.getExecutionNum() + 1);
					pc.calculateAchievementRate();
					queryResult = challengeDAO.updateChallengeVO(pc);
				} else {
					log.TraceLog(infor, "queryResult: " + queryResult + " / 새 피드 등록 실패");
				}
			}
		} else {
			log.TraceLog(infor, BuildDescription.get(LogDescription.REQUEST_UPDATE_FEED, Integer.toString(params.getFeedNo())));
			queryResult = feedDAO.updateFeed(params);
			if(queryResult == 1) {
				if("Y".equals(params.getChangeYn())) {
					feedImageDAO.deleteFeedImage(params.getFeedNo());
					if(params.getImageIdxs() != null && params.getImageIdxs().isEmpty() == false) {
						feedImageDAO.undeleteFeedImage(params.getImageIdxs());
					}
				}
			} else {
				log.TraceLog(infor, "queryResult: " + queryResult + " / 피드 수정 실패");
			}
		}
		return (queryResult == 1) ? true : false;
	}
	
	public boolean registerFeed(FeedVO params, String challengeNum, MultipartFile[] images, RequestInforVO infor) throws UploadFileException {
		int queryResult = 1;
		if(registerFeed(params, challengeNum, infor) == false) {
			return false;
		}
		log.TraceLog(infor, "피드 내용 등록/수정 성공");
		List<UploadImageVO> uploadList = fileUtils.uploadFeedImages(images, params.getFeedNo(), infor);
		if(uploadList.isEmpty() == false) {
			queryResult = feedImageDAO.insertFeedImage(uploadList);
			if(queryResult != -1) {
				log.TraceLog(infor, "피드 이미지 등록 실패");
				queryResult = 0;
			} else { 
				log.TraceLog(infor, "피드 이미지 등록 성공"); 
			}
		} 
		return (queryResult == -1 || queryResult == 1);
	}
	
	public FeedVO getFeedDetail(int feedNo) {
		FeedVO feed = feedDAO.selectFeedDetail(feedNo);
		feed.setImageIdxs(getImageIdxs(feed));
		return feed;
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
	
	public List<Integer> getImageIdxs(FeedVO feed) {
		List<Integer> imageIdxs = new ArrayList<>();
		List<UploadImageVO> imageList = getFeedImageList(feed.getFeedNo());
		for(UploadImageVO image : imageList) {
			imageIdxs.add(image.getIdx());
		}
		return imageIdxs;
	}
	
	public List<FeedVO> setCommentCntAndImageIdxs(List<FeedVO> feedList) {
		for(FeedVO feed : feedList) {
			FeedCommentVO comment = new FeedCommentVO();
			comment.setFeedNo(feed.getFeedNo());
			feed.setCommentCnt(feedCommentDAO.selectCommentTotalCount(comment));
			feed.setImageIdxs(getImageIdxs(feed));
		}
		return feedList;
	}
}
