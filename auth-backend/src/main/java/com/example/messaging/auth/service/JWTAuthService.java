package com.example.messaging.auth.service;

import com.example.messaging.auth.model.AuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JWTAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public Authentication auth(AuthDTO authDTO){
        String username = authDTO.getUsername();
        String password = authDTO.getPassword();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticationManager.authenticate(authentication);
        return authentication;
    }
}
