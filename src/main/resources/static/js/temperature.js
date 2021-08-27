$(document).ready(function(){ 
	function getFormatDate(date){
		var year = date.getFullYear();
		var month = (1 + date.getMonth());
		month = month >= 10 ? month : '0' + month;
		var day = date.getDate();
		day = day >= 10 ? day : '0' + day;
		return 1991 + '-'+ month +'-'+ day +' 00:00:00' ;
	}
	var tdate = getFormatDate(new Date());
	//console.log(tdate);
	
	$.post("../showTemperature.do", 
			{ 
				tdate:tdate
			}, 
			function(data, status){
				
				
				var obj = JSON.stringify(data);
				var sobj = JSON.parse(obj);
		
				
				//flag = 0;
				
					//var str="";
					//if(obj.temperatureList){
						//for(var i=0; i<obj.temperatureList.length-1; i++){
						//	str += obj.temperatureList[i] 
						//}
					//	str += obj.temperatureList[obj.temperatureList.length-1] ;
				//	}
				if(obj.todayfound){

					data = obj.todayfound
					+obj.highestfound
					+obj.lowestfound
					
				
					
				}
			
			
			
				$("#past_temperature").html("30년 전 기온"+sobj);
			
			
						
			
				
			
			});
	


				
		});	
				
				
				
				
					
	
	
	

	
	
	

	


