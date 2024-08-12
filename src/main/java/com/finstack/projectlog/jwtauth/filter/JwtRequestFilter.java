package com.finstack.projectlog.jwtauth.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finstack.projectlog.jwtauth.configuration.service.JWTUtil;
import com.finstack.projectlog.jwtauth.configuration.service.RSAUtil;

@Component
@Profile("JWT")
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	UserDetailsService securityUserDetailsService;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	RSAUtil rsaUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt, rsaUtil.getPublicKey());
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.securityUserDetailsService.loadUserByUsername(username);

			try {
				if (jwtUtil.validateToken(jwt, userDetails, rsaUtil.getPublicKey())) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		chain.doFilter(request, response);
	}

}
