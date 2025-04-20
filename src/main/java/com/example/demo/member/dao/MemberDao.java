package com.example.demo.member.dao;

import com.example.demo.member.vo.MemberVO;

public interface MemberDao {

	/**
	 * 파라미터로 전달 된 이메일이 DB에 몇건 존재하는지 확인한다.
	 * @param email 사용자가 가입 요청한 이메일
	 * @return 동일한 이메일로 등록된 회원의 수
	 */
	public int getEmailCount(String email);
	
	/**
	 * 회원가입 쿼리를 실행한다.
	 * @param memberVO 사용자가 입력한 회원 정보
	 * @return DB에 Insert한 회원의 개수
	 */
	public int createNewMember(MemberVO memberVO);
	
	/**
	 * 로그인시 비밀번호 암호화를 위해 기존에 발급했던 salt값을 조회.
	 * @param email 조회할 이메일
	 * @return 회원가입시 발급받은 salt 값
	 */
	public String getSalt(String email);
	
//	/**
//	 * 이메일과 비밀번호로 회원 정보를 조회.
//	 * @param memberVO 이메일과 비밀번호
//	 * @return 이메일과 비밀번호가 일치하는 회원의 정보
//	 */
//	public MemberVO getMember(MemberVO memberVO);
	
	/**
	 * 회원 DELETE 쿼리를 실행한다.
	 * @param email 삭제할 회원의 이메일
	 * @return DB에 Delete한 회원의 개수
	 */
	public int deleteMe(String email);
	
	/**
	 * 이메일로 회원 정보를 조회한다.
	 * @param email 로그인시 전달한 이메일
	 * @return email로 조회된 회원 정보
	 */
	public MemberVO getMemberByEmail(String email);

	/**
	 * OAuth2로 가입한 회원들의 정보를 작성한다.
	 * @param OAuth로 가입한 memberVO 회원 정보
	 * @return DB에 INSERT / UPDATE 한 회원의 개수
	 */
	public int createOrUpdate(MemberVO memberVO);

}
