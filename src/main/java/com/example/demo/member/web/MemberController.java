package com.example.demo.member.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/member/regist")
	public String viewRegistMemberPage() {
		return "member/memberregist";
	}
	
	@ResponseBody // JSON으로 응답한다.
	@GetMapping("/member/regist/available")
	public Map<String, Object> checkAvailableEmail(@RequestParam String email) {
		
		boolean isAvailableEmail = memberService.checkAvailableEmail(email); 
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("email", email);
		responseMap.put("available", isAvailableEmail);
		
		// Map을 Return하면 @ResponseBody에 의해 JSON으로 변환되어 응답된다.
		return responseMap;
	}
	
	@PostMapping("/member/regist")
	public ModelAndView doRegistMember(MemberVO memberVO) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		boolean isSuccess = memberService.createNewMember(memberVO);
		if (isSuccess) {
			modelAndView.setViewName("redirect:/member/login");
			return modelAndView;
		}
		
		modelAndView.setViewName("member/memberregist");
		modelAndView.addObject("memberVO", memberVO);
		return modelAndView;
	}
	
	@GetMapping("/member/login")
	public String viewLoginPage() {
		return "member/memberlogin";
	}
	
//	@PostMapping("/member/login")
//	public ModelAndView doLogin(@Validated(MemberLoginGroup.class) @ModelAttribute MemberVO memberVO
//							  , BindingResult bindingResult
//							  , @RequestParam(required=false, defaultValue="/board/list") String next
//							  , HttpSession session) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		if (bindingResult.hasErrors()) {
//			modelAndView.setViewName("member/memberlogin");
//			modelAndView.addObject("memberVO", memberVO);
//			return modelAndView;
//		}
//		
//		MemberVO member = memberService.getMember(memberVO);
//		session.setAttribute("_LOGIN_USER_", member);
//		modelAndView.setViewName("redirect:" + next);
//		return modelAndView;
//	}
	
	@GetMapping("/member/logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response, Authentication memberVO) {
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, memberVO);
		return "redirect:/board/list";
	}
	
	@GetMapping("/member/delete-me")
	public String doDeleteMe(HttpServletRequest request, HttpServletResponse response, Authentication memberVO) {
		boolean isSuccess = memberService.deleteMe(memberVO.getName());
		if (!isSuccess) {
			return "redirect:/member/fail-delete-me";
		}
		
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, memberVO);
		
		return "redirect:/member/success-delete-me";
	}
	
	@GetMapping("/member/{result}-delete-me")
	public String viewDeleteMePage(@PathVariable String result) {
		result = result.toLowerCase();
		if ( !result.equals("fail") && !result.equals("success") ) {
			// result의 값이 fail, success가 아니면 404페이지 보여주기
			return "error/404";
		}
		
		return "member/" + result + "deleteme";
	}
}
