package com.epam.esm.web.exception;

import com.epam.esm.service.exception.AccountExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.exception.model.ApiError;
import com.epam.esm.web.exception.model.BindApiError;
import com.epam.esm.web.exception.model.ConstraintError;
import com.epam.esm.web.exception.model.FieldConstraintError;
import com.epam.esm.web.exception.model.NestedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    private static final Integer TYPE_MISMATCH_CODE = 40001;
    private static final Integer BIND_EXCEPTION_CODE = 40002;
    private static final Integer CONSTRAINT_VIOLATION_EXCEPTION_CODE = 40003;
    private static final Integer METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE = 40004;
    private static final Integer ENTITY_NOT_FOUND_EXCEPTION_CODE = 40401;
    private static final Integer HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION_CODE = 40501;
    private static final Integer ACCOUNT_EXISTS_EXCEPTION_CODE = 40901;
    private static final Integer HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION_CODE = 41501;
    private static final Integer UNHANDLED_EXCEPTION_CODE = 50000;

    private final MessageSource messageSource;

    @Autowired
    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException exception,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        BindingResult bindingResult = exception.getBindingResult();
        List<NestedError> errors = this.handleBindingResult(bindingResult);
        String objectName = exception.getObjectName();
        String localizedMessage = this.getLocalizedMessage("method.argument.not.valid.exception", new Object[]{objectName}, request);
        BindApiError bindApiError = new BindApiError(localizedMessage, METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE, errors);
        return new ResponseEntity<>(bindApiError, headers, HttpStatus.BAD_REQUEST);
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
                HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION_CODE
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
        List<NestedError> errors = this.handleBindingResult(bindingResult);
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

        List<NestedError> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String code = constraintViolation.getMessageTemplate();
            String localizedMessage = constraintViolation.getMessage();

            NestedError error = new ConstraintError(
                    localizedMessage,
                    code
            );
            errors.add(error);
        }
        String localizedMessage = this.getLocalizedMessage("constraint.violation.exception", null, request);
        BindApiError bindApiError = new BindApiError(localizedMessage, CONSTRAINT_VIOLATION_EXCEPTION_CODE, errors);
        return new ResponseEntity<>(bindApiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountExistsException.class)
    protected ResponseEntity<Object> handleConflict(AccountExistsException exception, WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        String localizedMessage = this.getLocalizedMessage("account.exists.exception", null, request);

        ApiError apiError = new ApiError(localizedMessage, ACCOUNT_EXISTS_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.CONFLICT);
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

    private List<NestedError> handleBindingResult(BindingResult bindingResult) {
        List<NestedError> errors = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            Object rejectedValue = fieldError.getRejectedValue();
            String code = fieldError.getCode();
            String localizedMessage = fieldError.getDefaultMessage();

            NestedError error = new FieldConstraintError(
                    localizedMessage,
                    code,
                    field,
                    rejectedValue
            );
            errors.add(error);
        }
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
        for (ObjectError objectError : globalErrors) {
            String code = objectError.getCode();
            String localizedMessage = objectError.getDefaultMessage();

            NestedError error = new ConstraintError(
                    localizedMessage,
                    code
            );
            errors.add(error);
        }
        return errors;
    }

}
