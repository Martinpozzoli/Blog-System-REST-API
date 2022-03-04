package com.blog.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.system.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
