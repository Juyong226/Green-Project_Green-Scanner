

$(document).ready(function() {
	//페이지를 새로고침하거나 다른 링크로 옮겨가면서 새로 페이지가 로드되면,
	//쿠키와 세션을 확인하여 아직 로그인 상태가 유효한 지 체크하고,
	//세션이 만료된 경우 쿠키를 삭제함으로써 재로그인을 유도
	//맨 아래 함수를 참고
	fn_isLogined();
		
	$(document).on("click", "#logoutBtn", function(event) { //로그아웃 처리
		$.post("/logout.do",
			  {			   
			   
			  },
			  function(data, status){
			  	var obj = JSON.parse(data);
			  	if(obj.success) {  	
				  	alert(obj.success);
				  	$.removeCookie("logined", {path: '/' }); //path를 명시하여 쿠키가 잘 삭제 됨.
				  	$.removeCookie("blanked", {path: '/' });
				  	$.removeCookie("logouted", {path: '/' });
					location.reload();
				} else {
					$.removeCookie("logined", {path: '/' });
				  	$.removeCookie("blanked", {path: '/' });
				  	$.removeCookie("logouted", {path: '/' });
					location.reload();
				}						   
			  }
		);//end post() 
	
	});
	
	$("#loginBtn").click(function(){
		var email=$("#email").val();
		var pw=$("#pw").val();
		var space = /\s/g;
		if(email.match(space)){
			alert("이메일에는 공백이 포함될 수 없습니다.");
			$("#email").focus();
			return false;
		}
		
		$.post("/login.do",
				  {
				    email:email,
				    pw:pw,
				  },
				  function(data, status){
				  	var obj = JSON.parse(data);
				  	if(obj.memnickname) { 
					  	//sample_index.html의 nav와 맞추기 위해 이렇게 넣었습니다. 이렇게 자잘하게 쪼갠 이유는 이렇게 쪼게지 않으면 현재 js에 
					  	//sample_index의 script가 참고되지 않아서 그런지 class나 ara-current에서 오류가 나기 때문입니다.
					  	//때문에 콘솔창에서 에러가 안나는대로 쪼개서 넣느라 이런 모양이 되었습니다.
						 var nickname = obj.memnickname;
						 /*
						 var html="<a style='color:white;'";
                         
						 html+=" class=";
                         html+="'nav-link'"
                         html+=" aria-current=";
						 html+="'page'";
						 html+=" href='#'>";
						 nickname+="</a>";	 
						 html+=nickname;
						 var logoutBtn="<input type='button' value='logout' id='logoutBtn' class='btn btn-warning' margin-right='5px'>";
						 */
						 var nick='<a style="color:white;" class="nav-link" href="#" aria-disabled="true">'
							 + nickname
							 + '</a>';
						 var logout='<li id="logoutBtn" class="nav-item"><a style="color:white;" class="nav-link" href="#" aria-disabled="true">로그아웃</a></li>';
						 
						 var blankspace="";
						 //jquery에서 쿠키를 지정하는 방법입니다. expires:숫자  의 숫자는 일단위 입니다. 즉 1은 하루라는 뜻입니다.
						 //쿠키를 생성 시 같은 path를 지정해 주었기 때문에 삭제 시에도 path를 명시해 주어야 함
						 //삭제 시 path를 명시하지 않을 경우, 쿠키를 삭제하려는 위치가 '/'가 아닌 '/html/trashCan.html' 등에서는 삭제를 할 수 없음
						 
						 $("#loginBlankDiv1", opener.document).html(blankspace);
						 $("#loginBlankDiv2", opener.document).html(blankspace);
						 $("#nicknameDiv", opener.document).html(nickname);
						 $("#logoutDiv", opener.document).html(logout);
						 
						 $.cookie("logined", nick, {expires: 1, path: '/' });
						 $.cookie("blanked", blankspace, {expires: 1, path: '/' });
						 $.cookie("logouted", logout, {expires: 1, path: '/' });
						 //현재 이 팝업창은 자식창입니다. 이 코드는 이 자식창이 열린 부모창의 nicknameDiv에 값을 넣는 코드입니다.
						 

//						 window.opener.document.getElementById("loginBlankDiv1").innerHTML=blankspace;
//						 window.opener.document.getElementById("loginBlankDiv2").innerHTML=blankspace;
//						 window.opener.document.getElementById("nicknameDiv").innerHTML=html;
//						 window.opener.document.getElementById("logoutDiv").innerHTML=logoutBtn; 
						 window.close();
						 //로그인 완료되면 자식 창 닫은 뒤 부모 창 리로드
						 window.opener.location.reload();
						 
					} else if(obj.failed) {
						alert('입력하신 정보와 일치하는 회원이 없습니다.\n이메일과 비밀번호를 다시 확인해주세요.');
					} else if(obj.denied) {
						alert(obj.denied);
					}
				  });
			
	});
	
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
						$.removeCookie("logined", {path: '/' });
				  		$.removeCookie("blanked", {path: '/' });
				  		$.removeCookie("logouted", {path: '/' });
						location.reload();
					}
				});
	}
	
	//먼저 로그인 시 부여한 logined 쿠키의 존재 여부를 확인하고,
	//logined 쿠키가 존재한다면(로그인을 했었다는 의미) 세션이 아직 유효한 지 검사하는 함수를 호출
	//logined 쿠키가 없다면(로그인을 안 했었다는 의미) 상태 유지
	function fn_isLogined() {
		var loginCookie = $.cookie('logined');
		if(loginCookie) {
			fn_checkSession();
		}
	}
});