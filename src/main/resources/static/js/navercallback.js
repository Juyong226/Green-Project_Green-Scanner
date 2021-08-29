
	var naver_id_login = new naver_id_login("bGFwUHB3RA471BquswK2", "https://localhost/index.html");
	
	naver_id_login.get_naver_userprofile("naverSignInCallback()");

	function naverSignInCallback(){
		var navermemid = naver_id_login.getProfileData("id");
		var naveruseremail = naver_id_login.getProfileData("email");
		var naverusername = naver_id_login.getProfileData("name");
	
		$.post("naverSingUp.do",
			{
				navermemid : navermemid,
				naveruseremail : naveruseremail,
				naverusername : naverusername
			},
			function(data,status){
				let obj = JSON.parse(data);
				var returnnaverid = obj.returnnaverid;
				if (returnnaverid==0){	
					var redirect = obj.naverredirect;
					var nuemail = obj.naveruseremail;
					var nuname = obj.naverusername;
					//쿠시 시간 설정
					var expDate = new Date();
					expDate.setTime(expDate.getTime() + (15*60*1000));
					$.cookie("nuname", nuname, {path: '/', expires: expDate});
					$.cookie("nuemail", nuemail, {path: '/', expires: expDate});
					document.location.replace(redirect);
					}
				else{
					var redirect = obj.naverredirect;
					var nickname = obj.memnickname;
					
					var nick='<a style="color:white;" class="nav-link" href="#" aria-disabled="true">'
						 + nickname
						 + '</a>';
					var logout='<li id="logoutBtn" class="nav-item"><a style="color:white;" class="nav-link" href="#" aria-disabled="true">로그아웃</a></li>';
					var blankspace="";
					
					 $.cookie("logined", nick, {expires: 1, path: '/' });
					 $.cookie("blanked", blankspace, {expires: 1, path: '/' });
					 $.cookie("logouted", logout, {expires: 1, path: '/' });
					 location.replace(redirect)
				}
				
			}
		)};
		
	
	