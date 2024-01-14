/**
 * TransactionRepository.java:
 * <p>
 * This Java file defines the "TransactionRepository" interface, which serves as a repository for accessing
 * transactions-related data in the database. It extends the JpaRepository interface to provide basic CRUD
 * operations for the "Transaction" entity. Additionally, it includes custom query methods for retrieving books
 * that a user has either sold or purchased.
 * <p>
 * Key Features:
 * - Annotated with @Repository to mark it as a Spring repository component.
 * - Extends JpaRepository to inherit basic database operations for the Transaction entity.
 * - Provides custom queries to find books sold and purchased by a specific user based on transaction type.
 * <p>
 * The repository plays a vital role in interacting with the database to access and manage transaction-related
 * information within the book marketplace application.
 */

package com.books.bookmarketplace.repository;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing transactions-related data in the database.
 */
@Repository // Indicates that this interface is a Spring repository component.
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Extends JpaRepository to provide basic CRUD operations for the Transaction entity.

    /**
     * Custom query to retrieve a list of books sold by a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of books that the user has sold.
     */
    @Query("SELECT t.book FROM Transaction t WHERE t.user.userId = :userId AND t.transactionType = 'SELL'")
    List<Book> findBooksSoldByUser(Long userId);

    /**
     * Custom query to retrieve a list of books purchased by a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of books that the user has purchased.
     */
    @Query("SELECT t.book FROM Transaction t WHERE t.user.userId = :userId AND t.transactionType = 'BUY'")
    List<Book> findPurchasedBooksByUser(Long userId);
}