package com.code4ro.nextdoor.authentication.controller;

import com.code4ro.nextdoor.authentication.dto.LoginRequest;
import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.service.AuthenticationService;
import com.code4ro.nextdoor.authentication.service.impl.CustomUserDetailsService;
import com.code4ro.nextdoor.common.controller.AbstractControllerUnitTest;
import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Unit test for AuthenticationController")
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerUnitTest extends AbstractControllerUnitTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "pass";
    private static final UUID USER_ID = new UUID(10, 100);

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockBean
    protected AuthenticationManager authenticationManager;

    @Test
    void register_success() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(EMAIL);
        registrationRequest.setPassword(PASSWORD);

        User user = new User(EMAIL, PASSWORD);
        user.setId(USER_ID);

        when(authenticationService.register(any(RegistrationRequest.class))).thenReturn(user);

        String json = objectMapper.writeValueAsString(registrationRequest);

        mvc.perform(post("/api/authentication/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/api/users/" + USER_ID)));

        ArgumentCaptor<RegistrationRequest> requestArgumentCaptor = ArgumentCaptor.forClass(RegistrationRequest.class);
        verify(authenticationService, times(1)).register(requestArgumentCaptor.capture());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(requestArgumentCaptor.getValue()).isNotNull();
            assertThat(requestArgumentCaptor.getValue().getEmail()).isEqualTo(EMAIL);
            assertThat(requestArgumentCaptor.getValue().getPassword()).isEqualTo(PASSWORD);
        });
    }

    @Test
    void register_duplicate_error() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(EMAIL);
        registrationRequest.setPassword(PASSWORD);

        User user = new User(EMAIL, PASSWORD);
        user.setId(USER_ID);

        when(authenticationService.register(any(RegistrationRequest.class)))
            .thenThrow(new NextDoorValidationException("user.duplicate.email", HttpStatus.CONFLICT));

        String json = objectMapper.writeValueAsString(registrationRequest);

        mvc.perform(post("/api/authentication/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.i18nErrors[0].i18nErrorKey")
                .value("user.duplicate.email"));
    }

    @Test
    void login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        Authentication authentication = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String json = objectMapper.writeValueAsString(loginRequest);

        mvc.perform(post("/api/authentication/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void login_badCredentials_error() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        String json = objectMapper.writeValueAsString(loginRequest);

        mvc.perform(post("/api/authentication/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.i18nErrors[0].i18nErrorKey")
                .value("login.bad.credentials"));
    }
}
