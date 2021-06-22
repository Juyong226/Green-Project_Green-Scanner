

$(document).ready(function(){

	$.post("../boardListB.do", 
			{},
			function(data, status){
				//alert("요청 보냄");
				var obj = JSON.parse(data); //Json Object를 파싱
				//자유게시판 화면 처리
				var postB = obj.postListB; //내용에 해당하는 부분만 postB에 담기 
				var postB_html = "<div class='mx-3'>"; //화면에 보여줄 postB_html 선언
				//allpost를 돌면서 각 item의 요소들을 postB_html에 담기, board.xml에서 날짜순으로 내림차순 정렬(order by postdate desc)해 최신순으로 보임 
//				postB.forEach(function(item){ 
//					postB_html += '<li><a class="cls1" href="/viewPost?postno='+item.postno+'"style="text-decoration:none; color:black">'+ item.title + '</a></li>';
//				});
				
				for(var i=0; i<5; i++){
					postB_html += '<img style="width: 7px;" src="../img/nextpageBtn.png" alt="..." ><a class="cls1" href="/viewPost?postno='
						+ postB[i].postno +'"style="text-decoration:none; color:black"> '
						+ postB[i].title + '</a><br>';
				}
				postB_html += "<br></div>";
				$("#mainboard").html(postB_html); 
			});		
	
	var checkEvent = getCookie("Ck_01");
	// checkEvent == on이면 애니메이션은 하루에 한번만 뜸
	if(checkEvent == "on") {
		$('#bg_wrapper').hide();
	} else {
		$('#maincontainer').hide();
		var img = '<img id="mainbg" src="img/main_bg.gif" style="width:100%; background: #000000;">';
		
		$('#bg_wrapper').html(img);
		$('#bg_wrapper').css("background", "#1E2F30");
		setTimeout(function(){
		$('#bg_wrapper').fadeOut();
		$('#maincontainer').show();
		}, 3000);
	}
	
	setCookie("Ck_01","on","1")

	function setCookie(cName, cValue, cDay){
		var expire = new Date();
		expire.setDate(expire.getDate() + cDay);
		cookies = cName + '=' + escape(cValue) + '; path=/ '; // 한글 깨짐을 막기위해 escape(cValue)를 합니다.
		if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
		document.cookie = cookies;
	}

	function getCookie(cName) {
		cName = cName + '=';
		var cookieData = document.cookie;
		var start = cookieData.indexOf(cName);
		var cValue = '';
		if(start != -1){
		start += cName.length;
		var end = cookieData.indexOf(';', start);
		if(end == -1)end = cookieData.length;
			cValue = cookieData.substring(start, end);
		}
		return unescape(cValue);
	}
});


