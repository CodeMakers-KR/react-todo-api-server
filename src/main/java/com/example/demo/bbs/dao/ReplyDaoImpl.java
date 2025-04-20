package com.example.demo.bbs.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.bbs.vo.ReplyVO;
import com.example.demo.member.vo.MemberVO;
import com.example.demo.util.DateUtil;
import com.example.demo.util.FileDBHandler;
import com.google.gson.Gson;

@Repository
public class ReplyDaoImpl implements ReplyDao {

	private Gson gson;

	@Autowired
	private FileDBHandler fileDBHandler;

	public ReplyDaoImpl() {
		gson = new Gson();
	}

	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return fileDBHandler.selectRows("replies", (line) -> {
			ReplyVO replyVO = gson.fromJson(line, ReplyVO.class);
			if (replyVO.getBoardId() == boardId) {
				replyVO.setMemberVO(fileDBHandler.selectRow("members", (repLine) -> {
					MemberVO memberVO = gson.fromJson(repLine, MemberVO.class);
					if (memberVO.getEmail().equals(replyVO.getEmail())) {
						memberVO.setPassword(null);
						memberVO.setConfirmPassword(null);
						memberVO.setSalt(null);
						return memberVO;
					}
					return null;
				}));
				
				return replyVO;
			}
			return null;
		});
	}

	@Override
	public ReplyVO getOneReply(int replyId) {
		return fileDBHandler.selectRow("replies", (line) -> {
			ReplyVO replyVO = gson.fromJson(line, ReplyVO.class);
			if (replyVO.getReplyId() == replyId) {
				replyVO.setMemberVO(fileDBHandler.selectRow("members", (repLine) -> {
					MemberVO memberVO = gson.fromJson(repLine, MemberVO.class);
					if (memberVO.getEmail().equals(replyVO.getEmail())) {
						memberVO.setPassword(null);
						memberVO.setConfirmPassword(null);
						memberVO.setSalt(null);
						return memberVO;
					}
					return null;
				}));
				return replyVO;
			}
			else {
				return null;
			}
		});
	}

	@Override
	public int createNewReply(ReplyVO replyVO) {
		replyVO.setCrtDt(DateUtil.today());
		replyVO.setReplyId(fileDBHandler.nextSeq("replies"));
		return fileDBHandler.insert("replies", replyVO);
	}

	@Override
	public int deleteOneReply(int replyId) {
		return fileDBHandler.delete("replies", (line) -> {
			ReplyVO replyVO = gson.fromJson(line, ReplyVO.class);
			return replyVO.getReplyId() == replyId;
		});
	}

	@Override
	public int modifyOneReply(ReplyVO replyVO) {
		replyVO.setMdfyDt(DateUtil.today());
		return fileDBHandler.update("replies", (line) -> {
			ReplyVO storedReplyVO = gson.fromJson(line, ReplyVO.class);
			if (storedReplyVO.getReplyId() == replyVO.getReplyId()) {
				storedReplyVO.setContent(replyVO.getContent());
				storedReplyVO.setMdfyDt(replyVO.getMdfyDt());
				return storedReplyVO;
			}
			return null;
		});
	}

	@Override
	public int recommendOneReply(int replyId) {
		return fileDBHandler.update("replies", (line) -> {
			ReplyVO storedReplyVO = gson.fromJson(line, ReplyVO.class);
			if (storedReplyVO.getReplyId() == replyId) {
				storedReplyVO.setRecommendCnt(storedReplyVO.getRecommendCnt() + 1);
				return storedReplyVO;
			}
			return null;
		});
	}

}
