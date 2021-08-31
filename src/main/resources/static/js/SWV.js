$(document).ready(function() {
	
	// voices 리스트를 만든다.
	var voices = [];	
	
/*	 window.speechSynthesis.getVoices()는 현재의 디바이스에서 사용할 수 있는 SpeechSynthesisVoice objects들의 list를 반환한다. speechSynthesis의 경우 window가 가지고 있는 method이고 window는 내장 객체이기 때문에 사실 
	speechSynthesis.getVoices()으로만 선언해도 무방하다.
*/	
	function setVoiceList() {
		voices = window.speechSynthesis.getVoices();
		console.log(voices);
	}

/*	위에 정의된 함수를 실행한다.
 	이 방식은 chrome을 제외한 브라우저에서 setvoiceList를 실행해 voiceList를 반환받는 구문이다. 만약 사용자의 browser가 크롬이라면 이 구문은 필요하지 않고 이와 반대로
 	사용자의 브라우저가 크롬외의 다른 것이라면 아래의 chrome에서 voiceList들을 할당받는 구문인 if문이 필요하지않다. chrome사용자가 이를 확인해 보고 싶다면 아래의 console.log(voices);의 주석을 
 	지우고 실행을 해보자. js의 실행 순서상 console.log의 실행이 setVoiceList();-> console.log(voices);-> if문 순으로 실행됨에도 비어있는 list를 확인할 수 있다.		 
*/
	setVoiceList();	
	//console.log(voices);

/*	chrome에서 voiceList를 할당받는 구문이다.
  	chrome은 다른 browser들과 달리 eventhandler에 의해 getVoices method를 다뤄주지 않으면 list들을 받아오지 못한다.
  	따라서 "window.speechSynthesis.onvoiceschanged"의 method와 같이 voiceList가 변할 때 작동하는 eventhandler가 항상 null값을 갖게된다.
  	우리는 이 성질을  이용해 이벤트 핸들러를 작동시키고 또 이것으로 getVoices method를 작동시키는 것이다.
 	onvoiceschanged eventhandler를 이해하기 위해서는 우리에게 익숙한 onClick eventhandler를 떠올리면 된다. onclick event가 event가 발생하면 작동하는 것처럼
 	onvoiceschanged event도 아래와같이 window.speechSynthesis.onvoiceschanged=function(){};처럼 정의해주면 if문의 조건에 맞을 때 function method가 작동을 하게된다.
 	
 	참고하면 좋은 사이트
 	js의 실행 순서: https://velog.io/@_jouz_ryul/%EC%9E%90%EB%B0%94%EC%8A%A4%ED%81%AC%EB%A6%BD%ED%8A%B8%EC%99%80-%EC%97%94%EC%A7%84-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%8B%A4%ED%96%89-%EB%B0%A9%EC%8B%9D#4-%EC%9D%B4%EC%8A%88-%EA%B2%B0%EB%A1%A0%EC%9D%80-promise%EB%8B%A4
 	함수 호출시 "()" 유무: https://stackoverflow.com/questions/30751892/difference-of-calling-a-function-with-and-without-parentheses-in-javascript#:~:text=2%20Answers&text=With%20parentheses%20the%20method%20is,the%20function%20in%20the%20variable
	js undefined와 null비교: https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Global_Objects/null
 */
	if (window.speechSynthesis.onvoiceschanged !== undefined) {
		window.speechSynthesis.onvoiceschanged = setVoiceList;
		
	}
	
/*    현재 위와같은 방법은 실행상에 오류가 가끔 있는것으로 보인다. 그 이유는 크롬이 비동기로 작동하는 것이 원인으로 추측되는데 아래와 같은 방법같이 아예 js가 완전히 로드되고 서버의 데이터가 처리 된 후 받아오는 방법도 있다
 		그렇지만 현재로썬 이 방법 또한 완전히 안전한 방법인지 알 수 없다.
  */
	/*
	setTimeout(() => {
		setVoiceList();
	}, 0);
	*/
	//입력받은 garbagename에 따라 분리수거 방법을 음성으로 알려줄 function
	function speech(txt) {
		//음성지원이 되지 않는 브라우저에 띄울 메세지
		if(!window.speechSynthesis) {
			alert("음성 재생을 지원하지 않는 브라우저입니다. 크롬, 파이어폭스 등의 최신 브라우저를 이용하세요");
			return;
		}
		
	//인식대상 음성의 언어를 선택
		var lang = 'ko-KR';
	
	/*	interface이다. "xxx 어디에 버려"라고 input에 입력된 데이터와 같이 어떤 내용을 읽을지
		그리고 어떻게 읽을지에 대한 설정을 할 수 있다.
	*/
		var utterThis = new SpeechSynthesisUtterance(txt);
		
	//음성으로 대답하는 것이 끝났을 때 작동할 function을 정의해주는 eventhandler이다.
		utterThis.onend = function (event) {
			console.log('end');
		};

		utterThis.onerror = function(event) {
			console.log('error', event);
		};
	/*	voices의 list들은 이런식으로 반환된다. 
	 	SpeechSynthesisVoice {voiceURI: "Google Deutsch", name: "Google Deutsch", lang: "de-DE", localService: false, default: true}
	  	‘ko-KR’을 ‘ko_KR’ 로 바꿔서 비교하는 이유는 모바일 브라우저에서 언어셋이 "ko_KR"인 경우가 있기 때문이다.
	 */
		var voiceFound = false;
		for(var i = 0; i < voices.length ; i++) {
			if(voices[i].lang.indexOf(lang) >= 0 || voices[i].lang.indexOf(lang.replace('-', '_')) >= 0) {
				utterThis.voice = voices[i];
				voiceFound = true;
				console.log(voices[i].lang.indexOf(lang));
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
	
	let recognition = null;
	var fulltext=null;
	var count = 0;
	var flag = 0;
	
    function checkCompatibility() {
      recognition = new(window.SpeechRecognition || window.webkitSpeechRecognition)();
      recognition.lang = "ko-KR";
      recognition.maxAlternatives = 5;

      if (!recognition) {
        alert("You cannot use speech api.");
      }
    }

    function startSpeechRecognition() {
      console.log('Start');

      recognition.addEventListener("speechstart", () => {

    	  console.log('Speech Start');
    	  count = 0;
      });

      recognition.addEventListener("speechend", () => {
        console.log('Speech End');
        flag = 1;
      });

      recognition.addEventListener("result", (event) => {
    	  console.log('Speech Result', event.results);
          fulltext = event.results[0][0].transcript;
          if(flag==1 && count == 0){
        	  count++;
        	  getResult();
          }
          
      });

      recognition.start();
    }

    function endSpeechRecognition() {
      recognition.stop();
    }

    window.addEventListener('load', checkCompatibility);
	
    $("#speakBtn").click(function() {
    	startSpeechRecognition();
    });
    
	$('#loading').hide();

    function getResult(){

		if(fulltext=="" ||  fulltext=="empty string"){
			return;
		}
		$('#content').hide();
		$('#loading').show(); //로딩표시
		
		$.post("../selectGarbageList.do",
				{
					fulltext:fulltext
				}, 
				function(data, status){
					var obj=JSON.parse(data);
					flag = 0;
					$('#loading').hide();
					$('#content').show();
					if(obj.garbagefound){
						var str = "";
						if(obj.garbageList){
							str += "이런 쓰레기도 검색해보세요 : "
							for(var i=0; i<obj.garbageList.length-1; i++){
								str += obj.garbageList[i] + ","
							}
							str += obj.garbageList[obj.garbageList.length-1];
						}
						$("#question").html(obj.garbagefound);
						data = obj.garbagedmfound+"<br><br>"
						+ str
						+"<div id='boxComponent'><input id='textBox'><input id='searchBtn' type='button' value='직접 검색'>";
						$("#box").html(data);
						// 소리가 중복으로 나오는 문제 해결
						speech(""+obj.garbagedmfound);

					}else{
						data = obj.msg+".<br><br>"
						+"인식한 문장 : \"" + fulltext + "\"<br>"
						+"원하는 결과를 얻지 못하셨다면 쓰레기 이름을 직접 입력해 검색해 보세요.<br>"
						+"<div id='boxComponent'><input id='textBox'><input id='searchBtn' type='button' value='직접 검색'>"
						+"<a href='questionBoard.html'><button style='background-color: #5CAB7D;'>질문하러가기</button></a></div>";
						$("#box").attr(data);
					}
				});
    }

	// 검색 버튼 클릭시
	$(document).on("click", "#searchBtn", function(event){
		fulltext = $("#textBox").val();
		if(fulltext==""){
			return;
		}
		$('#content').hide();
		$('#loading').show(); //로딩표시
		
		$.post("../selectGarbageList.do",
				{
					fulltext:fulltext	
				}, 
				function(data, status){
					var obj=JSON.parse(data);
					$('#loading').hide();
					$('#content').show();
					var str = "";
					if(obj.garbageList){
						for(var i=0; i<obj.garbageList.length-1; i++){
							str += obj.garbageList[i] + ","
						}
						str += obj.garbageList[obj.garbageList.length-1];
					}
					if(obj.garbagefound){
						$("#question").text(obj.garbagefound);
						data = obj.garbagedmfound+"<br><br>"
						+"이런 쓰레기도 검색해보세요: "
						+str;
						$("#box").html(data);
						speech(""+obj.garbagedmfound);
					}else{
						data = obj.msg+".<br><br>"
						+"원하는 결과를 얻지 못하셨다면 쓰레기 이름을 직접 입력해 검색해 보세요.<br>"
						+"<div id='boxComponent'><input id='textBox'><input id='searchBtn' type='button' value='직접 검색'>";
						$("#box").html(data);
					}
				}); 
	});

});