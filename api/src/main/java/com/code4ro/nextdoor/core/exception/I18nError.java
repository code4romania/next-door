package com.code4ro.nextdoor.core.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class I18nError {
    private final String i18nErrorKey;
    private final List<String> i18nErrorArguments;

    public I18nError(final String i18nErrorKey, List<String> i18nErrorArguments) {
        this.i18nErrorKey = i18nErrorKey;
        this.i18nErrorArguments = i18nErrorArguments;
    }
}
