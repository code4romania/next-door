package com.code4ro.nextdoor.authentication.service;

import com.code4ro.nextdoor.authentication.dto.RegistrationRequest;
import com.code4ro.nextdoor.authentication.entity.User;

public interface AuthenticationService {
    User register(RegistrationRequest registrationRequest);
}
