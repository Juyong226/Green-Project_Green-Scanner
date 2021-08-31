package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.web.service.TemperatureService;
import com.garb.gbcollector.web.vo.TemperatureVO;
import com.garb.gbcollector.web.vo.TrashCanVO;


@Controller
public class TemperatureController {

	@Autowired
	TemperatureService temperatureService;

	@RequestMapping(value = "showTemperature.do", 
					method = {RequestMethod.POST }, 
					produces = "application/text; charset=utf8")
	
	@ResponseBody
	public String showTemperature(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("날짜 요청 들어옴");
		String tdate = request.getParameter("tdate");
		
		JSONObject json = new JSONObject();
		JSONArray jsonList = new JSONArray();
	
		
		
		//Temperature 테이블 리스트로 받아오기
		List<TemperatureVO> temperatureList = temperatureService.showTemperature();
		//리스트를 배열로 변환
		TemperatureVO[] temperatureArray = temperatureList.toArray(new TemperatureVO[temperatureList.size()]);
		ArrayList<String> todayList = new ArrayList<String>();
		
		String td_date = "";
		String td_dm1 = "";
		String td_dm2 = "";
		// 배열안에서 tdate와 today 비교
		for (int i=0; i<temperatureArray.length; i++) {
			if(tdate.indexOf(temperatureArray[i].getToday())!=-1) {
				if(td_date.length() < temperatureArray[i].getToday().length()) {
					if(td_date.compareTo("")!=0) {
						todayList.add(td_date);
					}
					td_date = temperatureArray[i].getToday();
					td_dm1 = temperatureArray[i].getHighest(); 
					td_dm2= temperatureArray[i].getLowest(); 
					System.out.println(td_date);
					
				}
				
			}
		}
		
		if(td_date.compareTo("")!=0) {
			json.put("최저기온", td_dm2);
			json.put("최고기온", td_dm1);
			json.put("Date",td_date);
			if(!todayList.isEmpty()) {
				for(String t : todayList) {
					jsonList.add(t);
				}
				json.put("temperatureList", jsonList);
				
			}
		
		}
		
		
		return json.toJSONString();
		
		
		
	}
	
	
	
	
}
