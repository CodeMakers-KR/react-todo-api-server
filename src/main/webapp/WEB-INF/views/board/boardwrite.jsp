<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성하기</title>
<style type="text/css">
	a:link, a:hover, a:visited, a:active {
		color: #333;
		text-decoration: none;
	}
	
	div.grid {
		display: grid;
		grid-template-columns: 80px 1fr;
		grid-template-rows: 28px 28px 320px 1fr;
		row-gap: 10px;
	}
		
	div.grid > div.btn-group {
		display: grid;
		grid-column: 1 / 3;
	}
	
	div.grid div.right-align{
		text-align: right;
	}
	
	label {
		padding-left: 10px;
	}
	button, input, textarea {
		padding: 10px;
	}
	input[type=file] {
		padding: 0px;
	}
	
	div.errors {
		background-color: #ff00004a;
		opacity: 0.8;
		padding: 10px;
		color: #333;
	}
	div.errors:last-child {
		margin-bottom: 15px;
	}
	
	ul.horizontal-list {
		padding: 0px;
		margin: 0px;
	}
	ul.horizontal-list li {
		display: inline;
	}
	
	.membermenu {
		text-align: right;
	}
</style>
</head>
<body>
	<div class="membermenu">
		<jsp:include page="../member/membermenu.jsp"></jsp:include>
	</div>
	<h1>게시글 작성</h1>	
	<form:form modelAttribute="boardVO" method="post" enctype="multipart/form-data">
		<div>
			<form:errors path="subject" element="div" cssClass="errors"/>
			<form:errors path="content" element="div" cssClass="errors"/>
		</div>
        
		<div class="grid">
			<label for="subject">제목</label>
			<input id="subject" type="text" name="subject" value="${boardVO.subject}" />
			
			<label for="file">첨부파일</label>
			<input id="file" type="file" name="file" />
			
			<label for="content">내용</label>
			<textarea id="content" name="content" style="height: 300px;">${boardVO.content}</textarea>
			
			<div class="btn-group">
				<div class="right-align">
					<input type="submit" value="저장" />
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>