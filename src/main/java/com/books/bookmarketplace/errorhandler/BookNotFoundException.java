package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

/**
 * A custom exception class representing an exception that is thrown when a book is not found.
 * This exception is typically thrown when attempting to retrieve a book that does not exist in the system.
 */
@Getter
public class BookNotFoundException extends RuntimeException {
    private final String error;

    public BookNotFoundException(String error) {
        this.error = error;
    }
}