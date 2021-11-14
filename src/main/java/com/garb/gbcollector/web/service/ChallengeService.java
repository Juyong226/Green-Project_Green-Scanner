package com.garb.gbcollector.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.util.GSCalendar;
import com.garb.gbcollector.util.GbcException;
import com.garb.gbcollector.web.dao.ChallengeDAO;
import com.garb.gbcollector.web.vo.BasicChallengeVO;
import com.garb.gbcollector.web.vo.FeedVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;
import com.garb.gbcollector.web.vo.RequestInforVO;
import com.garb.gbcollector.util.Log;

@Service
public class ChallengeService {

	private Log log = new Log();
	@Autowired
	ChallengeDAO challengeDAO;
	
	GSCalendar gsCalendar = GSCalendar.getInstance();

	public List<BasicChallengeVO> selectBasicChallenge() {
		return challengeDAO.selectBasicChallenge();
	}

	public List<PersonalChallengeVO> selectChallengeList(String email) {
		return challengeDAO.selectChallengeList(email);
	}
	
	public String duplicateCheck(String code, String email) {
		return challengeDAO.duplicateCheck(code, email);
	}
	
	public BasicChallengeVO getBasicChallenge(String code) {
		return challengeDAO.getBasicChallenge(code);
	}
	
	public PersonalChallengeVO getPersonalChallenge(String challengeNum) {
		PersonalChallengeVO pc = challengeDAO.getPersonalChallenge(challengeNum);
		pc.setCalList(gsCalendar.calendarToList(pc.getCalendar()));
		pc.calculateAchievementRate();
		return pc;
	}
	
	public int createChallenge(PersonalChallengeVO pc) {
		return challengeDAO.createChallenge(pc);
	}
	
	public boolean periodCheck(String ChallengeNum, String newPeriod) {
		PersonalChallengeVO pc = getPersonalChallenge(ChallengeNum);
		return gsCalendar.checkPeriod(pc, newPeriod);		
	}
	
	public int updateChallenge(PersonalChallengeVO pc, String newPeriod) throws GbcException {
		pc.setCalendar(gsCalendar.modifyCalendar(pc.getPeriod(), newPeriod, pc.getCalendar()));
		pc.setEndDate(getEndDate(pc.getStartDate(), newPeriod));
		pc.setPeriod(newPeriod);
		pc.calculateAchievementRate();
		return challengeDAO.updateChallengeVO(pc);
	}
	
	public int updateChallenge(List<PersonalChallengeVO> list) {
		return challengeDAO.updateChallengeList(list);
	}
	
	public int deleteChallenge(String challengeNum) {
		return challengeDAO.deleteChallenge(challengeNum);
	}
	public String getCurrentTime() {
		log.TraceLog("challengeService.getCurrentTime()");
		return gsCalendar.getCurrentTime();
	}
	
	public String getEndDate(String startDate, String period) throws GbcException {
		log.TraceLog("challengeService.getEndDate()");
		return gsCalendar.getEndDate(startDate, period);
	}

	public List<ArrayList<PersonalChallengeVO>> isCompleted(List<PersonalChallengeVO> list, RequestInforVO infor) throws GbcException {
		List<ArrayList<PersonalChallengeVO>> cList = gsCalendar.compare(list);
		int result1 = 0; 
		int result2 = 0;
		boolean proceeding = cList.get(0).isEmpty();
		boolean completed = cList.get(1).isEmpty();
		
		if( !proceeding && !completed ) {
			result1 = updateChallenge(cList.get(0));
			result2 = updateChallenge(cList.get(1));
			
		} else if ( proceeding && !completed ) {
			result2 = updateChallenge(cList.get(1));
			
		} else if ( !proceeding && completed ) {
			result1 = updateChallenge(cList.get(0));
		}
		return cList;
	}
	
	public List<ArrayList<PersonalChallengeVO>> isCompleted(List<PersonalChallengeVO> proceeding, List<PersonalChallengeVO> completed) throws GbcException {
		List<ArrayList<PersonalChallengeVO>> cList = gsCalendar.compare(proceeding, completed);
		return cList;
	}

	public String createCalendar(String period) {
		return gsCalendar.createCalendar(period);
	}
}
