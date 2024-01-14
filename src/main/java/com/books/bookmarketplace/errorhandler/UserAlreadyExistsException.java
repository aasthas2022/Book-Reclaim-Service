package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

/**
 * A custom exception class representing an exception that is thrown when attempting to create a user that already exists.
 * This exception is typically thrown when adding a new user with a duplicate identifier (e.g., username or email) that is already in use.
 */
@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String error;

    public UserAlreadyExistsException(String error) {
        this.error = error;
    }
}