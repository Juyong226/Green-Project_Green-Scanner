package com.garb.gbcollector.constant;

public interface LogDescription {
	
	/*
	 * 회원 관련 LOG DESC
	 * */
	
	public static final String ACCESS_LOGINPAGE = "로그인 페이지 접속";
	
	public static final String REQUEST_LOGIN = "로그인 요청";
	
	public static final String SUCCESS_LOGIN = "%s 님 로그인 성공";
	
	public static final String FAIL_LOGIN = "로그인 실패";
	
	public static final String REQUEST_LOGOUT = "%s 님이 로그아웃 요청";
	
	public static final String SUCCESS_LOGOUT = "%s 님 로그아웃 성공";
	
	public static final String FAIL_LOGOUT = "%s 님 로그아웃 실패";
	
	public static final String ACCESS_JOINPAGE = "회원가입 페이지 접속";
	
	public static final String REQUEST_JOIN = "회원가입 요청";

	public static final String SUCCESS_JOIN = "%s 님이 회원가입 성공";
	
	public static final String FAIL_JOIN = "회원가입 실패";
	
	public static final String ACCESS_MYPAGE = "%s 님이 마이페이지 접속";
	
	public static final String REQUEST_SIGNOUT = "%s 님이 회원탈퇴 요청";
	
	public static final String SUCCESS_SIGNOUT = "%s 님 회원탈퇴 성공";
	
	public static final String FAIL_SIGNOUT = "%s 님 회원탈퇴 실패";
	
	public static final String REQUEST_PASSWORD_RESET = "%s 님이 비밀번호 초기화 요청";
	
	public static final String SUCCESS_PASSWORD_RESET = "%s 님의 비밀번호 초기화 성공";
	
	public static final String FAIL_PASSWORD_RESET = "%s 님의 비밀번호 초기화 실패";
	
	public static final String REQUEST_PASSWORD_CHANGE = "%s 님이 비밀번호 변경 요청";
	
	public static final String SUCCESS_PASSWORD_CHANGE = "%s 님의 비밀번호 변경 성공";
	
	public static final String FAIL_PASSWORD_CHANGE = "%s 님의 비밀번호 변경 실패";
	
	/*
	 * 챌린지 관련 LOG DESC
	 * */
	
	public static final String ACCESS_CHALLMAIN = "%s 님이 챌린지 메인 페이지 접속";
	
	public static final String REQUEST_NEWCHALL = "%1$s 님이 새 챌린지( CHALLENGECODE: %2$s ) 시작 요청";
	
	public static final String SUCCESS_NEWCHALL = "%1$s 님의 새 챌린지( CHALLENGECODE: %2$s ) 시작 성공( CHALLENGENUM: %3$s )";
	
	public static final String FAIL_NEWCHALL = "%1$s 님의 새 챌린지( CHALLENGECODE: %2$s ) 시작 실패";
	
	public static final String ACCESS_MYCHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 상세 페이지 접속";
	
	public static final String REQUEST_UPDATE_CHALL = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 수정 요청";
	
	public static final String SUCCESS_UPDATE_CHALL = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 수정 성공";
	
	public static final String FAIL_UPDATE_CHALL = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 수정 실패";
	
	public static final String REQUEST_DELETE_CHALL = "%1$s 님이 챌린지( CHALLENNUM: %2$s ) 삭제 요청";
	
	public static final String SUCCESS_DELETE_CHALL = "%1$s 님의 챌린지( CHALLENNUM: %2$s ) 삭제 성공";
	
	public static final String FAIL_DELETE_CHALL = "%1$s 님의 챌린지( CHALLENNUM: %2$s ) 삭제 실패";
	
	public static final String REQUEST_WRITE_FEED = "%1$s 님이 챌린지( CHALLENGENUM: %2$s ) 피드 등록/수정 요청";
	
	public static final String REQUEST_NEWFEED = "새 피드 등록 요청";
	
	public static final String REQUEST_UPDATE_FEED = "피드( FEEDNO: %s ) 수정 요청";
	
	public static final String SUCCESS_WRITE_FEED = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 피드 등록/수정 성공( FEEDNO: %3$s )";
	
	public static final String FAIL_WIRTE_FEED = "%1$s 님의 챌린지( CHALLENGENUM: %2$s ) 피드 등록/수정 실패";	
	
	public static final String REQUEST_DELETE_FEED = "%1$s 님이 피드( FEEDNO: %2$s ) 삭제 요청";
	
	public static final String SUCCESS_DELETE_FEED = "%1$s 님의 피드( FEEDNO: %2$s ) 삭제 성공";
	
	public static final String FAIL_DELETE_FEED = "%1$s 님의 피드( FEEDNO: %2$s ) 삭제 실패";
	
	public static final String ACCESS_FEEDLIST = "%s 님이 피드 모아보기 페이지 접속";
	
	public static final String ACCESS_FEEDDETAIL = "%1$s 님이 피드( FEEDNO: %2$s ) 상세 페이지 접속";
	
	public static final String REQUEST_CHALL_WRITE_COMMENT = "%1$s 님이 피드( FEEDNO: %2$s ) 댓글 등록/수정 요청";
	
	public static final String REQUEST_UPDATE_COMMENT_FORM = "%1$s 님이 피드 (FEEDNO: %2$s ) 댓글( COMMENTIDX: %3$s ) 수정 폼 요청";
	
	public static final String REQUEST_NEWCOMMENT = "새 댓글 등록 요청";
	
	public static final String REQUEST_UPDATE_COMMENT = "댓글( COMMENTIDX: %s ) 수정 요청";
	
	public static final String SUCCESS_CHALL_WRITE_COMMENT = "%1$s 님의 피드( FEEDNO: %2$s ) 댓글 등록/수정 성공 ( COMMENTIDX: %3$s )";
	
	public static final String FAIL_CHALL_WRITE_COMMENT = "%1$s 님의 피드( FEEDNO: %2$s ) 댓글 등록/수정 실패";
	
	public static final String REQUEST_DELETE_COMMENT = "%1$s 님이 댓글( COMMENTIDX: %2$s ) 삭제 요청";
	
	public static final String SUCCESS_DELETE_COMMENT = "%1$s 님의 댓글( COMMENTIDX: %2$s ) 삭제 성공";
	
	public static final String FAIL_DELETE_COMMENT = "%1$s 님의 댓글( COMMENTIDX: %2$s ) 삭제 실패";
	
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
