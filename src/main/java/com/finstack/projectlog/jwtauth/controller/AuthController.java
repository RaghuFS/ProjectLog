package com.finstack.projectlog.jwtauth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.finstack.projectlog.jwtauth.configuration.model.AuthenticationRequestDetails;
import com.finstack.projectlog.jwtauth.configuration.model.AuthenticationResponseDetails;
import com.finstack.projectlog.jwtauth.configuration.service.JWTUtil;
import com.finstack.projectlog.jwtauth.configuration.service.RSAUtil;

@RestController
@Profile("JWT")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    RSAUtil rsaUtil;

    @Autowired
    UserDetailsService securityUserDetailsService;

    @CrossOrigin(origins = "http://localhost:8001")
    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponseDetails> authenticate(@RequestBody AuthenticationRequestDetails authenticationRequestDetails) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequestDetails.getEmailAddress(), authenticationRequestDetails.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = securityUserDetailsService
                .loadUserByUsername(authenticationRequestDetails.getEmailAddress());

        final String jwt = jwtUtil.generateToken(userDetails,rsaUtil.getPrivateKey());

        return new ResponseEntity<>(new AuthenticationResponseDetails(jwt),HttpStatus.OK);
    }


    @GetMapping(path = "/hello")
    public String sayHello()
    {
        return "Hello World";
    }
}
