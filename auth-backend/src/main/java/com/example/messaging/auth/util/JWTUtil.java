package com.example.messaging.auth.util;

import com.example.messaging.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;

@Component
public class JWTUtil {

    @Value("${ISS}")
    private  String iss;
    @Value("${SECRET}")
    private  String secret;
    @Value("${EXPTIME}")
    private  int expTime;
    @Value("${HEADERAUTH}")
    private  String headerAuth;

    public void generateToken(Authentication authentication, HttpServletResponse response) {
        User user = (User) authentication.getPrincipal();
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.MINUTE, expTime);

        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        claims.setExpiration(exp.getTime());
        claims.setIssuer(iss);
        Key secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();

        Cookie jwtCookie = new Cookie("jwt",token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(15*24*60*60*1000);
        jwtCookie.setAttribute("sameSite","strict");
        response.addCookie(jwtCookie);
    }

    public  String parseToken(String token){
        Key secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey).build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        String userName = claims.getSubject();
        return userName;
    }

}
