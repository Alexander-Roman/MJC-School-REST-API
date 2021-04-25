package com.epam.esm.web.exception.model;

import java.io.Serializable;

public interface RootError extends Serializable {

    String getMessage();

    Integer getCode();

}
