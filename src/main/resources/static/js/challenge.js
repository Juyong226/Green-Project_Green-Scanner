$(document).ready(function() {

 	$("#cancle-btn").click(function() {
 		if(confirm("선택한 내용이 저장되지 않습니다.\n취소하시겠습니까?")) {
 			history.back();			
 		} 
 	});		
 });
 
function fn_duplicate_check(code) {
 	let result;
 	$.ajax({
 		method: "POST",
 		url: "/challenge/duplicate_check",
 		data: { challengeCode: code },
 		async: false,
 		success: function(data) {
 			let obj = JSON.parse(data);
 			
 			if(obj.msg == "OK") {
 				result = true;
 				
 			} else {
 				alert(obj.msg);
 				result = false;
 				
 			}
 		} 		
 	});
 	return result;
 	
 }
 
function fn_feed_duplicate_check(cNum) {
 	let result = true;
 	let challengeNum = cNum;
 	
 	$.ajax({
 		method: "POST",
 		url: "/challenge/feed/duplicate_check",
 		data: { challengeNum: challengeNum },
 		async: false,
 		success: function(data) {
 			let obj = JSON.parse(data);
 			if(obj.msg) {
 				alert(obj.msg);
 				result = false;
 				
 			}
 		} 		
 	});
 	return result;
 	
}
 
function fn_comment_write_confirm() {
 	let content = $("#cmt-txtarea").val();
 	if(content == "") {
 		alert("댓글 내용을 입력해주세요.");
 		return false;
 	} else {
 		if(confirm("댓글을 등록하시겠습니까?")) {
 			return true;
 		} else {
 			return false;
 		}
 	}
}
 
function fn_comment_update_confirm(index) {
 	let content = $("#cmt-update-txtarea-" + index).val();
 	if(content == "") {
 		alert("댓글 내용을 입력해주세요.");
 		return false;
 	} else {
 		if(confirm("댓글을 수정하시겠습니까?")) {
 			return true;
 		} else {
 			return false;
 		}
 	}
}
 
function fn_cmt_delete_confirm() {
 
 	if(confirm("댓글을 삭제하시겠습니까?")) {
 		return true;
 		
 	} else {
 		return false;
 		
 	}
}

function fn_edit_confirm(challengeNum) {
 	
	let cNum = challengeNum;
	let period = $("#editForm select[name='period']").val();
	let colorCode = $("#editForm input[name='colorCode']:checked").val();
	
	if(period === undefined || colorCode === undefined ) {
		alert("기간과 색상을 선택해주세요.");
		return false;
		
	} else {
	 	if(confirm("챌린지를 수정하시겠습니까?")) {	 		
	 		let result;
	 		
	 		$.ajax({
	 			method: "POST",
				url: "/challenge/period_check",
				data: { cNum: cNum, newPeriod: period },
				async: false,
				success: function(data) {					
					let obj = JSON.parse(data);
					
					if(obj.msg) {
						alert(obj.msg);
						result = false;
						
					} else {
						result = true;
						
					}
				}
	 		});
	 		return result;
	 			 		
	 	} else {
	 		return false;
	 		
	 	}
	}
}
 
function fn_create_confirm() {
 
	let period = $("#createForm select[name='period']").val();
	let colorCode = $("#createForm input[name='colorCode']:checked").val();
	
	if(period == undefined || period == null || colorCode === undefined ) {
		alert("기간과 색상을 선택해주세요.");
		return false;
		
	} else {
	 	if(confirm("챌린지를 생성하시겠습니까?")) {
	 		return true;
	 		
	 	} else {
	 		return false;
	 		
	 	}
	 }
}
 
function fn_write_confirm() {
 	
	let content = $("#feedWriteForm textarea[name='content']").val();
	
	if(content == "") {
		alert("피드 내용을 입력해주세요.");
		return false;
		
	} else {
		if(confirm("피드를 등록하시겠습니까?")) {
			return true;
			
		} else {
			return false;
			
		}	
	}
 	
}
 
function fn_delete_confirm() {
 
 	if(confirm("챌린지를 삭제하시겠습니까?")) {
 		return true;
 		
 	} else {
 		return false;
 		
 	}
}
 
function fn_feed_delete_confirm() {
 	
 	if(confirm("피드를 삭제하시겠습니까?")) {
 		return true;
 		
 	} else {
 		return false;
 		
 	}
}
 
 /*---------더 보기 관련 script---------*/
 function more_feed(startIdx, requestPage, challengeNum) {
 	let endIdx;
 	if( (startIdx + searchStep - 1) > totalFeedCnt ) {
 		endIdx = totalFeedCnt;
 	} else {
 		endIdx = startIdx + searchStep -1;
 	}
 	/*alert("startIdx: " + startIdx + "/ endIdx: " + endIdx + "/ requestPage: " + requestPage + "/ challengeNum: " + challengeNum);*/ 
 	$.ajax({
 		method: "POST",
 		url: "/challenge/feed/more_feed",
 		data: 
 		{ 
 			startIdx: startIdx, 
 			endIdx: endIdx, 
 			requestPage: requestPage, 
 			challengeNum: challengeNum 
 		},
 		dataType: "html",
 		success: function(html) {
 			$(html).appendTo($(".chall-cont-feed-wrapper")).slideDown();
 			
 			if( (startIdx + searchStep) > totalFeedCnt ) {
 				$('#chall-more-btn').remove();
 			}
 		}
 	});
 }
 
 /*---------toggle 관련 script---------*/
 function chall_toggle(e) {		
	var point = $(e.target);
	console.log(point);
	if(point.attr('class') === '3dots-label-img') {
		console.log(menu);
		if(menu !== undefined && menu.css('display') === 'flex') {
			menu.css('display', 'none');
		}
		if(point.attr('id') === 'my-detail-3dots') {
			menu = $('.my-detail-dropdown-submenu');
			if(menu.css('display') === 'none') {
	 			menu.css('position', 'absolute');
	 			menu.css('display', 'flex');
	 			menu.css('flex-direction', 'column');
	 			menu.css('justify-content','space-evenly');
	 		} else {
	 			menu.css('display', 'none');
	 		}
		} else {
			menu = $('#feed-3dots-submenu-' + point.attr('id'));
			if(menu.css('display') === 'none') {
	 			menu.css('position', 'absolute');
	 			menu.css('display', 'flex');
	 			menu.css('flex-direction', 'column');
	 			menu.css('justify-content','space-evenly');
	 		} else {
	 			menu.css('display', 'none');
	 		}
		}
	} else {
		console.log(menu);
		if(menu !== undefined && menu.css('display') === 'flex') {
			menu.css('display', 'none');
		}
	}
 }