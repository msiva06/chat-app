package com.example.messaging.auth.controller;

import com.example.messaging.auth.model.AuthDTO;
import com.example.messaging.auth.model.User;
import com.example.messaging.auth.service.AuthService;
import com.example.messaging.auth.service.JWTAuthService;
import com.example.messaging.auth.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTAuthService jwtAuthService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping ("/signup")
    public String signUp(@RequestBody AuthDTO authDTO){
        return authService.signUpUser(authDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO, HttpServletResponse resp){
            User user = authService.loadUserByUsername(authDTO.getUsername());
            if(user == null){
                ResponseEntity<String> error = new ResponseEntity<>("Auth failed",HttpStatus.BAD_REQUEST);
                return error;
            }

            if(!passwordEncoder.matches(authDTO.getPassword(),user.getPassword())){
                ResponseEntity<String> error = new ResponseEntity<>("Bad credentials",HttpStatus.BAD_REQUEST);
                return error;
            }
            Authentication auth = jwtAuthService.auth(authDTO);
            jwtUtil.generateToken(auth,resp);
            ResponseEntity<String> response = new ResponseEntity<>("User successfully logged in",HttpStatus.OK);
            return response;
    }
}
