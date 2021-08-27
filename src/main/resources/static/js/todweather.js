$(document).ready(function(){
	let weatherIcon={
			'01':'fas fa-sun',
			'02':'fas fa-cloud-sun',
			'03':'fas fa-cloud'
	}
	$.ajax({
		url:'https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=bd4e8036ff9ebd78f21ba7b838d9934e&units=metric',
		dataType:'json',
		type:'GET',
		success:function(data){
			var $Icon = data.weather[0].icon;
			var $Temp_min = (data.main.temp_min)+'˚';//최저기온
			var $Temp_max = (data.main.temp_max)+'˚';//최고기온
			var $city = data.name;
			
			$('.City').append($city)
			//$('.CurrIcon').append("http://openweathermap.org/img/wn/"+ $Icon +"@2x.png");
			$('.CurrTemp').prepend("최저기온 "+$Temp_min);
			$('.CurrTemph').prepend("최고기온 "+$Temp_max);
			
		}
	})
});