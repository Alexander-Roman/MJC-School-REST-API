package com.epam.esm.web.exception.model;

import java.io.Serializable;

public interface NestedError extends Serializable {

    String getMessage();

    String getCode();

}
