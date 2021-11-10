$(document).ready(function(){
	//아이디 중복체크
	$(document).on("click", "#emailChkBtn", function(event) { //로그아웃 처리
		 if ($('#email').val() == '') {
		      alert('이메일을 입력해주세요.')
		      return;
		    }
		 var regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
		 var email=$("#email").val();
		if (email.match(regExp) != null) {
				
		} 
		else{ 
			alert('이메일 형식에 맞춰주세요. ex)email@naver.com'); 
			$("#email").focus();
			return false;
		}
		 	
		$.post("../emailChk.do",
			  {			   			  
				email:email	
			  },
			  function(data, status){
				  var object = JSON.parse(data);
				  var check=object.chk;
			  		if(check==1){
			  			alert("중복된 이메일입니다.")
			  		}else if(check==0){
			  			$('#email').attr("check_result", "success");
			  			alert("사용가능한 이메일입니다.")
			  		}
			  }
		);//end post() 
	
	});
	
	$('#nicknameChkBtn').click(function() {
		var nickName = $('#nickname').val();
		if(nickName == '') {
			alert('닉네임을 입력해주세요.');
			$("#nickname").focus();
			return false;
		} else if(nickName.length < 2) {
			alert('닉네임을 2자 이상 6자 이하로 입력해주세요.');
			$("#nickname").focus();
			return false;
		} else if(nickName.length > 6) {
			alert('닉네임을 2자 이상 6자 이하로 입력해주세요.');
			$("#nickname").focus();
			return false;
		} else {
			$.post('../checkNickname.do',
					{
						nickName: nickName
					},
					function(data, status) {
						var obj = JSON.parse(data);
						var result = obj.checked;
						if(result == 0) {
							$('#nickname').attr('check_result', 'success');
							alert('사용가능한 닉네임입니다.');
						} else {
							alert('중복된 닉네임입니다.');
							$("#nickname").focus();
						}
					});
		}
	});
	
	$("#memberInsertBtn").click(function(){//회원 가입 처리

		var name=$("#name").val();
		var email=$("#email").val();
		var pw=$("#pw").val();
		var pwcfm=$("#pwcfm").val();
		var nickname=$("#nickname").val();
		//이메일 형식제한
		var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		//띄어쓰기
		var space = /\s/g;
		if(name.length < 2){
			alert("이름은 2글자 이상 입력해 주세요.");
			//focus함수는 #name의 창에 커서를 위치시켜 바로 입력이 가능하게 한다.
			$("#name").focus();
			return false;
		}
		//공백이 없는경우 -1을 반환 
		if(name.match(space)){
			alert("이름에는 공백이 포함될 수 없습니다.");
			$("#name").focus();
			return false;
		}
		else if($('#email').attr("check_result") == "fail"){
		    alert("이메일 중복체크를 해주시기 바랍니다.");
		    $('#email').focus();
		    return false;
		}
		else if(email.match(regExp) == null) {
			alert('이메일 형식에 맞춰주세요. ex)email@naver.com'); 
			$("#email").focus();
			return false;
		}
		else if(nickname.length < 2){
			alert("닉네임은 2글자 이상 6글자 이하로 입력해주세요.");
			//focus함수는 #name의 창에 커서를 위치시켜 바로 입력이 가능하게 한다.
			$("#nickname").focus();
			return false;
		}
		else if(nickname.length > 6){
			alert("닉네임은 2글자 이상 6글자 이하로 입력해주세요.");
			$("#nickname").focus();
			return false;
		}
		//공백이 없는경우 -1을 반환 
		else if(nickname.match(space)){
			alert("닉네임에는 공백이 포함될 수 없습니다.");
			$("#nickname").focus();
			return false;
		}
		else if($('#nickname').attr('check_result') == 'fail') {
			alert("닉네임 중복체크를 해주시기 바랍니다.");
		    $('#nickname').focus();
		    return false;
		}		
		else if(pw.length < 6){
			alert("비밀번호는 6글자 이상 입력해 주세요.");
			$("#name").focus();
			return false;
		}
		else if(pw != pwcfm){
	        alert("비밀번호가 서로 다릅니다. 비밀번호를 확인해 주세요."); 
	        $("#pwcfm").focus();
	        return false; 
	    }
		else if($("#agree_rule1").is(":checked") != true) {
			//작업
			alert("개인정보처리방침에 동의해주세요."); 
			$("#agree_rule1").focus();
	        return false; 
		}
		else if($("#agree_rule2").is(":checked") != true) {
			//작업
			alert("이용약관에 동의해주세요."); 
			$("#agree_rule2").focus();
	        return false; 
		}
		else if(confirm("회원가입을 하시겠습니까?")){
	        //alert("회원가입을 축하합니다!");
	        
	    }else{
	    	return false;
	    }
		
		$.post("../memberInsert.do",
			  {
			    name:name,
			    email:email,
			    pw:pw,
			    nickname:nickname
			  },
			  function(data, status){
			  	var obj = JSON.parse(data);
			  	if(obj.success) {
			  		alert(obj.success);
			    	window.close();
			    } else {
			    	alert(obj.failed);
			    }
			  });
		
	});

});
