<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 인터셉터 없이도 권한 정보를 가져올 수 있다 -->
<sec:authentication property="principal.authorities[0]" var="role"/>
<!-- 인터셉터 없이도 인증 정보를 가져올 수 있다 -->
<sec:authentication property="principal" var="su" />

<!DOCTYPE html>
<html>
<head>
<sec:csrfMetaTags />
<meta charset="UTF-8">
<title>게시글 내용 조회</title>
<style type="text/css">
	* {
		color: #333;
		font-size: 17px;
	}
	
	a:link, a:hover, a:visited, a:active {
		color: #333;
		text-decoration: none;
	}
	div.grid {
		display: grid;
		grid-template-columns: 80px 1fr;
		grid-template-rows: 28px 28px 28px 28px 28px 28px 320px 1fr 28px;
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
	
	div.grid > div.replies {
		display: grid;
		grid-column: 1 / 3;
	}
	div.replies > .write-reply {
		margin-top: 10px;
		display: grid;
		grid-template-columns: 1fr 80px;
		grid-template-rows: 1fr 40px;
		row-gap: 10px;
		column-gap: 10px; 
		align-items: center;
	}
	div.replies > .write-reply > textarea {
		height: 150px;
		display: grid;
		grid-column: 1 / 3;
	}
	
	div.replies > .reply-items {
		display: grid;
		grid-template-columns: 1fr;
		grid-template-rows: 1fr;
		row-gap: 10px;
	}
	
	pre.content {
		margin: 0px;
	}
</style>
<script type="text/javascript" src="/js/lib/jquery-3.7.0.js"></script>
<script type="text/javascript">
	$().ready(function() {
		
		var modifyReply = function(event) {
			var reply = $(event.currentTarget).closest(".reply");
			var replyId = reply.data("reply-id");
			
			var content = reply.find(".content").text();
			$("#txt-reply").val(content);
			$("#txt-reply").focus();
			
			$("#txt-reply").data("mode", "modify");
			$("#txt-reply").data("target", replyId);
		}
		
		var deleteReply = function(event) {
			var reply = $(event.currentTarget).closest(".reply");
			var replyId = reply.data("reply-id");
			
			$("#txt-reply").removeData("mode");
			$("#txt-reply").removeData("target");
			
			if (confirm("댓글을 삭제하시겠습니까?")) {
				$.get(`/board/reply/delete/\${replyId}`, function(response) {
					var result = response.result;
					if (result) {
						loadReplies();
						$("#txt-reply").val("");
					}
				});
			}
		}
		
		var reReply = function(event) {
			var reply = $(event.currentTarget).closest(".reply");
			var replyId = reply.data("reply-id");
			
			$("#txt-reply").data("mode", "re-reply");
			$("#txt-reply").data("target", replyId);
			$("#txt-reply").focus();
		}
		
		var recommendReply = function(event) {
			var reply = $(event.currentTarget).closest(".reply");
			var replyId = reply.data("reply-id");
			
			$("#txt-reply").removeData("mode");
			$("#txt-reply").removeData("target");
			
			$.get(`/board/reply/recommend/\${replyId}`, function(response) {
				var result = response.result;
				console.log(result)
				if (result) {
					loadReplies();
					$("#txt-reply").val("");
				}
			});
		}
		
		// 댓글 조회하기.
		var loadReplies = function() {
			$(".reply-items").html("");
			$.get("/board/reply/${boardVO.id}", function(response) {
				var replies = response.replies;
				for (var i = 0; i < replies.length; i++) {
					var reply = replies[i];
					var replyTemplate = 
						`<div class="reply" 
							  data-reply-id="\${reply.replyId}" 
							  style="padding-left: \${(reply.level-1) * 40}px">
							<div class="author">
								\${reply.memberVO.name} (\${reply.email})
							</div>
							<div class="recommend-count">
								추천수: \${reply.recommendCnt}
							</div>
							<div class="datetime">
								<span class="crtdt">등록: \${reply.crtDt}</span> 
								\${reply.mdfyDt != reply.crtDt ? 
										`<span class="mdfydt">(수정: \${reply.mdfyDt})</span>` 
										: ""}
							</div>
							<pre class="content">\${reply.content}</pre>
							\${reply.email == "${su.username}" || "${role}" == 'ROLE_ADMIN' ? 
									`<div>
										<span class="modify-reply">수정</span>
										<span class="delete-reply">삭제</span>
										<span class="re-reply">답변하기</span>
									</div>` 
									: `<div>
										<span class="recommend-reply">추천하기</span>
										<span class="re-reply">답변하기</span>
									</div>`}
						</div>`;
						
					var replyDom = $(replyTemplate);
					replyDom.find(".modify-reply").click(modifyReply);
					replyDom.find(".delete-reply").click(deleteReply);
					replyDom.find(".recommend-reply").click(recommendReply);
					replyDom.find(".re-reply").click(reReply);
					
					$(".reply-items").append(replyDom);
				}
			});
		}
		
		loadReplies();
		
		$("#btn-save-reply").click(function() {
			var reply = $("#txt-reply").val();
			var mode = $("#txt-reply").data("mode");
			var target = $("#txt-reply").data("target");
			
			if (reply.trim() != "") {
				var body = {"content": reply};
				var url = "http://localhost:8080/board/reply/${boardVO.id}";
				
				if (mode == "re-reply") { // 답변달기
					body.parentReplyId = target;
				}
				
				if (mode == "modify") {
					url = `/board/reply/modify/\${target}`;
				}
				
				body.parentReplyId = target;
				
				var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
		        var csrfToken = $("meta[name='_csrf']").attr("content");
		        body[csrfParameter] = csrfToken;
		        
				$.post(url, body, function(response) {
					var result = response.result;
					if (result) {
						loadReplies();
						$("#txt-reply").val("");
					}
				});
			}
		});
	});
</script>
</head>
<body>
	<div class="membermenu">
		<jsp:include page="../member/membermenu.jsp"></jsp:include>
	</div>
	<h1>게시글 작성</h1>
	<div class="grid">
		<label for="subject">제목</label>
		<div>
			${boardVO.subject}
		</div>
		<label for="email">이메일</label>
		<div>
			${boardVO.memberVO.name} (${boardVO.email})
		</div>
		<label for="viewCnt">조회수</label>
		<div>
			${boardVO.viewCnt}
		</div>
		<label for="originFileName">첨부파일</label>
		<div>
			<a href="/board/file/download/${boardVO.id}">
				${boardVO.originFileName}
			</a>
		</div>
		<label for="crtDt">등록일</label>
		<div>
			${boardVO.crtDt}
		</div>
		<label for="mdfyDt">수정일</label>
		<div>
			${boardVO.mdfyDt}
		</div>
		<label for="content">내용</label>
		<div>
			${boardVO.content}
		</div>
		
		<div class="replies">
			<div class="reply-items"></div>
			<div class="write-reply">
				<textarea id="txt-reply"></textarea>
				<button id="btn-save-reply">등록</button>
				<button id="btn-cancel-reply">취소</button>
			</div>
		</div>
		
		<div class="btn-group">
			<c:if test="${su.username eq boardVO.email || role eq 'ROLE_ADMIN'}">
				<div class="right-align">
					<a href="/board/modify/${boardVO.id}">수정</a>
					<a href="/board/delete/${boardVO.id}">삭제</a>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>