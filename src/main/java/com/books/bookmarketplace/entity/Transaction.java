/**
 * Transaction.java:
 * <p>
 * Defines the "Transaction" entity class for a book marketplace application.
 * It represents a transaction made within the marketplace and includes details such as the transaction ID,
 * associated user and book, transaction date, amount, status, notes, and type (BUY or SELL).
 * <p>
 * Key Features:
 * - Annotated with @Entity to indicate it's a persistent entity in a database.
 * - Utilizes Lombok annotations for generating common methods like getters, setters, and constructors.
 * - Defines two enums for transaction types and statuses (TransactionType and TransactionStatus).
 * - Specifies validation constraints using Jakarta Bean Validation annotations.
 * <p>
 * The class serves as a blueprint for storing and managing transaction data in the application's database.
 */

package com.books.bookmarketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entity class representing a Transaction in the book marketplace application.
 */
@Entity // Indicates that this class is an entity that can be persisted in a database.
@Data // Lombok annotation that generates getters, setters, equals, hashCode, and toString methods.
@NoArgsConstructor // Lombok annotation for generating a no-argument constructor.
@AllArgsConstructor // Lombok annotation for generating an all-argument constructor.
@Builder // Lombok annotation for generating a builder pattern for creating instances of this class.

@Table(name = "Transactions") // Specifies the name of the table in the database that corresponds to this entity.
public class Transaction {
    @Id // Indicates that the following field is the primary key for the table.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies the strategy for generating primary key values.
    private Long transactionId;

    @ManyToOne // Indicates a many-to-one relationship with the User entity.
    @JoinColumn(name = "user_id") // Specifies the name of the foreign key column in the Transactions table.
    @JsonIgnore // Ignores this field during JSON serialization and deserialization.
    private User user;

    @ManyToOne // Indicates a many-to-one relationship with the Book entity.
    @JoinColumn(name = "book_id") // Specifies the name of the foreign key column in the Transactions table.
    @JsonIgnore // Ignores this field during JSON serialization and deserialization.
    private Book book;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date must be in the past or present")
    private Date transactionDate;

    @NotNull(message = "Transaction amount is required")
    @Positive(message = "Transaction amount must be a positive number")
    private Double transactionAmount;

    @NotNull
    @Enumerated(EnumType.STRING) // Specifies that the TransactionStatus is stored as a string in the database.
    private TransactionStatus status;

    @Size(max = 255, message = "Transaction notes must not exceed 255 characters")
    private String transactionNotes;

    @Enumerated(EnumType.STRING) // Specifies that the TransactionType is stored as a string in the database.
    @NotNull
    private TransactionType transactionType;

    // Enum for defining the possible transaction types (BUY and SELL).
    public enum TransactionType {
        BUY,
        SELL
    }

    // Enum for defining the possible transaction statuses (PENDING, COMPLETED, CANCELLED).
    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }

}