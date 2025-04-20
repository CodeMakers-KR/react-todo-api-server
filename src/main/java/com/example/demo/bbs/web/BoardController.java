package com.example.demo.bbs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.bbs.service.BoardService;
import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.beans.FileHandler;
import com.example.demo.exceptions.FileNotExistsException;
import com.example.demo.exceptions.MakeXlsxFileException;
import com.example.demo.exceptions.PageNotFoundException;

import jakarta.validation.Valid;

@Controller
public class BoardController {

	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private BoardService boardService;

	@GetMapping("/board/list")
	public ModelAndView viewBoardList(@ModelAttribute SearchBoardVO searchBoardVO) {
		BoardListVO boardListVO = boardService.getAllBoard(searchBoardVO);
		searchBoardVO.setPageCount(boardListVO.getBoardCnt());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardlist");
		modelAndView.addObject("boardList", boardListVO);
		modelAndView.addObject("searchBoardVO", searchBoardVO);
		return modelAndView;
	}
	
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}

	@PostMapping("/board/write")
	public ModelAndView doBoardWrite(@Valid @ModelAttribute BoardVO boardVO, BindingResult bindingResult,
			@RequestParam MultipartFile file, Authentication memberVO) {

		logger.debug("첨부파일명: " + file.getOriginalFilename());
		logger.debug("제목: " + boardVO.getSubject());
		logger.debug("이메일: " + boardVO.getEmail());
		logger.debug("내용: " + boardVO.getContent());
		logger.debug("등록일: " + boardVO.getCrtDt());
		logger.debug("수정일: " + boardVO.getMdfyDt());
		logger.debug("FileName: " + boardVO.getFileName());
		logger.debug("OriginFileName: " + boardVO.getOriginFileName());

		ModelAndView modelAndView = new ModelAndView();

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("board/boardwrite");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
		}
		// 게시글 등록
		boardVO.setEmail(memberVO.getName());
		boolean isSuccess = boardService.createNewBoard(boardVO, file);
		if (isSuccess) {
			// 게시글 등록 결과가 성공이라면
			// /board/list URL로 이동한다.
			modelAndView.setViewName("redirect:/board/list");
			return modelAndView;
		} else {
			// 게시글 등록 결과가 실패라면
			// 게시글 등록(작성) 화면으로 데이터를 보내준다.
			// 게시글 등록(작성) 화면에서 boardVO 값으로 등록 값을 설정해야 한다.
			modelAndView.setViewName("board/boardwrite");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
		}

	}

	@GetMapping("/board/view") // http://localhost:8080/board/view?id=1
	public ModelAndView viewOneBoard(@RequestParam int id) {
		BoardVO boardVO = boardService.getOneBoard(id, true);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardview");
		modelAndView.addObject("boardVO", boardVO);
		return modelAndView;
	}

	@GetMapping("/board/file/download/{id}")
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

	@GetMapping("/board/modify/{id}") // http://localhost:8080/board/modify/2
	public ModelAndView viewBoardModifyPage(@PathVariable int id, Authentication memberVO) {
		// 게시글 수정을 위해 게시글의 내용을 조회한다.
		// 게시글 조회와 동일한 코드 호출
		BoardVO boardVO = boardService.getOneBoard(id, false);
		
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		if (!isAdmin && !boardVO.getEmail().equals(memberVO.getName())) {
			throw new PageNotFoundException("잘못된 접근입니다!");
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardmodify");
		modelAndView.addObject("boardVO", boardVO);
		return modelAndView;
	}

	@PostMapping("/board/modify")
	public ModelAndView doBoardUpdate(@Valid @ModelAttribute BoardVO boardVO, BindingResult bindingResult,
			@RequestParam MultipartFile file, Authentication memberVO) {

		logger.debug("ID: " + boardVO.getId());
		logger.debug("제목: " + boardVO.getSubject());
		logger.debug("이메일: " + boardVO.getEmail());
		logger.debug("내용: " + boardVO.getContent());
		logger.debug("등록일: " + boardVO.getCrtDt());
		logger.debug("수정일: " + boardVO.getMdfyDt());
		logger.debug("FileName: " + boardVO.getFileName());
		logger.debug("OriginFileName: " + boardVO.getOriginFileName());

		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("board/boardmodify");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
		}
		
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		BoardVO originBoardVO = boardService.getOneBoard(boardVO.getId(), false);
		if (!isAdmin && !originBoardVO.getEmail().equals(memberVO.getName())) {
			throw new PageNotFoundException("잘못된 접근입니다!");
		}

		// 게시글 수정
		boardVO.setEmail(memberVO.getName());
		boolean isSuccess = boardService.updateOneBoard(boardVO, file);
		if (isSuccess) {
			// 게시글 수정 결과가 성공이라면
			// /board/view?id=id URL로 이동한다.
			modelAndView.setViewName("redirect:/board/view?id=" + boardVO.getId());
			return modelAndView;
		} else {
			// 게시글 수정 결과가 실패라면
			// 게시글 수정 화면으로 데이터를 보내준다.
			modelAndView.setViewName("board/boardmodify");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
		}
	}

	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id, Authentication memberVO) {
		boolean isAdmin = memberVO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));		
		BoardVO originBoardVO = boardService.getOneBoard(id, false);
		if (!isAdmin && !originBoardVO.getEmail().equals(memberVO.getName())) {
			throw new PageNotFoundException("잘못된 접근입니다!");
		}

		boolean isSuccess = boardService.deleteOneBoard(id);
		if (isSuccess) {
			return "redirect:/board/list";
		} else {
			return "redirect:/board/view?id=" + id;
		}
	}

	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> downloadExcelFile() {
		// 모든 게시글 조회
		BoardListVO boardListVO = boardService.getAllBoard(null);

		// XLSX 문서 만들기
		Workbook workbook = new SXSSFWorkbook(-1);

		// 엑셀 시트 만들기
		Sheet sheet = workbook.createSheet("게시글 목록");

		// 행 만들기
		Row row = sheet.createRow(0);
		// 타이틀 만들기
		Cell cell = row.createCell(0);
		cell.setCellValue("번호");

		cell = row.createCell(1);
		cell.setCellValue("제목");

		cell = row.createCell(2);
		cell.setCellValue("첨부파일명");

		cell = row.createCell(3);
		cell.setCellValue("작성자이메일");

		cell = row.createCell(4);
		cell.setCellValue("조회수");

		cell = row.createCell(5);
		cell.setCellValue("등록일");

		cell = row.createCell(6);
		cell.setCellValue("수정일");

		// 데이터 행 만들고 쓰기
		List<BoardVO> boardList = boardListVO.getBoardList();
		int rowIndex = 1;
		for (BoardVO boardVO : boardList) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("" + boardVO.getId());

			cell = row.createCell(1);
			cell.setCellValue(boardVO.getSubject());

			cell = row.createCell(2);
			cell.setCellValue(boardVO.getOriginFileName());

			cell = row.createCell(3);
			cell.setCellValue(boardVO.getEmail());

			cell = row.createCell(4);
			cell.setCellValue(boardVO.getViewCnt());

			cell = row.createCell(5);
			cell.setCellValue(boardVO.getCrtDt());

			cell = row.createCell(6);
			cell.setCellValue(boardVO.getMdfyDt());

			rowIndex += 1;
		}

		// 엑셀 파일 만들기
		File storedFile = fileHandler.getStoredFile("게시글_목록.xlsx");
		OutputStream os = null;
		try {
			ZipSecureFile.setMinInflateRatio(0);
			os = new FileOutputStream(storedFile);
			workbook.write(os);
		} catch (IOException e) {
			throw new MakeXlsxFileException("엑셀파일을 만들 수 없습니다.");
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {}
			if (os != null) {
				try {
					os.flush();
				} catch (IOException e) {}
				try {
					os.close();
				} catch (IOException e) {}
			}
		}

		// 엑셀 파일 다운로드
		String downloadFileName = URLEncoder.encode("게시글목록.xlsx", Charset.defaultCharset());
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.setContentLength(storedFile.length());
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFileName);
		
		InputStreamResource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(storedFile));
		} catch (FileNotFoundException e) {
			throw new FileNotExistsException("파일이 존재하지 않습니다.");
		}

		return ResponseEntity.ok()
				.headers(header)
				.body(resource);
	}
}
