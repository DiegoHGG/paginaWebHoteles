package com.uva.roomBookingAuth.Model;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.kid}")
  private String kid;


  public String generateToken(String email, String name, long expirationTime) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);

    return JWT
        .create()
        .withKeyId(kid)
        .withClaim("name", name)
        .withClaim("email", email)
        .withIssuedAt(new Date())
        .withExpiresAt(expiryDate)
        .sign(algorithm);
  }
}