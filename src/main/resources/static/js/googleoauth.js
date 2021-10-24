var clicked = false;
function GoogleLogin(){
	clicked = true;
}

function onSignIn(googleUser){
	if (clicked){
		var profile = googleUser.getBasicProfile();
		var googlememid = profile.getId();
		var googleuseremail = profile.getEmail();
		
		$.post("../googleSignUp.do",
			{
				googlememid : googlememid,
				googleuseremail : googleuseremail
			},
			function(data, status){
				let obj = JSON.parse(data);
				var returngoogleid = obj.returngoogleid;
				if (returngoogleid == 0){
					var redirect = obj.googleredirect;
					var gooemail = obj.googleuseremail;
					var expDate = new Date();
					expDate.setTime(expDate.getTime() + (15*60*1000));
					$.cookie("gooemail",gooemail);
					document.location.replace(redirect);
				}
				else{
					var redirect = obj.googleredirect;
					let nickname = ['<span id="nickname">' + obj.memnickname + '</span>'].join('');
					let logout = ['<span id="logoutBtn">로그아웃</span>'].join('');
					var blankspace="";
					$.cookie("nick-cookie", nickname, {expires: 1, path: '/' });
					$.cookie("logout-btn-cookie", logout, {expires: 1, path: '/' });
					location.replace(redirect)
				}
			}
		)}
		};	
