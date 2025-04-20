package com.example.demo.beans.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.beans.SHA;

public class SecuritySHA extends SHA implements PasswordEncoder {

	private String salt;
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		return super.getEncrypt(String.valueOf(rawPassword), salt);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = this.encode(rawPassword);
		return password.equals(encodedPassword);
	}
	
}
