package com.books.bookmarketplace.errorhandler;

import lombok.Getter;

/**
 * A custom exception class representing an exception that is thrown when the inventory is full and cannot accommodate more items.
 * This exception is typically thrown when trying to add a book to the inventory when it has reached its capacity.
 */
@Getter
public class InventoryFullException extends RuntimeException {
    private final String error;

    public InventoryFullException(String error) {
        this.error = error;
    }
}