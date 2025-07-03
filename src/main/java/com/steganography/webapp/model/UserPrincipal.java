package com.steganography.webapp.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Users user;
	
	public UserPrincipal(Users user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getUserPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;	}
	
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	

	@Override
	public boolean isEnabled() {
		return true;
	}
	

}
