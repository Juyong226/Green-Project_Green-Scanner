<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<!-- navbar Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="../css/my.css">
	<link rel="icon" type="image/x-icon" href="../assets/favicon.ico" />
<title>게시판</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="../js/login.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#cancleBtn").click(function(){
		if(confirm("글 수정을 취소하시겠습니까? 수정한 내용이 저장되지 않습니다.")==true)
			history.back();
	});
});
</script>

</head>
<body>
<!-- 1번 navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-back">
		<div class="container-fluid">
		<div class="text-left">	
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      			<span class="navbar-toggler-icon"></span>     
    		</button>
			<a style='color:white;' class="navbar-brand navcolor" href="index.html">GarbageCollector</a>
		</div>
	   
	    <div class="collapse navbar-collapse" id="navbarSupportedContent">
	    	<ul class="navbar-nav me-auto mb-2 mb-lg-0">
		        <li id="loginBlankDiv1" class="nav-item">	        
		        	<a style='color:white;' class="nav-link" aria-current="page" href="#" onclick="window.open('/html/loginForm.html', 'indexwindow', 'toolbar=yes,scrollbars=yes,resizable=yes,top=50,left=500,width=400,height=400');" >로그인</a>
		        </li>
		        <li id="loginBlankDiv2" class="nav-item">
		        	<a style='color:white;' class="nav-link" href="#" onclick="window.open('/html/memberInsertForm.html', '_blank', 'toolbar=yes,scrollbars=yes,resizable=yes,top=50,left=500,width=400,height=750');">회원가입</a>
		        </li>
		        <li id="nicknameDiv" class="nav-item">
		        </li>
		        <li id="logoutDiv" class="nav-item">
		        </li>
		        <li class="nav-item">
		          	<a style='color:white;' class="nav-link" href="#" aria-disabled="true">공지사항</a>
		        </li>
		        <li class="nav-item">
		          	<a style='color:white;' class="nav-link" href="html/questionBoard.html" aria-disabled="true">FAQ</a>
		        </li>
		        <li class="nav-item">
		          	<a style='color:white;' class="nav-link" href="#" aria-disabled="true">개인정보처리방침</a>
		        </li>
	      	</ul>
			<form class="d-flex">
		        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
		        <button id="searchBtn" class="btn btn-outline-success" type="submit">Search</button>
		    </form>
	    </div>
	</div>
</nav>
<!-- 글 쓰기 -->
<form name="form1" action="updatePostSave" style="width:100%">
	<div class="board-font" style="padding: 20px; ">
		<!-- 게시판구분 -->
		<div class="form-group">
			<label for="title" class="col-sm-2 control-label" style="font-size:14px; font-weight: bold">게시판구분</label>
			<div class="col-sm-10 my-2">
			<select name="selectBox" id="boardname">
				<option class="form-control" value="bulletinBoard"
				<c:if test = "${post.boardname=='자유게시판'}">selected</c:if>>자유게시판</option>
				<option class="form-control" value="questionBoard"
				<c:if test = "${post.boardname=='질문게시판'}">selected</c:if>>질문게시판</option>
			</select>
			</div>
		</div>
		<!-- 제목 -->
		<div class="form-group my-3">
			<label for="title" class="col-sm-2 control-label mb-1" style="font-size:14px; font-weight: bold">제목</label>
			<div>
				<input type="text" id="title" name="title" style="width:100%" class="form-control" placeholder="제목을 입력해 주세요." value="<c:out value="${post.title}"/>">
			</div>
		</div>
		<!-- 닉네임 -->
		<div class="form-group my-3">
			<label for="writer" class="col-sm-2 control-label mb-1" style="font-size:14px; font-weight: bold">닉네임</label>
			<div>
				<input type="text" id="nickname" name="nickname" style="width:100%" disabled class="form-control" placeholder="닉네임을 입력해 주세요." value="<c:out value="${post.nickname}"/>" />
			</div>
		</div>
		<!-- 내용 -->
		<div class="form-group my-3">
			<label for="content" class="col-sm-2 control-label mb-1" style="font-size:14px; font-weight: bold">내용</label>
			<div>
				<textarea id="content" name="content" style="width:100%" class="form-control" placeholder="내용을 입력해 주세요."><c:out value="${post.content}"/>
				</textarea>
			</div>
		</div>
		<input type="hidden" name="postno" value="<c:out value="${post.postno}"/>">
		<input type="hidden" name="boardname" value="<c:out value="${post.boardname}"/>">
		<br><br><br>
		
		<!-- 등록/취소 버튼 -->
		<div class="text-center">
			<input type="button" onclick="form1.submit()" class="btn btn-success waves-effect waves-light" id="registerPostBtn" value="등록" style="background-color: #5CAB7D; border-color: #5CAB7D">
			<input type="button" class="btn btn-light waves-effect waves-light" id="cancleBtn" value="취소">
		</div>
	</div>
</form>


<!-- Fixed Bottom -->
<nav class="navbar fixed-bottom navbar-light bg-light">
	<div class="container-fluid">
				<!-- 이미지 수정 필요 -->
		<a class="navbar-brand" href="#"></a>
		<a class="navbar-brand" href="index.html"><img src="img/homewhite.png" alt="..." ></a>
		<a class="navbar-brand" href="#"></a>
		<a class="navbar-brand" href="html/SWI.html"><img src="img/camerawhite.png" alt="..." ></a>
		<a class="navbar-brand" href="#"></a>
		<a class="navbar-brand" href="html/SWV.html"><img src="img/microphonewhite.png" alt="..." ></a>
		<a class="navbar-brand" href="#"></a>
		<a class="navbar-brand" href="html/trashCanMap.html"><img src="img/mapwhite.png" alt="..." ></a>
		<a class="navbar-brand" href="#"></a>
		<a class="navbar-brand" href="html/boardPage.html"><img src="img/clipboard.png" alt="..." ></a>
		<a class="navbar-brand" href="#"></a>
	</div>
</nav>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>	
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script>
//여기에 이 코드를 쓰기 위해선 loginForm.html에도 위 쿠키 라이브러리르 추가해 주어야합니다.
$(function() {
	var blank=$.cookie("blanked");
 	var login=$.cookie("logined"); 
 	var logout=$.cookie("logouted"); 
		$("#loginBlankDiv1").html(blank);
		$("#loginBlankDiv2").html(blank);
 		$("#nicknameDiv").html(login);
 		$("#logoutDiv").html(logout);
});
</script>
</html>