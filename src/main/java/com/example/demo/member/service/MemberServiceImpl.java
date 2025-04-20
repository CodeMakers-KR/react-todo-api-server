package com.example.demo.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.beans.SHA;
import com.example.demo.exceptions.AlreadyUseException;
import com.example.demo.member.dao.MemberDao;
import com.example.demo.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SHA sha;
	
	@Override
	public boolean checkAvailableEmail(String email) {
		int emailCount = memberDao.getEmailCount(email);
		return emailCount == 0;
	}
	
	@Transactional
	@Override
	public boolean createNewMember(MemberVO memberVO) {
		int emailCount = memberDao.getEmailCount(memberVO.getEmail());
		if (emailCount > 0) {
			throw new AlreadyUseException(memberVO, "Email이 이미 사용중입니다.");
		}
		
		String salt = sha.generateSalt();
		String password = memberVO.getPassword();
		String encryptedPassword = sha.getEncrypt(password, salt);
		memberVO.setPassword(encryptedPassword);
		memberVO.setSalt(salt);
		
		int insertCount = memberDao.createNewMember(memberVO);
		return insertCount > 0;
	}
	
	@Transactional
	@Override
	public boolean deleteMe(String email) {
		int deleteCount = memberDao.deleteMe(email);
		return deleteCount > 0;
	}
	
	@Override
	public MemberVO getMyInfo(String email) {
		return memberDao.getMemberByEmail(email);
	}

}
