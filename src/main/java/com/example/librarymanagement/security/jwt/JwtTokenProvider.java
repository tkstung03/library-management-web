package com.example.librarymanagement.security.jwt;

import com.example.librarymanagement.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtTokenProvider {

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final String CARD_NUMBER_KEY = "card-number";

    @Value("${jwt.secret:76947ef7-7af1-4745-bfda-ab2d5cb09290}")
    private String SECRET_KEY;

    @Value("${jwt.access.expiration_time:60}")
    private int EXPIRATION_TIME_ACCESS_TOKEN;

    @Value("${jwt.refresh.expiration_time:1440}")
    private int EXPIRATION_TIME_REFRESH_TOKEN;

    public String generateToken(CustomUserDetails userDetails, boolean isRefreshToken){
        Map<String, Object> claim = new HashMap<>();
        claim.put(CLAIM_TYPE, isRefreshToken ? TYPE_REFRESH : TYPE_ACCESS);
        claim.put(CARD_NUMBER_KEY, userDetails.getCardNumber());

        if (isRefreshToken) {
            return Jwts.builder()
                    .setClaims(claim)
                    .setSubject(userDetails.getUserId())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME_REFRESH_TOKEN * 60L * 1000L)))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }

        return Jwts.builder()
                .setClaims(claim)
                .setSubject(userDetails.getUserId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME_ACCESS_TOKEN * 60L * 1000L)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.warn("Invalid JWT token: {} - Token: {}", ex.getMessage(), token, ex);
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractSubjectFromJwt(String token) {
        return getClaims(token).getSubject();
    }

    public String extractClaimCardNumber(String token) {
        Object cardNumber = getClaims(token).get(CARD_NUMBER_KEY);
        return cardNumber != null ? cardNumber.toString() : null;
    }

    public boolean isRefreshToken(String token) {
        try {
            return TYPE_REFRESH.equals(getClaims(token).get(CLAIM_TYPE));
        } catch (Exception ex) {
            log.error("Unable to determine token type access");
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            return TYPE_ACCESS.equals(getClaims(token).get(CLAIM_TYPE));
        } catch (Exception ex) {
            log.error("Unable to determine token type refresh");
            return false;
        }
    }

    public long getExpirationTime(String token) {
        try {
            return getClaims(token).getExpiration().getTime();
        } catch (Exception ex) {
            log.error("Unable to get expiration time from token");
            return -1;
        }
    }

    public long getRemainingTime(String token) {
        long expirationTime = getExpirationTime(token);
        if (expirationTime < 0) {
            return 0;
        }
        long remainingTime = expirationTime - System.currentTimeMillis();
        return Math.max(remainingTime, 0);
    }
}
