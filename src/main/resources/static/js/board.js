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
			location.href="/html/login.html"
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

function cmt_submit(){
	let content = $("#cmt-write-textarea").val();
	let totalBytes = fn_totalBytes(content);
	if (content == "") {
		alert("댓글 내용을 입력해주세요.");
		return false;
	} else if(totalBytes > 300) {
 		alert("글자 수를 확인해주세요.\n최대 300Byte까지만 입력가능합니다.");
 		return false;
	}
}


function DeletePost() {
	if (!confirm("게시물을 삭제하시겠습니까?")) {
		return false;
	}
}

function writePost() {
	const content = $("#content").val();
	const title = $("#title").val();
	const totalBytes = fn_totalBytes(content);
	if (typeof title == "undefined" || title == "" || title == null) {
		alert("제목을 입력해주세요.");
		return false;
	}
	else if (typeof content == "undefined" || content == "" || content == null) {
		alert("내용을 입력해주세요.");
		return false;
	}
	else if (totalBytes > 1000) {
		alert("글자 수를 확인해주세요.\n최대 1000Byte까지만 입력가능합니다.");
 		return false;
	}
}


$(document).ready(function() {

	//$('#ex_filename').change(function() {
	//	var filename = $(this).val();
	//	$('.upload-name').val(filename);
	//});

	var fileTarget = $('.file-upload .upload-hidden');

	fileTarget.on('change', function() {  // 값이 변경되면
		if (window.FileReader) {  // modern browser
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

imgTarget.on('change', function() {
	var parent = $(this).parent();
	parent.children('.upload-display').remove();

	if (window.FileReader) {
		//image 파일만
		if (!$(this)[0].files[0].type.match(/image\//)) return;

		var reader = new FileReader();
		reader.onload = function(e) {
			var src = e.target.result;
			parent.prepend('<div class="upload-display"><div class="upload-thumb-wrap"><img src="' + src + '" class="upload-thumb"></div></div>');
		}
		reader.readAsDataURL($(this)[0].files[0]);
	}

	else {
		$(this)[0].select();
		$(this)[0].blur();
		var imgSrc = document.selection.createRange().text;
		parent.prepend('<div class="upload-display"><div class="upload-thumb-wrap"><img class="upload-thumb"></div></div>');

		var img = $(this).siblings('.upload-display').find('img');
		img[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enable='true',sizingMethod='scale',src=\"" + imgSrc + "\")";
	}
});


/* ---------------더보기 관련 JS--------------*/

function more_post(startIdx) {
/*	alert(startIdx);
	alert(searchStep);
	alert(totalBoardCnt);*/
	let boardname = $("#boardname").val();
	let request_url;
	if (boardname=="자유게시판"){
		request_url = "/board/bulletin_boardlist/more_post"
	}else{
		request_url = "/board/question_boardlist/more_post"
	}
	
	let endIdx;
 	if( (startIdx + searchStep - 1) > totalBoardCnt ) {
 		endIdx = totalBoardCnt;
 	} else {
 		endIdx = startIdx + searchStep -1;
 	}
 	$.ajax({
 		method: "POST",
 		url: request_url,
 		data: { startIdx: startIdx, endIdx: endIdx },
 		dataType: "html",
 		success: function(html) {
 			$(html).appendTo($(".Post_wrapper")).slideDown();
 			
 			if( (startIdx + searchStep) > totalBoardCnt ) {
 				$('#board_more_btn').remove();
 			}
 		}
 	});
	
	
	
}





/* --------------- 댓글 관련 JS--------------*/

/*---------댓글 수정폼 요청 script---------*/
	function fn_update_form(idx) {
		let requestUrl = urlHead + idx;
		let originCmtId = cmtIdHead + idx;
		console.log("requestUrl: " + requestUrl + " / originCmtId: " + originCmtId);
		$.ajax({
			method: "GET",
			url: requestUrl,
			dataType: "html",
			success: function(html) {
				$(originCmtId).hide();
				$(originCmtId).after(html);
			}
		});
	}
	
/*---------댓글 수정 script---------*/	
	function board_update_cmt(idx) {
		let content = $('#cmt-update-textarea').val();
		let totalBytes = fn_totalBytes(content);
//		console.log(content);
	 	if(content == "" || content === undefined) {
	 		alert("댓글 내용을 입력해주세요.");
	 	} else if(totalBytes > 300) {
	 		alert("글자 수를 확인해주세요.\n최대 300Byte까지만 입력가능합니다.");	
	 	}else {
	 		if(confirm("댓글을 수정하시겠습니까?")) {
	 			let requestUrl = urlHead + idx;
				let originCmtId = cmtIdHead + idx;
				let queryString = $('#cmt-update-form').serialize();
				$.ajax({
					method: "PATCH",
					url: requestUrl,
					data: queryString,
					dataType: "html",
					success: function(html) {
						let updateForm = ".cmt-update-wrapper"
						$(originCmtId).remove();
						$(updateForm).after(html);
						$(updateForm).remove();
					}
				});
	 		}
	 	}
	}  

/*---------댓글 수정폼 삭제 script---------*/	
	function fn_delete_update_form(idx) {
		$('.cmt-update-wrapper').remove();
		$('#cmt-content-' + idx).show();
	}

/*---------글자수 제한 script---------*/
	function fn_checkByte(obj){
		const textarea = $(obj);
	    const target = textarea.parent().find('#nowByte');
	    const text_val = obj.value; //입력한 문자
	    const text_len = text_val.length; //입력한 문자수
	    
	    let totalByte=0;
	    for(let i=0; i<text_len; i++) {
	    	const each_char = text_val.charAt(i);
	        const uni_char = escape(each_char) //유니코드 형식으로 변환
	        if(uni_char.length>4) {
	        	// 한글 : 2Byte
	            totalByte += 2;
	        } else {
	        	// 영문,숫자,특수문자 : 1Byte
	            totalByte += 1;
	        }
	    }
	    
	    const textareaClassName = textarea.attr('class');
	    let maxByte;
	    if(textareaClassName === 'board-write-content-input') {
	    	maxByte = 1000;
	    }
	    else {
	    	maxByte = 300;
	    }
	    if(totalByte>maxByte) {
	    	alert('최대 ' + maxByte + 'Byte까지만 입력가능합니다.');
	        target.text(totalByte);
	        target.css('color', 'red');
	    } else {
	        target.text(totalByte);
	        target.css('color', 'green');
	    }
		    
	}
	
	/*---------글자수 세기(한글=2byte) script---------*/	
	function fn_totalBytes(textValue) {
		const text_len = textValue.length;
		
		let totalByte = 0;
		for(let i=0; i<text_len; i++) {
			const each_char = textValue.charAt(i);
			const uni_char = escape(each_char);
			if(uni_char.length > 4) {
				totalByte += 2;
			} else {
				totalByte += 1;
			}
		}
		return totalByte;
	}

var updateReno = updateRememo = null;
/*댓글 수정 폼 보이기
숨겨져 있던 댓글 입력창(replyUpdateDiv)을 보여주며 기존에 입력한 댓글 내용을 받아와 넣어준다. 
댓글 수정 상태에서 취소/저장을 하지 않고 다른 글을 수정하려고 할 때 현재 입력하던 내용을 취소하기 위해 
현재 입력하는 댓글 번호를 updateReno에 저장한다. */

function fn_replyUpdate(reno) {
	alert(reno);
	var form = document.form2;
	var reply = document.getElementById(reno);
	var replyDiv = document.getElementById("replyUpdateDiv");
	replyDiv.style.display = "";

	if (updateReno) {
		document.body.appendChild(replyDiv);
		var oldReno = document.getElementById(updateReno);
		oldReno.innerText = updateRememo;
	}
	form.reno.value = reno;
	//수정하고자 하는 댓글의 내용을 가져와 입력창의 입력박스에 넣어준다 
	form.rememo.value = reply.innerText;
	reply.innerText = ""; //""처리를 해주지 않으면 입력상자 위에 텍스트가 중복 표시된다. 
	reply.appendChild(replyDiv);
	updateReno = reno;
	updateRememo = form.rememo.value;
	form.rememo.focus();
}


/*댓글 수정 후 저장
댓글을 수정한 후 저장 버튼을 누르면 입력된 내용을 서버로 전송*/
function fn_replyUpdateSave() {
	var form = document.form2;
	if (form.rememo.value == "") {
		alert("글 내용을 입력해주세요.");
		form.rememo.focus();
		return;
	}
	form.action = "boardReplySave";
	form.submit();
}

/*취소 버튼 처리
수정하던 댓글 내용을 취소하고 입력창을 화면에서 보이지 않게 한다*/
function fn_replyUpdateCancel() {
	var replyDiv = document.getElementById("replyUpdateDiv");
	document.body.appendChild(replyDiv);
	replyDiv.style.display = "none";
	var oldReno = document.getElementById("reply" + updateReno);
	oldReno.innerText = updateRememo;
	updateReno = updateRememo = null;
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
