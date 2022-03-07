package com.blog.system.services;

import java.util.List;

import com.blog.system.dto.CommentDTO;

public interface CommentService {

	public CommentDTO createComment(long postID, CommentDTO commentDTO);
	
	public List<CommentDTO> getCommentsByPostId(long postID);
	
	public CommentDTO getCommentById(long postId, long commentId);
	
	public CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest);
	
	public void deleteComment(long postId, long commentId);
}
