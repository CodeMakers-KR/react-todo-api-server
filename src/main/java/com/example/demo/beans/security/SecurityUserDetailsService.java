package com.example.demo.beans.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.member.dao.MemberDao;
import com.example.demo.member.vo.MemberVO;

public class SecurityUserDetailsService implements UserDetailsService {

	private MemberDao memberDao;
	
	public SecurityUserDetailsService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO memberVO = memberDao.getMemberByEmail(username);
		
		if (memberVO == null) {
			throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return new SecurityUser(memberVO);
	}
	
}