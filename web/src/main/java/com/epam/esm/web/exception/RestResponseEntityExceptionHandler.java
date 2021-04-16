package com.epam.esm.web.exception;

import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.exception.model.ApiError;
import com.epam.esm.web.exception.model.BindApiError;
import com.epam.esm.web.exception.model.FieldApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

    private static final Integer TYPE_MISMATCH_CODE = 40001;
    private static final Integer BIND_EXCEPTION_CODE = 40002;
    private static final Integer CONSTRAINT_VIOLATION_EXCEPTION_CODE = 40003;
    private static final Integer ENTITY_NOT_FOUND_EXCEPTION_CODE = 40401;
    private static final Integer HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION_CODE = 40501;
    private static final Integer HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION = 41501;
    private static final Integer UNHANDLED_EXCEPTION_CODE = 50000;

    private final MessageSource messageSource;

    @Autowired
    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NonNull HttpMediaTypeNotSupportedException exception,
                                                                     @NonNull HttpHeaders headers,
                                                                     @NonNull HttpStatus status,
                                                                     @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        String localizedMessage = this.getLocalizedMessage("http.media.type.not.supported.exception", null, request);
        ApiError apiError = new ApiError(
                localizedMessage,
                HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION
        );
        return new ResponseEntity<>(apiError, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException exception,
                                                                         @NonNull HttpHeaders headers,
                                                                         @NonNull HttpStatus status,
                                                                         @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        String localizedMessage = this.getLocalizedMessage("http.request.method.not.supported.exception", null, request);
        ApiError apiError = new ApiError(
                localizedMessage,
                HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException exception,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        Object value = exception.getValue();
        String localizedMessage = this.getLocalizedMessage("type.mismatch.exception", new Object[]{value}, request);
        ApiError apiError = new ApiError(
                localizedMessage,
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
            String field = fieldError.getField();
            Object rejectedValue = fieldError.getRejectedValue();
            String code = fieldError.getCode();
            String localizedMessage = this.getLocalizedMessage(code, null, request);

            FieldApiError error = new FieldApiError(
                    field,
                    rejectedValue,
                    localizedMessage,
                    code
            );
            errors.add(error);
        }
        String objectName = exception.getObjectName();
        String localizedMessage = this.getLocalizedMessage("bind.exception", new Object[]{objectName}, request);
        BindApiError bindApiError = new BindApiError(localizedMessage, BIND_EXCEPTION_CODE, errors);
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

        String localizedMessage = this.getLocalizedMessage("exception.unhandled", null, request);
        ApiError apiError = new ApiError(localizedMessage, UNHANDLED_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(EntityNotFoundException exception, WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        String localizedMessage = this.getLocalizedMessage("entity.not.found.exception", null, request);
        ApiError apiError = new ApiError(
                localizedMessage,
                ENTITY_NOT_FOUND_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict(ConstraintViolationException exception, WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        String message = exception.getMessage();
        String[] parts = message.split(" ");
        String code = parts[1];
        String localizedMessage = this.getLocalizedMessage(code, null, request);
        ApiError apiError = new ApiError(
                localizedMessage,
                CONSTRAINT_VIOLATION_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);

        String localizedMessage = this.getLocalizedMessage("exception.unhandled", null, request);
        ApiError apiError = new ApiError(localizedMessage, UNHANDLED_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getLocalizedMessage(String code, Object[] args, WebRequest request) {
        Locale locale = request.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
