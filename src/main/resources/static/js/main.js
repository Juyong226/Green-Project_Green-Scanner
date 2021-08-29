$(function(){
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

})

