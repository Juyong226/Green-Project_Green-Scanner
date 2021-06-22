 $(document).ready(function(){
  	
  		// index.html <div id='body'>에 맵이 들어갈 <div> 추가
  		// var mapDiv = '<div id="map" style="width: 100%; height: 88vh;"></div>';
  		// $('#body').html(mapDiv);
  		
  		// 맵 <div>에 맵을 표시 
  		// 기본 맵의 중심은 '역삼동 삼성 멀티캠퍼스 빌딩(역삼역 주변)'으로 설정
  		const multiCampus = new naver.maps.LatLng(37.5012743, 127.039585);
	 	var map = new naver.maps.Map(document.getElementById('map'), {
			    zoom: 17,
			    mapTypeId: 'normal',
			    zoomControl: true,
        		zoomControlOptions: {
            		style: naver.maps.ZoomControlStyle.SMALL,
            		position: naver.maps.Position.RIGHT_TOP
            		},
            	mapDataControl: false,
			    center: multiCampus
			});	
		// 사용자의 현재 위치를 얻은 후 이를 맵의 중심으로 설정하는 fn_setUserCurrentLct() 선언
		// 지속적으로 변하는 userCurrentLocation은 전역변수로 한 번만 선언하여 여러개의 변수가 생기지 않도록 함
		// circle은 반지름이 100m, 반경을 표시하기 위해 생성
		let userCurrentLocation
		let circle = new naver.maps.Circle({
			map: map,
			radius: 100,
			strokeColor: '#7FFF00',
			strokeOpacity: 0.3,
			fillColor: '#7FFF00',
			fillOpacity: 0.3,
			center: multiCampus
		});
		
		// Web API인 navigator를 이용하여 사용자의 위치 정보(위도와 경도)를 얻고,
		// 이를 userCurrentLocation 변수에 초기화하여
		// 맵의 중심과 circle의 중심으로 설정하는 함수
		function fn_setUserCurrentLct() {	
		    if (navigator.geolocation) {
		        
		        navigator.geolocation.getCurrentPosition(function(position){
		        	userCurrentLocation = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
		        	
		        	map.setCenter(userCurrentLocation);
		        	map.setZoom(17);
		        	circle.setCenter(userCurrentLocation);
		        }, function(error){
		        	alert('에러메세지: ' + error.message);
		        }, {
		        	enableHighAccuracy : true, 
		        	maximumAge : 0, 
		        	timeout : 3000 
		        });
		        
		    } else {
		    	alert("geolocation을 지원하지 않음");
		        
		    }
		    $("#map").css("position", "fixed");	
	    }
	    // 함수를 실행
	    // fn_setUserCurrentLct();
	    
	    var currentLocationBtnHtml = '<a href="#"><image src="../img/userLocation.png" style="border: solid 1px black"></a>';	
	    naver.maps.Event.once(map, 'init_stylemap', function() {
	    	var customControl = new naver.maps.CustomControl(currentLocationBtnHtml, {
	    		position: naver.maps.Position.RIGHT_TOP
	    	});
	    	
	    	customControl.setMap(map);
	    
	    	naver.maps.Event.addDOMListener(customControl.getElement(), 'click', function() {
	    		fn_setUserCurrentLct();
	    	});
	    });
	    
	    // 클러스터 로고 관련 설정(이미지, 크기 등)
	    var htmlMarker1 = {
	        content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:12px;color:white;text-align:center;font-weight:bold;background:url(../img/cluster-marker-1.png);background-size:contain;"></div>',
	        size: N.Size(60, 60),
	        anchor: N.Point(20, 20)
	    },
	    htmlMarker2 = {
	        content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:12px;color:white;text-align:center;font-weight:bold;background:url(../img/cluster-marker-2.png);background-size:contain;"></div>',
	        size: N.Size(60, 60),
	        anchor: N.Point(20, 20)
	    },
	    htmlMarker3 = {
	        content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:12px;color:white;text-align:center;font-weight:bold;background:url(../img/cluster-marker-3.png);background-size:contain;"></div>',
	        size: N.Size(60, 60),
	        anchor: N.Point(20, 20)
	    },
	    htmlMarker4 = {
	        content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:12px;color:white;text-align:center;font-weight:bold;background:url(../img/cluster-marker-4.png);background-size:contain;"></div>',
	        size: N.Size(60, 60),
	        anchor: N.Point(20, 20)
	    },
	    htmlMarker5 = {
	        content: '<div style="cursor:pointer;width:40px;height:40px;line-height:42px;font-size:12px;color:white;text-align:center;font-weight:bold;background:url(../img/cluster-marker-5.png);background-size:contain;"></div>',
	        size: N.Size(60, 60),
	        anchor: N.Point(20, 20)
	    };
	    
	    // 서버로 쓰레기통 위치 데이터 요청을 하고, 
	    // 이를 받아 latlngs 배열에 위도와 경도를 추가,
	    // addressArr 배열에 쓰레기통 주소를 추가
	    let marker;
	    let infoWindow;
  		var latlngs = [];
  		var trashCans = [];
  		var addressArr = [];
  		var markers = [];
  		var infoWindows = [];
  		$.post('../showTrashCans.do', 
  				{}, 
  				function(data, status){
  					var object = JSON.parse(data);
  					trashCans = object.trashCanList;
  					//console.log(trashCans);
  					$.each(trashCans, function(index, value) {
  						var address = value.address;
  						var latitude = value.latitude;
  						var longitude = value.longitude;
  						
  						latlngs.push(new naver.maps.LatLng(latitude, longitude));
  						addressArr.push(address);
  					});	
					createMarkers();
					createInfoWindows();
					console.log('markers, infowindows 생성 완료');
					
					// for문을 통해 개별 marker에 클릭 시 infoWindow를 띄우는 이벤트를 단다
					for (var i=0; i<markers.length; i++) {
						naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
					}
					console.log('marker 클릭 이벤트 달기 완료');  					
  				});

  		// 클러스터를 맵 위에 표시
  		// 이미 가지고 있는 markers의 marker에 대하여 적용
  		// 10, 100, 200, 500, 1000을 기준으로 클러스터 로고를 다르게 사용
  		setTimeout(function() { 		
				var markerClustering = new MarkerClustering({
			        minClusterSize: 2,
			        maxZoom: 17,
			        map: map,
			        markers: markers,
			        disableClickZoom: false,
			        gridSize: 150,
			        icons: [htmlMarker1, htmlMarker2, htmlMarker3, htmlMarker4, htmlMarker5],
			        indexGenerator: [10, 100, 200, 500, 1000],
			        averageCenter: true,
			        stylingFunction: function(clusterMarker, count) {
			            $(clusterMarker.getElement()).find('div:first-child').text(count);
			        }
			    });
		   		console.log('맵에 마커 표시 완료');
		   	}, 8000);	
  		
  		// marker 클릭시 실행되는 function
		function getClickHandler(seq) {
			return function(e) {
				marker = markers[seq];
				infoWindow = infoWindows[seq];
				
				if(infoWindow.getMap()) {
					infoWindow.close();
				} else {
					infoWindow.open(map, marker);
				}
			}
		}
  		
  		function createMarkers() {
			// for문을 통해 latlngs 배열에 저장된 정보를 marker에 저장하고,
			// 개별 marker 객체를 markers 배열에 저장
			for (var i=0; i<latlngs.length; i++) {
			
				// 각 latlngs 요소의 위치를 marker에 저장하고 이를 미리 선언한 markers 배열에 추가
					marker = new naver.maps.Marker({
					position: latlngs[i]
				});
				markers.push(marker);
			}
		}
		
		function createInfoWindows() {
			// for문을 통해 addressArr 배열에 저장된 정보를 infoWindow(정보창)에 저장하고,
			// 개별 infoWindow 객체를 infoWidnows 배열에 저장
			for (var i=0; i<addressArr.length; i++) {	
				// 정보창에 입력할 쓰레기통 주소 HTML을 contentString에 저장하고,
				// 각 contentString을 infoWindow에 저장한 뒤
				// 각 infoWindow를 미리 선언한 infoWindows 배열에 추가
				let contentString = [
					'<div style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;background-color:#fff;color:black; text-align:center;border:1px solid #008000; border-radius:14px; opacity:75%">'+					
					'<div style="font-weight: bold;font-size:14px">',addressArr[i],
					'<div>'+
					'</div>'
				].join('');
				infoWindow = new naver.maps.InfoWindow({
					content: contentString,
					maxWidth: 350,
				    borderWidth: 0,
				    anchorColor: "#008000",
					anchorSize: new naver.maps.Size(10, 10)
				});
				infoWindows.push(infoWindow);
			}							
		}
  			
 });