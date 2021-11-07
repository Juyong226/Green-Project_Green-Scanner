

$(function(){
	//페이지를 새로고침하거나 다른 링크로 옮겨가면서 새로 페이지가 로드되면,
	//쿠키와 세션을 확인하여 아직 로그인 상태가 유효한 지 체크하고,
	//세션이 만료된 경우 쿠키를 삭제함으로써 재로그인을 유도
	//맨 아래 함수를 참고
	fn_isLogined();
	fn_login_data();
	
	$(document).on("click", "#logoutBtn", function(event) { //로그아웃 처리
		$.post("/logout.do", {},
			  function(data, status) {
			  	var obj = JSON.parse(data);
			  	if(obj.success) { 	
				  	alert(obj.success);
				  	$.removeCookie("nick-cookie", {path: '/' }); //path를 명시하여 쿠키가 잘 삭제 됨.
					$(".login-nickname").hide();
				  	$("#login-btn").show();
				  	$("#join-btn").show();
					location.reload();
				}						   
		});//end post() 
	});
	
	$("#loginBtn").click(function(){
		let email = $("#login-form-email").val();
		let pw = $("#login-form-pw").val();
		var regExp =  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		
		if(!regExp.test(email) || email == "") {
			alert("이메일을 다시 확인해주세요.");
			$("#login-form-email").focus();
			return false;
		} else if(pw == "") {
			alert("비밀번호를 입력해주세요.");
			$("#login-form-pw").focus();
			return false;
		}
		
		$.post("/login.do", 
			{ 
				email: email, 
				pw: pw 
			},  
			function(data, status) {
			  	var obj = JSON.parse(data);
			  	if(obj.memnickname) {
					$.cookie('nick-cookie', data, {expires: 1, path: '/'});
					location.replace(document.referrer);					
				} else if(obj.failed) {
					alert('입력하신 정보와 일치하는 회원이 없습니다.\n이메일과 비밀번호를 다시 확인해주세요.');
				} else if(obj.denied) {
					alert(obj.denied);
				}
		});			
	});
	
	//화면에 로그인 정보(쿠키)를 표시하는 함수
	function fn_login_data() {
		if($.cookie("nick-cookie")){
			logindata = JSON.parse($.cookie("nick-cookie"));
			$("#login-btn").hide();
			$("#join-btn").hide();
			$(".login-nickname").html(logindata.memnickname);
			$(".join-logout").html(logindata.logout);
		}
	}
	
	//logined 쿠키가 존재할 경우 아직 세션이 유효한 지 검사하는 함수
	//서버로 세션을 체크하라는 요청을 보내고,
	//답변이 sessionNull일 경우 로그아웃 되었다는 알림을 띄우고 쿠키를 삭제함
	//답변에 sessionNull이 아닐 경우 로그인 상태를 유지(쿠키 삭제 안 함)
	function fn_checkSession() {
		$.post("/checkSession.do",
				{},
				function(data, status) {
					let obj = JSON.parse(data);
					if(obj.sessionNull) {
						alert(obj.sessionNull);
						$.removeCookie("nick-cookie", {path: '/' });
				  		$("#login-btn").show();
				  		$("#join-btn").show();
						location.reload();
					}
				});
	}
	
	//먼저 로그인 시 부여한 logined 쿠키의 존재 여부를 확인하고,
	//logined 쿠키가 존재한다면(로그인을 했었다는 의미) 세션이 아직 유효한 지 검사하는 함수를 호출
	//logined 쿠키가 없다면(로그인을 안 했었다는 의미) 상태 유지
	function fn_isLogined() {
		var loginCookie = $.cookie('nick-cookie');
		if(loginCookie) {
			fn_checkSession();
		}
	}
})