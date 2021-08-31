package com.garb.gbcollector;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garb.gbcollector.web.service.FeedService;
import com.garb.gbcollector.web.vo.FeedVO;

@SpringBootTest
public class FeedServiceTest {

	@Autowired
	private FeedService feedService;
	
	@Test
	public void testOfRegister() {
		FeedVO params1 = new FeedVO();
		params1.setChallengeNum("f47b8b7290a");
		params1.setEmail("song2@naver.com");
		params1.setContent("FeedService 등록 테스트 내용입니다.");
		params1.setPicURL("FeedService 등록 테스트 사진 url입니다.");
		params1.setPostDate("2021/08/18");
		params1.setWriter("FeedService regi");
		
		FeedVO params2 = new FeedVO();
		params2.setFeedNo(4);
		params2.setWriter("FeedService edit");
		params2.setContent("FeedService 수정 테스트 내용입니다.");
		params2.setPicURL("FeedService 수정 테스트 사진 url입니다.");
		
		System.out.println("========================================");
		System.out.println(params1.toString());
		System.out.println(params2.toString());
		System.out.println("========================================");
		
		boolean result1 = feedService.registerFeed(params1);
		boolean result2 = feedService.registerFeed(params2);
		
		if(result1 && result2) {
			System.out.println("========================================");
			System.out.println("등록/수정 모두 성공");
			System.out.println("새 피드 등록 결과 = " + result1);
			System.out.println("기존 피드 수정 결과 = " + result2);
			System.out.println("========================================");
		} else if(result1 ^ result2) {
			System.out.println("========================================");
			System.out.println("등록/수정 중 하나만 성공");
			System.out.println("새 피드 등록 결과 = " + result1);
			System.out.println("기존 피드 수정 결과 = " + result2);
			System.out.println("========================================");
		} else if(!result1 && !result2) {
			System.out.println("========================================");
			System.out.println("등록/수정 모두 실패");
			System.out.println("새 피드 등록 결과 = " + result1);
			System.out.println("기존 피드 수정 결과 = " + result2);
			System.out.println("========================================");
		}
		
	}
	
	@Test
	public void testOfFeedDetail() {
		FeedVO feed = feedService.getFeedDetail(3);
		try {
			String feedJson = new ObjectMapper().writeValueAsString(feed);
			
			System.out.println("==========================================");
			System.out.println(feedJson);
			//System.out.println(feed.toString());
			System.out.println("==========================================");
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfDeleteFeed() {
		boolean result = feedService.deleteFeed(4);
		try {
			if(result) {
				FeedVO feed = feedService.getFeedDetail(4);
				
				String feedJson = new ObjectMapper().writeValueAsString(feed);
				
				System.out.println("==========================================");
				System.out.println("피드 삭제 성공");
				System.out.println(feedJson);
				//System.out.println(feed.toString());
				System.out.println("==========================================");
							
			} else {
				FeedVO feed = feedService.getFeedDetail(4);
				
				String feedJson = new ObjectMapper().writeValueAsString(feed);
				
				System.out.println("==========================================");
				System.out.println("피드 삭제 실패");
				System.out.println(feedJson);
				//System.out.println(feed.toString());
				System.out.println("==========================================");
				
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfGetAllFeedList() {
		List<FeedVO> feedList = feedService.getAllFeedList();
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
	public void testOfGetMyFeedList() {
		List<FeedVO> feedList = feedService.getMyFeedList("f47b8b7290a");
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
}
