package com.code4ro.nextdoor.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExceptionResponse {
    private List<I18nError> i18nErrors;
    private Map<String, I18nError> i18nFieldErrors;
    private String additionalInfo;
}
