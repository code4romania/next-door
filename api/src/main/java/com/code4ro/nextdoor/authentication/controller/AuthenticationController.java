package com.code4ro.nextdoor.authentication.controller;

import com.code4ro.nextdoor.authentication.dto.JwtAuthenticationResponse;
import com.code4ro.nextdoor.authentication.dto.LoginRequest;
import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;
import com.code4ro.nextdoor.authentication.service.AuthenticationService;
import com.code4ro.nextdoor.authentication.service.RefreshTokenService;
import com.code4ro.nextdoor.security.entity.UserPrincipal;
import com.code4ro.nextdoor.security.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Api(value = "Authentication Controller")
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthenticationController(final AuthenticationService authenticationService,
                                    final AuthenticationManager authenticationManager,
                                    final JwtTokenProvider tokenProvider,
                                    final RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @ApiOperation("Register an user")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest signUpRequest) {
        final User savedUser = authenticationService.register(signUpRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(savedUser.getId().toString()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation("Login user in application")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final String accessTokenJwt = tokenProvider.generateAccessToken(userPrincipal.getId());
        final String refreshTokenJwt = refreshTokenService.generate(userPrincipal.getId());

        return ResponseEntity.ok(new JwtAuthenticationResponse(accessTokenJwt, refreshTokenJwt));
    }

    @ApiOperation("Request a new authentication token based on a refresh token")
    @GetMapping("/token")
    public ResponseEntity<JwtAuthenticationResponse> getAccessToken(@RequestParam String refreshToken) {
        final String accessToken = authenticationService.getAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken));
    }
}
