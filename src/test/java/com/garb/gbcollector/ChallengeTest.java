package com.garb.gbcollector;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

@SpringBootTest
public class ChallengeTest {
	
	@Autowired
	ChallengeService challengeService;
	
	@Test
	public void testOfRegister() {
		PersonalChallengeVO params = new PersonalChallengeVO();
		try {
			params.setChallengeCode("C002");
			params.setChallengeName("테스트2");
			params.setChallengeNum(UUID.randomUUID().toString().replace("-", "").substring(0, 11));
			params.setEmail("song2@naver.com");
			params.setPeriod("14");
			params.setColorCode("#FF0000");
			params.setThumbnailURL("C002의 썸네일 url");
			params.setStartDate(challengeService.getCurrentTime());
			params.setEndDate(challengeService.getEndDate(params.getStartDate(), params.getPeriod()));
			params.setCalendar(challengeService.createCalendar(params.getPeriod()));
			
			int result = challengeService.createChallenge(params);
			if(result == 1) {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(params.getChallengeNum());
				System.out.println("============================================");
				System.out.println(pc.toString());
				System.out.println("============================================");
			} else {
				System.out.println("============================================");
				System.out.println("챌린지 등록 실패");
				System.out.println("============================================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfUpdate() {
		PersonalChallengeVO params = challengeService.getPersonalChallenge("3baa262c0bd");
		String newPeriod = "7";
		try {
			int result = challengeService.updateChallenge(params, newPeriod);
			if(result == 100) {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(params.getChallengeNum());
				System.out.println("============================================");
				System.out.println("수정 실패");
				System.out.println(pc.toString());
				System.out.println("============================================");
			} else {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(params.getChallengeNum());
				System.out.println("============================================");
				System.out.println("수정 성공");
				System.out.println(pc.toString());
				System.out.println("============================================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
