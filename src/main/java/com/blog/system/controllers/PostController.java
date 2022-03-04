package com.blog.system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.system.dto.PostDTO;
import com.blog.system.dto.ResponsePosts;
import com.blog.system.services.PostService;
import com.blog.system.utilities.AppConstants;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping
	public ResponsePosts getPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_ORDER_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_ORDER_DIR, required = false) String sortDir) {
		return postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(postService.getPostById(id));
	}

	@PostMapping
	public ResponseEntity<PostDTO> savePost(@RequestBody PostDTO postDTO) {

		return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable(name = "id") Long id) {
		PostDTO responsePost = postService.updatePost(postDTO, id);
		return new ResponseEntity<>(responsePost, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
		postService.deletePost(id);
		return new ResponseEntity<>("Post successfully deleted", HttpStatus.OK);

	}
}
