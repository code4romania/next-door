package com.code4ro.nextdoor.authentication.service.impl;

import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DisplayName("Unit test for CustomUserDetailsService")
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService service;

    private static final String EMAIL = "test@test.com";
    private static final UUID USER_ID = new UUID(10, 100);
    private static final String PASSWORD = "a password";
    private static final String EMAIL_UNKNOWN = "unknown";

    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;

    @DisplayName("successful loading by user name")
    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(USER_ID);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);

        UserDetails result = service.loadUserByUsername(EMAIL);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result).isNotNull();
            softly.assertThat(result.getUsername()).isEqualTo(EMAIL);
            softly.assertThat(result.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @DisplayName("error loading by user name")
    @Test
    void loadUserByUnknownUsername() {
        when(userRepository.findByEmail(EMAIL_UNKNOWN)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> service.loadUserByUsername(EMAIL_UNKNOWN))
            .withMessage("User not found with email: " + EMAIL_UNKNOWN);
    }

    @DisplayName("successful loading by user id")
    @Test
    void loadUserById() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(USER_ID);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);

        UserDetails result = service.loadUserById(USER_ID);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result).isNotNull();
            softly.assertThat(result.getUsername()).isEqualTo(EMAIL);
            softly.assertThat(result.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @DisplayName("error loading by user id")
    @Test
    void loadUserByUnknownId() {
        UUID unknownId = new UUID(30, 300);
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> service.loadUserById(unknownId))
            .withMessage("User not found with id: " + unknownId.toString());
    }
}