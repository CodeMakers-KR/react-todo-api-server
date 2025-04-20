package com.example.demo.beans.security.oauth2.user.providers;

import java.util.Map;

import com.example.demo.beans.security.oauth2.user.OAuth2UserInfo;

public class GoogleUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
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
