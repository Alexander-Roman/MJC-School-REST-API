package com.epam.esm.web.exception;

import com.epam.esm.web.exception.model.ApiError;
import com.epam.esm.web.exception.model.BindApiError;
import com.epam.esm.web.exception.model.FieldApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

    private static final Integer TYPE_MISMATCH_CODE = 40001;
    private static final Integer BIND_EXCEPTION_CODE = 40002;
    private static final Integer UNHANDLED_EXCEPTION_CODE = 50000;

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException exception,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        Object object = exception.getValue();
        String message = "Unable to process the request! Invalid variable value: " + object;
        ApiError apiError = new ApiError(
                message,
                TYPE_MISMATCH_CODE
        );
        return new ResponseEntity<>(apiError, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(@NonNull BindException exception,
                                                         @NonNull HttpHeaders headers,
                                                         @NonNull HttpStatus status,
                                                         @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldApiError> errors = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldApiError error = new FieldApiError(
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage(),
                    fieldError.getCode()
            );
            errors.add(error);
        }
        String objectName = exception.getObjectName();
        String message = "Object is not valid: " + objectName;
        BindApiError bindApiError = new BindApiError(message, BIND_EXCEPTION_CODE, errors);
        return new ResponseEntity<>(bindApiError, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception exception,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);

        String message = "Something going wrong. We are already working on a solution!";
        ApiError apiError = new ApiError(message, UNHANDLED_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
