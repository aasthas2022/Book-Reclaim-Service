/**
 * UserDetails.java:
 * <p>
 * Defines the "UserDetails" model class for the book marketplace application.
 * It represents a simplified view of user details and is used for data transfer and presentation.
 * <p>
 * Key Features:
 * - Utilizes Lombok to generate getters, setters, equals, hashCode, and toString methods for convenience.
 * - Contains fields to represent essential information about a user, including their unique identifier,
 * username, email, first name, last name, phone number, available funds, and a list of associated transaction details.
 * <p>
 * The class is designed to streamline the handling and presentation of user information within the application,
 * offering a structured representation of user details for data transfer and display purposes.
 */

package com.books.bookmarketplace.model;

import lombok.Data;

import java.util.List;

/**
 * A model class representing details of a user in the book marketplace application.
 */
@Data // Lombok annotation that generates getters, setters, equals, hashCode, and toString methods.
public class UserDetails {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double funds;
    private List<TransactionDetails> transactionDetails;
}
