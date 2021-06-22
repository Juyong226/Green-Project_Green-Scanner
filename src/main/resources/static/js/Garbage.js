$(document).ready(function() {
	
	
	$("#selectGarbageListBtn").click(function() {
		var fulltext=$("#fulltext").val();
		//alert(fulltext);
		
		$.post("selectGarbageList.do",
				{
					fulltext:fulltext
			
				}, 
				function(data, status){
					//alert(status);
					alert(data);
					
				}); 
	});
	
});