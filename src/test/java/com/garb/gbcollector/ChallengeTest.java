package com.garb.gbcollector;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.garb.gbcollector.web.service.ChallengeService;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

import com.garb.gbcollector.util.Log;

@SpringBootTest
public class ChallengeTest {

	private Log log = new Log();
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
				log.TraceLog("============================================");
				log.TraceLog(pc.toString());
				log.TraceLog("============================================");
			} else {
				log.TraceLog("============================================");
				log.TraceLog("챌린지 등록 실패");
				log.TraceLog("============================================");
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
				log.TraceLog("============================================");
				log.TraceLog("수정 실패");
				log.TraceLog(pc.toString());
				log.TraceLog("============================================");
			} else {
				PersonalChallengeVO pc = challengeService.getPersonalChallenge(params.getChallengeNum());
				log.TraceLog("============================================");
				log.TraceLog("수정 성공");
				log.TraceLog(pc.toString());
				log.TraceLog("============================================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
