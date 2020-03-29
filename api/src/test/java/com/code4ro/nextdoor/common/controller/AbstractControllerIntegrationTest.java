package com.code4ro.nextdoor.common.controller;

import com.code4ro.nextdoor.NextDoorApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base class for all REST controllers integration tests.
 *
 * The integration tests are run with an autoconfigured test database (H2), they boot the container and use persistence,
 * clean the database before each test is executed.
 *
 * Because we do not want to run Flyway migration scripts that use MySQL syntax on H2, we use a dedicated test profile.
 */
@SpringBootTest(classes = NextDoorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles({ "test" })
public abstract class AbstractControllerIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected HttpHeaders headers = new HttpHeaders();

    /**
     * Gets the base url for the tested REST endpoint.
     *
     * @return the endpoint's base url
     */
    protected abstract String getBaseUrl();
}
