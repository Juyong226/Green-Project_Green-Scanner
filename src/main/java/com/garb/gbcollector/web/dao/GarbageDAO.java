package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.GarbageVO;


@Repository
public interface GarbageDAO {
	public String selectGarbage(GarbageVO garbageVO);
	public List<GarbageVO> selectGarbageList();
}
