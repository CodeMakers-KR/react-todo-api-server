package com.example.demo.bbs.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bbs.service.ReplyService;
import com.example.demo.bbs.vo.ReplyVO;
import com.example.demo.util.AjaxResponse;

@RestController
@RequestMapping("/api/v1")
public class ReplyApiController {

	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/reply/{boardId}")
	public AjaxResponse getAllReplies(@PathVariable int boardId) {
		List<ReplyVO> replyList = replyService.getAllReplies(boardId);
		
		return AjaxResponse.OK(replyList, replyList != null ? replyList.size() : 0);
	}
	
	@PostMapping("/reply/{boardId}")
	public AjaxResponse doCreateNewReplies(@PathVariable int boardId, 
												  @RequestBody ReplyVO replyVO, 
												  Authentication memberVO) {
		replyVO.setBoardId(boardId);
		replyVO.setEmail(memberVO.getName());
		boolean isSuccess = replyService.createNewReply(replyVO);
		
		return AjaxResponse.OK(isSuccess);
	}
	
	@DeleteMapping("/reply/{replyId}")
	public AjaxResponse doDeleteReplies(@PathVariable int replyId,
											   Authentication memberVO) {
		boolean isSuccess = replyService.deleteOneReply(replyId, memberVO.getName());
		
		return AjaxResponse.OK(isSuccess);
	}
	
	@PutMapping("/reply/{replyId}")
	public AjaxResponse doModifyReplies(@PathVariable int replyId,
			@RequestBody ReplyVO replyVO, 
											   Authentication memberVO) {
		replyVO.setReplyId(replyId);
		replyVO.setEmail(memberVO.getName());
		boolean isSuccess = replyService.modifyOneReply(replyVO);
		
		return AjaxResponse.OK(isSuccess);
	}
	
	@PutMapping("/reply/recommend/{replyId}")
	public AjaxResponse doRecommendReplies(@PathVariable int replyId,
												  Authentication memberVO) {
		boolean isSuccess = replyService.recommendOneReply(replyId, memberVO.getName());
		
		return AjaxResponse.OK(isSuccess);
	}
}
