package com.code4ro.nextdoor.authentication.service.impl;

import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.repository.UserRepository;
import com.code4ro.nextdoor.authentication.service.AuthenticationService;
import com.code4ro.nextdoor.authentication.service.RefreshTokenService;
import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import com.code4ro.nextdoor.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthenticationServiceImpl(final UserRepository userRepository,
                                     final PasswordEncoder passwordEncoder,
                                     final JwtTokenProvider tokenProvider,
                                     final RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    @Override
    public User register(final RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new NextDoorValidationException("user.duplicate.email", HttpStatus.CONFLICT);
        }

        final String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        final User user = new User(registrationRequest.getEmail(), encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public String getAccessToken(final String refreshTokenJwt) {
        if (!StringUtils.hasText(refreshTokenJwt) || !tokenProvider.isValid(refreshTokenJwt)) {
            throw new NextDoorValidationException("authentication.refreshToken.invalid", HttpStatus.BAD_REQUEST);
        }

        final String refreshToken = tokenProvider.getRefreshTokenFromJWT(refreshTokenJwt);
        final UUID userId = tokenProvider.getUserIdFromJWT(refreshTokenJwt);

        if (!refreshTokenService.isValid(userId, refreshToken)) {
            throw new NextDoorValidationException("authentication.refreshToken.invalid", HttpStatus.BAD_REQUEST);
        }

        return tokenProvider.generateAccessToken(userId);
    }
}
