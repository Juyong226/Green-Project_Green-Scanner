package com.garb.gbcollector.constant;

public interface LogDescription {
	
	/*
	 * 회원 관련 LOG DESC
	 * */
	
	public static final String ACCESS_LOGINPAGE = "%s 님이 로그인 페이지 접속";
	
	public static final String REQUEST_LOGIN = "%s 님이 로그인 요청";
	
	public static final String LOGIN = "%s 님이 로그인";
	
	public static final String ACCESS_JOINPAGE = "%s 님이 회원가입 페이지 접속";
	
	public static final String REQUEST_JOIN = "%s 님이 회원가입 요청";

	public static final String JOIN = "%s 님이 회원가입 성공";
	
	/*
	 * 챌린지 관련 LOG DESC
	 * */
	
	public static final String ACCESS_CHALLMAIN = "%s 님이 챌린지 페이지 접속";
	
	public static final String REQUEST_NEWCHALL = "%1$s 님이 새 챌린지( CHALLENGECODE: %2$s ) 시작 요청";
	
	public static final String SUCCESS_NEWCHALL = "%1$s 님의 새 챌린지( CHALLENGECODE: %2$s ) 시작 성공( CHALLENGENUM: %3$s )";
	
	public static final String FAIL_NEWCHALL = "%1$s 님의 새 챌린지( CHALLENGECODE: %2$s ) 시작 실패";
	
	public static final String ACCESS_MYCHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 상세 페이지 접속";
	
	public static final String REQUEST_UPDATE_CHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 수정 요청";
	
	public static final String SUCCESS_UPDATE_CHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 수정 성공";
	
	public static final String FAIL_UPDATE_CHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 수정 실패";
	
	public static final String REQUEST_NEWFEED = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 피드 등록 요청";
	
	public static final String SUCCESS_NEWFEED = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 피드 등록 성공( FEEDNO: %3$s )";
	
	public static final String FAIL_NEWFEED = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 피드 등록 실패";
	
	public static final String ACCESS_FEED = "%1$s 님이 피드( FEEDNO: %2$s ) 상세 페이지 접속";
	
	public static final String REQUEST_CHALL_COMMENT = "%1$s 님이 피드( FEEDNO: %2$s ) 댓글 등록 요청";
	
	public static final String SUCCESS_CHALL_COMMENT = "%1$s 님이 피드( FEEDNO: %2$s ) 댓글 등록 성공 ( COMMENTIDX: %3$s )";
	
	public static final String FAIL_CHALL_COMMENT = "%1$s 님이 피드( FEEDNO: %2$s ) 댓글 등록 실패";
	
	/*
	 * 게시판 관련 LOG DESC
	 * */
	
	public static final String ACCESS_BOARDMAIN = "%s 님이 게시판 메인 페이지 접속";
	
	public static final String ACCESS_BULLETIN = "%s 님이 자유게시판 페이지 접속";
	
	public static final String ACCESS_QUESTION = "%s 님이 질문게시판 페이지 접속";
	
	public static final String REQUEST_POST = "%s 님이 새 게시글 등록 요청";
	
	public static final String ACCESS_POST = "%1$s 님이 게시글( POSTNO: %2$s ) 상세 페이지 접속";
	
	public static final String REQUEST_UPDATE_POST = "%1$s 님이 게시글( POSTNO: %2$s ) 수정 요청";
	
	public static final String SUCCESS_UPDATE_POST = "%1$s 님이 게시글( POSTNO: %2$s ) 수정 성공";
	
	public static final String FAIL_UPDATE_POST = "%1$s 님의 게시글( POSTNO: %2$s ) 수정 실패";
	
	public static final String REQUEST_BOARD_COMMENT = "%1$s 님이 게시글( POSTNO: %2$s ) 댓글 등록 요청";
	
	public static final String SUCCESS_BOARD_COMMENT = "%1$s 님이 게시글( POSTNO: %2$s ) 댓글 등록 성공 ( COMMENTIDX: %3$s )";
	
	public static final String FAIL_BOARD_COMMENT = "%1$s 님이 게시글( POSTNO: %2$s ) 댓글 등록 실패";
	
}
