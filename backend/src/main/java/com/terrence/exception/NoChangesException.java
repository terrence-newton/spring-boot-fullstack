package com.terrence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoChangesException extends RuntimeException{
    public NoChangesException(String message) {
        super(message);
    }
}
