/**
 * BookDetails.java:
 * <p>
 * Defines the "BookDetails" model class for the book marketplace application.
 * It represents a simplified view of book details and is used for data transfer and presentation.
 * <p>
 * Key Features:
 * - Utilizes Lombok to generate getters, setters, equals, hashCode, and toString methods for convenience.
 * - Contains fields to represent essential information about a book, including their unique identifier,
 * isbn, title, author, price and so on.
 * <p>
 * The class is designed to streamline the handling and presentation of book information within the application,
 * offering a structured representation of user details for data transfer and display purposes.
 */
package com.books.bookmarketplace.model;

import lombok.Data;

/**
 * A model class representing details of a book in the book marketplace application.
 */
@Data // Lombok annotation that generates getters, setters, equals, hashCode, and toString methods.
public class BookDetails {
    private String ISBN;
    private String title;
    private String author;
    private int edition;
    private int publicationYear;
    private String language;
    private String publisher;
    private Double originalPrice;
    private Double currentPrice;
    private String description;
    private String category;
    private String conditionDescription;
    private int quantity;
    private Long bookId;
    private String condition;
    private String status;
}