$(document).ready(function(){ 
	//페이지가 로드되면 게시판에 작성된 글 불러오는 post요청 수행 
	$.post("../boardListQ.do", 
			{},
			function(data, status){
				//alert("요청 보냄");
				var obj = JSON.parse(data); //Json Object를 파싱
				//질문게시판 화면 처리
				var postQ = obj.postListQ;
				var postQ_html = ""; //화면에 보여줄 postB_html 선언
				postQ.forEach(function(item){ 
					//allpost를 돌면서 각 item의 요소들을 postB_html에 담기
					//console.log(item.postno);
					postQ_html += '<div class="postno board-font"  id="'+item.postno+'">';
					postQ_html += '<div><a class="cls1" href="../viewPost?postno='+item.postno+'" style="text-decoration:none; color:black">'
					postQ_html += '<label class = "txt_line" style="font-weight:bold">'+item.title+'</label></div>';
					postQ_html += '<label class = "txt_line">'+item.content+'</label><br>';
					postQ_html += '<label style="float:right; color:#adb5bd; font-size:0.9em">'+item.postdate+'</label></div>';
					postQ_html += '<label style="font-size:0.9em"><img src="../img/user.png"  style="width: 18px; height: 18px;" alt="..." > '+item.nickname+'</label>';
					postQ_html += '&nbsp;&nbsp;<img src="../img/bubble.png">&nbsp<label style="font-size:0.9em">'+item.reply_cnt+'</label></a>';
					//post_html += '<input type="button" class="postno" id="'+item.postno+'"/>'
					postQ_html += '<hr>';
				});
				$("#Question_Board").html(postQ_html);	
			});		
});
