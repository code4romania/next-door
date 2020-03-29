package com.code4ro.nextdoor.common.controller;

import com.code4ro.nextdoor.security.jwt.JwtAuthenticationEntryPoint;
import com.code4ro.nextdoor.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for REST controllers unit tests.
 */
public abstract class AbstractControllerUnitTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    protected HttpHeaders headers = new HttpHeaders();
}
