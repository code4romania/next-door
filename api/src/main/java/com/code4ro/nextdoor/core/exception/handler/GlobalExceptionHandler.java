package com.code4ro.nextdoor.core.exception.handler;

import com.code4ro.nextdoor.core.exception.ExceptionResponse;
import com.code4ro.nextdoor.core.exception.I18nError;
import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NextDoorValidationException.class)
    protected ResponseEntity<Object> handleLegalValidationException(final NextDoorValidationException ex) {
        final I18nError error = new I18nError(ex.getI18nKey(), ex.getI8nArguments());
        return buildResponseEntity(ex.getHttpStatus(), Collections.singletonList(error), null, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final Map<String, I18nError> violations = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        err -> new I18nError(err.getDefaultMessage(), null)));
        return buildResponseEntity(HttpStatus.BAD_REQUEST, null, violations, ex.getLocalizedMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(final HttpStatus httpStatus,
                                                       final List<I18nError> errors,
                                                       final Map<String, I18nError> fieldErrors,
                                                       final String additionalInfo) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setI18nErrors(errors);
        exceptionResponse.setI18nFieldErrors(fieldErrors);
        exceptionResponse.setAdditionalInfo(additionalInfo);
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}
