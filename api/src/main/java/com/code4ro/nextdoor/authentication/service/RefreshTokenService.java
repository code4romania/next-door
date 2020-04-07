package com.code4ro.nextdoor.authentication.service;

import java.util.UUID;

public interface RefreshTokenService {
    boolean isValid(UUID userId, String jti);

    String generate(UUID userId);
}
