<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<%
  request.setCharacterEncoding("UTF-8");
%> 

<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- navbar Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/my.css">
    <title>GarbageCollector</title>
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <!-- hot 게시글 bootstrap -->
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src="../js/login.js"></script>
</head>

<script type="text/javascript">
	/*댓글 삭제 처리*/
	function fn_replyDelete(reno){
		if(!confirm("댓글을 삭제하시겠습니까?")){
			return;
		}
		var form = document.form2;
		form.action="deleteReply";
		form.reno.value = reno;
		form.submit();
		alert("댓글이 삭제되었습니다.");	
	}
	
	/*게시글 삭제 처리*/
	function fn_postDelete(postno){
		
		if(!confirm("글을 삭제하시겠습니까?")){
			return;
		}
		var frm = document.frmArticle;
		frm.action="deletePost";
		frm.postno.value = postno;
		frm.submit();
		alert("글이 삭제되었습니다.");	
	}
	
	var updateReno = updateRememo = null;
	/*댓글 수정 폼 보이기
	숨겨져 있던 댓글 입력창(replyUpdateDiv)을 보여주며 기존에 입력한 댓글 내용을 받아와 넣어준다. 
	댓글 수정 상태에서 취소/저장을 하지 않고 다른 글을 수정하려고 할 때 현재 입력하던 내용을 취소하기 위해 
	현재 입력하는 댓글 번호를 updateReno에 저장한다. */
	
	function fn_replyUpdate(reno){
		var form = document.form2;
		var reply = document.getElementById("reply"+reno);
		var replyDiv = document.getElementById("replyUpdateDiv");
		replyDiv.style.display = "";
	
		if(updateReno){
			document.body.appendChild(replyDiv);
			var oldReno = document.getElementById("reply"+updateReno);
			oldReno.innerText = updateRememo;
		}
		form.reno.value=reno;
		//수정하고자 하는 댓글의 내용을 가져와 입력창의 입력박스에 넣어준다 
	    form.rememo.value = reply.innerText;
	    reply.innerText =""; //""처리를 해주지 않으면 입력상자 위에 텍스트가 중복 표시된다. 
	    reply.appendChild(replyDiv);
	    updateReno   = reno;
	    updateRememo = form.rememo.value;
	    form.rememo.focus();
	}
	
	
	/*댓글 수정 후 저장
	댓글을 수정한 후 저장 버튼을 누르면 입력된 내용을 서버로 전송*/
	function fn_replyUpdateSave(){
	    var form = document.form2;
	    if (form.rememo.value=="") {
	        alert("글 내용을 입력해주세요.");
	        form.rememo.focus();
	        return;
	    }
	    form.action="boardReplySave";
	    form.submit();   
	}
	
	/*취소 버튼 처리
	수정하던 댓글 내용을 취소하고 입력창을 화면에서 보이지 않게 한다*/
	function fn_replyUpdateCancel(){
    var replyDiv = document.getElementById("replyUpdateDiv");
    document.body.appendChild(replyDiv);
    replyDiv.style.display = "none";
    var oldReno = document.getElementById("reply"+updateReno);
    oldReno.innerText = updateRememo;
    updateReno = updateRememo = null;
	}
	
	$(document).ready(function(){
		//로그인 하지 않고 댓글 작성시 알림 띄우기
		$("#loginAlertBtn").click(function(){
			alert("로그인 후 댓글 작성이 가능합니다.");
		});
		
		if("${post.boardname}"=="자유게시판"){
			$("#board").attr("href", "html/bulletinBoard.html");
		} else if("${post.boardname}"=="질문게시판"){
			$("#board").attr("href", "html/questionBoard.html");
		}
		
	});
	
	
	
	
	
	
</script>

<body>

<!-- 1번 navbar -->
<nav class="navbar fixed-top navbar-expand-lg navbar-light bg-back">
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
<br><br>
<!-- 뒤로가기 버튼/게시판 제목 -->

<div class= "board-font my-2" style="font-family:NanumBarunGothic">
	<a id="board" class="navbar-brand" style="text-decoration:none;">
	<img src="../img/back.png" alt="..." style="width: 20px; height:20px; margin-left:5px" >
	<label id="board_name" style="font-size: 20px; font-weight: bold; padding: 30px,30px,2px,30px; margin-top:10px; margin-left: 5px; color:#5A9367">${post.boardname}</label>
	</a>
	<br>
</div>

 <!-- 글이 보이는 곳 -->
