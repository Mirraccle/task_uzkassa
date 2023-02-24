package uz.company.task.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.company.task.domain.enums.UserType;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {
    @Value("${application.jwt.secret}")
    private String secret;

    @Value("${application.jwt.expired}")
    private long validityInMilliseconds;

    public String generateToken(UserDetails userDetails, UserType userType, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", userType);
        claims.put("email", email);
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        Date current = new Date();
        Date validity = new Date(current.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(current)
                .setExpiration(validity)
                .setHeader(headers)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (!claims.getExpiration().before(new Date()));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes =Base64.getEncoder().encode(secret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
