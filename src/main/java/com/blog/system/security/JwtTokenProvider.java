package com.blog.system.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.blog.system.exceptions.BlogAppException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs;
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date actualDate = new Date();
		Date expirationDate = new Date(actualDate.getTime() + jwtExpirationInMs);
		
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		
		return token;
	}
	
	public String getJwtUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Invalid JWT Signature");
		
		} catch (MalformedJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
		
		}catch (ExpiredJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
		
		}catch (UnsupportedJwtException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "JWT Token not supported");
		
		}catch (IllegalArgumentException ex) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "JWTs claims chain is empty");
		}
		
	}
}
