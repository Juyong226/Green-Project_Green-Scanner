package com.garb.gbcollector.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
				String temp = cl.getEndDate();
				endDate = dateFormat.parse(temp);
				
				if(toDay.equals(endDate) || toDay.after(endDate)) {
					cl.setIsCompleted(1);
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
				String temp = cl.getEndDate();
				endDate = dateFormat.parse(temp);
				
				if(toDay.equals(endDate) || toDay.after(endDate)) {
					cl.setIsCompleted(1);
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


	public boolean checkPeriod(PersonalChallengeVO pc, String newPeriod) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate endDate = LocalDate.parse(pc.getEndDate(), format);
		LocalDate toDay = LocalDate.now();
		int op = Integer.parseInt(pc.getPeriod());
		int np = Integer.parseInt(newPeriod);
		
		int daysRemained = between2Dates(endDate, toDay);
		int daysChanged = op - np;
		
		if(daysRemained >= daysChanged) {
			return true;
			
		} else {
			return false;
			
		}
				
	}
	
	/* 
	* 챌린지를 새로 생성할 때 실행
	* 챌린지 기간 일수 만큼 0으로 문자열을 생성
	*/
	public String createCalendar(String period) {
	    int p = Integer.parseInt(period);
	    String calendar = "";
	    for(int i = 0; i<p; i++) {
	        calendar += "0";
	    }
	    return calendar;
	}
	
	/*
	* 챌린지를 수정할 때 실행 (challengeController의 챌린시 수정 메서드(update) - challengeService.updateChallenge(VO))
	* 수정된 기간 만큼 calendar 문자열의 0을 뒤에서부터 가감
	* 기간이 1주일 늘었다면 calendar 문자열에 0을 7개 더하고
	* 기간이 2주일 줄었다면 calendar 문자열에서 0을 14개 빼고 난 뒤 DB에 저장
	*/
	public String modifyCalendar(String oldPeriod, String newPeriod, String calendar) {
	    int op = Integer.parseInt(oldPeriod);
	    int np = Integer.parseInt(newPeriod);
	    int periodDifference = np - op; // 수정한 기간이 늘어나면 (+), 수정한 기간이 줄었으면 (-)
	    
	    String adjustedCal = null;
	    
	    if(periodDifference < 0) { // 수정한 기간이 줄었으면
	        int length = calendar.length();
	        int targetIndex = length + periodDifference;
	        adjustedCal = calendar.substring(0, targetIndex); // index 0부터 targetIndex-1까지의 string을 추출
	        
	    } else if(periodDifference == 0) { // 수정한 기간에 변동이 없으면
	        adjustedCal = calendar;
	        
	    } else if(periodDifference > 0) { // 수정한 기간이 늘었으면
	        adjustedCal = calendar;
	      
	        for(int i=0; i<periodDifference; i++) {
	            adjustedCal += "0";
	        }
	        
	    }
	    return adjustedCal;
	}
	
	/* 
	* 또는 피드를 새로 생성하거나, 삭제할 때 실행(FeedService에서 피드 생성 DAO 메서드 실행 전, 피드 삭제 DAO 메서드 실행 전)
	* 생성되거나 삭제되는 피드의 postDate와 해당 챌린지의 startDate를 받는다
	* postDate와 startDate 사이의 일수 차이를 java.time.LocalDate의 compareTo()를 통해 계산한다
	* 두 날짜의 차이 수와 같은 calendar 문자열 index 위치의 값을 찾는다
	* 이 때 그 값이 0이면 1로(피드 생성 시), 1이면 0으로(피드 삭제 시) 바꾼다
	*/
	public String updateCalander(String startDate, String postDate, String calendar) {
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	    LocalDate start = LocalDate.parse(startDate, format);
	    LocalDate post = LocalDate.parse(postDate, format);
	    
	    int targetIndex = between2Dates(post, start); // postDate은 언제나 startDate과 같거나 나중이기 때문에 targetIndex는 항상 양수
	    
	    StringBuilder adjustedCal = new StringBuilder(calendar);
	    if(adjustedCal.charAt(targetIndex) == '0') {
	        adjustedCal.setCharAt(targetIndex, '1');
	    } else {
	        adjustedCal.setCharAt(targetIndex, '0');
	    }
	    return adjustedCal.toString();
	}
	
	/*
	* challenge/my-challenge-detail.html에서 진행 중인 개별 챌린지를 조회할 때 실행
	* challengeService의 getPersonnalChallenge(String challengeNum) 메소드에서 DAO의 리턴 객체를 받아서 실행
	* DTO에 리턴된 calList를 같이 담아서 challengeController로 리턴
	*/
	public List<ArrayList<Character>> calendarToList(String calendar) {
	    List<ArrayList<Character>> calList = new ArrayList<ArrayList<Character>>();
	    ArrayList<Character> weekArr;
	    
	    int length = calendar.length(); // length는 7, 14, 21, 28 중 하나
	    int week = length / 7; // length를 7로 나눈 몫은 1, 2, 3, 4 중 하나
	    
	    for(int w=0; w<week; w++) {
	        weekArr = new ArrayList<Character>();
	        for(int i=w*7; i<w*7+7; i++) {
	            weekArr.add(calendar.charAt(i));
	        }
	    	calList.add(weekArr);
	    }
	    return calList;
	} 

	/*
	 * date = 비교하는 두 날짜 중 더 이후의 날
	 * otherDate = 비교하는 두 날짜 중 더 이전의 날
	 * 두 날짜 사이의 일수를 구하는 함수
	 * */
	public int between2Dates(LocalDate date, LocalDate otherDate) {
		int cmp = (date.getYear() - otherDate.getYear());
		if (cmp == 0) {
			cmp = (date.getMonthValue() - otherDate.getMonthValue());
			if (cmp == 0) {
				cmp = (date.getDayOfMonth() - otherDate.getDayOfMonth()); 
			} else {
				cmp = ((otherDate.lengthOfMonth() - otherDate.getDayOfMonth()) + date.getDayOfMonth());
			}
		} else {
			cmp = ((otherDate.lengthOfYear() - otherDate.getDayOfYear()) + date.getDayOfYear());
		}
		return cmp;
	}

}

