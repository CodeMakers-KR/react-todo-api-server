package com.example.demo.member.service;

import com.example.demo.member.vo.MemberVO;

public interface MemberService {

	/**
	 * 파라미터로 전달 된 이메일이 사용가능한지 확인
	 * @param email 사용자가 가입 요청한 이메일
	 * @return 사용가능한 이메일인지 여부 (true: 사용가능한 이메일)
	 */
	public boolean checkAvailableEmail(String email);
	
	/**
	 * 회원가입을 처리한다.
	 * @param memberVO 사용자가 작성한 사용자 정보
	 * @return 회원가입이 정상적으로 처리되었는지 여부
	 */
	public boolean createNewMember(MemberVO memberVO);
	
//	/**
//	 * 이메일과 비밀번호로 회원 정보를 조회.
//	 * @param memberVO 이메일과 비밀번호
//	 * @return 이메일과 비밀번호가 일치하는 회원의 정보
//	 */
//	public MemberVO getMember(MemberVO memberVO);
	
	/**
	 * 회원을 탈퇴시킨다.
	 * @param email 탈퇴시킬 회원의 이메일
	 * @return 탈퇴 성공 여부
	 */
	public boolean deleteMe(String email);

	public MemberVO getMyInfo(String email);
}
