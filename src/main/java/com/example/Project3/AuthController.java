// package com.example.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final String googleClientId;
  private final String appJwtSecret;
  private final GoogleIdTokenVerifier verifier;

  public AuthController(
      @Value("${google.oauth.client-id}") String googleClientId,
      @Value("${app.jwt.secret}") String appJwtSecret
  ) {
    this.googleClientId = googleClientId;
    this.appJwtSecret = appJwtSecret;
    this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), Utils.getDefaultJsonFactory())
        .setAudience(Collections.singletonList(googleClientId))
        .build();
  }

  public record IdTokenRequest(@NotBlank String idToken) {}

  @PostMapping("/google")
  public Map<String, Object> verify(@RequestBody IdTokenRequest body) throws Exception {
    GoogleIdToken idToken = verifyGoogle(body.idToken());
    GoogleIdToken.Payload p = idToken.getPayload();

    Instant now = Instant.now();
    String sessionToken = Jwts.builder()
        .setSubject((String)p.getSubject()) // Google user id
        .claim("email", p.getEmail())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(3600)))
        .signWith(SignatureAlgorithm.HS256, appJwtSecret.getBytes())
        .compact();


    return Map.of(
        "user", Map.of(
            "id", p.getSubject(),
            "email", p.getEmail(),
            "name", p.get("name"),
            "picture", p.get("picture")
        ),
        "sessionToken", sessionToken
    );
  }

  private GoogleIdToken verifyGoogle(String token) throws GeneralSecurityException, java.io.IOException {
    GoogleIdToken idToken = verifier.verify(token);
    if (idToken == null) throw new GeneralSecurityException("invalid_id_token");
    return idToken;
  }
}
