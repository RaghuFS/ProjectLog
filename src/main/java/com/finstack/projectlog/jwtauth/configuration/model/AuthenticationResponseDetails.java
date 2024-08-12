package com.finstack.projectlog.jwtauth.configuration.model;

import org.springframework.context.annotation.Profile;

import java.io.Serializable;

@Profile("JWT")
public class AuthenticationResponseDetails implements Serializable {

    private final String jwt;

    public AuthenticationResponseDetails(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
