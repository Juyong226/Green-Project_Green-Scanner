package com.garb.gbcollector.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.garb.gbcollector.web.vo.TemperatureVO;

@Repository
public interface TemperatureDAO {
	List<TemperatureVO> showTemperature();
}
