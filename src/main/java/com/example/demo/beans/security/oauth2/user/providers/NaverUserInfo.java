package com.example.demo.beans.security.oauth2.user.providers;

import java.util.Map;

import com.example.demo.beans.security.oauth2.user.OAuth2UserInfo;

public class NaverUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
	public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
