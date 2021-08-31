package com.garb.gbcollector;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garb.gbcollector.web.dao.FeedDAO;
import com.garb.gbcollector.web.vo.FeedVO;

@SpringBootTest
public class FeedDAOTest {
	
	@Autowired
	private FeedDAO feedDAO;
	
	@Test
	public void testOfInsert() {
		FeedVO params = new FeedVO();
		params.setChallengeNum("a9695a37d1b");
		params.setEmail("song2@naver.com");
		params.setPostDate("2021/08/20");
		params.setWriter("테스터");
		params.setContent("테스트 게시글 내용");
		params.setPicURL("테스트 게시글 사진 URL");
		
		int result = feedDAO.insertFeed(params);
		System.out.println("결과는 " + result + "입니다.");
	}
	
	@Test
	public void testOfSelectDetail() {
		FeedVO feed = feedDAO.selectFeedDetail(2);
		try {
			String feedJson = new ObjectMapper().writeValueAsString(feed);
			
			System.out.println("==========================================");
			System.out.println(feedJson);
			System.out.println(feed.toString());
			System.out.println("==========================================");
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	@Test
	public void testOfUpdate() {
		FeedVO params = new FeedVO();
		params.setFeedNo(2);
		params.setWriter("테스터");
		params.setContent("테스트 게시글 내용을 수정합니다");
		params.setPicURL("테스트 게시글 사진 URL을 수정합니다");
		
		int result = feedDAO.updateFeed(params);
		if(result == 1) {
			FeedVO feed = feedDAO.selectFeedDetail(2);
			
			System.out.println("==========================================");
			System.out.println(feed.toString());
			System.out.println("==========================================");
		}
	}
	
	@Test
	public void testOfDelete() {
		int result = feedDAO.deleteFeed(3);
		if(result == 1) {
			FeedVO feed = feedDAO.selectFeedDetail(3);
			try {
				String feedJson = new ObjectMapper().writeValueAsString(feed);
				
				System.out.println("==========================================");
				System.out.println(feedJson);
				System.out.println("==========================================");
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testOfSelectAllFeedList() {
		List<FeedVO> feedList = feedDAO.selectAllFeedList();
		if(feedList.isEmpty() == false) {
			for(FeedVO feed : feedList) {
				System.out.println("==========================================");
				System.out.println(feed.getFeedNo());
				System.out.println(feed.getWriter());
				System.out.println(feed.getContent());
				System.out.println("==========================================");
			}
		} else {
			System.out.println("등록된 피드가 없습니다.");
		}
	}
	
	@Test
	public void testOfSelectMyFeedList() {
		List<FeedVO> feedList = feedDAO.selectMyFeedList("a9695a37d1b");
		if(feedList.isEmpty() == false) {
			for(FeedVO feed : feedList) {
				System.out.println("==========================================");
				System.out.println(feed.getFeedNo());
				System.out.println(feed.getWriter());
				System.out.println(feed.getContent());
				System.out.println("==========================================");
			}
		} else {
			System.out.println("이 챌린지에 등록된 피드가 없습니다.");
		}
	}
}
