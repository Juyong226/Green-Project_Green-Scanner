package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.FeedImageVO;

@Repository
public interface FeedImageDAO {

	public int insertFeedImage(List<FeedImageVO> imgList);
	
	public int deleteFeedImage(Integer feedNo);
	
	public List<FeedImageVO> selectFeedImageList(Integer feedNo);
	
	public int selectFeedImageTotalCount(Integer feedNo);
}
