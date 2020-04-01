package com.code4ro.nextdoor.authentication.controller;

import com.code4ro.nextdoor.authentication.dto.LoginRequest;
import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.common.controller.AbstractControllerIntegrationTest;
import com.code4ro.nextdoor.core.exception.ExceptionResponse;
import com.code4ro.nextdoor.util.RandomObjectFiller;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration test for /api/authentication endpoint")
class AuthenticationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @DisplayName("tests registration scenarios")
    @Test
    void register() throws MalformedURLException {
        RegistrationRequest registrationRequest = RandomObjectFiller.createAndFill(RegistrationRequest.class);
        HttpEntity<RegistrationRequest> entity = new HttpEntity<>(registrationRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
            new URL(getBaseUrl() + "/register").toString(), entity, Void.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull();
            assertThat(response.getHeaders().getLocation().toString().contains("/api/users/")).isTrue();
        });

        // TODO: we could now call the users API, when that gets implemented, and check if we get the actual user

        // Try second registration with the same data, should result in error

        ResponseEntity<ExceptionResponse> response_duplicate = restTemplate.postForEntity(
            new URL(getBaseUrl() + "/register").toString(), entity, ExceptionResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(response_duplicate).isNotNull();
            assertThat(response_duplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response_duplicate.getHeaders().getLocation()).isNull();
            assertThat(response_duplicate.getBody()).isNotNull();
            assertThat(response_duplicate.getBody().getI18nErrors().size()).isEqualTo(1);
            assertThat(response_duplicate.getBody().getI18nErrors().get(0).getI18nErrorKey()).isEqualTo("user.duplicate.email");
        });
    }

    @DisplayName("tests successful login")
    @Sql(scripts = "AuthenticationControllerIntegrationTest.sql")
    @Test
    void login_success() throws MalformedURLException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("pass");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
            new URL(getBaseUrl() + "/login").toString(), entity, Void.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        });
    }

    @DisplayName("tests wrong password login")
    @Sql(scripts = "AuthenticationControllerIntegrationTest.sql")
    @Test
    void login_bad_password() throws MalformedURLException {
        LoginRequest loginRequestWrongPass = new LoginRequest();
        loginRequestWrongPass.setEmail("test@test.com");
        loginRequestWrongPass.setPassword("wrongpass");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequestWrongPass, headers);

        ResponseEntity<ExceptionResponse> response_wrong_pass = restTemplate.postForEntity(
            new URL(getBaseUrl() + "/login").toString(), entity, ExceptionResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(response_wrong_pass).isNotNull();
            assertThat(response_wrong_pass.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response_wrong_pass.getBody()).isNotNull();
            assertThat(response_wrong_pass.getBody().getI18nErrors().size()).isEqualTo(1);
            assertThat(response_wrong_pass.getBody().getI18nErrors().get(0).getI18nErrorKey()).isEqualTo("login.bad.credentials");
        });
    }

    @DisplayName("tests wrong username login")
    @Sql(scripts = "AuthenticationControllerIntegrationTest.sql")
    @Test
    void login_bad_username() throws MalformedURLException {
        LoginRequest loginRequestWrongUser = new LoginRequest();
        loginRequestWrongUser.setEmail("testwrong@test.com");
        loginRequestWrongUser.setPassword("pass");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequestWrongUser, headers);

        ResponseEntity<ExceptionResponse> response_wrong_user = restTemplate.postForEntity(
            new URL(getBaseUrl() + "/login").toString(), entity, ExceptionResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(response_wrong_user).isNotNull();
            assertThat(response_wrong_user.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response_wrong_user.getBody()).isNotNull();
            assertThat(response_wrong_user.getBody().getI18nErrors().size()).isEqualTo(1);
            assertThat(response_wrong_user.getBody().getI18nErrors().get(0).getI18nErrorKey()).isEqualTo("login.bad.credentials");
        });
    }

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port + "/api/authentication";
    }
}