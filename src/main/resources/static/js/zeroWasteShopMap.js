 $(document).ready(function(){
  	
  		// index.html <div id='body'>에 맵이 들어갈 <div> 추가
  		//var mapDiv = '<div id="map" style="width: 100%; height: 65vh;"></div>';
  		//$('#body').html(mapDiv);
  		
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
		        	
		        	map.setCenter(multiCampus); //임시로 multiCampus로 맵의 센터를 적용
		        	map.setZoom(17);
		        	circle.setCenter(multiCampus);
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
	    fn_setUserCurrentLct(); 
	    	    
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
	    // addressArr, nameArr, cdayArr, contactArr 배열에 각각 상점 주소, 상점 이름, 휴무일, 전화번호를 추가
  		let marker;
  		let infoWindow;
  		var latlngs = [];
  		var zeroWasteShop = [];
  		var addressArr = [];
  		var nameArr = [];
  		var cdayArr = [];
  		var contactArr = [];
  		var typeArr = [];
  		var markers = [];
  		let markersOnMap = [];
  		let shopsOnMap = [];
  		var infoWindows = [];
  		
  		$.post('../showZeroWasteShop.do', 
  				{}, 
  				function(data, status){
  					var object = JSON.parse(data);
  					zeroWasteShop = object.zeroWasteShopList;
  					//console.log(zeroWasteShop);
  					$.each(zeroWasteShop, function(index, value){
  						var type = value.type;
  						var address = value.address;
  						var name = value.name;
  						var closeddays = value.closeddays;
  						var contact = value.contact;
  						var latitude = value.latitude;
  						var longitude = value.longitude;
  						
  						latlngs.push(new naver.maps.LatLng(latitude, longitude));
  						addressArr.push(address);
  						nameArr.push(name);
  						cdayArr.push(closeddays);
  						contactArr.push(contact);
  						typeArr.push(type);
  					});
  					
  					// for문을 통해 latlngs, addressArr, nameArr, cdayArr, contactArr 배열에 저장된 정보를 각각 marker와 infoWindow(정보창)에 저장하고,
  					// 개별 marker와 infoWindow 객체를 각각 markers, infoWidnows 배열에 저장
  					 
					for (var i=0; i<latlngs.length; i++) {
					// for문과 if-else구문을 통해 types의 type이 ma인 경우 초록색 마커를, 그 이외인 경우 보라색 마커를 생성하여 markers 배열에 저장
  						if(typeArr[i].substr(0,2)=="ma") {							
  								marker = new naver.maps.Marker({
  								position: latlngs[i],
  								icon: {
									url:'../img/greenmarker.png'
								}
							});													
						} else {
								marker = new naver.maps.Marker({
								position: latlngs[i],
								icon: {
									url:'../img/purplemarker.png'
								}
							});
						};
						markers.push(marker);

						// 정보창에 입력할 상점 주소, 상점 이름, 휴무일, 전화번호 HTML을 contentString에 저장하고,
						// 각 contentString을 infoWindow에 저장한 뒤
						// 각 infoWindow를 미리 선언한 infoWindows 배열에 추가
						let contentString = [
							'<div style="padding-top:18px;padding-bottom:4px;padding-left:5px;padding-right:5px;background-color:#fff;color:black; text-align:center;border:1px solid #008000; border-radius:14px; opacity:75%">'+					
							'<div style="font-weight: bold;font-size:14px">'+
							'<h3>&nbsp;&nbsp;' + nameArr[i] + '&nbsp;&nbsp;</h3>',
							'<p>&nbsp;&nbsp;' + addressArr[i] + '&nbsp;&nbsp;<br>',							
							'&nbsp;&nbsp;전화번호: ' + contactArr[i] + '&nbsp;&nbsp;<br>',
							'&nbsp;&nbsp;휴무일: ' + cdayArr[i] + '&nbsp;&nbsp;',
							'<div>'+
							'</div>'	
						].join('');
						infoWindow = new naver.maps.InfoWindow({
							content: contentString,
							maxWidth: 350,
							anchorColor: "#008000",
							borderWidth: 0,
							anchorSize: new naver.maps.Size(10, 10)
						});
						infoWindows.push(infoWindow);		
					};
					
					// for문을 통해 개별 marker에 클릭 시 infoWindow를 띄우는 이벤트를 단다
					for (var i=0; i<markers.length; i++) {
						naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
					};
					
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
					
			  		// 클러스터를 맵 위에 표시
			  		// 이미 가지고 있는 markers의 marker에 대하여 적용
			  		// 10, 100, 200, 500, 1000을 기준으로 클러스터 로고를 다르게 사용
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
				    
				    updateList(map, markers);
				    
				    // 맵 전체에 화면 움직임이 멈출 때마다(사용자가 맵을 만지지 않는 상태가 되면) 화면에 표시된 마커로 리스트를 업데이트 하는 이벤트를 달아줌
					naver.maps.Event.addListener(map, 'idle', function(){
						updateList(map, markers);
					});
					
										
  		});
  		
  		// 상점 리스트 클릭 시 해당 상점의 이름을 받아 showThisOnly()를 호출
  		$('#markerList tbody').on('click', '.shopName', function() {
			let thisShop = $(this).text();
			let index;
			for (var i=0; i<nameArr.length; i++) {
				if(nameArr[i] === thisShop) {
					index = i;
				}
			}
			showThisOnly(map, thisShop, index);
		});
		
  		// 화면이 멈출 때 실행되는 function
		// 현재 보이는 화면의 Bounds(화면에 보이는 지도 범위의 좌표 경계값)를 얻고,
		// markers 배열의 marker 중 현재 Bounds 내부에 위치한 marker만 리스트에 표시
		function updateList(map, markers) {
			let mapBounds;
			let position;
			let content = [];
			let html;
			
			mapBounds = map.getBounds();
			
			// for문을 통해 makers 배열의 모든 marker의 위경도 데이터를 position에 초기화
			for (var i=0; i<markers.length; i++) {
				
				marker = markers[i];
				position = marker.getPosition();
				
				// 현재 Bounds에 position이 포함되었는지 여부를 검사하고,
				// 포함된 경우 리스트에 추가, 아닌 경우 리스트에서 삭제
				if(mapBounds.hasLatLng(position)) {
					let flag = 0;
					
					// 현재의 marker가 이미 리스트에 포함된 marker인지 확인하는 작업
					// 최초로 Bounds 내부로 marker가 진입했을 시 전역변수 shopsOnMap 배열에 해당 상점의 이름을, markersOnMap에 해당 상점의 마커를 삽입
					// for와 if문을 통해 shopsOnMap에 현재 marker의 상점 이름이 있는지 확인하고,
					// 있다면 flag값을 1로 변경하여 리스트에 중복되어 추가되는 것을 방지,
					// 없다면 리스트에 해당 marker의 상점 정보를 노출시킴
					for (var k=0; k<shopsOnMap.length; k++) {
						if(shopsOnMap[k] === nameArr[i]) {
							flag = 1;										
						}
					}
					if(flag == 0) {
						shopsOnMap.push(nameArr[i]);
						markersOnMap.push(marker);
						content = [
							'<tr class="elements" id=' + i + '><td style="font-weight: bold;font-size:25px; padding-left:15px;" class="shopName" colspan="3"><b>' + nameArr[i] + '</b></td></tr>',
							'<tr class="elements" id=' + i + '><td style="padding-left:20px;" colspan="3">' + addressArr[i] + '</td></tr>',
							'<tr class="elements" id=' + i + '><td style="padding-left:20px;">전화번호</td><td style="position:absolute; left: 100px;" colspan="2">' + ": "+contactArr[i] + '</td></tr>',
							'<tr class="elements" id=' + i + '><td style=" padding-left:20px;">휴무일 </td><td style="position:absolute; left: 100px;" colspan="2">' + ": "+cdayArr[i] + '</td></tr>',							
							'<tr class="elements" id=' + i + '><td colspan="3"><hr></td></tr>'
						]
						html = content.join('');
						$('#markerList>tbody').append(html);
					}
				
				// 현재 Bounds에 position이 포함되지 않은 경우
				// 리스트에서 현재 marker의 데이터를 삭제	
				} else {
				
					// for와 if문을 통해 현재 shopsOnMap에 현재 marker의 상점 이름이 있는지 확인하고,
					// 있다면(이전 Bounds 내부에 위치하였다가 현재 Bounds 밖에 위치한 경우) shopsOnMap 배열에서 해당 상점의 이름을, markersOnMap에서 해당 상점의 마커를 삭제
					for (var j=0; j<shopsOnMap.length; j++) {
						if(shopsOnMap[j] === nameArr[i]) {
							$('#markerList>tbody>tr').remove('#' + i);
							shopsOnMap.splice(j, 1);
							markersOnMap.splice(j, 1);
							j--;
						}
					}								
				} 
			}
							
			if( $('#markerList>tbody>tr').hasClass('elements') ) {
				$('#markerList>tbody>tr').remove('.tableIsEmpty');
			} else {		
				if( !$('#markerList>tbody>tr').hasClass('tableIsEmpty') ) {				
					content = [
						'<tr class="tableIsEmpty"><td></td></tr>',
						'<tr class="tableIsEmpty"><td><h3 id="zerowastenone"><br><br><br>주변에 위치한 제로 웨이스트 상점이 없습니다</h3></td></tr>'
					].join('');
					$('#markerList>tbody').append(content);
				}
			}
		}
		
		// 리스트에서 원하는 상점의 이름을 클릭 시 해당 상점의 마커와 정보창만 표시하는 함수
		function showThisOnly(map, thisShop, index) {
			infoWindow = infoWindows[index]
			for (var i=0; i<shopsOnMap.length; i++) {
				if(shopsOnMap[i] === thisShop) {
					if(infoWindow.getMap()) {
						infoWindow.close();
					} else {
						infoWindow.open(map, markersOnMap[i]);
					}
				}
			}
		}
		
				
	/*
		// 맵 전체에 화면 움직임이 멈출 때마다(사용자가 맵을 만지지 않는 상태가 되면) 보이는 화면에 marker를 띄우는 이벤트를 단다
		naver.maps.Event.addListener(map, 'idle', function(){
			updatemarkers(map, markers);
		});
		
		// 화면이 멈출 때 실행되는 function
		// 현재 보이는 화면의 Bounds(화면에 보이는 지도 범위의 좌표 경계값)를 얻고,
		// markers 배열의 각 marker 중 현재 Bounds 내부에 위치한 marker만 표시하고 아닌 marker는 숨김
		function updatemarkers(map, markers) {
			var mapBounds = map.getBounds();
			var marker;
			var position;
			
			for (var i=0; i<markers.length; i++) {
				marker = markers[i];
				position = marker.getPosition();
				
				if(mapBounds.hasLatLng(position)) {
					showMarker(map, marker);
				} else {
					hideMarker(map, marker);
				}
			}
		}
		
		function showMarker(map, marker) {
			if(marker.setMap()) {
				return;
			} else {
				marker.setMap(map);
			}
		}
		
		function hideMarker(map, marker) {
			if(!marker.setMap()) {
				return;
			} else {
				marker.setMap(null);
			}
		}
	*/
  		  
 });