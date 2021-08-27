$(document).ready(function() {
	$('#loading').hide();
	
	var voices = [];
	
	function setVoiceList() {
		voices = window.speechSynthesis.getVoices();
	}
	setVoiceList();
	
	if (window.speechSynthesis.onvoiceschanged !== undefined) {
		window.speechSynthesis.onvoiceschanged = setVoiceList;
	}
	
	function speech(txt) {
		if(!window.speechSynthesis) {
			alert("음성 재생을 지원하지 않는 브라우저입니다. 크롬, 파이어폭스 등의 최신 브라우저를 이용하세요");
			return;
		}
		
		var lang = 'ko-KR';
		var utterThis = new SpeechSynthesisUtterance(txt);
		
		utterThis.onend = function (event) {
			console.log('end');
		};
		
		utterThis.onerror = function(event) {
			console.log('error', event);
		};
		
		var voiceFound = false;
		for(var i = 0; i < voices.length ; i++) {
			if(voices[i].lang.indexOf(lang) >= 0 || voices[i].lang.indexOf(lang.replace('-', '_')) >= 0) {
				utterThis.voice = voices[i];
				voiceFound = true;
			}
		}
		
		if(!voiceFound) {
			alert('voice not found');
			return;
		}
		utterThis.lang = lang;
		utterThis.pitch = 1;
		utterThis.rate = 1; //속도
		window.speechSynthesis.speak(utterThis);
	}
	
	// file을 base64로 인코딩 하기위해 FileReader 객체를 reader에 초기화
	var reader = new FileReader();
	
	// reader.readAsDataURL(file)이 성공적을 호출되었을 때 실행하는 함수
	// 1. base64로 이미지 인코딩 
	// 2. base64 데이터를 서버로 전송하여 이미지 분석 요청
	reader.addEventListener('load', () => {
		// DataURL은 <Data:image/jpeg;base64,[data]> 이런 형식을 갖는데,
	   	// 여기서 base64만을 추출하기 위한 절차임
	   	// , 뒤에 base64 데이터가 오기 때문에 콤마 뒤의 인덱스부터 마지막 인덱스까지를 변수 base64에 초기화하는 코드
	   	var dataIndex = reader.result.indexOf(',') + 1;
	   	var base64 = reader.result.substring(
			dataIndex,
	      	reader.result.length
	  	);
	   
	   	// 이미지 분석을 위해 서버로 보낼 데이터
	   	var userImgData = {
	      	data: base64
	   	}
	   
	   	console.log(JSON.stringify(userImgData));         	  
	   	var detectionResult;
	   	var detectedImg;
	   	$('#loading').show();
	   	// 서버로 base64 데이터를 보내 이미지 분석을 요청 
		$.post('https://greenscanner.org:8888/swi',
				JSON.stringify(userImgData),
				function(data, status){
		            var obj = JSON.parse(data);
		            console.log(obj);
		            if(!obj.err) {
						imgClass = obj.class;
						detectionResult = obj.seg;
						detectedImg = 'data:image/jpg;base64,'+ detectionResult;
						$('#userPic').attr('src', detectedImg);
						$('#userwrapper').css("overflow", "hidden");
						$('#userPic').css("margin","-10% -10% -10% -10%");
						$('#userPic').css('max-width', '80%');
						$('#userPic').css('height', 'auto');
						// 이미지 분석 서버의 응답이 성공적인 경우 배출 방법을 조회함 
						getGarbageDM(imgClass);
		            } else {
		            	const msg = '<h3>이미지 분석에 실패하였습니다.<h3><br>다시 시도해 주세요.';
		                $('#content').html(msg);
						$('#loading').hide();
		            }
				});
	});	

	// 사진을 찍어 업로드를 하면 서버로 이미지 분석 요청을 보냄
	var file
	var imgClass; 
	// ->> 나중에 text를 분석하는 함수에서도 쓰이기 때문에 전역변수로 빼 놓음
	// 사진을 업로드하면 업로드한 사진을 userPic에 보여준다.
	
	$('#imageInput').change(function(e) {
		$('#content').empty();
		$('#userPic').empty();
		$('#userPic').attr('src', URL.createObjectURL(e.target.files[0]));

		URL.revokeObjectURL(e.target.files[0]);
			      
		// 업로드 된 이미지를 file에 초기화
		file = $('#imageInput')[0].files[0];
		//console.log(file);
		
		// reader.readAsDataURL(file) 이게 실행되었기에 위에 모든 명령이 실행된 것
		// 53~96까지가 아래 한 줄에 담겨있는 실행문 
		reader.readAsDataURL(file);
	});
				
	// how 이미지가 리턴된 후 자동으로 실행되는 부분
	// 이미지 분석이 완료된 후 배출방법조회 버튼을 클릭했을 때 실행되는 함수
	// 이미지 분석 후 받은 imgClass(쓰레기 분류 키워드)를 DB에서 찾아 배출방법을 불러오는 과정
	function getGarbageDM(imgClass) {
		var userKeyword = imgClass;	                
		$.post('../selectGarbageList.do',
			{
				fulltext: userKeyword	
			},
			function(data, status) {
	 			$('#loading').hide();
	 			$('#content').empty();
				var obj = JSON.parse(data);
				if(obj.garbagefound) {					
					data = "<h1>" + obj.garbagefound + "</h1><br>"
						+ "<h3>" + obj.garbagedmfound + "</h3><br>"
						+ "<h5>원하는 결과를 얻지 못하셨다면 쓰레기 이름을 직접 입력해 검색해 보세요.</h5><br>"
						+ "<div id='searchWaste'><input size='15' id='fulltext1'><input type='button' id='selectGarbageListBtn1' value='검색'></div>";
	       			$("#resultDiv").html(data);
	       			speech("" + obj.garbagedmfound);
				} else {
					data = "<h1>" + obj.msg + "</h1><br>"
						+ "<h5>원하는 결과를 얻지 못하셨다면 쓰레기 이름을 직접 입력해 검색해 보세요.</h5><br>"
						+ "<div id='searchWaste'><input size='15' id='fulltext1'><input type='button' id='selectGarbageListBtn1' value='검색'></div>";
					$("#resultDiv").html(data);
				}
	 		});
	 };
	 
	 // 원하는 값을 얻지 못했을 때 사용자가 직접 검색한 키워드를 가지고 배출방법을 조회하는 함수
	$(document).on("click", "#selectGarbageListBtn1", function(event) {
		$('#loading').show(); //로딩표시
	 	$('#content').html('배출방법을 조회하는 중입니다.<br>잠시만 기다려 주세요.');
		var userKeyword = $("#fulltext1").val();
		
		$.post("../selectGarbageList.do",
			{
				fulltext: userKeyword	
			}, 
			function(data, status) {
				$('#loading').hide();
				$('#content').empty();
				var obj=JSON.parse(data);
				if(obj.garbagefound) {						
					data = "<h1>"+obj.garbagefound+"</h1><br>"
						+"<h3>"+obj.garbagedmfound+"</h3><br>";
					$('#loading').hide();
					$("#resultDiv").html(data);
					speech(""+obj.garbagedmfound);
				} else {
					data = "<h1>"+obj.msg+"</h1><br>"
						+"<h5>원하는 결과를 얻지 못하셨다면 쓰레기 이름을 직접 입력해 검색해 보세요.</h5><br>"
						+"<div id='searchWaste'><input size='15' id='fulltext1'><input type='button' id='selectGarbageListBtn1' value='검색'></div>";
					$("#resultDiv").html(data);
					
				}
			});
	});

});