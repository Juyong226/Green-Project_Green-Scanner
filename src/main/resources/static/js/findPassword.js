$(function(){
	fn_isLogined()
	
	$("#findBtn").click(function(){
		let email = $("#find-password-form-email").val();
		var space = /\s/g;
		var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		
		if(email == ""){
			alert("이메일을 입력해주세요.");
			return;
		}
		if(email.match(space)){
			alert("이메일에는 공백이 포함될 수 없습니다.");
			$("#email").focus();
			return false;
		}
		if(email.match(regExp) == null){
			alert('이메일 형식에 맞춰주세요. ex)email@naver.com'); 
			return;
		}
		
		$.post("../toFindPassword.do",
			{
				email: email
			},
			function(data, status){
				var obj = JSON.parse(data);
				if(obj.snsSignUp){
					alert(obj.snsSignUp);
				}
				if(obj.success){
					alert(obj.success);
					location.replace(obj.redirect);
				}
				if(obj.nonSignUp){
					alert(obj.nonSignUp);
				}
			}
		)
			
		
	});
	
	
	function fn_isLogined(){
		var loginCookie = $.cookie('nick-cookie');
		if (loginCookie != null){
			location.replace("/");
		}
	}
	
});