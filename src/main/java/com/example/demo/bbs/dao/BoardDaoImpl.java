package com.example.demo.bbs.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.member.vo.MemberVO;
import com.example.demo.util.DateUtil;
import com.example.demo.util.FileDBHandler;
import com.google.gson.Gson;

@Repository
public class BoardDaoImpl implements BoardDao {

	private Gson gson;

	@Autowired
	private FileDBHandler fileDBHandler;

	public BoardDaoImpl() {
		this.gson = new Gson();
	}

	@Override
	public int getBoardAllCount() {
		return fileDBHandler.selectRowsCount("boards", (line) -> {
			return gson.fromJson(line, BoardVO.class);
		});
	}

	@Override
	public List<BoardVO> getAllBoard() {
		return fileDBHandler.selectRows("boards", (line) -> {
			BoardVO boardVO = gson.fromJson(line, BoardVO.class);
			boardVO.setMemberVO(fileDBHandler.selectRow("members", (boardLine) -> {
				MemberVO memberVO = gson.fromJson(boardLine, MemberVO.class);
				if (memberVO.getEmail().equals(boardVO.getEmail())) {
					memberVO.setPassword(null);
					memberVO.setConfirmPassword(null);
					memberVO.setSalt(null);
					return memberVO;
				}
				return null;
			}));

			return boardVO;
		});
	}

	@Override
	public List<BoardVO> searchAllBoard(SearchBoardVO searchBoardVO) {
		int skip = searchBoardVO.getPageNo() * searchBoardVO.getListSize();
		return fileDBHandler.selectRows("boards", (line) -> {
			BoardVO boardVO = gson.fromJson(line, BoardVO.class);
			boardVO.setMemberVO(fileDBHandler.selectRow("members", (boardLine) -> {
				MemberVO memberVO = gson.fromJson(boardLine, MemberVO.class);
				if (memberVO.getEmail().equals(boardVO.getEmail())) {
					memberVO.setPassword(null);
					memberVO.setConfirmPassword(null);
					memberVO.setSalt(null);
					return memberVO;
				}
				return null;
			}));

			return boardVO;
		}, (o1, o2) -> o2.getId() - o1.getId(), skip, searchBoardVO.getListSize());

	}

	@Override
	public int createNewBoard(BoardVO boardVO) {
		boardVO.setId(fileDBHandler.nextSeq("boards"));
		boardVO.setCrtDt(DateUtil.today());
		return fileDBHandler.insert("boards", boardVO);
	}

	@Override
	public int increaseViewCount(int id) {
		return fileDBHandler.update("boards", (line) -> {
			BoardVO boardVO = gson.fromJson(line, BoardVO.class);
			if (boardVO.getId() == id) {
				boardVO.setViewCnt(boardVO.getViewCnt() + 1);
				return boardVO;
			}
			return null;
		});
	}

	@Override
	public BoardVO getOneBoard(int id) {
		return fileDBHandler.selectRow("boards", (line) -> {
			BoardVO boardVO = gson.fromJson(line, BoardVO.class);
			if (boardVO.getId() == id) {
				boardVO.setMemberVO(fileDBHandler.selectRow("members", (boardLine) -> {
					MemberVO memberVO = gson.fromJson(boardLine, MemberVO.class);
					if (memberVO.getEmail().equals(boardVO.getEmail())) {
						memberVO.setPassword(null);
						memberVO.setConfirmPassword(null);
						memberVO.setSalt(null);
						return memberVO;
					}
					return null;
				}));
				
				return boardVO;
			}

			return null;
		});
	}

	@Override
	public int updateOneBoard(BoardVO boardVO) {
		boardVO.setMdfyDt(DateUtil.today());
		return fileDBHandler.update("boards", (line) -> {
			BoardVO storedBoardVO = gson.fromJson(line, BoardVO.class);
			if (storedBoardVO.getId() == boardVO.getId()) {
				storedBoardVO.setContent(boardVO.getContent());
				storedBoardVO.setSubject(boardVO.getSubject());
				storedBoardVO.setFileName(boardVO.getFileName());
				storedBoardVO.setOriginFileName(boardVO.getOriginFileName());
				storedBoardVO.setMdfyDt(boardVO.getMdfyDt());
				return storedBoardVO;
			}

			return null;
		});
	}

	@Override
	public int deleteOneBoard(int id) {
		return fileDBHandler.delete("boards", (line) -> {
			BoardVO boardVO = gson.fromJson(line, BoardVO.class);
			return boardVO.getId() == id;
		});
	}

}
