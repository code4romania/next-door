package com.code4ro.nextdoor.security.jwt;

import com.code4ro.nextdoor.authentication.entity.RefreshToken;
import com.code4ro.nextdoor.security.entity.RefreshTokenHolder;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.accessTokenExpirationInMs}")
    private long accessTokenExpirationInMs;

    @Value("${app.jwt.refreshTokenExpirationInMs}")
    private long refreshTokenExpirationInMs;

    public String generateAccessToken(final UUID userId) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + accessTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public RefreshTokenHolder generateRefreshToken(final UUID userId, final String refreshToken) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + refreshTokenExpirationInMs);

        final String refreshTokenJwt = Jwts.builder()
                .setId(refreshToken)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        final RefreshToken refreshTokenEntity = new RefreshToken(userId.toString(), refreshToken, expiryDate);
        return new RefreshTokenHolder(refreshTokenJwt, refreshTokenEntity);
    }

    public UUID getUserIdFromJWT(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public String getRefreshTokenFromJWT(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getId();
    }

    public boolean isValid(final String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }

        return false;
    }
}
