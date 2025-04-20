package com.example.demo.exceptions;

import com.example.demo.member.vo.MemberVO;

public class AlreadyUseException extends RuntimeException {

	private static final long serialVersionUID = 2623350356585553276L;

	private MemberVO memberVO;
	
	public AlreadyUseException(MemberVO memberVO, String message) {
		super(message);
		this.memberVO = memberVO;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}
}
