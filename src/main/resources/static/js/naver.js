//네아로 api id, 네아로의 callbackurl		
var naver_id_login = new naver_id_login("bGFwUHB3RA471BquswK2", "https://localhost/index.html");
var state = naver_id_login.getUniqState();
naver_id_login.setButton("white", 2,40);
naver_id_login.setDomain("https://localhost/html/loginForm.html");
naver_id_login.setState(state);
//새로운 창에 뜨게하는 것
/*naver_id_login.setPopup();*/
naver_id_login.init_naver_id_login();




