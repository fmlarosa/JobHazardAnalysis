package com.jha.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Custom Exception to prevent duplicate insertions
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ItemAlreadyExistsException extends RuntimeException{
    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
