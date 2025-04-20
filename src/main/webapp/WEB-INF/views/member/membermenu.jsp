<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="right-align">
	<ul class="horizontal-list">
		<sec:authorize access="!isAuthenticated()">
			<li>
				<a href="/member/regist">회원가입</a>
			</li>
			<li>
				<a href="/member/login">로그인</a>
			</li>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<li style="margin-right: 15px;">
				${memberName}
				(<sec:authentication property="name"/>)
			</li>
			<li>
				<a href="/member/logout">로그아웃</a>
			</li>
			<li>
				<a href="/member/delete-me">탈퇴</a>
			</li>
		</sec:authorize>
	</ul>
</div>