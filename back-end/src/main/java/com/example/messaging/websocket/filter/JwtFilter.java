package com.example.messaging.websocket.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.security.Key;

@Component
public class JwtFilter extends GenericFilterBean {

    @Value("${SECRET}")
    private String secret;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        }
//        else {
//            if(authHeader == null || !authHeader.startsWith("Bearer ")){
//                filterChain.doFilter(request, response);
//            }
//        }
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            final String token = authHeader.substring(7);
            Key secretKey = Keys.hmacShaKeyFor(secret.getBytes());
            try {
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
                request.setAttribute("username", servletRequest.getParameter("username"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            filterChain.doFilter(request, response);
        }


    }
}
