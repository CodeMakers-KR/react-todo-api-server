package com.example.demo.beans.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.member.vo.MemberVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JsonWebTokenProvider {

	@Value("${jwt.issuer:mcjang1116@gmail.com}")
	private String issuer;
	
	@Value("${jwt.secret-key:spring-boot-security}")
	private String secretKey;
	
	/**
	 * 토큰을 생성한다.
	 * @param expiredAt 토큰의 유효 기간
	 * @param memberVO 토큰 본문에 첨부할 사용자 정보
	 * @return JWT 토큰
	 */
	public String generateToken(Duration expiredAt, MemberVO memberVO) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expiredAt.toMillis());
		
		// JWT 암/복호화를 위해 대칭키 생성.
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		
		return Jwts.builder()
				   // Header typ: JWT
				   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				   // 토큰 발급자 iss: mcjang1116@gmail.com
				   .setIssuer(issuer)
				   // 토큰발생일자 iat: 현재 시간
				   .setIssuedAt(now)
				   // 토큰 만료시간 exp: 유효기간
				   .setExpiration(expiry)
				   // 토큰 제목 sub: 사용자의 이메일
				   .setSubject(memberVO.getEmail())
				   // JWT 본문 작성 {user: memberVO}
				   .setClaims(Map.of("user", memberVO))
				   // 암복호화를 위한 서명 (암호화)
				   .signWith(key)
				   .compact();
	}
	
	/**
	 * 토큰에서 사용자 정보를 조회한다.
	 * @param token HttpRequest Header로 전달된 token 정보
	 * @return 회원 정보
	 * @throws JsonProcessingException 
	 */
	public MemberVO getUserFromToken(String token) throws JsonProcessingException {
		// JWT 암/복호화를 위해 대칭키 생성.
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
				
		Claims claims = Jwts.parserBuilder()
							// 토큰을 복호화하기 위한 키 설정
							.setSigningKey(key)
							.build()
							// 토큰을 복호화 한다.
							.parseClaimsJws(token)
							// 토큰의 Claims 정보를 가져온다.
							.getBody();
		
		ObjectMapper om = new ObjectMapper();
		
		Object user = claims.get("user");
		String json = om.writeValueAsString(user);
		
		MemberVO memberVO = om.readValue(json, MemberVO.class);
		return memberVO;
	}
	
}
