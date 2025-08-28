package com.example.messaging.auth.service;

import com.example.messaging.auth.model.AuthDTO;
import com.example.messaging.auth.model.User;
import com.example.messaging.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public String signUpUser(AuthDTO authDTO){
        if(authRepository.findByUsername(authDTO.getUsername()) != null){
            return "User already exists";
        }
        User user = new User(authDTO.getUsername(),passwordEncoder.encode(authDTO.getPassword()));
        authRepository.save(user);
        return "User registered";
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authRepository. findByUsername(username);
        return user;
    }
}
