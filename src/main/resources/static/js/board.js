$(document).ready(function() {
	$("#cancleBtn").click(function() {
		if (confirm("글 작성을 취소하시겠습니까? 작성한 내용이 저장되지 않습니다.") == true)
			history.back();
	});
});

$(function() {
	var blank = $.cookie("blanked");
	var login = $.cookie("logined");
	var logout = $.cookie("logouted");
	$("#loginBlankDiv1").html(blank);
	$("#loginBlankDiv2").html(blank);
	$("#nicknameDiv").html(login);
	$("#logoutDiv").html(logout);
});

$(document).ready(function() {
	$("#writeBtn").click(function() {
		user = $("#sessionNickname").val();
		if (!user) {
			alert("로그인 후 글을 작성하실 수 있습니다.");
		} else {
			location.href = "/board/write";
		}
	});
});

function DeleteComment() {
	if (!confirm("댓글을 삭제하시겠습니까?")) {
		return false;
	}
}

function DeletePost() {
	if (!confirm("게시물을 삭제하시겠습니까?")) {
		return false;
	}
}

function writePost() {
	var content = $("#content").val();
	var title = $("#title").val();
	if (typeof title == "undefined" || title == "" || title == null) {
		alert("제목을 입력해주세요.");
		return false;
	}
	else if (typeof content == "undefined" || content == "" || content == null) {
		alert("내용을 입력해주세요.");
		return false;
	}
}

/*function deleteComment(reno, postno){
	if(confirm("댓글을 삭제하시겠습니까?")){
	$.ajax({
		url:'/board/deleteReply',
		method:'post',
		data:{reno:reno,
			postno:postno},
		datatype:'JSON',
		success:function(data){
			alert(data);
			alert("댓글이 삭제되었습니다.");
		}
	});


};
};*/
