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
			location.href = "/qna/write";
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

$(document).ready(function(){

     //$('#ex_filename').change(function() {
     //	var filename = $(this).val();
     //	$('.upload-name').val(filename);
     //});

     var fileTarget = $('.file-upload .upload-hidden');

     fileTarget.on('change', function(){  // 값이 변경되면
          if(window.FileReader){  // modern browser
               var filename = $(this)[0].files[0].name;
          } 
          else {  // old IE
               var filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
          }

          // 추출한 파일명 삽입
          $(this).siblings('.upload-name').val(filename);
     });
}); 

//preview image 
var imgTarget = $('.preview-image .upload-hidden');

imgTarget.on('change', function(){
     var parent = $(this).parent();
     parent.children('.upload-display').remove();

     if(window.FileReader){
          //image 파일만
          if (!$(this)[0].files[0].type.match(/image\//)) return;

          var reader = new FileReader();
          reader.onload = function(e){
               var src = e.target.result;
               parent.prepend('<div class="upload-display"><div class="upload-thumb-wrap"><img src="'+src+'" class="upload-thumb"></div></div>');
          }
          reader.readAsDataURL($(this)[0].files[0]);
     }

     else {
          $(this)[0].select();
          $(this)[0].blur();
          var imgSrc = document.selection.createRange().text;
          parent.prepend('<div class="upload-display"><div class="upload-thumb-wrap"><img class="upload-thumb"></div></div>');

          var img = $(this).siblings('.upload-display').find('img');
          img[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enable='true',sizingMethod='scale',src=\""+imgSrc+"\")";        
     }
});

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
