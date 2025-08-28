package com.example.messaging.auth.config;

import com.example.messaging.auth.model.User;
import com.example.messaging.auth.service.AuthService;
import com.example.messaging.auth.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Value("${HEADERAUTH}")
    private String headerAuth;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(headerAuth);
        if (!ObjectUtils.isEmpty(authHeader)){
            String accessToken = authHeader.replace("Bearer ", "");
            String username = jwtUtil.parseToken(accessToken);

            User user = authService.loadUserByUsername(username);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username, user.getPassword());
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
