$(document).ready(function(){ 
	//페이지가 로드되면 게시판에 작성된 글 불러오는 post요청 수행 
	$.post("../boardListB.do", 
			{},
			function(data, status){
				//alert("요청 보냄");
				var obj = JSON.parse(data); //Json Object를 파싱
				//자유게시판 화면 처리
				var postB = obj.postListB; //내용에 해당하는 부분만 postB에 담기 
				var postB_html = ""; //화면에 보여줄 postB_html 선언
				postB.forEach(function(item){ 
					//allpost를 돌면서 각 item의 요소들을 postB_html에 담기
					//console.log(item.postno);
					postB_html += '<div class="postno board-font"  id="'+item.postno+'">';
					postB_html += '<div><a class="cls1" href="../viewPost?postno='+item.postno+'" style="text-decoration:none; color:black">'
					postB_html += '<label class = "txt_line"  style="font-weight:bold">'+item.title+'</label></div>';
					postB_html += '<label class = "txt_line">'+item.content+'</label><br>';
					postB_html += '<label style="font-size:0.9em"><img src="../img/user.png"  style="width: 18px; height: 18px;" alt="..." > '+item.nickname+'</label>';
					postB_html += '&nbsp;&nbsp;<img src="../img/bubble.png">&nbsp<label style="font-size:0.9em">'+item.reply_cnt+'</label></a>';
					postB_html += '<label style="float:right; color:#adb5bd; font-size:0.9em">'+item.postdate+'</label></div>';
					//post_html += '<input type="button" class="postno" id="'+item.postno+'"/>'
					postB_html += '<hr>';
				});
				//boardPage.html의 <div id='Bulletin_board'>에 추가 
				//board.xml에서 날짜순으로 내림차순 정렬(order by postdate desc)해 최신순으로 보임 
				$("#Bulletin_board").html(postB_html); 			
			});

});
