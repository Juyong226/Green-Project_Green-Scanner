$(document).ready(function(){
	$("#cancleBtn").click(function(){
		if(confirm("글 수정을 취소하시겠습니까? 수정한 내용이 저장되지 않습니다.")==true)
			history.back();
	});
});



$(function() {
	var blank=$.cookie("blanked");
 	var login=$.cookie("logined"); 
 	var logout=$.cookie("logouted"); 
		$("#loginBlankDiv1").html(blank);
		$("#loginBlankDiv2").html(blank);
 		$("#nicknameDiv").html(login);
 		$("#logoutDiv").html(logout);
});
