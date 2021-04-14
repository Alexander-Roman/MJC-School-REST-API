package com.epam.esm.web.exception;

import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.exception.model.ApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);

    private static final Integer UNHANDLED_EXCEPTION_CODE = 50000;
    private static final Integer ENTITY_NOT_FOUND_EXCEPTION_CODE = 40401;
    private static final Integer CONSTRAINT_VIOLATION_EXCEPTION_CODE = 40003;

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(EntityNotFoundException exception, WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                exception.getMessage(),
                ENTITY_NOT_FOUND_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict(ConstraintViolationException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                exception.getMessage(),
                CONSTRAINT_VIOLATION_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);

        String message = "Something going wrong. We are already working on a solution!";
        ApiError apiError = new ApiError(message, UNHANDLED_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
