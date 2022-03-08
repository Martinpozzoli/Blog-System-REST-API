package com.blog.system.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Se obtiene el token de la solicitud http
		String token = getJwtFromRequest(request);
		
		//Se valida el token
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			//Se obtiene el username del token
			String username = jwtTokenProvider.getJwtUsername(token);
			
			//Se carga el usuario asociado al token
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			//Se establece la seguridad
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}
		
		filterChain.doFilter(request, response);
		
		
	}
	
	//Bearer token de acceso
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7, bearerToken.length()); //Se borrar 'Bearer ' para obtener solamente el token
		}
		return null;
	}
	
}
