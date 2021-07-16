package com.garb.gbcollector.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.util.GSCalendar;
import com.garb.gbcollector.util.GbcException;
import com.garb.gbcollector.web.dao.ChallengeDAO;
import com.garb.gbcollector.web.vo.BasicChallengeVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

@Service
public class ChallengeService {

	@Autowired
	ChallengeDAO challengeDAO;
	
	GSCalendar gsCalendar = GSCalendar.getInstance();

	public List<BasicChallengeVO> selectBasicChallenge() {
		return challengeDAO.selectBasicChallenge();
	}

	public List<PersonalChallengeVO> selectChallengeList() {
		return challengeDAO.selectChallengeList();
	}
	
	public void createChallenge(PersonalChallengeVO pc) {
		challengeDAO.createChallenge(pc);
	}
	
	public String getCurrentTime() {		
		return gsCalendar.getCurrentTime();
	}
	
	public String getEndDate(String startDate, String period) throws GbcException {
		return gsCalendar.getEndDate(startDate, period);
	}

	public List<ArrayList> isCompleted(List<PersonalChallengeVO> list) throws GbcException {
		List<ArrayList> cList = gsCalendar.compare(list);
		
		return cList;
	}
	
	public List<ArrayList> isCompleted(List<PersonalChallengeVO> proceeding, List<PersonalChallengeVO> completed) throws GbcException {
		List<ArrayList> cList = gsCalendar.compare(proceeding, completed);

		return cList;
	}

}
