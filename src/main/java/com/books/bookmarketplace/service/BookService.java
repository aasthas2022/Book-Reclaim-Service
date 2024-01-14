/**
 * BookService.java:
 * <p>
 * This interface, "BookService," defines a set of operations that can be performed on books within the book marketplace.
 * It serves as a contract outlining the methods available for managing book-related data and interactions in the application.
 * <p>
 * Key Features:
 * - Specifies methods for retrieving book information, including lists of all books, available books, and books by category.
 * - Allows for book retrieval by unique identifiers, ISBN, and searching based on keywords.
 * - Provides methods for adding, updating, and deleting books in the marketplace.
 * - Includes methods for users to purchase and sell books, both by their unique identifiers and ISBNs.
 * <p>
 * Implementations of this interface are responsible for facilitating book-related actions and interactions
 * within the book marketplace application.
 */

package com.books.bookmarketplace.service;

import com.books.bookmarketplace.entity.Book;

import java.util.List;

/**
 * This interface defines the operations that can be performed on books within the marketplace.
 */
public interface BookService {
    /**
     * Retrieve a list of all books available in the marketplace.
     *
     * @return A list of all books.
     */
    List<Book> getAllBooks();

    /**
     * Retrieve a list of books that are currently available for purchase.
     *
     * @return A list of available books.
     */
    List<Book> getAvailableBooks();

    /**
     * Retrieve a book by its unique identifier.
     *
     * @param id The unique identifier of the book.
     * @return The book with the specified identifier.
     */
    Book getBookById(Long id);

    /**
     * Retrieve a list of books in a specific category.
     *
     * @param category The category of books to retrieve.
     * @return A list of books belonging to the specified category.
     */
    List<Book> getBooksByCategory(String category);

    /**
     * Retrieve a book by its ISBN (International Standard Book Number).
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return The book with the specified ISBN.
     */
    Book getBookByISBN(String isbn);

    /**
     * Searches for books based on the provided search criteria, including keyword, price range, sorting options, and more.
     *
     * @param keyword   The keyword to search for in book titles, authors, or category.
     * @param minPrice  The minimum price of the books to include in the search (filtering with minPrice).
     * @param maxPrice  The maximum price of the books to include in the search (filtering with maxPrice).
     * @param sortBy    The field by which the search results should be sorted (e.g., "title", "author", "category").
     * @param sortOrder The sorting order for the results, either "asc" (ascending) or "desc" (descending).
     * @return A list of books that match the search criteria, sorted according to the specified sorting options.
     */
    List<Book> searchBooks(String keyword, Double minPrice, Double maxPrice, String sortBy, String sortOrder);

    /**
     * Add a new book to the marketplace.
     *
     * @param newBook The book to be added.
     * @return The added book.
     */
    Book addBook(Book newBook);

    /**
     * Update an existing book's information.
     *
     * @param newBook The updated book information.
     * @param id      The unique identifier of the book to update.
     * @return The updated book.
     */
    Book updateBook(Book newBook, Long id);

    /**
     * Delete a book from the marketplace.
     *
     * @param id The unique identifier of the book to delete.
     */
    void deleteBook(Long id);

    /**
     * Purchase a book by a user.
     *
     * @param userId The unique identifier of the user making the purchase.
     * @param bookId The unique identifier of the book to purchase.
     */
    void buyBook(Long userId, Long bookId);

    /**
     * Sell a book by a user.
     *
     * @param userId The unique identifier of the user selling the book.
     * @param bookId The unique identifier of the book to sell.
     */
    void sellBook(Long userId, Long bookId);

    /**
     * Sell a book by providing the user's unique identifier, the book's ISBN (International Standard Book Number),
     * and the book object to sell.
     *
     * @param userId The unique identifier of the user who is selling the book.
     * @param isbn   The ISBN of the book to be sold.
     * @param book   The book object representing the book to be sold.
     */
    void sellBookByISBN(Long userId, String isbn, Book book);
}
