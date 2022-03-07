package com.blog.system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.system.dto.CommentDTO;
import com.blog.system.entities.Comment;
import com.blog.system.entities.Post;
import com.blog.system.exceptions.BlogAppException;
import com.blog.system.exceptions.ResourceNotFoundException;
import com.blog.system.repositories.CommentRepository;
import com.blog.system.repositories.PostRepository;

@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	private CommentRepository commentRepo;

	@Autowired
	private PostRepository postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDTO createComment(long postId, CommentDTO commentDTO) {

		Comment comment = mapEntity(commentDTO);
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		comment.setPost(post);
		Comment newComment = commentRepo.save(comment);
		return mapDTO(newComment);
	}

	@Override
	public List<CommentDTO> getCommentsByPostId(long postID) {
		List<Comment> comments = commentRepo.findByPostId(postID);
		return comments.stream().map(comment -> mapDTO(comment)).collect(Collectors.toList());
	}

	@Override
	public CommentDTO getCommentById(long postId, long commentId) {

		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "The comment doesn't belong to this post");
		}

		return mapDTO(comment);
	}

	@Override
	public CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "The comment doesn't belong to this post");
		}

		comment.setName(commentRequest.getName());
		comment.setEmail(commentRequest.getEmail());
		comment.setBody(commentRequest.getBody());

		Comment updatedComment = commentRepo.save(comment);

		return mapDTO(updatedComment);
	}

	@Override
	public void deleteComment(long postId, long commentId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST, "The comment doesn't belong to this post");
		}

		commentRepo.delete(comment);
	}

	// Converts Entity into DTO
	private CommentDTO mapDTO(Comment comment) {

		CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

		return commentDTO;
	}

	// Converts DTO into Entity
	private Comment mapEntity(CommentDTO commentDTO) {

		Comment comment = modelMapper.map(commentDTO, Comment.class);

		return comment;
	}

}
