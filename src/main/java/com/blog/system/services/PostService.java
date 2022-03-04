package com.blog.system.services;

import com.blog.system.dto.PostDTO;
import com.blog.system.dto.ResponsePosts;

public interface PostService {

	public PostDTO createPost(PostDTO postDTO);
	
	public ResponsePosts getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	public PostDTO getPostById(Long id);
	
	public PostDTO updatePost(PostDTO postDTO, long id);
	
	public void deletePost(Long id);
}
