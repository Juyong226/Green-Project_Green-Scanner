package com.garb.gbcollector.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.GarbageDAO;
import com.garb.gbcollector.web.vo.GarbageVO;

@Service
public class GarbageService {
	
	@Autowired
	GarbageDAO garbageDAO;
	
	public String selectGarbage(GarbageVO garbageVO) {
		return garbageDAO.selectGarbage(garbageVO);
	}
	
	public List<GarbageVO> selectGarbageList(){
		return garbageDAO.selectGarbageList();
	}
}
