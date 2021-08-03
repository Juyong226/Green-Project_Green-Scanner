package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.BasicChallengeVO;
import com.garb.gbcollector.web.vo.PersonalChallengeVO;

@Repository
public interface ChallengeDAO {

	List<BasicChallengeVO> selectBasicChallenge();

	List<PersonalChallengeVO> selectChallengeList(String email);
	
	BasicChallengeVO getBasicChallenge(String code);
	
	PersonalChallengeVO getPersonalChallenge(String challengeNum);

	int createChallenge(PersonalChallengeVO pc);

	int updateChallenge(PersonalChallengeVO pc);

	int deleteChallenge(String challengeNum);


}
