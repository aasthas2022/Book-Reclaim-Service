/**
 * BookController.java
 * <p>
 * This class acts as the controller for managing book-related operations within the Book Marketplace application.
 * It handles incoming HTTP requests related to books, including retrieving books by various criteria,
 * adding, updating, and deleting books, as well as buying and selling books. The controller interfaces with
 * the BookService to execute these operations.
 * <p>
 * Endpoints:
 * - GET /books/                  : Retrieve all books or books matching specific criteria.
 * - GET /books/getBookByISBN?isbn={isbn} : Retrieve a book by its ISBN.
 * - GET /books/getBookById?bookId={bookId} : Retrieve a book by its ID.
 * - GET /books/getBooksByCategory?category={category} : Retrieve books by category.
 * - GET /books/searchBooks?keyword={searchTerm} : Search for books based on a keyword.
 * - POST /books/addBook          : Add a new book to the marketplace.
 * - PUT /books/updateBook?bookId={bookId}    : Update an existing book.
 * - DELETE /books/deleteBook?bookId={bookId} : Delete a book by its ID.
 * - POST /books/buyBook          : Purchase a book by a user.
 * - POST /books/sellBook         : Sell a book by a user.
 * - POST /books/sellBookByISBN   : Sell a book by ISBN.
 * <p>
 * This class integrates validation using annotations to ensure the integrity of incoming data. It also
 * includes comprehensive error handling for various exceptional situations and logs events and errors using a Logger.
 */
