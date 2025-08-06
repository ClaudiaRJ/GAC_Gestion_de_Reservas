package com.gac.gac_gestion_de_reservas.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JWTUtil {

    private static final String SECRET_KEY =
            "ae6caf7a38bc2ef1f7cc390571300aae36004480fb366f61ecf57db0f0a18b9b977fed71f40e8d200f8a49e2001d771a06ec193936a7dc0cc14dfe4f27ab74252421f858fe7870abefedcf52b8ff6f2dac06019e3bf28dda3a367f576d0a72c8f451da7794f9366c3e3fd62cc71362d9d5e47f2952c045f2fc8ec516f34b8fff3357e19c4b18ee333e32672e2f5e6be1641b9fe2cbc3e989e1ca65f534ecebabc4428411cf1199ce9e591e3bdca2e436e7836bf7e2d546363f44a507bb2f7bfcd2e6b00865be4169ff0e0b4bf83a9587fe5158c643e028ca1c28c3da2eaa3044f0ce519a7328a5058e287b2bf02791ae258174a23c49cec9fc8d7c388836d17c";

    public String issueToken(String email,String role) {
        return Jwts
                .builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(1, DAYS)
                        )
                )
                .setIssuer("https://easytable.ddns.net")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }
}
