package com.code4ro.nextdoor.authentication.service.impl;

import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.repository.UserRepository;
import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Unit test for AuthenticationServiceImpl")
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl service;

    private static final String PASSWORD = "pass";
    private static final String EMAIL = "test@test.com";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("successful registration")
    @Test
    void registrationSuccess() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        User result = service.register(request);

        SoftAssertions.assertSoftly(softly -> {
           softly.assertThat(result.getEmail()).isEqualTo(EMAIL);
           softly.assertThat(request.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @DisplayName("error registering an existing user")
    @Test
    void registerFailsForExistingUser() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("existing");
        when(userRepository.existsByEmail("existing")).thenReturn(true);

        NextDoorValidationException exception = catchThrowableOfType(() -> service.register(request), NextDoorValidationException.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(exception.getI18nKey()).isEqualTo("user.duplicate.email");
            softly.assertThat(exception.getI8nArguments()).isNull();
            softly.assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
        });
    }
}