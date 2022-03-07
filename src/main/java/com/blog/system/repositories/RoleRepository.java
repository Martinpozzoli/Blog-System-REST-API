package com.blog.system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.system.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	public Optional<Role> findByName(String name);
	
}
