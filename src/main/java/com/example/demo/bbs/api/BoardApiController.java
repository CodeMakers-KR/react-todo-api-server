package com.example.demo.bbs.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bbs.service.BoardService;
import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.beans.FileHandler;
import com.example.demo.exceptions.FileNotExistsException;
import com.example.demo.exceptions.PageNotFoundException;
import com.example.demo.util.AjaxResponse;
import com.example.demo.util.ValidationItems;
import com.example.demo.util.ValidationType;
import com.example.demo.util.Validator;
import com.example.demo.util.Validator.Type;

@RestController
@RequestMapping("/api/v1")
public class BoardApiController {

	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private BoardService boardService;

	@GetMapping("/boards")
	public AjaxResponse viewBoardList(SearchBoardVO searchBoardVO) {
		Validator<SearchBoardVO> validator = new Validator<>(searchBoardVO);
		validator.add(ValidationItems.of("pageNo", new ValidationType(Type.NOT_EMPTY, "페이지 번호는 필수 값입니다."))
									 .add(ValidationType.of(Type.MIN, "페이지 번호는 1 이상이어야 합니다.", 0)));
		
		if (!validator.start()) {
			return new AjaxResponse(HttpStatus.BAD_REQUEST).addError(validator.getErrors());
		}
		
		BoardListVO boardListVO = boardService.getAllBoard(searchBoardVO);

		return AjaxResponse.OK(boardListVO.getBoardList(), boardListVO.getBoardCnt())
				.setPages(searchBoardVO.getPageCount())
				.setNext(searchBoardVO.getPageCount() - 1 > searchBoardVO.getPageNo());
	}

	@PostMapping("/boards")
	public AjaxResponse doBoardWrite(BoardVO boardVO, @RequestParam(required = false) MultipartFile file,
			Authentication memberVO) {

		// 게시글 등록
		boardVO.setEmail(memberVO.getName());
		boolean isSuccess = boardService.createNewBoard(boardVO, file);

		return AjaxResponse.OK(isSuccess);

	}

	@GetMapping("/boards/{id}")
	public AjaxResponse viewOneBoard(@PathVariable int id) {
		BoardVO boardVO = boardService.getOneBoard(id, true);
		return AjaxResponse.OK(boardVO);
	}

	@GetMapping("/boards/files/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id) {
		BoardVO boardVO = boardService.getOneBoard(id, false);
		if (boardVO == null) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}

		File storedFile = fileHandler.getStoredFile(boardVO.getFileName());

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + boardVO.getOriginFileName());

		InputStreamResource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(storedFile));
		} catch (FileNotFoundException e) {
			throw new FileNotExistsException("파일이 존재하지 않습니다.");
		}

		return ResponseEntity.ok().headers(header).contentLength(storedFile.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}

	@PutMapping("/boards/{id}")
	public AjaxResponse doBoardUpdate(BoardVO boardVO, @RequestParam(required = false) MultipartFile file,
			Authentication memberVO) {

		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		BoardVO originBoardVO = boardService.getOneBoard(boardVO.getId(), false);
		if (!isAdmin && !originBoardVO.getEmail().equals(memberVO.getName())) {
			throw new PageNotFoundException("잘못된 접근입니다!");
		}

		// 게시글 수정
		boardVO.setEmail(memberVO.getName());
		boolean isSuccess = boardService.updateOneBoard(boardVO, file);

		return AjaxResponse.OK(isSuccess);
	}

	@DeleteMapping("/boards/{id}")
	public AjaxResponse doDeleteBoard(@PathVariable int id, Authentication memberVO) {
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		BoardVO originBoardVO = boardService.getOneBoard(id, false);
		if (!isAdmin && !originBoardVO.getEmail().equals(memberVO.getName())) {
			throw new PageNotFoundException("잘못된 접근입니다!");
		}

		boolean isSuccess = boardService.deleteOneBoard(id);

		return AjaxResponse.OK(isSuccess);
	}


}
