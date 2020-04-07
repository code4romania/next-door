package com.code4ro.nextdoor.security.entity;

import com.code4ro.nextdoor.authentication.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenHolder {
    private final String refreshTokenJwt;
    private final RefreshToken refreshToken;
}
