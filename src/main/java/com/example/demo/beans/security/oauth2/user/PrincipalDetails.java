package com.example.demo.beans.security.oauth2.user;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.beans.security.SecurityUser;
import com.example.demo.member.vo.MemberVO;

public class PrincipalDetails extends SecurityUser implements OAuth2User {

	private static final long serialVersionUID = 8605358015228485309L;
	
    private OAuth2UserInfo oAuth2UserInfo;

    public PrincipalDetails(MemberVO user, OAuth2UserInfo oAuth2UserInfo) {
    	super(user);
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }
    
}
