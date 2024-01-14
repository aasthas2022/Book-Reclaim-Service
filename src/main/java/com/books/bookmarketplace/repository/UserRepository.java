/**
 * UserRepository.java
 * <p>
 * Defines the "UserRepository" interface, which serves as a repository for accessing user-related data
 * in the database. It extends the JpaRepository interface to provide basic CRUD operations for the "User" entity.
 * Additionally, it includes custom query methods for retrieving users based on specific search criteria, such as keywords
 * in username or email, and matching by either email or username.
 * <p>
 * Key Features:
 * - Annotated with @Repository to mark it as a Spring repository component.
 * - Extends JpaRepository to inherit basic database operations for the User entity.
 * - Provides custom queries to find users based on keywords and match users by either email or username.
 * <p>
 * The repository is essential for interacting with the database to access and manage user-related information within
 * the book marketplace application, facilitating user retrieval and searching functionality.
 */

package com.books.bookmarketplace.repository;

import com.books.bookmarketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing user-related data in the database.
 */
@Repository // Indicates that this interface is a Spring repository component.
public interface UserRepository extends JpaRepository<User, Long> {
    // Extends JpaRepository to provide basic CRUD operations for the User entity.

    /**
     * Custom query to retrieve a user by searching for a keyword in either username or email.
     *
     * @param keyword The keyword used for searching in username or email fields.
     * @return An optional user that matches the keyword in either username or email.
     */
    @Query("SELECT u FROM User u WHERE u.username = :keyword OR u.email = :keyword")
    Optional<User> findUserByKeyword(@Param("keyword") String keyword);

    /**
     * Custom query to retrieve a user by matching either email or username.
     *
     * @param email    The email to match.
     * @param username The username to match.
     * @return The user that matches either the provided email or username.
     */
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username")
    User findByEmailOrUsername(@Param("email") String email, @Param("username") String username);
}