package org.example.bookreview.common.exception;

import io.jsonwebtoken.JwtException;

public class InvalidTokenException extends JwtException {

    public InvalidTokenException(String message) {
        super(message);
    }
}