package com.code4ro.nextdoor.authentication.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "login.email.not.empty")
    private String email;
    @NotBlank(message = "login.password.not.empty")
    private String password;
}
