package com.dh.hotblue.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dh.hotblue.user.entity.UserEntity;



public class PrincipalDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private UserEntity user;
	
	public PrincipalDetails(UserEntity user) {
		this.user = user;
	}
	
	public UserEntity getUser() {
		return this.user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<> ();
		user.getRoleList().forEach((r -> {
			authorities.add(()->r);
		}));
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getNickname();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
