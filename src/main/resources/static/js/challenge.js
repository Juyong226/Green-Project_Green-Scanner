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
 	
	const content = $("#feedWriteForm textarea[name='content']").val();
	const firstImg = $(".feed-img-div").first().children("input[type='file']").val();
	if(content == "") {
		alert("피드 내용을 입력해주세요.");
		return false;
		
	} else if(firstImg == "" || firstImg === undefined) {
		const firstImgIdx = $(".feed-img-div").first().children("input[type='hidden']").val();
		if(firstImgIdx == "" || firstImgIdx === undefined) {
			alert("사진을 1장 이상 업로드해주세요.");
			return false;
		}
		alert("사진을 1장 이상 업로드해주세요.");
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
 			
 			const swiper = new Swiper('.swiper', {
		 		pagination: {
		 			el: '.swiper-pagination',
		 		},
		 		
		 		navigation: {
		 			nextEl: '.swiper-button-next',
		 			prevEl: '.swiper-button-prev',
		 		},
		 	});
 			
 			if( (startIdx + searchStep) > totalFeedCnt ) {
 				$('#chall-more-btn').remove();
 			}
 		}
 	});
 }
 
/*---------toggle 관련 script---------*/
 function chall_toggle(e) {		
	let pointClass = $(e.target).attr('class');
	let pointIdTmp = $(e.target).attr('id');
	console.log("pointClass: " + pointClass + " / pointId: " + pointId);
	if(pointClass === '3dots-label-img') {
		console.log(menu);
		if(menu !== undefined && pointId !== undefined && pointId !== pointIdTmp && menu.css('display') === 'flex') {
			menu.css('display', 'none');
		}
		if(pointIdTmp === 'my-detail-3dots') {
			menu = $('.my-detail-dropdown-submenu');
			pointId = pointIdTmp;
			if(menu.css('display') === 'none') {
	 			menu.css('position', 'absolute');
	 			menu.css('display', 'flex');
	 			menu.css('flex-direction', 'column');
	 			menu.css('justify-content','space-evenly');
	 		} else {
	 			menu.css('display', 'none');
	 		}
		} else {
			menu = $('#feed-3dots-submenu-' + pointIdTmp);
			pointId = pointIdTmp;
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
 
 /*---------댓글 등록 관련 script---------*/
  function chall_regi_cmt() {
    let content = writeTextarea.val();
    console.log(writeTextarea);
 	if(content == "") {
 		alert("댓글 내용을 입력해주세요.");
 	} else {
 		if(confirm("댓글을 등록하시겠습니까?")) {
 			let requestUrl = urlHead;
			let queryString = $('#cmt-write-form').serialize();
			writeTextarea.val('');
 			$.ajax({
		  		method: "POST",
		  		url: requestUrl,
		  		data: queryString,
		  		dataType: "html",
		  		success: function(html) {
		  			$(html).prependTo(cmtListWrapper).slideDown();
		  			if(document.querySelector('.no-cmt-list')) {
		  				$('.no-cmt-list').remove();
		  			} 
		  		}
  			});
 		}
 	}		
  }
  
/*---------댓글 수정폼 요청 script---------*/
	function fn_update_form(idx) {
		let requestUrl = urlHead + idx;
		let originCmtId = cmtIdHead + idx;
		console.log("requestUrl: " + requestUrl + " / challengeNum: " + challengeNum + " / originCmtId: " + originCmtId);
		$.ajax({
			method: "GET",
			url: requestUrl,
			data: { challengeNum: challengeNum },
			dataType: "html",
			success: function(html) {
				$(originCmtId).hide();
				$(originCmtId).after(html);
			}
		});
	}
	
/*---------댓글 수정 script---------*/	
	function chall_update_cmt(idx) {
		let content = $('#cmt-update-textarea').val();
		console.log(content);
	 	if(content == "") {
	 		alert("댓글 내용을 입력해주세요.");
	 	} else {
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

/*---------댓글 삭제 script---------*/	
	function chall_delete_cmt(idx) {
		let requestUrl = urlHead + idx;
		if(confirm("댓글을 삭제하시겠습니까?")) {
	 		$.ajax({
	 			method: "DELETE",
	 			url: requestUrl,
	 			success: function(data) {
	 				let obj = JSON.parse(data);
	 				if(obj.success) {
	 					$('#cmt-content-' + idx).remove();
	 					if(!document.querySelector('.cmt-cont')) {
	 						if(!document.querySelector('.no-cmt-list')) {
	 							let html = `
	 								<div class="no-cmt-list">
	 									<span>아직 등록된 댓글이 없습니다.</span>
	 								</div>
	 								`;
	 							$(html).prependTo(cmtListWrapper).slideDown();
	 						}
	 					} 
	 				} else if(obj.failed) {
	 					alert(obj.failed);
	 				} else {
	 					alert(obj.loginRequired);
	 					location.replace('/challenge/main');
	 				}
	 			}
	 		});
	 	}
	}
	
/*---------글자수 제한 script---------*/
	function fn_checkByte(obj){
		const textarea = $(obj);
	    const target = textarea.parent().find('#nowByte');
	    const maxByte = 300; //최대 100바이트
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
	    
	    if(totalByte>maxByte) {
	    	alert('최대 300Byte까지만 입력가능합니다.');
	        target.text(totalByte);
	        target.css('color', 'red');
	    } else {
	        target.text(totalByte);
	        target.css('color', 'green');
	    }
	}
	
	