<div class="postArea board-font" style="padding: 10px; border-bottom: 0.1px solid #bebebe;">
	 <form name="frmArticle" method="post"  action="deletePost"  enctype="multipart/form-data">
	 	
	  	<div class="nicknameDiv" style="float:left; font-family:NanumBarunGothic">
	  	<img src="../img/user.png"  style="width: 24px; height: 24px;" alt="..." >&nbsp${post.nickname }</h4> 
	  	</div>
	 	<br><br>
	 	<div class="titleDiv" style="font-size: 1.2em; font-weight:bold; font-family: NanumBarunGothic" align="left">
			<label>${post.title }</label>
		</div>
		<div class="contentDiv mx-2">
			<label style="font-size: 1.1em; font-family: NanumBarunGothic" align="left">
		 	<br>${post.content }<br></label>
	 	</div>
	 	<input type="hidden" name="postno"><br>
	 	
	 	<div style="font-size:0.9em" align="right">
	 		<%-- ${post.postdate } --%>
			<a href="/updatePost?postno=${post.postno}" style="text-decoration: none; color:#adb5bd">게시글수정</a>
			&nbsp;<a href="#" onclick="fn_postDelete(${post.postno})" style="text-decoration: none; color:#adb5bd">게시글삭제</a>
			<br><fmt:formatDate value="${post.postdate }" pattern="yyyy/MM/dd HH시 mm분" />
			
		 </div>
	 </form>
 </div>
 
 <!-- 댓글 리스트 출력 -->
 <form action="deleteReply" method="post">
	 <c:forEach var="reply" items="${reply}" varStatus="status">
	    <div class= "board-font" style="padding: 10px; border-bottom: 0.1px solid #bebebe; ">   
	        <div class="replyDiv" >
	        <label style="font-size: 0.9em; font-family: NanumBarunGothic" align="left" >
	        <img src="../img/user.png"  style="width: 16px; height: 16px;" alt="..." >  <c:out value="${reply.nickname}"/>&nbsp&nbsp
	        <fmt:formatDate value="${reply.redate }" pattern="yyyy/MM/dd HH시 mm분" /></label>
	        </div>
			<%-- 댓글번호<c:out value="${reply.reno}"/> --%>
			<span style="display: inline-block; width: 100%; text-align: right;">
			<a href="#" onclick="fn_replyUpdate('<c:out value="${reply.reno}"/>')"style="text-decoration: none; font-size:0.9em; color:#adb5bd;">수정</a>
	        <a href="#" onclick="fn_replyDelete('<c:out value="${reply.reno}"/>')"style="text-decoration: none; font-size:0.9em; color:#adb5bd;">삭제</a>
	        </span>
	        
	        <br/>
	        <div id="reply<c:out value="${reply.reno}"/>" class='mx-2'><c:out value="${reply.rememo}"/></div>
	    </div>
	</c:forEach>
</form>
 
<!-- 댓글 수정 폼 -->
<div id="replyUpdateDiv" class= "board-font" style="width: 99%; display:none">
    <form name="form2" action="updateReply" method="post">
        <input type="hidden" name="postno" value="<c:out value="${post.postno}"/>">
		<input type="hidden" name="reno">
        <input type="text" name="rememo" />
        <a href="#" onclick="fn_replyUpdateSave()" style="text-decoration: none; font-size:0.9em; color:#198754;">저장</a>
        <a href="#" onclick="fn_replyUpdateCancel()"style="text-decoration: none; font-size:0.9em; color:#198754;">취소</a>
    </form>
</div>


<!-- 댓글 작성 폼 -->
<div class="replyform board-font p-3" style="width: 100%; background-color: #F2F7CE"><!--  #F2F7CE" >-->
	
	<c:if test="${sessionScope.memnickname != null}">
		<img src="../img/user.png"  style="width: 24px; height: 24px;" alt="..." >&nbsp;${sessionScope.memnickname}
		<p></p>
		<form name="form1" action="boardReplySave" method="post" id="memoid">
			<input type="hidden" name="postno" value="<c:out value="${post.postno}"/>">
	       	<textarea name="rememo" form="memoid" style="width: 89%; height: 50px; "autofocus required wrap="hard" placeholder="댓글을 입력하세요."></textarea>
	      	<button class="btn btn-success btn-sm" id="commentInsertBtn" style="float: right; background-color:#5CAB7D; border-color:#5CAB7D; width: 10%; height: 50px">등록</button>
		</form>	
    </c:if>
       
	<!-- 로그인하지 않은 사용자에게는 비활성 -->
	<c:if test="${sessionScope.memnickname == null}">
		<form name="form3" action="boardReplySave" method="post" id="memoid2">
	     	<textarea name="rememo" form="memoid2" style="width: 100%; height: 50px; "disabled wrap="hard" placeholder="댓글을 입력하려면 로그인하세요."></textarea>
		</form>	
	</c:if>
</div>


<br><br><br><br><br><br><br>

<!-- 하단바 -->
<nav class="navbar fixed-bottom navbar-light bg-light" style="padding-top: 0;">
	
 
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