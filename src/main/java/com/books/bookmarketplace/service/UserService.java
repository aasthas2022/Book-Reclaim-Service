/**
 * UserService.java
 * This interface defines a set of operations that can be performed on users within the marketplace application.
 * Users are an integral part of the system, and this interface specifies methods for managing user-related
 * tasks such as retrieval, addition, update, and deletion of user profiles.
 * It also provides functionality to retrieve user purchase and sales history and to add funds to user accounts.
 * <p>
 * The interface encompasses the following key areas:
 * - Retrieving user details, both in bulk and by individual ID.
 * - Searching for users by email or username.
 * - Adding new users to the marketplace.
 * - Updating user information, including profiles.
 * - Deleting users from the system.
 * - Retrieving lists of books purchased and sold by a user.
 * - Adding funds to a user's account.
 * <p>
 * Implementations of this interface are expected to provide the necessary functionality for managing user-related operations in the marketplace,
 * contributing to a seamless user experience and data consistency.
 */

package com.books.bookmarketplace.service;

import com.books.bookmarketplace.entity.User;
import com.books.bookmarketplace.model.BookDetails;
import com.books.bookmarketplace.model.UserDetails;

import java.util.List;

/**
 * This interface defines the operations that can be performed on users within the marketplace.
 */
public interface UserService {

    /**
     * Retrieves a list of all users in the marketplace.
     *
     * @return List of user details for all users.
     */
    List<UserDetails> getAllUsers();

    /**
     * Retrieves user details by their unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return User details for the user with the given ID.
     */
    UserDetails getUserById(Long id);

    /**
     * Searches for users by email or username based on a keyword.
     *
     * @param keyword The keyword to search for in user emails and usernames.
     * @return User details matching the search keyword.
     */
    UserDetails searchUsersByEMailOrUserName(String keyword);

    /**
     * Adds a new user to the marketplace.
     *
     * @param user The new user to add.
     * @return The added user.
     */
    User addUser(User user);

    /**
     * Updates the information of an existing user.
     *
     * @param user The updated user information.
     * @param id   The ID of the user to update.
     * @return The updated user.
     */
    User updateUser(User user, Long id);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to delete.
     */
    void deleteUser(Long id);

    /**
     * Retrieves a list of books purchased by a user.
     *
     * @param id The ID of the user to retrieve purchased books for.
     * @return List of books purchased by the user.
     */
    List<BookDetails> getPurchasedBooksByUser(Long id);

    /**
     * Retrieves a list of books sold by a user.
     *
     * @param id The ID of the user to retrieve books sold by.
     * @return List of books sold by the user.
     */
    List<BookDetails> getBooksSoldByUser(Long id);

    /**
     * Adds funds to a user's account.
     *
     * @param userId The ID of the user to add funds to.
     * @param amount The amount to add to the user's funds.
     * @return The updated user with the added funds.
     */
    User addFundsToUser(Long userId, Double amount);
}