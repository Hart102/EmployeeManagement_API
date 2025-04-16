package com.hart.employee_management.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsService userDetailsService;

    @Value("${auth.token.jwtSecret}")
    private String SECRET_KEY;
    private final Date expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);


    //Create token with user email
    public String generateToke(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    //Validate generated token
    public Boolean validateToken(String token) {
        String extractedUsername = extractUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(extractedUsername);
        return userDetails.getUsername().equals(extractedUsername) && !isTokenExpired(token);
    }

    //Extract username from token (email)
    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    //Check if token has expired
    public Boolean isTokenExpired(String token) {
        Date expirationTime = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getExpiration();
        return expirationTime.before(new Date());
    }
}