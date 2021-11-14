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
				var returnsnsid = obj.returnsnsid;
				if (returnsnsid == 0){
					$.cookie("tosnssignup", data, {expires: 1, path: '/'});
					document.location.replace(obj.snsredirect);
				}
				else{
					$.cookie("nick-cookie", data, {expires: 1, path: '/' });
					location.replace(obj.snsredirect)
				}
			}
		)}
		};	
