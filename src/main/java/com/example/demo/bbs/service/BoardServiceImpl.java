package com.example.demo.bbs.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bbs.dao.BoardDao;
import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.beans.FileHandler;
import com.example.demo.beans.FileHandler.StoredFile;
import com.example.demo.exceptions.PageNotFoundException;

@Service
public class BoardServiceImpl implements BoardService {

	private Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	@Autowired
	private FileHandler fileHandler;
	
	/**
	 * Bean Context에 등록된 boardDao Bean을 가져와 주입시킨다.
	 */
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard(SearchBoardVO searchBoardVO) {
		// 게시글 건수와 게시글 목록을 가지는 VO 객체 선언
		BoardListVO boardListVO = new BoardListVO();
		// 게시글 총 건수 조회
		boardListVO.setBoardCnt(boardDao.getBoardAllCount());
		
		// 게시글 목록 조회
		if (searchBoardVO == null) {
			List<BoardVO> boardList = boardDao.getAllBoard();
			boardListVO.setBoardList(boardList);
		}
		else {
			List<BoardVO> boardList = boardDao.searchAllBoard(searchBoardVO);
			boardListVO.setBoardList(boardList);
			searchBoardVO.setPageCount(boardListVO.getBoardCnt());
		}
		return boardListVO;
	}

	@Transactional
	@Override
	public boolean createNewBoard(BoardVO boardVO, MultipartFile file) {
		
		// 파일을 업로드하고 결과를 받아온다.
		if (file != null && !file.isEmpty()) {
			StoredFile storedFile = fileHandler.storeFile(file);
			logger.debug("FileName: " + storedFile.getFileName());
			logger.debug("RealFileName: " + storedFile.getRealFileName());
			logger.debug("FileSize: " + storedFile.getFileSize());
			logger.debug("RealFilePath: " + storedFile.getRealFilePath());
			
			boardVO.setFileName(storedFile.getRealFileName());
			boardVO.setOriginFileName(storedFile.getFileName());
		}
		
		// DB에 게시글 등록
		// createCount에는 DB에 등록한 게시글의 개수를 반환.
		int createCount = boardDao.createNewBoard(boardVO);
		// DB에 등록한 개수가 0보다 크다면 성공. 아니라면 실패.
		return createCount > 0;
	}
	
	@Transactional
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		if (isIncrease) {
			// 파라미터로 전달받은 게시글의 조회 수 증가
			// updateCount에는 DB에 업데이트한 게시글의 수를 반환.
			int updateCount = boardDao.increaseViewCount(id);
			if (updateCount == 0) {
				// updateCount가 0이라는 것은 
				// 파라미터로 전달받은 id 값이 DB에 존재하지 않는다는 의미이다.
				// 이 경우, 잘못된 접근입니다. 라고 사용자에게 예외 메시지를 보내준다.
				throw new PageNotFoundException("잘못된 접근입니다.");
			}
		}
		
		// 예외가 발생하지 않았다면, 게시글 정보를 조회한다.
		BoardVO boardVO = boardDao.getOneBoard(id);
		if (boardVO == null) {
			// 파라미터로 전달받은 id 값이 DB에 존재하지 않을 경우
			// 잘못된 접근입니다. 라고 사용자에게 예외 메시지를 보내준다.
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return boardVO;
	}
	
	@Transactional
	@Override
	public boolean updateOneBoard(BoardVO boardVO, MultipartFile file) {
		
		// 파일을 업로드 했는지 확인.
		if (file != null && !file.isEmpty()) {
			// 변경되기 전의 게시글 정보 가져오기
			BoardVO originBoardVO = boardDao.getOneBoard(boardVO.getId());
			if (originBoardVO != null && originBoardVO.getFileName() != null) {
				// 변경되기 전의 게시글이 파일이 업로드된 게시글일 경우
				File originFile = fileHandler.getStoredFile(originBoardVO.getFileName());
				// 파일이 있는지 확인하고, 삭제한다.
				if (originFile.exists() && originFile.isFile()) {
					originFile.delete();
				}
			}
			
			// 파일을 업로드하고 결과를 받아온다.
			StoredFile storedFile = fileHandler.storeFile(file);
			boardVO.setFileName(storedFile.getRealFileName());
			boardVO.setOriginFileName(storedFile.getFileName());
		}
		
		// 파라미터로 전달받은 수정된 게시글의 정보로 DB 수정
		// updateCount에는 DB에 업데이트한 게시글의 수를 반환.
		int updateCount = boardDao.updateOneBoard(boardVO);
		return updateCount > 0;
	}
	
	@Transactional
	@Override
	public boolean deleteOneBoard(int id) {
		
		// 삭제되기 전의 게시글 정보 가져오기
		BoardVO originBoardVO = boardDao.getOneBoard(id);
		if (originBoardVO != null && originBoardVO.getFileName() != null) {
			// 삭제되기 전의 게시글이 파일이 업로드된 게시글일 경우
			File originFile = fileHandler.getStoredFile(originBoardVO.getFileName());
			// 파일이 있는지 확인하고, 삭제한다.
			if (originFile.exists() && originFile.isFile()) {
				originFile.delete();
			}
		}
		
		// 파라미터로 전달받은 id로 게시글을 삭제.
		// deleteCount에는 DB에서 삭제한 게시글의 수를 반환.
		int deleteCount = boardDao.deleteOneBoard(id);
		return deleteCount > 0;
	}
	
}