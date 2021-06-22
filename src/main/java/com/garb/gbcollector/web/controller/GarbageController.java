package com.garb.gbcollector.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.garb.gbcollector.web.service.GarbageService;
import com.garb.gbcollector.web.vo.GarbageVO;

@Controller
public class GarbageController {
	
	@Autowired
	GarbageService garbageService;
	
	@RequestMapping(value="selectGarbageList.do",
			method= {RequestMethod.POST},
			produces="application/text; charset=utf8")
	
	@ResponseBody
	public String selectGarbageList(HttpServletRequest request, HttpServletResponse response) {
		
		//fulltext: 화면에 입력 또는 음성인식을 통해 입력된 텍스트
		String fulltext = request.getParameter("fulltext");
		String fulltext2 = fulltext.replaceAll(" ", "");
		JSONObject json = new JSONObject();
		JSONArray jsonList = new JSONArray();
		
		//Garbage테이블을 리스트로 받아오기
		List<GarbageVO> garbageList = garbageService.selectGarbageList();
		
		//리스트를 	배열로 변환 
		GarbageVO[] garbageArray = garbageList.toArray(new GarbageVO[garbageList.size()]);
		
		//시간 측정
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		//쓰레기 리스트 저장
		ArrayList<String> dmList = new ArrayList<String>();
		
		//가장 적합한 쓰레기 찾기
		String gb_lmx_name = "";
		String gb_lmx_dm = "";
		
		//배열 안에서 fulltext와 gabagename 비교 
		for (int i=0; i<garbageArray.length; i++) {
			if(fulltext2.indexOf(garbageArray[i].getGarbagename())!=-1) {
				if(gb_lmx_name.length() < garbageArray[i].getGarbagename().length()) {
					if(gb_lmx_name.compareTo("")!=0) {
						dmList.add(gb_lmx_name);
					}
					gb_lmx_name = garbageArray[i].getGarbagename();
					gb_lmx_dm = garbageArray[i].getGarbagedm();
				}
				else {
					dmList.add(garbageArray[i].getGarbagename());
				}
			}
		}
		//파이썬에서 알수없음으로 넘어왔을때
		if(gb_lmx_name.compareTo("알수없음")==0) {
			json.put("msg", "일치하는 데이터가 없습니다");
		}
		else if(gb_lmx_name.compareTo("")!=0) {
			json.put("garbagefound", gb_lmx_name);
			json.put("garbagedmfound", gb_lmx_dm);
			if(!dmList.isEmpty()) {
				for(String s : dmList) {
					jsonList.add(s);
				}
				json.put("garbageList", jsonList);
			}
		}
		else {
			json.put("msg", "일치하는 데이터가 없습니다");
		}
		
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
		return json.toJSONString();
		
		
		/*
		if(flag!=1) {
			System.out.println("일치 텍스트 없음");
		}
		//garbagedmfound: 화면에 출력 or tts로 음성합성에 사용할 분리배출정보 데이터 
		
		return garbagedmfound;
		*/
	}
	
	
	
	/*
	 * @RequestMapping(value="selectGarbage.heyrin", method= {RequestMethod.POST},
	 * produces="application/text; charset=utf8")
	 * 
	 * @ResponseBody public String selectGarbage(HttpServletRequest request,
	 * HttpServletResponse response) { String garbagename =
	 * request.getParameter("garbagename");
	 * 
	 * try { GarbageVO garbageVO=new GarbageVO(garbagename); String
	 * garbagedm=garbageService.selectGarbage(garbageVO); if(garbagedm!=null) {
	 * HttpSession session = request.getSession(); session.setAttribute("garbage",
	 * garbageVO); return garbagedm; }else { return "조회 실패"; }
	 * 
	 * }catch(Exception e) { return e.getMessage(); }
	 * 
	 * }
	 */
}
