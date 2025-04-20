package com.example.demo.member.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.exceptions.AlreadyUseException;
import com.example.demo.member.dao.MemberDao;
import com.example.demo.member.dao.MemberDaoImpl;
import com.example.demo.member.vo.MemberVO;

@SpringBootTest // 모든 Bean을 생성하기 위해 선언.
@ExtendWith(SpringExtension.class) //JUnit5 사용 명시
// MemberServiceImpl테스트를 위해 Import
// MemberDaoImpl을 주입하기 위해 Import
@Import({MemberServiceImpl.class, MemberDaoImpl.class})
public class MemberServiceImplTest {

	/**
	 * MemberService Autowired
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * MemberService에 DI해주기 위한 MemberDao 선언.
	 * 가짜 객체를 생성하기 위한 MockBean 선언.
	 */
	@MockBean
	private MemberDao memberDao;
	
	@Test
	@DisplayName("회원 ID 중복체크 테스트")
	public void checkAvailableEmailTest() {
		/*
		 * 1. MemberServiceImpl의 checkAvailableEmail 테스트를 위해
		 *    memberDao의 getEmailCount가 해야할 일을 작성한다.
		 *    MemberServiceImpl의 코드만 테스트 하기 위한 목적.
		 * 
		 * memberDao의 getEmailCount의 할일.
		 *   given(파라미터로 user01@gmail.com파라미터로 전달하면)
		 *   willReturn(0); 0을 반환한다.
		 */
		given(memberDao.getEmailCount("user01@gmail.com"))
				.willReturn(0);
		
		/*
		 * 1. MemberServiceImpl의 checkAvailableEmail 테스트를 위해
		 *    memberDao의 getEmailCount가 해야할 일을 작성한다.
		 *    MemberServiceImpl의 코드만 테스트 하기 위한 목적.
		 * 
		 * memberDao의 getEmailCount의 할일.
		 *   given(파라미터로 user02@gmail.com파라미터로 전달하면)
		 *   willReturn(1); 1을 반환한다.
		 */
		given(memberDao.getEmailCount("user02@gmail.com"))
				.willReturn(1);
		
		/*
		 * when
		 * 2. 메소드 실행.
		 * memberService의 checkAvailableEmail 를 실행한다.
		 */
		boolean isAvailableEmail = memberService.checkAvailableEmail("user01@gmail.com");
		// then 3. 결과가 true인지 확인
		assertTrue(isAvailableEmail);
		
		/*
		 * when
		 * 2. 메소드 실행.
		 * memberService의 checkAvailableEmail 를 실행한다.
		 */
		boolean isNotAvailableEmail = memberService.checkAvailableEmail("user02@gmail.com");
		// then 3. 결과가 false인지 확인
		assertFalse(isNotAvailableEmail);
		
		// 4. given으로 작성한 MockBean이 실행되었는지 확인
		verify(memberDao).getEmailCount("user01@gmail.com");
		verify(memberDao).getEmailCount("user02@gmail.com");
		
		// 모든 테스트가 성공해야만 하나의 TestCase가 성공한다.
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void memberRegistFailTest() {
		given(memberDao.getEmailCount("user01@gmail.com"))
				.willReturn(1);
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		
		/*
		 * AlreadyUseException이 발생하길 기대하는 테스트
		 * memberService.createNewMember(memberVO))가 실행되면 
		 * AlreadyUseException이 발생해야 성공.
		 */
		AlreadyUseException exception = 
				// 발생해야하는 예외
				assertThrows(AlreadyUseException.class, 
							 // 예외가 발생하는 메소드
							 () -> memberService.createNewMember(memberVO)); 
		
		// 예상되는 예외 메시지
		String expectMessage = "Email이 이미 사용중입니다.";
		// 예상되는 예외 메시지와 실제 메시지가 동일한지 확인.
		assertEquals(expectMessage, exception.getMessage());
		
		// memberDao의 getEmailCount가 실행되었는지 확인
		verify(memberDao).getEmailCount("user01@gmail.com");
	}
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void memberRegistSuccessTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setName("테스트사용자");
		memberVO.setPassword("testpassword");
		memberVO.setConfirmPassword("testpassword");
		
		given(memberDao.getEmailCount(memberVO.getEmail()))
				.willReturn(0);
		
		given(memberDao.createNewMember(memberVO))
				.willReturn(1);
		
		boolean isSuccess = memberService.createNewMember(memberVO);
		
		assertTrue(isSuccess);
		assertNotNull(memberVO.getSalt());
		assertNotNull(memberVO.getPassword());
		assertNotEquals(memberVO.getPassword(), memberVO.getConfirmPassword());
		
		verify(memberDao).getEmailCount(memberVO.getEmail());
		verify(memberDao).createNewMember(memberVO);
	}
	
}
