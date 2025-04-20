package com.example.demo.beans.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.member.vo.MemberVO;

public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = -2962689397285446387L;
	
	private MemberVO memberVO;

	public SecurityUser(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	/**
	 * 로그인 사용자의 권한 정보
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.memberVO.getRole()));
	}

	/**
	 * 로그인 사용자의 비밀번호
	 */
	@Override
	public String getPassword() {
		return this.memberVO.getPassword();
	}

	/**
	 * 로그인 사용자의 ID
	 */
	@Override
	public String getUsername() {
		return this.memberVO.getEmail();
	}

	/**
	 * 로그인 사용자 계정이 만료되지 않았는지 여부
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 로그인 사용자 계정이 잠금처리 되지 않았는지 여부
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 비밀번호 변경일이 만료되지 않았는지 여부
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 로그인 사용자 계정이 유효한지 여부
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getSalt() {
		return memberVO.getSalt();
	}
	
	public String getName() {
		return memberVO.getName();
	}
}
