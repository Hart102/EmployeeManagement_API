package com.hart.employee_management.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserDetailsService userDetailsService;

    @Value("${auth.token.jwtSecret}")
    private String JWT_SECRET_KEY;
    private final Date JWT_EXPIRATION_TIME = new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 10);

    // Create Token with user email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(JWT_EXPIRATION_TIME)
                .signWith(SignatureAlgorithm.ES256, JWT_SECRET_KEY).compact();
    }

    //Validate generated token
    public Boolean validateToken(String token) {
        String extractedUsername = extractUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(extractedUsername);
        return userDetails.getUsername().equals(extractedUsername) && !isTokenExpired(token);
    }

    //Extract username from token (email)
    public String extractUsernameFromToken (String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    //Check if token has expired
    public Boolean isTokenExpired(String token) {
        Date expirationTime = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody().getExpiration();
        return expirationTime.before(new Date());
    }
}
