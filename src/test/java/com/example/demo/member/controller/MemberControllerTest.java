package com.example.demo.member.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.MemberServiceImpl;
import com.example.demo.member.web.MemberController;

@WebMvcTest(MemberController.class)
@Import(MemberServiceImpl.class)
public class MemberControllerTest {

	/**
	 * Controller 테스트를 위한 Mock 객체
	 */
	@Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @Test
	public void checkAvailableEmailTest() throws Exception {
    	String email = "user01@gmail.com";
		given(memberService.checkAvailableEmail(email))
				.willReturn(true);
		
		// MVC 요청 처리
		mvc.perform(get("/member/regist/available?email=" + email))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().string(containsString(email)))
					.andExpect(content().string(containsString("true")));
		
		verify(memberService).checkAvailableEmail(email);
	}
    
    @Test
    @DisplayName("회원 가입 성공 테스트")
    public void doRegistMemberTest() throws Exception {
    	// memberService.createNewMember에 아무 파라미터라도 넘어간다면 true를 반환.
    	given(memberService.createNewMember(any()))
    		.willReturn(true);
    	
		mvc.perform(post("/member/regist")
						.param("email", "user02@gmail.com")
						.param("name", "testuser")
						.param("password", "testpassword")
						.param("confirmPassword", "testpassword"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("redirect:/member/login"));
    }
    
    @Test
    @DisplayName("회원 가입 실패 테스트 - hasError")
    public void doRegistMemberTest2() throws Exception {
    	// memberService.createNewMember에 아무 파라미터라도 넘어간다면 true를 반환.
    	given(memberService.createNewMember(any()))
    		.willReturn(true);
    	
		mvc.perform(post("/member/regist")
						.param("email", "user02@gmail.com")
						.param("password", "testpassword")
						.param("confirmPassword", "testpassword"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(view().name("member/memberregist"));
    }
    
    @Test
    @DisplayName("회원 가입 실패 테스트 - isSuccess is false")
    public void doRegistMemberTest3() throws Exception {
    	// memberService.createNewMember에 아무 파라미터라도 넘어간다면 true를 반환.
    	given(memberService.createNewMember(any()))
    		.willReturn(false);
    	
		mvc.perform(post("/member/regist")
						.param("email", "user02@gmail.com")
						.param("name", "testuser")
						.param("password", "testpassword")
						.param("confirmPassword", "testpassword"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("member/memberregist"));
    }
}
