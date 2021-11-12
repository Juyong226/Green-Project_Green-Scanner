$(function(){
	fn_isLogined();
	fn_login_data();
	
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
						location.replace("https://localhost");
					}
				});
	}
	
	function fn_isLogined() {
		var loginCookie = $.cookie('nick-cookie');
		if(loginCookie) {
			fn_checkSession();
		}
	}

	function fn_login_data(){
		if($.cookie("nick-cookie")){
			logindata = JSON.parse($.cookie("nick-cookie"));
			$("#login-btn").hide();
			$("#join-btn").hide();
			$(".login-nickname").html(logindata.memnickname);
			$(".join-logout").html(logindata.logout);
		}
	}
	
})

