package com.example.demo.bbs.vo;

import com.example.demo.member.vo.MemberVO;
import com.google.gson.Gson;

public class BoardVO {

	private int id;
	private String subject;
	private String content;
	private String email;
	private int viewCnt;
	private String crtDt;
	private String mdfyDt;
	private String fileName;
	private String originFileName;

	/**
	 * 게시글을 작성한 회원의 정보
	 */
	private MemberVO memberVO;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public String getCrtDt() {
		return crtDt;
	}

	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}

	public String getMdfyDt() {
		return mdfyDt;
	}

	public void setMdfyDt(String mdfyDt) {
		this.mdfyDt = mdfyDt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginFileName() {
		return originFileName;
	}

	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
