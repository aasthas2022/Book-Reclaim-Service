/**
 * Book.java:
 * <p>
 * Defines the "Book" entity class for a book marketplace application.
 * It represents books available in the marketplace, storing information such as book ID, ISBN, title, author,
 * edition, publication year, language, publisher, prices, description, category, condition, and status.
 * <p>
 * Key Features:
 * - Annotated with @Entity to indicate it's a persistent entity to be mapped to a database table.
 * - Utilizes Lombok annotations to generate common methods like getters, setters, constructors, and builders.
 * - Defines validation constraints using Jakarta Bean Validation annotations for various book attributes.
 * - Establishes one-to-many relationships with the "Transaction" entity to track book transactions.
 * - Enumerates possible book categories, conditions, and statuses for standardized classification.
 * - Provides a method, depreciatePrice, for reducing the book's current price by 10%.
 * <p>
 * The class serves as a blueprint for storing and managing book data in the application's database,
 * facilitating the exchange and tracking of books in the marketplace.
 */

package com.books.bookmarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Book in the book marketplace application.
 */
@Entity // Indicates that this class is a JPA entity, to be mapped to a database table.
@Data // Lombok annotation to generate getter and setter methods, toString, equals, and hashCode.
@NoArgsConstructor // Lombok annotation for generating a no-argument constructor.
@AllArgsConstructor // Lombok annotation for generating a constructor with all fields.
@Builder // Lombok annotation for a builder pattern, useful for creating instances.
@Table(name = "Books") // Specifies the name of the database table for this entity.
public class Book {
    @Id // Marks this field as the primary key of the database table.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies how the primary key is generated.
    private Long bookId;

    @NotBlank(message = "ISBN is required") // Validation constraint, the ISBN field must not be blank.
    private String ISBN;

    @NotBlank(message = "Title is required") // Validation constraint, the title field must not be blank.
    private String title;

    @NotBlank(message = "Author is required") // Validation constraint, the author field must not be blank.
    private String author;

    private int edition; // Edition of the book.

    @Positive(message = "Year must be a positive number") // Validation constraint, the year must be a positive number.
    private int publicationYear;

    @NotBlank(message = "Language is required") // Validation constraint, the language field must not be blank.
    private String language;

    private int quantity; // Quantities of each book

    private String publisher; // Publisher of the book.

    @NotNull // Validation constraint, the original price must not be null.
    @Positive(message = "Original price must be a positive number") // Original price must be a positive number.
    private Double originalPrice;

    @NotNull // Validation constraint, the current price must not be null.
    @Positive(message = "Current price must be a positive number") // Current price must be a positive number.
    private Double currentPrice;

    @Lob // Large object annotation, used for large text fields like descriptions.
    private String description;

    @Enumerated(EnumType.STRING) // Specifies that the category is represented as a string in the database.
    @NotNull // Validation constraint, the category must not be null.
    private Category category;

    private String conditionDescription; // Description of the book's condition.

    @Enumerated(EnumType.STRING) // Specifies that the condition is represented as a string in the database.
    @Column(name = "book_condition") // Specifies the column name in the database.
    private Condition condition; // The condition of the book.

    @Enumerated(EnumType.STRING) // Specifies that the status is represented as a string in the database.
    @Column(name = "book_status") // Specifies the column name in the database.
    private Status status; // The status of the book.

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL) // Defines a one-to-many relationship with transactions.
    private List<Transaction> transactions = new ArrayList<>(); // List of transactions associated with the book.

    public enum Category {
        // Enumerated values for book categories.
        FICTION,
        NON_FICTION,
        SCIENCE,
        HISTORY,
        MYSTERY,
        ROMANCE,
        TECHNOLOGY,
        COOKING,
        TRAVEL,
        BIOGRAPHY,
        FANTASY,
        HORROR,
        BUSINESS,
        POETRY,
        PHILOSOPHY,
        RELIGION,
        ART,
        EDUCATION,
        KIDS
    }

    public enum Condition {
        // Enumerated values for book conditions.
        NEW,
        LIKE_NEW,
        GOOD,
        ACCEPTABLE,
        POOR,
        VERY_POOR,
        EXCELLENT,
        FAIR,
        UNUSED
    }

    public enum Status {
        // Enumerated values for book statuses.
        AVAILABLE,
        PENDING,
        SOLD,
        RESERVED,
        OUT_OF_STOCK,
        DISCONTINUED,
        DAMAGED
    }

    public void depreciatePrice() {
        // Method to calculate the new current price after depreciation (10% reduction).
        double depreciationFactor = 0.90; // 10% depreciation
        currentPrice = currentPrice * depreciationFactor;
    }
}