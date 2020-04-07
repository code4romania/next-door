package com.code4ro.nextdoor.authentication.service.impl;

import com.code4ro.nextdoor.authentication.entity.RefreshToken;
import com.code4ro.nextdoor.authentication.repository.RefreshTokenRepository;
import com.code4ro.nextdoor.authentication.service.RefreshTokenService;
import com.code4ro.nextdoor.security.entity.RefreshTokenHolder;
import com.code4ro.nextdoor.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RefreshTokenServiceImpl(final RefreshTokenRepository refreshTokenRepository,
                                   final JwtTokenProvider tokenProvider,
                                   final PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(final UUID userId, final String jti) {
        final List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserId(userId.toString());
        final Optional<RefreshToken> refreshTokenOptional = refreshTokens.stream()
                .filter(rt -> passwordEncoder.matches(jti, rt.getToken()))
                .findFirst();
        if (refreshTokenOptional.isEmpty()) {
            return false;
        }

        final RefreshToken refreshToken = refreshTokenOptional.get();
        final boolean isValid = refreshToken.getExpiryDate().after(new Date());
        if (!isValid) {
            refreshTokenRepository.delete(refreshToken);
        }

        return isValid;
    }

    @Override
    public String generate(final UUID userId) {
        final String refreshToken = UUID.randomUUID().toString();
        final RefreshTokenHolder refreshTokenHolder = tokenProvider.generateRefreshToken(userId, refreshToken);
        final RefreshToken refreshTokenEntity = refreshTokenHolder.getRefreshToken();
        refreshTokenEntity.setToken(passwordEncoder.encode(refreshToken));
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshTokenHolder.getRefreshTokenJwt();
    }
}
