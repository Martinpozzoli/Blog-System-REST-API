package com.blog.system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.system.dto.PostDTO;
import com.blog.system.dto.ResponsePosts;
import com.blog.system.entities.Post;
import com.blog.system.exceptions.ResourceNotFoundException;
import com.blog.system.repositories.PostRepository;

@Service
public class PostServiceImp implements PostService {

	@Autowired
	private PostRepository postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDTO createPost(PostDTO postDTO) {
		Post post = mapEntity(postDTO);

		Post newPost = postRepo.save(post);

		PostDTO responsePost = mapDTO(newPost);

		return responsePost;
	}

	@Override
	public ResponsePosts getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> posts = postRepo.findAll(pageable);

		List<Post> postsList = posts.getContent();

		List<PostDTO> content = postsList.stream().map(post -> mapDTO(post)).collect(Collectors.toList());

		ResponsePosts responsePosts = new ResponsePosts();
		responsePosts.setContent(content);
		responsePosts.setPageNumber(posts.getNumber());
		responsePosts.setPageSize(posts.getSize());
		responsePosts.setTotalElements(posts.getTotalElements());
		responsePosts.setTotalPages(posts.getTotalPages());
		responsePosts.setLast(posts.isLast());

		return responsePosts;
	}

	@Override
	public PostDTO getPostById(Long id) {
		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		return mapDTO(post);
	}

	@Override
	public PostDTO updatePost(PostDTO postDTO, long id) {
		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		post.setTitle(postDTO.getTitle());
		post.setDescription(postDTO.getDescription());
		post.setContent(postDTO.getContent());

		Post updatedPost = postRepo.save(post);

		return mapDTO(updatedPost);
	}

	@Override
	public void deletePost(Long id) {
		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		postRepo.delete(post);

	}

	// Converts Entity into DTO
	private PostDTO mapDTO(Post post) {
		PostDTO postDTO = modelMapper.map(post, PostDTO.class);

		return postDTO;
	}

	// Converts DTO into Entity
	private Post mapEntity(PostDTO postDTO) {
		Post post = modelMapper.map(postDTO, Post.class);

		return post;
	}

}
