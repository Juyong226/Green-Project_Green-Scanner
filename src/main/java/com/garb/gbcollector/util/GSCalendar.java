package com.garb.gbcollector.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.garb.gbcollector.web.vo.PersonalChallengeVO;

public class GSCalendar {

	Calendar cal = Calendar.getInstance();
	
	private static final GSCalendar gsCalendar = new GSCalendar();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	// Constructor	
	private GSCalendar() {
		
	}
	
	
	// Method
	public static GSCalendar getInstance() {
		return gsCalendar;
	}
	
	public String getCurrentTime() {
		System.out.println("gsCalendar.getCurrentTime()");
		System.out.println(dateFormat.format(new Date()));
		return dateFormat.format(new Date());
	}
	
	public String getEndDate(String startDate, String period) throws GbcException {
		System.out.println("gsCalendar.getEndDate()");
		int stringToInt = Integer.parseInt(period);
		Date date;
		
		try {
			date = dateFormat.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new GbcException("ParseException!");
		}
		
		cal.setTime(date);
		cal.add(Calendar.DATE, stringToInt);
		System.out.println(dateFormat.format(cal.getTime()));
		
		return dateFormat.format(cal.getTime());
	}
	
	public List<ArrayList> compare(List<PersonalChallengeVO> list) throws GbcException {
		
		/* list에서 ChallengeListVO 객체를 하나씩 꺼낸다.
		 * 각 VO 객체에서 endDate 값을 꺼낸다.
		 * 꺼낸 endDate를 오늘 날짜와 비교한다.
		 * endDate가 오늘 이후이면 VO 객체의 isCompleted 값을 true로 바꾼다.
		 * isCompleted가 true인 VO 객체는 ArrayList<ChallengeListVO> completed에 추가한다.
		 * 아닌 객체는 ArrayList<ChallengeListVO> proceeding에 추가한다.
		 * proceeding과 completed를 각각 List<ArrayList> cList의 0번과 1번에 추가한다.
		 * */
		
		List<ArrayList> cList = new ArrayList();
		ArrayList<PersonalChallengeVO> proceeding = new ArrayList<PersonalChallengeVO>();
		ArrayList<PersonalChallengeVO> completed = new ArrayList<PersonalChallengeVO>();
		
		Date currentTime = new Date();
		Date endDate;
		
		String now = dateFormat.format(currentTime);
		Date toDay;
		
		try {
			toDay = dateFormat.parse(now);
		
			for(PersonalChallengeVO cl : list) {
				cl.calculateAchievementRate(cl.getPeriod(), cl.getExecutionNum());
				String temp = cl.getEndDate();
				endDate = dateFormat.parse(temp);
				
				if(toDay.equals(endDate) || toDay.after(endDate)) {
					cl.setCompleted("1");
					completed.add(cl);
				} else {
					proceeding.add(cl);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new GbcException("ParseException!");
		}
		cList.add(0, proceeding);
		cList.add(1, completed);
		
		return cList;
	}
	
	public List<ArrayList> compare(List<PersonalChallengeVO> proceeding, 
								List<PersonalChallengeVO> completed) throws GbcException {
		
		List<ArrayList> cList = new ArrayList();
		
		Date currentTime = new Date();
		Date endDate;
		
		String now = dateFormat.format(currentTime);
		Date toDay;
		
		try {
			toDay = dateFormat.parse(now);
		
			for(PersonalChallengeVO cl : proceeding) {
				cl.calculateAchievementRate(cl.getPeriod(), cl.getExecutionNum());
				String temp = cl.getEndDate();
				endDate = dateFormat.parse(temp);
				
				if(toDay.equals(endDate) || toDay.after(endDate)) {
					cl.setCompleted("1");
					completed.add(cl);
					proceeding.remove(cl);
				} 
			}
			cList.add(0, (ArrayList<PersonalChallengeVO>) proceeding);
			cList.add(1, (ArrayList<PersonalChallengeVO>) completed);
			
		} catch (ParseException e) {
			e.printStackTrace();
			throw new GbcException("ParseException!");
		}
		return cList;
	}

}

