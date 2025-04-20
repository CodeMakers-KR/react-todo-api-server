package com.example.demo.member.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import com.example.demo.member.vo.MemberVO;

// MyBatis를 테스트하기 위한 설정.
@MybatisTest
// 실제 DB 를 테스트하기 위한 설정.
// 이 설정이 없으면, TestDB를 사용하게 된다.
// 테스트 DB가 없다면 에러가 발생함.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// MemberDaoImpl을 테스트하기 위한 설정.
@Import(MemberDaoImpl.class)
public class MemberDaoImplTest {

	@Autowired
	private MemberDao memberDao;
	
	@Test
	public void getEmailCountTest() {
		int count = memberDao.getEmailCount("user01@gmail.com");
		assertEquals(count, 1);
	}
	
	@Test
	public void createNewMemberTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("testuser01@gmail.com");
		memberVO.setName("테스트사용자");
		memberVO.setPassword("testpassword");
		memberVO.setConfirmPassword("testpassword");
		memberVO.setSalt("testsalt");
		
		// Insert 이후 Rollback된다.
		int count = memberDao.createNewMember(memberVO);
		assertEquals(count, 1);
	}
	
	@Test
	public void deleteMeTest() {
		// delete 이후 Rollback된다.
		int count = memberDao.deleteMe("user01@gmail.com");
		assertEquals(count, 1);
	}
}
