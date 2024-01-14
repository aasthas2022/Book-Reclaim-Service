/**
 * BookRepository.java:
 * <p>
 * Defines the "BookRepository" interface, which serves as a repository for accessing book-related data
 * in the database. It extends the JpaRepository interface to provide basic CRUD operations for the "Book" entity.
 * Additionally, it includes custom query methods for retrieving books by category, ISBN, keyword, and status.
 * <p>
 * Key Features:
 * - Annotated with @Repository to mark it as a Spring repository component.
 * - Extends JpaRepository to inherit basic database operations for the Book entity.
 * - Provides custom queries to search for and retrieve books based on various criteria.
 * <p>
 * The repository plays a crucial role in interacting with the database to access and manage book-related information
 * within the book marketplace application.
 */

package com.books.bookmarketplace.repository;

import com.books.bookmarketplace.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing book-related data in the database.
 */
@Repository // Indicates that this interface is a Spring repository component.
public interface BookRepository extends JpaRepository<Book, Long> {
    // Extends JpaRepository to provide basic CRUD operations for the Book entity.

    /**
     * Custom query to retrieve a list of books belonging to a specific category.
     *
     * @param category The category of books to retrieve.
     * @return A list of books matching the specified category.
     */
    @Query("SELECT b FROM Book b WHERE b.category = :category")
    List<Book> findBooksByCategory(@Param("category") Book.Category category);

    /**
     * Custom query to retrieve a book by its ISBN.
     *
     * @param ISBN The ISBN of the book to retrieve.
     * @return The book with the specified ISBN.
     */
    @Query("SELECT b FROM Book b WHERE b.ISBN = :ISBN")
    Book findBookByISBN(@Param("ISBN") String ISBN);

    /**
     * Custom query to search for books based on a keyword in their title or author or category.
     *
     * @param keyword The keyword to search for in titles and authors and category.
     * @return A list of books matching the provided keyword.
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.category) LIKE LOWER(:keyword)")
    List<Book> findBooksByKeyword(@Param("keyword") String keyword);

    /**
     * Custom query to retrieve a list of available books based on their status.
     *
     * @param status The status of the books to retrieve (e.g., AVAILABLE, PENDING).
     * @return A list of books with the available status.
     */
    @Query("SELECT b FROM Book b WHERE b.status = :status")
    List<Book> findAvailableBooks(@Param("status") Book.Status status);
}