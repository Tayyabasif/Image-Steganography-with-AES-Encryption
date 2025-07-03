package com.steganography.webapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.steganography.webapp.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer>{

	Users findByEmail(String email);
}
