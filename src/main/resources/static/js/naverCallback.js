var naver_id_login = new naver_id_login("bGFwUHB3RA471BquswK2", "https://localhost/index.html");
	
naver_id_login.get_naver_userprofile("naverSignInCallback()");

function naverSignInCallback(){
	var navermemid = naver_id_login.getProfileData("id");
	var naveruseremail = naver_id_login.getProfileData("email");
	var naverusername = naver_id_login.getProfileData("name");
	$.post("naverSignUp.do",
		{
			navermemid : navermemid,
			naveruseremail : naveruseremail,
			naverusername : naverusername
		},
		function(data,status){
			let obj = JSON.parse(data);
			var returnsnsid = obj.returnsnsid;
			if (returnsnsid==0){	
				$.cookie("tosnssignup", data, {expires:1, path:'/'});
				location.replace(obj.snsredirect);
				}
			else{
				$.cookie("nick-cookie", data, {expires:1, path:'/'});
				location.replace(obj.snsredirect);
			}
				
		}
	)};
		
		
	
	