package com.steganography.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.steganography.webapp.model.UserPrincipal;
import com.steganography.webapp.model.Users;
import com.steganography.webapp.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = repo.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return new UserPrincipal(user);
	}

}
