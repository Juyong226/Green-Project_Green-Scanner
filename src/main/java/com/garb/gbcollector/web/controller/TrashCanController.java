package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
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

import com.garb.gbcollector.web.service.TrashCanService;
import com.garb.gbcollector.web.vo.TrashCanVO;
import com.garb.gbcollector.util.Log;

@Controller
public class TrashCanController {

	private Log log = new Log();
	@Autowired
	TrashCanService trashCanService;

	@RequestMapping(value = "showTrashCans.do", 
					method = {RequestMethod.POST }, 
					produces = "application/text; charset=utf8")
	@ResponseBody
	public String showTrashCan(HttpServletRequest request, HttpServletResponse response) {
		log.TraceLog("쓰레기통 위치 요청 들어옴");
		JSONObject resJson = new JSONObject();
		JSONArray jsonList = new JSONArray();
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		
		// trashCanList를 JsonArray로 변환하기
		String[] set = {"address", "latitude", "longitude"};
		List<TrashCanVO> trashCanList = trashCanService.showTrashCan();
		
		// trashCanList에서 VO객체를 한 개씩 꺼내어 이를 Map형태로 변환
		for(TrashCanVO t : trashCanList) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				map.put(set[0], t.getAddress());
				map.put(set[1], t.getLatitude());
				map.put(set[2], t.getLongitude());
			} catch (Exception e) {
				e.printStackTrace();
			}
			//미리 선언해 둔 listMap(Map을 요소로 갖는 List)에 각 Map객체를 저장
			listMap.add(map);
		}
		// listMap에서 Map 객체를 한 개씩 꺼내어 이를 미리 선언해 둔 jsonList(JsonArray)에 저장
		for(Map<String, Object> jsonItem : listMap) {
			jsonList.add(jsonItem);
		}
		// 미리 선언해 둔 JsonObject에 jsonList를 "trashCanList"라는 Key 값으로 저장하여 보낸다.
		resJson.put("trashCanList", jsonList);
		resJson.put("code", "OK");
		return resJson.toJSONString();

	}
}