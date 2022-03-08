package com.blog.system.security;

public class JWTAuthResponseDTO {
	
	private String accessToken;
	private String tokenType = "Bearer";
	
	public JWTAuthResponseDTO(String accessToken, String tokenType) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
	
	public JWTAuthResponseDTO(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}	

}
