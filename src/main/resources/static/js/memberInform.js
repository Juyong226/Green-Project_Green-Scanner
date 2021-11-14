$(function(){
	fn_isLogined();
	fn_login_data();
	
	$(document).on("click", "#realPasswordChageBtn", function(event){
		logindata = JSON.parse($.cookie("nick-cookie"));
		let email = logindata.mememail;
		var pw = $("#passwordChangeInput1").val();
		var pwcfm =$("#passwordChangeInput2").val();
		var space = /\s/g;		
		
		if(pw.length < 6){
			alert("비밀번호는 6글자 이상 입력해 주세요.");
			$("#passwordChangeInput1").focus();
			return false;
		}
		else if(pw.length >12){
			alert("비밀번호는 12글자 이하로 입력해 주세요.");
			$("#passwordChangeInput1").focus();
			return false;
		}
		else if(pw.match(space)){
			alert("비밀번호에는 공백이 포함될 수 없습니다.");
			$("#passwordChangeInput1").focus();
			return false;
		}
		else if(pw != pwcfm){
			alert("비밀번호가 서로 다릅니다. 비밀번호를 확인해 주세요."); 
			$("#passwordChangeInput1").focus();
			return false; 
		}
		else if(confirm("비밀번호를 변경하시겠습니까?")){
		}else{
			return false;
		}
		
		$.post("/changePassword.do",
			{
				email: email,
				pw: pw
			},
			function(data,status){
				let obj = JSON.parse(data);
				if(obj.success){
					alert(obj.success);
					$.removeCookie("nick-cookie", {path: '/' });
					$(".login-nickname").hide();
			  		$("#login-btn").show();
			  		$("#join-btn").show();
					location.replace("/");
				}
				if(obj.failed){
					alert(obj.failed)
				}
			}
		)
		
		
	})
	
	$(document).on("click", "#passwordCheckBtnAfter", function(event) { 
		logindata = JSON.parse($.cookie("nick-cookie"));
		let email = logindata.mememail;
		let pw = $("#password-check").val();
		let space = /\s/g;
		
		if(pw.match(space)){
			alert("비밀번호에는 공백이 포함될 수 없습니다.");
			$("#password-check").focus();
			return false;
		}
		
		$.post("/checkPassword.do",
			{
				email: email,
				pw: pw
			},
		function(data,status){
			let obj = JSON.parse(data);
			if(obj.exist){
				alert(obj.exist);
				var passwordChangeInput1 = "<input type=\"password\" id =\"passwordChangeInput1\" placeholder=\"비밀번호\">";
				var passwordChangeInput2 = "<input type=\"password\" id =\"passwordChangeInput2\" placeholder=\"비밀번호 확인\">";
				var passwordcheck = document.getElementById("password-check");
				passwordcheck.type = "hidden";
				$("#passwordCheckBtnBefore2").html(passwordChangeInput1);
				$("#passwordCheckBtnBefore").html(passwordChangeInput2);
				var realPasswordChageBtn = "<button id =\"realPasswordChageBtn\" style=\"padding-right: 28px; padding-left: 28px;\">변경!</button>";
				$("#passwordCheckBtnBefore3").html(realPasswordChageBtn);
			}
			if(obj.notExist){
				alert(obj.notExist);
			}
		});
	});
	
	
	$("#passwordChangeBtn").click(function(){
		if(confirm("비밀번호를 변경하시겠습니가?"))
			{
				var passwordcheck = document.getElementById("password-check");
				passwordcheck.type = "password";
				var passwordCheckBtnAfter = "<button id =\"passwordCheckBtnAfter\" style=\"padding-right: 28px; padding-left: 28px;\">비밀번호 확인</button>";
				$("#passwordCheckBtnBefore").html(passwordCheckBtnAfter);
			}
		
	});
	

	
	$("#memberOutBtn").click(function(){
		if(confirm("가시려구요? ㅠㅠ"))
			{

				$.post("/signout.do",
						{},
						function(data, status){
							let obj = JSON.parse(data);
							if(obj.chk){
								alert(obj.chk);
								$.removeCookie("nick-cookie", {path: '/'});
								location.replace(obj.redirect);
							}
					});
			}else{
				false;
			}
			});
	
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
	
	function fn_checkSession() {
		$.post("/checkSession.do",
				{},
				function(data, status) {
					let obj = JSON.parse(data);
					if(obj.sessionNull) {
						alert(obj.sessionNull);
						$.removeCookie("nick-cookie", {path: '/' });
						$(".login-nickname").hide();
				  		$("#login-btn").show();
				  		$("#join-btn").show();
						location.replace("/");
					}
				});
	}
	
	function fn_isLogined() {
		var loginCookie = $.cookie('nick-cookie');
		if(loginCookie) {
			fn_checkSession();
		}else{
			location.replace("/");
		}
	}

	function fn_login_data(){
		if($.cookie("nick-cookie")){
			logindata = JSON.parse($.cookie("nick-cookie"));
			$("#login-btn").hide();
			$("#join-btn").hide();
			$(".login-nickname").html(logindata.memnickname);
			$(".join-logout").html(logindata.logout);
			if(logindata.oauth){
				$("#password-change-text").hide();
				$("#passwordChangeBtn").hide();
			}
		}
	}
	
})

