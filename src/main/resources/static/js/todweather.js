$(document).ready(function(){
	function getFormatDate(date){
		var year = date.getFullYear();
		var month = (1 + date.getMonth());
		month = month >= 10 ? month : '0' + month;
		var day = date.getDate();
		day = day >= 10 ? day : '0' + day;
		return year + '-'+ month +'-'+ day  ;
	}
	
	//console.log(tdate);
	$.ajax({
		url:'https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=key&units=metric',
		dataType:'json',
		type:'GET',
		success:function(data){
			//var $Icon = data.weather[0].icon;
			var $Temp_min = (data.main.temp_min)+'˚';//최저기온
			var $Temp_max = (data.main.temp_max)+'˚';//최고기온
			var $city = data.name;
			var $tdate = getFormatDate(new Date());
			$('.City').append($city)
			//$('.CurrIcon').append("http://openweathermap.org/img/wn/"+ $Icon +"@2x.png");
			$('.CurrTemp').prepend("최저기온 "+$Temp_min);
			$('.CurrTemph').prepend("최고 기온 "+$Temp_max);
			$('.CurrDate').prepend($tdate);
			
		}
	})
});
