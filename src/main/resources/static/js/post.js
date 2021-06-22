$(document).ready(function(){
	$("#registerPostBtn").click(function(){
		//postInsertForm.html에서 '등록' 버튼 클릭
		//option태그 안의 text값을 받아오기(자유게시판or질문게시판) 
		var target=document.getElementById("boardname");
		var boardname = target.options[target.selectedIndex].text;
		var title=$("#title").val();
		var nickname=$("#nickname").val();
		var content=$("#content").val();
		
		$.post("../registerPost.do",
			  {
				boardname:boardname,
			    title:title,
			    nickname:nickname,
			    content:content
			  },
			  function(data, status){
				//'글이 성공적으로 등록되었습니다'알림 띄우기 
			    alert(data); 
			    //전체글을 볼 수 있는 페이지로 이동
			    window.location.replace("boardPage.html"); 
			  });
	}); //글 등록 처리  
	
	$("#cancleBtn").click(function(){
		if(confirm("글 작성을 취소하시겠습니까?")==true)
			window.location.replace("boardPage.html");
		
	});
	

});
