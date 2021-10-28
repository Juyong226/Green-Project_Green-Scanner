package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.UploadImageVO;

@Repository
public interface FeedImageDAO {

	public int insertFeedImage(List<UploadImageVO> imgList);
	
	public UploadImageVO selectFeedImageDetail(Integer idx);
	
	public int deleteFeedImage(Integer feedNo);
	
	public int undeleteFeedImage(List<Integer> imgIdxs);
	
	public List<UploadImageVO> selectFeedImageList(Integer feedNo);
	
	public int selectFeedImageTotalCount(Integer feedNo);
	
}
