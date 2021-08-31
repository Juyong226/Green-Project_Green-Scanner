package com.garb.gbcollector.web.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garb.gbcollector.web.dao.TemperatureDAO;
import com.garb.gbcollector.web.vo.TemperatureVO;

@Service
public class TemperatureService {

	@Autowired
	TemperatureDAO temperatureDAO;
	
	public List<TemperatureVO> showTemperature(){
		return temperatureDAO.showTemperature();
	}
}