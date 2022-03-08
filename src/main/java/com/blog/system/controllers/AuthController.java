package com.blog.system.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.system.dto.LoginDTO;
import com.blog.system.dto.RegistrationDTO;
import com.blog.system.entities.Role;
import com.blog.system.entities.User;
import com.blog.system.repositories.RoleRepository;
import com.blog.system.repositories.UserRepository;
import com.blog.system.security.JWTAuthResponseDTO;
import com.blog.system.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Se obtiene el token de JwtTokenProvider
		String token = jwtTokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new JWTAuthResponseDTO(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO){
		if(userRepo.existsByUsername(registrationDTO.getUsername())) {
			return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
		}
		if(userRepo.existsByEmail(registrationDTO.getEmail())) {
			return new ResponseEntity<>("There is already an account with this email", HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setName(registrationDTO.getName());
		user.setUsername(registrationDTO.getUsername());
		user.setEmail(registrationDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		
		Role roles = roleRepo.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singleton(roles));
		
		userRepo.save(user);
		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);		
	}
}
