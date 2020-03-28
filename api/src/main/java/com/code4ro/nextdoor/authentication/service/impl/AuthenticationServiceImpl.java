package com.code4ro.nextdoor.authentication.service.impl;

import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.repository.UserRepository;
import com.code4ro.nextdoor.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(final UserRepository userRepository,
                                     final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
