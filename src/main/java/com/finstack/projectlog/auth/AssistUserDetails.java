package com.finstack.projectlog.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.finstack.projectlog.entity.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssistUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Resource resource;

	public AssistUserDetails(Resource resource) {
		this.resource = resource;
	}

	@Override
	@Transactional
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

		resource.getRoles().forEach((role) -> log.info("role injected to the user is " + role.getName()));
		resource.getRoles().forEach((role) -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		return authorities;
	}

	@Override
	public String getPassword() {
		return "12345";
	}

	@Override
	public String getUsername() {
		return resource.getEmailAddress();
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
		return resource.isStatus();
	}
}
