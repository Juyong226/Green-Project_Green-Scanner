package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.FeedPaginationVO;
import com.garb.gbcollector.web.vo.FeedVO;

@Repository
public interface FeedDAO {
	
	List<FeedVO> selectAllFeedList(FeedPaginationVO params);
	
	List<FeedVO> selectMyFeedList(FeedPaginationVO params);
	
	FeedVO selectFeedDetail(int feedNo);
	
	List<FeedVO> duplicateCheck(String challengeNum);

	int insertFeed(FeedVO params);
	
	int updateFeed(FeedVO params);
	
	int deleteFeed(int feedNo);
	
	int selectFeedTotalCount();

	int selectMyFeedCount(String challengeNum);

}
