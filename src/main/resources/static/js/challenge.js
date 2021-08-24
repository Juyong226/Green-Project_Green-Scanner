/**
 * 
 */
 
 $(document).ready(function() {
 
 	$("#cancleBtn").click(function() {

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
 
 function fn_feed_duplicate_check() {
 	let result = true;
 	
 	$.ajax({
 		method: "GET",
 		url: "/challenge/feed/duplicate_check",
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
 	let period = $("#editForm input[name='period']:checked").val();
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
 
 	let period = $("#createForm input[name='period']:checked").val();
 	let colorCode = $("#createForm input[name='colorCode']:checked").val();
 	
 	if(period === undefined || colorCode === undefined ) {
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