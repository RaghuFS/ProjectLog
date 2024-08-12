package com.finstack.projectlog.jwtauth.configuration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Profile("JWT")
public class AuthenticationRequestDetails {

    private String emailAddress;
    private String password;
}
