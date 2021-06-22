$(document).ready(function(){ 
	//페이지가 로드되면 게시판에 작성된 글 불러오는 post요청 수행 
	$.post("../boardList.do", 
			{},
			function(data, status){
				//alert("요청 보냄");
				var obj = JSON.parse(data); //Json Object를 파싱
				//자유게시판 화면 처리
				var postB = obj.postListB; //내용에 해당하는 부분만 postB에 담기 
				var postB_html = ""; //화면에 보여줄 postB_html 선언
				var count = 0;
				postB.forEach(function(item){ 
					//allpost를 돌면서 각 item의 요소들을 postB_html에 담기
					//console.log(item.postno);
					postB_html += '<div class="postno board-font" style="padding-top:3px; padding-bottom:3px; border-top: 0.1px solid #bebebe;" id="'+item.postno+'">';
					postB_html += '<div class="mx-2 my-1"><a class="cls1" href="../viewPost?postno='+item.postno+'" style="text-decoration:none; color:black">'
					postB_html += '<label class = "txt_line" style="font-weight:bold; padding:5px,5px,5px,5px">'+item.title+'</label>';
					postB_html += '<label class = "txt_line" style="color: #424242; font-size:4">'+item.content+'</label><br>';
					postB_html += '<label style="font-weight:lighter; font-size:0.9em"><img src="../img/user.png" style="width: 18px; height: 18px;"alt="..." >&nbsp'+item.nickname+'</label>';
					postB_html += '&nbsp&nbsp&nbsp<img src="../img/bubble.png">&nbsp<label style="font-size:0.9em"> '+item.reply_cnt+'</label></a>';
					/*postQ_html += '<label>'+item.postdate+'</label>';*/
					postB_html += '</div></div>';
				});
				
				//boardPage.html의 <div id='Bulletin_board'>에 추가 
				//board.xml에서 날짜순으로 내림차순 정렬(order by postdate desc)해 최신순으로 보임 
				$("#Bulletin_board").html(postB_html); 
			
				//질문게시판 화면 처리
				var postQ = obj.postListQ;
				var postQ_html = ""; //화면에 보여줄 postB_html 선언
				postQ.forEach(function(item){ 
					//allpost를 돌면서 각 item의 요소들을 postB_html에 담기
					//console.log(item.postno);
					postQ_html += '<div class="postno board-font" style="padding-top:3px; padding-bottom:3px; border-top: 0.1px solid #bebebe;" id="'+item.postno+'">';
					postQ_html += '<div class="mx-2 my-1"><a class="cls1" href="../viewPost?postno='+item.postno+'" style="text-decoration:none; color:black">'
					postQ_html += '<label class = "txt_line" style="font-weight:bold; padding:5px,5px,5px,5px">'+item.title+'</label>';
					postQ_html += '<label class = "txt_line" style="color: #424242; font-size:4">'+item.content+'</label><br>';
					postQ_html += '<label style="font-weight:lighter; font-size:0.9em"><img src="../img/user.png" style="width: 18px; height: 18px;"alt="..." >&nbsp'+item.nickname+'</label>';
					postQ_html += '&nbsp&nbsp&nbsp<img src="../img/bubble.png">&nbsp<label style="font-size:0.9em"> '+item.reply_cnt+'</label></a>';
					/*postQ_html += '<label>'+item.postdate+'</label>';*/
					postQ_html += '</div></div>';
				});
				$("#Question_Board").html(postQ_html);
			});		
});
