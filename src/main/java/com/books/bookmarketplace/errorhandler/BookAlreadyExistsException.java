package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

/**
 * A custom exception class representing an exception that is thrown when attempting to add a book that already exists.
 * This exception is typically thrown when adding a new book with a duplicate identifier (e.g., ISBN or title) that is already in use.
 */
@Getter
public class BookAlreadyExistsException extends RuntimeException {
    private final String error;

    public BookAlreadyExistsException(String error) {
        this.error = error;
    }
}