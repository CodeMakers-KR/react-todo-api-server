<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<style type="text/css">
	div.grid {
		display: grid;
		grid-template-columns: 120px 1fr;
		grid-template-rows: 28px 28px 1fr;
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
	button, input {
		padding: 10px;
		border: 1px solid #ccc;
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
}
</style>
</head>
<body>
	<h1>로그인</h1>
	<form:form modelAttribute="memberVO" method="post" action="/member/login-proc">
		<div>
			<form:errors path="email" element="div" cssClass="errors"/>
			<form:errors path="password" element="div" cssClass="errors"/>
			<c:if test="${not empty message}">
				<div class="errors">
					${message}
				</div>
			</c:if>
		</div>
        
		<div class="grid">
			<input type="hidden" name="next" value="${next}" />
			<label for="email">이메일</label>
			<input id="email" type="email" name="email" value="${memberVO.email}" />
			
			<label for="password">비밀번호</label>
			<input id="password" type="password" name="password" value="${memberVO.password}" />
			
			<div class="btn-group">
				<div class="right-align">
					<input id="btn-regist" type="submit" value="로그인" />
				</div>
			</div>
			
			<a href="/oauth2/authorization/naver">Naver Login</a><br>
			<a href="/oauth2/authorization/google">Google Login</a><br>
		</div>
	</form:form>
</body>
</html>