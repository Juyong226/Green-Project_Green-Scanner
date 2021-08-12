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

 function fn_edit_confirm() {
 
 	let period = $("#editForm input[name='period']:checked").val();
 	let colorCode = $("#editForm input[name='colorCode']:checked").val();
 	
 	if(period === undefined || colorCode === undefined ) {
 		alert("기간과 색상을 선택해주세요.");
 		return false;
 		
 	} else {
	 	if(confirm("챌린지를 수정하시겠습니까?")) {
	 		return true;
	 		
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
 
 function fn_delete_confirm() {
 
 	if(confirm("챌린지를 삭제하시겠습니까?")) {
 		return true;
 		
 	} else {
 		return false;
 		
 	}
 }