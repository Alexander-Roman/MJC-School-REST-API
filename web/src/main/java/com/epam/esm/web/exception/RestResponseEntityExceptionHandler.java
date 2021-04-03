package com.epam.esm.web.exception;

import com.epam.esm.web.exception.model.ApiError;
import com.epam.esm.web.exception.model.BindApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);


    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleConflict(ResponseStatusException exception, WebRequest request) {
        String message = exception.getReason();
        LOGGER.error(message, exception);

        HttpStatus httpStatus = exception.getStatus();
        int value = httpStatus.value();
        String code = Integer.toString(value);

        ApiError apiError = new ApiError(message, code);

        return new ResponseEntity<>(apiError, new HttpHeaders(), httpStatus);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(@NonNull BindException exception,
                                                         @NonNull HttpHeaders headers,
                                                         @NonNull HttpStatus status,
                                                         @NonNull WebRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> errors = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.add(field + ": " + message);
        }
        List<ObjectError> objectErrors = bindingResult.getGlobalErrors();
        for (ObjectError objectError : objectErrors) {
            String name = objectError.getObjectName();
            String message = objectError.getDefaultMessage();
            errors.add(name + ": " + message);
        }
        String message = exception.getObjectName() + " is not valid!";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        int value = httpStatus.value();
        String code = Integer.toString(value);
        BindApiError bindApiError = new BindApiError(message, code, errors);
        return new ResponseEntity<>(bindApiError, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception exception,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        String message = exception.getMessage();
        LOGGER.error(message, exception);
        return new ResponseEntity<>(message, headers, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        String message = exception.getMessage();
        LOGGER.error(message, exception);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        int value = httpStatus.value();
        String code = Integer.toString(value);

        ApiError apiError = new ApiError(message, code);

        return new ResponseEntity<>(apiError, new HttpHeaders(), httpStatus);
    }
}
