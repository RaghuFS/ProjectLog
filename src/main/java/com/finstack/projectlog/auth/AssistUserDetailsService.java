package com.finstack.projectlog.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.repository.ResourceRepository;

@Service
public class AssistUserDetailsService implements UserDetailsService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        Resource resource= resourceRepository.findByEmailAddress(emailAddress).orElseThrow(() -> new RuntimeException("user with the given email address not found"));
        return new AssistUserDetails(resource);
    }


}
