package com.example.demo.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bbs.service.ReplyService;
import com.example.demo.bbs.vo.ReplyVO;

@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/board/reply/{boardId}")
	public Map<String, Object> getAllReplies(@PathVariable int boardId) {
		List<ReplyVO> replyList = replyService.getAllReplies(boardId);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("count", replyList.size());
		resultMap.put("replies", replyList);
		return resultMap;
	}
	
	@PostMapping("/board/reply/{boardId}")
	public Map<String, Object> doCreateNewReplies(@PathVariable int boardId, 
												  @ModelAttribute ReplyVO replyVO, 
												  Authentication memberVO) {
		replyVO.setBoardId(boardId);
		replyVO.setEmail(memberVO.getName());
		boolean isSuccess = replyService.createNewReply(replyVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@GetMapping("/board/reply/delete/{replyId}")
	public Map<String, Object> doDeleteReplies(@PathVariable int replyId,
											   Authentication memberVO) {
		boolean isSuccess = replyService.deleteOneReply(replyId, memberVO.getName());
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@PostMapping("/board/reply/modify/{replyId}")
	public Map<String, Object> doModifyReplies(@PathVariable int replyId,
											   @ModelAttribute ReplyVO replyVO, 
											   Authentication memberVO) {
		replyVO.setReplyId(replyId);
		replyVO.setEmail(memberVO.getName());
		boolean isSuccess = replyService.modifyOneReply(replyVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@GetMapping("/board/reply/recommend/{replyId}")
	public Map<String, Object> doRecommendReplies(@PathVariable int replyId,
												  Authentication memberVO) {
		boolean isSuccess = replyService.recommendOneReply(replyId, memberVO.getName());
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	
}
