package com.epam.esm.service.exception;

public class AccountExistsException extends ServiceException {

    public AccountExistsException() {
    }

    public AccountExistsException(String message) {
        super(message);
    }

    public AccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountExistsException(Throwable cause) {
        super(cause);
    }

}
