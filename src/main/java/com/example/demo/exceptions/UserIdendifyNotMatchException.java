package com.example.demo.exceptions;

import com.example.demo.member.vo.MemberVO;

public class UserIdendifyNotMatchException extends RuntimeException {
	private static final long serialVersionUID = -8793056757109164458L;

	private MemberVO memberVO;
	
	public UserIdendifyNotMatchException(MemberVO memberVO, String message) {
		super(message);
		this.memberVO = memberVO;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}

}