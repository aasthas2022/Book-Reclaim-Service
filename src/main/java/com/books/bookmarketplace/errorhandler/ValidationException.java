package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

import java.util.List;

/**
 * A custom exception class representing an exception that is thrown when input validation fails.
 * This exception is typically thrown to handle validation errors when the provided data does not meet certain criteria or requirements.
 */
@Getter
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

}