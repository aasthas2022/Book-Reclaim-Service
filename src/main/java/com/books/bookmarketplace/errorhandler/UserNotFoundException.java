package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

/**
 * A custom exception class representing an exception that is thrown when a user is not found.
 * This exception is typically thrown when attempting to retrieve a user who does not exist in the system.
 */
@Getter
public class UserNotFoundException extends RuntimeException {
    private final String error;

    public UserNotFoundException(String error) {
        this.error = error;
    }
}