package com.books.bookmarketplace.controller;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.errorhandler.*;
import com.books.bookmarketplace.service.BookService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for managing book-related operations.
 * This class handles requests related to books,
 * including retrieving, adding, updating, selling, buying, and deleting books.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieve all books.
     *
     * @return A list of all available books.
     */
    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            // Retrieve all books from the service
            List<Book> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                // If no books are found, return a no-content response
                return ResponseEntity.noContent().build();
            }
            // If books are found, return them in the response body
            return ResponseEntity.ok().body(books);
        } catch (Exception e) {
            // Handle any exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve available books.
     *
     * @return A list of available books.
     */
    @GetMapping("/getAvailableBooks")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        try {
            logger.log(Level.INFO, "Fetching all available books");
            // Retrieve available books from the service
            List<Book> books = bookService.getAvailableBooks();
            if (books.isEmpty()) {
                logger.log(Level.INFO, "No books available");
                // If no available books are found, return a no-content response
                return ResponseEntity.noContent().build();
            } else {
                logger.log(Level.INFO, "Retrieved " + books.size() + " available books");
                // If available books are found, return them in the response body
                return ResponseEntity.ok().body(books);
            }
        } catch (Exception e) {
            // Handle any exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve a book by its ID.
     *
     * @param bookId The unique identifier of the book.
     * @return The book matching the provided ID.
     */
    @GetMapping("/getBookById")
    public ResponseEntity<Book> getBookById(@Valid @RequestParam(name = "bookId") @Positive Long bookId) {
        if (bookId == null || bookId <= 0) {
            // Validate the input, and if it's invalid, throw a ValidationException
            throw new ValidationException(Collections.singletonList("Invalid book ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "Fetching book with ID: " + bookId);
            // Retrieve a book by its ID from the service
            Book book = bookService.getBookById(bookId);
            logger.log(Level.INFO, "Retrieved book: " + book.getTitle());
            // Return the retrieved book in the response body
            return ResponseEntity.ok().body(book);
        } catch (BookNotFoundException | ValidationException e) {
            // Handle specific exceptions by rethrowing them
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve books by category.
     *
     * @param category The category of books to retrieve.
     * @return A list of books in the specified category.
     */
    @GetMapping("/getBooksByCategory")
    public ResponseEntity<List<Book>> getBooksByCategory(@RequestParam(name = "category") String category) {
        if (category.isBlank()) {
            // Validate that the category is provided, and if it's empty, throw a ValidationException
            throw new ValidationException(Collections.singletonList("Category is required"));
        }
        List<String> validCategories = Arrays.stream(Book.Category.values())
                .map(Enum::name)
                .toList();
        if (!validCategories.contains(category.toUpperCase())) {
            // Validate that the provided category is valid, and if not, throw a ValidationException
            throw new ValidationException(Collections.singletonList("Invalid category: " + category.toUpperCase()));
        }
        try {
            logger.log(Level.INFO, "Fetching all books from category: " + category.toUpperCase());
            // Retrieve books from the specified category
            List<Book> books = bookService.getBooksByCategory(category.toUpperCase());
            if (books.isEmpty()) {
                logger.log(Level.INFO, "No books found in category: " + category.toUpperCase());
                // If no books are found, return a no-content response
                return ResponseEntity.noContent().build();
            } else {
                logger.log(Level.INFO, "Retrieved " + books.size() + " books in category: " + category.toUpperCase());
                // If books are found, return them in the response body
                return ResponseEntity.ok(books);
            }
        } catch (ValidationException e) {
            // Handle ValidationException by rethrowing it
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve a book by ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The book matching the provided ISBN.
     */
    @GetMapping("/getBookByISBN")
    public ResponseEntity<Book> getBookByISBN(@RequestParam(name = "isbn") String isbn) {
        if (isbn.isBlank()) {
            // Validate that ISBN is provided, and if it's empty, throw a ValidationException
            throw new ValidationException(Collections.singletonList("ISBN is required"));
        }
        try {
            logger.log(Level.INFO, "Fetching book with ISBN: " + isbn);
            // Retrieve a book by its ISBN
            Book book = bookService.getBookByISBN(isbn);
            logger.log(Level.INFO, "Retrieved book: " + book.getTitle());
            // Return the retrieved book in the response body
            return ResponseEntity.ok().body(book);
        } catch (BookNotFoundException | ValidationException e) {
            // Handle specific exceptions by rethrowing them
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Search for books based on a keyword and optional filters.
     *
     * @param keyword   The keyword to search for in book titles and descriptions.
     * @param sortBy    (Optional) The field to sort the results by (e.g., "title" or "author").
     * @param sortOrder (Optional) The sorting order ("asc" for ascending, "desc" for descending).
     * @param minPrice  (Optional) The minimum price filter.
     * @param maxPrice  (Optional) The maximum price filter.
     * @return A list of books matching the search criteria.
     */
    @GetMapping("/searchBooks")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice
    ) {
        if (keyword.isBlank()) {
            throw new ValidationException(Collections.singletonList("Search term cannot be blank."));
        }
        try {
            logger.log(Level.INFO, "Fetching all books with keyword: " + keyword);
            List<Book> books = bookService.searchBooks(keyword, minPrice, maxPrice, sortBy, sortOrder);
            if (books.isEmpty()) {
                logger.log(Level.INFO, "No books found with keyword: " + keyword);
                return ResponseEntity.noContent().build();
            } else {
                logger.log(Level.INFO, "Retrieved " + books.size() + " books with keyword: " + keyword);
                return ResponseEntity.ok(books);
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add a new book to the marketplace.
     *
     * @param book          The book to be added.
     * @param bindingResult The result of validation.
     * @return The newly added book.
     */
    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Validate the input, and if there are binding errors, throw a ValidationException
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            // Add a new book and return the saved book in the response
            Book savedBook = bookService.addBook(book);
            logger.log(Level.INFO, "Added new book: " + savedBook.getTitle());
            return ResponseEntity.ok().body(savedBook);
        } catch (InventoryFullException | BookAlreadyExistsException | HttpMessageNotReadableException e) {
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            logger.log(Level.SEVERE, "Error adding a new book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing book by its ID.
     *
     * @param bookId        The unique identifier of the book to update.
     * @param book          The updated book details.
     * @param bindingResult The result of validation.
     * @return The updated book.
     */
    @PutMapping("/updateBook")
    public ResponseEntity<Book> updateBook(@RequestParam(name = "bookId") @Positive Long bookId, @Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bookId == null || bookId <= 0) {
            // Validate the book ID, and if it's invalid, throw a ValidationException
            throw new ValidationException(Collections.singletonList("Invalid book ID. Please provide a positive numeric value."));
        }
        if (bindingResult.hasErrors()) {
            // Validate the input, and if there are binding errors, throw a ValidationException
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            // Update the book and return the updated book in the response
            Book updatedBook = bookService.updateBook(book, bookId);
            logger.log(Level.INFO, "Updated the book: " + updatedBook.getTitle());
            return ResponseEntity.ok().body(updatedBook);
        } catch (BookNotFoundException | BookAlreadyExistsException | HttpMessageNotReadableException |
                 ValidationException e) {
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a book by its ID.
     *
     * @param bookId The unique identifier of the book to delete.
     * @return A message indicating the success of the deletion.
     */
    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(@RequestParam(name = "bookId") Long bookId) {
        if (bookId == null || bookId <= 0) {
            // Validate the book ID, and if it's invalid, throw a ValidationException
            throw new ValidationException(Collections.singletonList("Invalid book ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "Deleting book with ID: " + bookId);
            // Delete the book by ID
            bookService.deleteBook(bookId);
            logger.log(Level.INFO, "Book with ID " + bookId + " deleted successfully.");
            return ResponseEntity.ok("Book with ID " + bookId + " deleted successfully.");
        } catch (BookNotFoundException | ValidationException e) {
            // Handle specific exceptions by rethrowing them
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions by returning an internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Purchase a book by user and book ID.
     *
     * @param userId The unique identifier of the user purchasing the book.
     * @param bookId The unique identifier of the book to purchase.
     * @return A message indicating the success of the purchase.
     */
    @PostMapping("/buyBook")
    public ResponseEntity<String> buyBookById(@RequestParam(name = "userId") Long userId, @RequestParam(name = "bookId") Long bookId) {
        if (bookId == null || bookId <= 0 || userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid book ID or user ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "User with ID " + userId + " is attempting to buy book with ID " + bookId);
            bookService.buyBook(userId, bookId);
            logger.log(Level.INFO, "User with ID " + userId + " purchased book with ID " + bookId + " successfully.");
            return ResponseEntity.ok("Book purchased successfully.");
        } catch (BookNotFoundException | UserNotFoundException | ValidationException e) {
            logger.log(Level.WARNING, "User with ID " + userId + " failed to purchase the book: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    /**
     * Sell a book by user and book ID.
     *
     * @param userId The unique identifier of the user selling the book.
     * @param bookId The unique identifier of the book to sell.
     * @return A message indicating the success of the sale.
     */
    @PostMapping("/sellBook")
    public ResponseEntity<String> sellBookById(@RequestParam(name = "userId") Long userId, @RequestParam(name = "bookId") Long bookId) {
        if (bookId == null || bookId <= 0 || userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid book ID or user ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "User with ID " + userId + " is attempting to sell book with ID " + bookId);
            bookService.sellBook(userId, bookId);
            logger.log(Level.INFO, "User with ID " + userId + " sold book with ID " + bookId + " successfully.");
            return ResponseEntity.ok("Book sold successfully.");
        } catch (BookNotFoundException | UserNotFoundException | ValidationException e) {
            logger.log(Level.WARNING, "User with ID " + userId + " failed to purchase the book: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }


    /**
     * Sell a book by ISBN (International Standard Book Number).
     *
     * @param userId The unique identifier of the user selling the book.
     * @param isbn   The ISBN of the book to sell.
     * @param book   The book details if the book is new to our service
     */
    @PostMapping("/sellBookByISBN")
    public ResponseEntity<String> sellBookByISBN(
            @RequestParam(name = "userId", required = true) Long userId,
            @RequestParam(name = "isbn", required = true) String isbn,
            @RequestBody @Nullable Book book) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
        }
        if (isbn.isBlank()) {
            throw new ValidationException(Collections.singletonList("ISBN is required. Cannot be blank"));
        }
        try {
            logger.log(Level.INFO, "User with ID " + userId + " is attempting to sell book with ISBN " + isbn);
            bookService.sellBookByISBN(userId, isbn, book);
            logger.log(Level.INFO, "User with ID " + userId + " sold book with ISBN " + isbn + " successfully.");
            return ResponseEntity.ok("Book sold successfully.");
        } catch (BookNotFoundException | UserNotFoundException | ValidationException e) {
            logger.log(Level.WARNING, "User with ID " + userId + " failed to sell the book: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}