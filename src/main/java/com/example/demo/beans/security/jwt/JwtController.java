package com.example.demo.beans.security.jwt;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.SHA;
import com.example.demo.member.dao.MemberDao;
import com.example.demo.member.vo.MemberVO;

@RestController
@RequestMapping("/api/v1")
public class JwtController {

	@Autowired
	private JsonWebTokenProvider jsonWebTokenProvider;
	
	@Autowired
	private MemberDao memberDao;
	
	@PostMapping("/auth/token")
	public ResponseEntity<Map<String, Object>> createNewAccessToken(@RequestBody MemberVO memberVO) {
		MemberVO member = memberDao.getMemberByEmail(memberVO.getEmail());
		
		if (member == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
	 	  			 			 .body(Map.of("message", "아이디 또는 비밀번호가 일치하지 않습니다."));
		}
		
		SHA sha = new SHA();
		String encodePassword = sha.getEncrypt(memberVO.getPassword(), member.getSalt());
		
		if (member.getPassword().equals(encodePassword)) {
			String jwt = jsonWebTokenProvider.generateToken(Duration.ofHours(12), member);
			return ResponseEntity.status(HttpStatus.CREATED)
							 	 .body(Map.of("token", jwt));
		}
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			 	  			 .body(Map.of("message", "아이디 또는 비밀번호가 일치하지 않습니다."));
	}
	
}
