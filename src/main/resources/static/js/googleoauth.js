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
					$.cookie("togooglesignup", data, {expires: 1, path: '/'});
					document.location.replace(obj.googleredirect);
				}
				else{
					$.cookie("nick-cookie", data, {expires: 1, path: '/' });
					location.replace(obj.googleredirect)
				}
			}
		)}
		};	
