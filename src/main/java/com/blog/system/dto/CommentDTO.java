package com.blog.system.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CommentDTO {
	
	private long id;
	
	@NotEmpty(message = "Name must not be empty")
	private String name;
	
	@NotEmpty(message = "Email must not be empty")
	@Email(message = "Must be a properly formatted email address")
	private String email;
	
	@NotEmpty(message = "Body must not be empty")
	@Size(min = 10, message = "Body must be at least 10 characters long")
	private String body;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public CommentDTO(long id, String name, String email, String body) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.body = body;
	}
	public CommentDTO() {
		super();
	}
	
	
}
