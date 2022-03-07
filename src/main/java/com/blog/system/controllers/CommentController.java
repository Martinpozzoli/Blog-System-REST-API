package com.blog.system.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.blog.system.dto.CommentDTO;
import com.blog.system.services.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@GetMapping("/posts/{postID}/comments")
	public List<CommentDTO> listCommentsByPostId(@PathVariable(value = "postID") long postID){
		return commentService.getCommentsByPostId(postID);
	}
	
	@GetMapping("/posts/{postID}/comments/{id}")
	public ResponseEntity<CommentDTO> findCommentById(@PathVariable(value = "postID") long postID,
			@PathVariable(value = "id") long commentId){
		CommentDTO commentDTO = commentService.getCommentById(postID, commentId);
		return new ResponseEntity<>(commentDTO, HttpStatus.OK);
	}

	@PostMapping("/posts/{postID}/comments")
	public ResponseEntity<CommentDTO> saveComment(@PathVariable(value = "postID") long postID,
			@RequestBody CommentDTO commentDTO) {

		return new ResponseEntity<>(commentService.createComment(postID, commentDTO), HttpStatus.CREATED);

	}
	
	@PutMapping("/posts/{postID}/comments/{id}")
	public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postID") long postID,
			@PathVariable(value = "id") long commentId,
			@RequestBody CommentDTO commentDTO){
		CommentDTO updatedComment = commentService.updateComment(postID, commentId, commentDTO);
		
		return new ResponseEntity<>(updatedComment, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/posts/{postID}/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable(value = "postID") long postID,
			@PathVariable(value = "id") long commentId){
		
		commentService.deleteComment(postID, commentId);
		
		return new ResponseEntity<>("Comment successfully deleted", HttpStatus.OK);
	}
}





