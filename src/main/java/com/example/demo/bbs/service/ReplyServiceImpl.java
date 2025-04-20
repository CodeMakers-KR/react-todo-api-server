package com.example.demo.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.bbs.dao.ReplyDao;
import com.example.demo.bbs.vo.ReplyVO;
import com.example.demo.exceptions.PageNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return replyDao.getAllReplies(boardId);
	}

	@Override
	public boolean createNewReply(ReplyVO replyVO) {
		int insertCount = replyDao.createNewReply(replyVO);
		return insertCount > 0;
	}

	@Override
	public boolean deleteOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDao.getOneReply(replyId);
		
		Authentication memberVO = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		if (!isAdmin && !email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.deleteOneReply(replyId) > 0;
	}

	@Override
	public boolean modifyOneReply(ReplyVO replyVO) {
		ReplyVO originReplyVO = replyDao.getOneReply(replyVO.getReplyId());
		
		Authentication memberVO = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		if (!isAdmin && !replyVO.getEmail().equals(originReplyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.modifyOneReply(replyVO) > 0;
	}
	
	@Override
	public boolean recommendOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDao.getOneReply(replyId);
		if (email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.recommendOneReply(replyId) > 0;
	}

}
