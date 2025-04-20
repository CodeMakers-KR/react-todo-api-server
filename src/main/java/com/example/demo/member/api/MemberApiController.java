package com.example.demo.member.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.security.SecurityUser;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.MemberVO;
import com.example.demo.util.AjaxResponse;
import com.example.demo.util.RequestUtil;

@RestController
@RequestMapping("/api/v1")
public class MemberApiController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/available/{email}/")
	public AjaxResponse checkAvailableEmail(@PathVariable String email) {
		boolean isAvailableEmail = memberService.checkAvailableEmail(email); 
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("email", email);
		responseMap.put("available", isAvailableEmail);
		
		return AjaxResponse.OK(responseMap);
	}
	
	@PostMapping("/member")
	public AjaxResponse doRegistMember(@RequestBody MemberVO memberVO) {
		boolean isSuccess = memberService.createNewMember(memberVO);
		return AjaxResponse.OK(isSuccess);
	}
	
	@GetMapping("/member")
	public AjaxResponse getMyInfo(Authentication memberVO) {
		MemberVO storedMemberVO = memberService.getMyInfo(((SecurityUser) memberVO.getPrincipal()).getUsername());
		return AjaxResponse.OK(storedMemberVO);
	}
	
	@GetMapping("/member/logout")
	public AjaxResponse doLogout(Authentication memberVO) {
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(RequestUtil.getRequest(), RequestUtil.getResponse(), memberVO);
		return AjaxResponse.OK();
	}
	
	@DeleteMapping("/member")
	public AjaxResponse doDeleteMe(Authentication memberVO) {
		boolean isSuccess = memberService.deleteMe(memberVO.getName());
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(RequestUtil.getRequest(), RequestUtil.getResponse(), memberVO);
		return AjaxResponse.OK(isSuccess);
	}
	
}
