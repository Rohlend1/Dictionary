package com.example.dictionary.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username",username)
                .withIssuedAt(new Date())
                .withIssuer("dictionary-app")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        DecodedJWT jwt = createJwtVerifier().verify(token);
        return jwt.getClaim("username").asString();
    }

    public String rewriteUsernameInToken(String username, String token){
        DecodedJWT jwt = createJwtVerifier().verify(token);
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(jwt.getIssuedAt())
                .withIssuer("dictionary-app")
                .withExpiresAt(jwt.getExpiresAt())
                .sign(Algorithm.HMAC256(secret));
    }

    private JWTVerifier createJwtVerifier(){
        return JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("dictionary-app")
                .build();
    }

    public String generateSharingToken(String dictId){
        Date expirationDate = Date.from(ZonedDateTime.now().plusWeeks(1).toInstant());
        return JWT.create()
                .withSubject("Dict details")
                .withClaim("dictId", dictId)
                .withIssuedAt(new Date())
                .withIssuer("dictionary-app")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    private JWTVerifier createSharedJwtVerifier(){
        return JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Dict details")
                .withIssuer("dictionary-app")
                .build();
    }

    public String validateSharedTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        DecodedJWT jwt = createSharedJwtVerifier().verify(token);
        return jwt.getClaim("dictId").asString();
    }
}
