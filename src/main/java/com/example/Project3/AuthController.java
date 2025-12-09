package com.example.Project3;

import com.example.Project3.entities.User;         
import com.example.Project3.UserRepository;        

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
@CrossOrigin(
    origins = "http://localhost:8081",  
    allowedHeaders = "*",
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS }
)
public class AuthController {

  private final String googleClientId;
  private final String appJwtSecret;
  private final GoogleIdTokenVerifier verifier;
  private final UserRepository userRepository;   // 🔹 NEW

  public AuthController(
      @Value("${google.oauth.client-id}") String googleClientId,
      @Value("${app.jwt.secret}") String appJwtSecret,
      UserRepository userRepository                  // 🔹 NEW
  ) {
    this.googleClientId = googleClientId;
    this.appJwtSecret = appJwtSecret;
    this.userRepository = userRepository;           // 🔹 NEW
    this.verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            Utils.getDefaultJsonFactory()
        )
        .setAudience(Collections.singletonList(googleClientId))
        .build();
  }

  // existing Google login body
  public record IdTokenRequest(@NotBlank String idToken) {}

  // 🔹 NEW: username/password body
  public record CredentialsRequest(
      @NotBlank String name,
      @NotBlank String password
  ) {}

  // ---------- GOOGLE LOGIN (existing) ----------
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

  // ---------- NEW: CREATE ACCOUNT ----------
  @PostMapping("/signup")
  public Map<String, Object> signup(@RequestBody CredentialsRequest body) {
    // check if name already exists
    if (userRepository.existsByName(body.name())) {
      // simple error style; you can replace with ResponseEntity if you prefer
      throw new RuntimeException("Name already taken");
    }

    // create and save user
    User user = new User();
    user.setName(body.name());
    user.setPassword(body.password()); // plain text for now (matches your table)
    User saved = userRepository.save(user);

    // create a JWT similar to Google login
    Instant now = Instant.now();
    String sessionToken = Jwts.builder()
        .setSubject("local:" + saved.getId()) // distinguish local vs google if you like
        .claim("name", saved.getName())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(3600)))
        .signWith(SignatureAlgorithm.HS256, appJwtSecret.getBytes())
        .compact();

    return Map.of(
        "user", Map.of(
            "id", saved.getId(),
            "name", saved.getName()
        ),
        "sessionToken", sessionToken
    );
  }

  // ---------- NEW: LOGIN WITH NAME + PASSWORD ----------
  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody CredentialsRequest body) {
    User user = userRepository
        .findByNameAndPassword(body.name(), body.password())
        .orElseThrow(() -> new RuntimeException("Invalid name or password"));

    Instant now = Instant.now();
    String sessionToken = Jwts.builder()
        .setSubject("local:" + user.getId())
        .claim("name", user.getName())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(3600)))
        .signWith(SignatureAlgorithm.HS256, appJwtSecret.getBytes())
        .compact();

    return Map.of(
        "user", Map.of(
            "id", user.getId(),
            "name", user.getName()
        ),
        "sessionToken", sessionToken
    );
  }

  // ---------- helper ----------
  private GoogleIdToken verifyGoogle(String token)
      throws GeneralSecurityException, java.io.IOException {
    GoogleIdToken idToken = verifier.verify(token);
    if (idToken == null) throw new GeneralSecurityException("invalid_id_token");
    return idToken;
  }
}
