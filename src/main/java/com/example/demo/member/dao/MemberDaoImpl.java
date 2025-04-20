package com.example.demo.member.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.member.vo.MemberVO;
import com.example.demo.util.FileDBHandler;
import com.google.gson.Gson;

@Repository
public class MemberDaoImpl implements MemberDao {

	private Gson gson;

	@Autowired
	private FileDBHandler fileDBHandler;
	
	public MemberDaoImpl() {
		this.gson = new Gson();
	}

	@Override
	public int getEmailCount(String email) {
		return fileDBHandler.selectRowsCount("members", (line) -> {
			MemberVO memberVO = gson.fromJson(line, MemberVO.class);
			if (memberVO.getEmail().equals(email)) {
				return memberVO;
			}
			
			return null;
		});
	}

	@Override
	public int createNewMember(MemberVO memberVO) {
		return fileDBHandler.insert("members", memberVO);
	}

	@Override
	public String getSalt(String email) {
		return fileDBHandler.selectRow("members", (line) -> {
			MemberVO memberVO = gson.fromJson(line, MemberVO.class);
			if (memberVO.getEmail().equals(email)) {
				return memberVO.getSalt();
			}
			
			return null;
		});
	}

	@Override
	public int deleteMe(String email) {
		return fileDBHandler.delete("members", (line) -> {
			MemberVO memberVO = gson.fromJson(line, MemberVO.class);
			return memberVO.getEmail().equals(email);
		});
	}

	@Override
	public MemberVO getMemberByEmail(String email) {
		return fileDBHandler.selectRow("members", (line) -> {
			MemberVO memberVO = gson.fromJson(line, MemberVO.class);
			if (memberVO.getEmail().equals(email)) {
				return memberVO;
			}
			
			return null;
		});
	}

	@Override
	public int createOrUpdate(MemberVO memberVO) {
		int count = this.getEmailCount(memberVO.getEmail());
		if (count > 0) {
			return fileDBHandler.update("members", (line) -> {
				MemberVO storedMemberVO = gson.fromJson(line, MemberVO.class);
				if (storedMemberVO.getEmail().equals(memberVO.getEmail())) {
					storedMemberVO.setPassword(memberVO.getPassword());
					storedMemberVO.setName(memberVO.getName());
					storedMemberVO.setProvider(memberVO.getProvider());
					storedMemberVO.setRole(memberVO.getRole());
					storedMemberVO.setSalt(memberVO.getSalt());
					return storedMemberVO;
				}
				return null;
			});
		}
		else {
			return this.createNewMember(memberVO);
		}
	}

}
