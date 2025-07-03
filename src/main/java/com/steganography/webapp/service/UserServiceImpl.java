package com.steganography.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.steganography.webapp.model.Users;
import com.steganography.webapp.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepo repo;

	@Override
	public boolean registerUser(Users user) {
		
		try {
			repo.save(user);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
