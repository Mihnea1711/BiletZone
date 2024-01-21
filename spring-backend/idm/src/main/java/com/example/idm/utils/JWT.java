package com.example.idm.utils;

import com.example.idm.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWT {
    private static final SecretKey JWT_SECRET = new SecretKeySpec(Constants.JWT_SECRET_STRING.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static String createToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 24*60*60*1000); // Token will expire in 1 day

        return Jwts.builder()
                .setSubject(user.getUuid()) // Set UUID as the subject
                .claim("role", user.getRole()) // Set role in the token body
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(JWT_SECRET)
                .compact();
    }
}
