package com.code4ro.nextdoor.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class NextDoorValidationException extends RuntimeException {
    private final String i18nKey;
    private final List<String> i8nArguments;
    private final HttpStatus httpStatus;

    public NextDoorValidationException(final String i18nKey,
                                       final List<String> i8nArguments,
                                       final HttpStatus httpStatus) {
        this.i18nKey = i18nKey;
        this.i8nArguments = i8nArguments;
        this.httpStatus = httpStatus;
    }

    public NextDoorValidationException(final String i18nKey,
                                       final HttpStatus httpStatus) {
        this.i18nKey = i18nKey;
        this.httpStatus = httpStatus;
        this.i8nArguments = null;
    }
}
