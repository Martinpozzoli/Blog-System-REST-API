package com.blog.system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.system.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	public List<Comment> findByPostId(long postID);
}
