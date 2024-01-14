/**
 * BookServiceImpl.java:
 * <p>
 * Defines a service for managing books and book-related operations in a marketplace application. It includes methods for retrieving, adding, updating, buying, and selling books. It also handles various exceptions related to book operations and maintains the integrity of the marketplace inventory. This class serves as the implementation of the BookService interface.
 * <p>
 * The class structure includes the following sections:
 * - Dependencies: Declarations and initialization of repositories and constants.
 * - Methods for Managing Books: Retrieval, addition, and update of books.
 * - Methods for Buying and Selling Books: Purchase and sale of books, including by ISBN.
 * - Helper Methods: Private methods for creating transaction records.
 * <p>
 * This class is designed to ensure the smooth operation of a book marketplace by providing essential book-related services and maintaining data consistency.
 */

package com.books.bookmarketplace.service;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import com.books.bookmarketplace.entity.User;
import com.books.bookmarketplace.errorhandler.*;
import com.books.bookmarketplace.model.BookDetails;
import com.books.bookmarketplace.repository.BookRepository;
import com.books.bookmarketplace.repository.TransactionRepository;
import com.books.bookmarketplace.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing books and book-related operations.
 */
@Transactional
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final int MAX_INVENTORY_SIZE = 100; //Defining the fixed pool size of books

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository, TransactionRepository transactionRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    /**
     * Retrieves a list of all books in the marketplace.
     *
     * @return List of all books.
     */
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a list of available books in the marketplace.
     *
     * @return List of available books.
     */
    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks(Book.Status.AVAILABLE);
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param bookId The ID of the book to retrieve.
     * @return The book with the given ID.
     * @throws BookNotFoundException if the book does not exist.
     */
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for the given ID: " + bookId));
    }

    /**
     * Retrieves books by a specified category.
     *
     * @param category The category of books to retrieve.
     * @return List of books in the specified category.
     */
    @Override
    public List<Book> getBooksByCategory(String category) {
        Book.Category bookCategory = Book.Category.valueOf(category);
        return bookRepository.findBooksByCategory(bookCategory);
    }

    /**
     * Retrieves a book by its ISBN (International Standard Book Number).
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return The book with the given ISBN.
     * @throws BookNotFoundException if the book does not exist.
     */
    @Override
    public Book getBookByISBN(String isbn) {
        return Optional.ofNullable(bookRepository.findBookByISBN(isbn))
                .orElseThrow(() -> new BookNotFoundException("Book not found for the given ISBN: " + isbn));
    }

    /**
     * Searches for books based on a keyword.
     *
     * @param keyword   The keyword to search for in book titles, author or category
     * @param minPrice  The minimum price for filtering (can be null).
     * @param maxPrice  The maximum price for filtering (can be null).
     * @param sortBy    The field to sort the results by ("title", "author", "category").
     * @param sortOrder The sorting order ("asc" or "desc").
     * @return List of books matching the search criteria.
     */
    @Override
    public List<Book> searchBooks(String keyword, Double minPrice, Double maxPrice, String sortBy, String sortOrder) {
        List<Book> books = bookRepository.findBooksByKeyword(keyword);

        if (minPrice != null || maxPrice != null) {
            books = books.stream()
                    .filter(book -> (minPrice == null || book.getCurrentPrice() >= minPrice) &&
                            (maxPrice == null || book.getCurrentPrice() <= maxPrice))
                    .collect(Collectors.toList());
        }

        Comparator<Book> bookComparator;

        if (sortBy.equalsIgnoreCase("title")) {
            bookComparator = Comparator.comparing(Book::getTitle);
        } else if (sortBy.equalsIgnoreCase("author")) {
            bookComparator = Comparator.comparing(Book::getAuthor);
        } else if (sortBy.equalsIgnoreCase("category")) {
            bookComparator = Comparator.comparing(Book::getCategory);
        } else {
            bookComparator = Comparator.comparing(Book::getTitle);
        }

        if (sortOrder.equalsIgnoreCase("desc")) {
            bookComparator = bookComparator.reversed();
        }

        books.sort(bookComparator);

        return books;
    }

    /**
     * Adds a new book to the marketplace.
     *
     * @param newBook The new book to add.
     * @return The added book.
     * @throws InventoryFullException     if the inventory is full.
     * @throws BookAlreadyExistsException if a book with the same ISBN already exists.
     */
    @Override
    public Book addBook(Book newBook) {
        if (bookRepository.count() > MAX_INVENTORY_SIZE) {
            throw new InventoryFullException("The inventory is full. No more new books can be added.");
        }

        // Check if a book with the same ISBN already exists
        Book existingBook = bookRepository.findBookByISBN(newBook.getISBN());

        if (existingBook != null) {
            // Check if the title and author also match
            if (existingBook.getTitle().equals(newBook.getTitle()) && existingBook.getAuthor().equals(newBook.getAuthor())) {
                // If the book already exists, increment its quantity and set status to AVAILABLE
                existingBook.setQuantity(existingBook.getQuantity() + 1);
                existingBook.setStatus(Book.Status.AVAILABLE);
                return bookRepository.save(existingBook);
            } else {
                throw new BookAlreadyExistsException("A book with the same ISBN but different title and author already exists.");
            }
        }

        // If no matching book is found, set quantity to 1 and status to AVAILABLE
        newBook.setQuantity(1);
        newBook.setStatus(Book.Status.AVAILABLE);
        return bookRepository.save(newBook);
    }

    /**
     * Updates the information of an existing book.
     *
     * @param book The updated book information.
     * @param id   The ID of the book to update.
     * @return The updated book.
     * @throws BookNotFoundException      if the book does not exist.
     * @throws BookAlreadyExistsException if the book already exists in the inventory
     */
    @Override
    public Book updateBook(Book book, Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found for the given ID: " + id));

        // Check if the updated ISBN is not the same as an existing book's ISBN
        Book bookWithSameISBN = bookRepository.findBookByISBN(book.getISBN());
        if (bookWithSameISBN != null && !bookWithSameISBN.getBookId().equals(id)) {
            throw new BookAlreadyExistsException("An existing book with the same ISBN already exists.");
        }

        existingBook.setISBN(book.getISBN());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setCategory(book.getCategory());
        existingBook.setCondition(book.getCondition());
        existingBook.setDescription(book.getDescription());
        existingBook.setEdition(book.getEdition());
        existingBook.setConditionDescription(book.getConditionDescription());
        existingBook.setCurrentPrice(book.getCurrentPrice());
        existingBook.setOriginalPrice(book.getOriginalPrice());
        existingBook.setTitle(book.getTitle());
        List<Transaction> updatedTransactions = new ArrayList<>();
        for (Transaction transaction : book.getTransactions()) {
            transaction.setBook(existingBook);
            updatedTransactions.add(transaction);
        }
        existingBook.setTransactions(updatedTransactions);
        existingBook.setStatus(book.getStatus());
        return bookRepository.save(existingBook);
    }


    /**
     * Deletes a book from the marketplace. If the book has a quantity greater than 1, the quantity is decremented by 1.
     * If the book has a quantity of 1, it is removed from the marketplace.
     *
     * @param id The ID of the book to delete.
     * @throws BookNotFoundException if the book does not exist.
     */
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found for the given id: " + id));

        if (book.getQuantity() > 1) {
            // If the book has a quantity greater than 1, decrement the count and keep it AVAILABLE
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
        } else {
            // If the book has a quantity of 1, delete it from the inventory
            bookRepository.delete(book);
        }
    }

    /**
     * Buys a book for a user, updating the user's funds, depreciates book price by 10% and book's availability.
     *
     * @param userId The ID of the user buying the book.
     * @param bookId The ID of the book to buy.
     * @throws UserNotFoundException if the user does not exist.
     * @throws BookNotFoundException if the book does not exist.
     * @throws ValidationException   if there are issues with the purchase.
     */
    public void buyBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + userId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found for the given id: " + bookId));

        if (book.getQuantity() < 1 || !book.getStatus().equals(Book.Status.AVAILABLE)) {
            throw new ValidationException(Collections.singletonList("Book is not available for purchase."));
        }

        if (user.getFunds() < book.getCurrentPrice()) {
            throw new ValidationException(Collections.singletonList("Insufficient funds to purchase the book."));
        }

        Double buyingPrice = book.getCurrentPrice();
        user.setFunds(user.getFunds() - buyingPrice);
        book.depreciatePrice();

        if (book.getQuantity() == 1) {
            // If it's the last copy of the book, set the status to SOLD
            book.setStatus(Book.Status.SOLD);
            book.setQuantity(0);
        } else {
            // If there are more copies, decrement the quantity by 1
            book.setQuantity(book.getQuantity() - 1);
        }

        if (book.getCurrentPrice() <= 0 || book.getQuantity() <= 0) {
            book.setStatus(Book.Status.DISCONTINUED);
        }

        Transaction transaction = createTransaction(user, book, buyingPrice, Transaction.TransactionType.BUY);
        transactionRepository.save(transaction);
        userRepository.save(user);
        bookRepository.save(book);

    }

    /**
     * Sells back a book that the user owns, updating the user's funds, depreciates book price by 10% and book's availability.
     *
     * @param userId The ID of the user selling the book.
     * @param bookId The ID of the book to sell.
     * @throws UserNotFoundException if the user does not exist.
     * @throws BookNotFoundException if the book does not exist.
     * @throws ValidationException   if there are issues with the sale.
     */

    public void sellBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + userId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found for the given id: " + bookId));

        if (!userService.getPurchasedBooksByUser(userId).contains(convertBookToBookDetails(book))) {
            throw new ValidationException(Collections.singletonList("Seller does not own the book with the given ID."));
        }

        double sellingPrice = book.getCurrentPrice();
        user.setFunds(user.getFunds() + sellingPrice);
        book.setQuantity(book.getQuantity() + 1);
        double depreciationFactor = 0.90; // 10% depreciation
        double currentPrice = book.getCurrentPrice() * depreciationFactor;
        book.setCurrentPrice(currentPrice);
        book.setStatus(Book.Status.AVAILABLE);

        Transaction transaction = createTransaction(user, book, sellingPrice, Transaction.TransactionType.SELL);
        transactionRepository.save(transaction);

        userRepository.save(user);
        bookRepository.save(book);
    }

    /**
     * Sells a book by ISBN for a user if the isbn already exists in the inventory
     * Or else adding a new book if the ISBN does not exist for sale.
     *
     * @param userId          The ID of the user selling the book.
     * @param isbn            The ISBN of the book to sell.
     * @param sellBookDetails Details of the book being sold, if it's a new book.
     * @throws UserNotFoundException if the user does not exist.
     * @throws BookNotFoundException if the book does not exist.
     * @throws ValidationException   if there are issues with the sale.
     */
    @Override
    public void sellBookByISBN(Long userId, String isbn, Book sellBookDetails) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + userId));
        Book existingBook = bookRepository.findBookByISBN(isbn);

        if (existingBook != null) {
            if (!existingBook.getStatus().equals(Book.Status.AVAILABLE)) {
                throw new ValidationException(Collections.singletonList("Book is not available for purchase."));
            }

            double sellingPrice = existingBook.getCurrentPrice();
            user.setFunds(user.getFunds() + sellingPrice);
            existingBook.setQuantity(existingBook.getQuantity() + 1);
            existingBook.depreciatePrice(); //depreciates book price by 10%
            existingBook.setStatus(Book.Status.AVAILABLE);

            Transaction transaction = createTransaction(user, existingBook, sellingPrice, Transaction.TransactionType.SELL);
            transactionRepository.save(transaction);

            userRepository.save(user);
            bookRepository.save(existingBook);
        } else {
            // Book with the given ISBN doesn't exist, add a new book
            if (sellBookDetails != null) {
                Book newBook = getBook(isbn, sellBookDetails);
                Transaction transaction = createTransaction(user, newBook, newBook.getOriginalPrice(), Transaction.TransactionType.SELL);
                transactionRepository.save(transaction);

                userRepository.save(user);
                bookRepository.save(newBook);
            } else {
                throw new ValidationException(Collections.singletonList("New book details are required for a new book."));
            }
        }
    }

    /**
     * Creates a new Book object using the provided ISBN and book details, initializing its attributes.
     *
     * @param isbn            The International Standard Book Number (ISBN) of the book.
     * @param sellBookDetails Details of the book being sold, including title, author, and other attributes.
     * @return A new Book object with attributes initialized from the provided details.
     */
    private static Book getBook(String isbn, Book sellBookDetails) {
        Book newBook = new Book();
        newBook.setISBN(isbn);
        newBook.setStatus(Book.Status.AVAILABLE);
        newBook.setQuantity(1);
        newBook.setCurrentPrice(sellBookDetails.getCurrentPrice()); // Set the current price
        newBook.setTitle(sellBookDetails.getTitle());
        newBook.setAuthor(sellBookDetails.getAuthor());
        newBook.setLanguage(sellBookDetails.getLanguage());
        newBook.setOriginalPrice(sellBookDetails.getOriginalPrice());
        newBook.setEdition(sellBookDetails.getEdition());
        newBook.setPublicationYear(sellBookDetails.getPublicationYear());
        newBook.setPublisher(sellBookDetails.getPublisher());
        newBook.setDescription(sellBookDetails.getDescription());
        newBook.setCategory(sellBookDetails.getCategory());
        newBook.setConditionDescription(sellBookDetails.getConditionDescription());
        newBook.setCondition(sellBookDetails.getCondition());
        return newBook;
    }

    /**
     * Creates a transaction record for a book-related action.
     *
     * @param user              The user involved in the transaction.
     * @param book              The book involved in the transaction.
     * @param transactionAmount The amount involved in the transaction.
     * @param transactionType   The type of transaction (BUY or SELL).
     * @return The created transaction record.
     */
    private Transaction createTransaction(User user, Book book, double transactionAmount, Transaction.TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setUser(user);
        transaction.setBook(book);
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        transaction.setTransactionDate(date);
        transaction.setTransactionAmount(transactionAmount);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        return transaction;
    }

    /**
     * Converts a Book entity to a BookDetails model for user display.
     *
     * @param book The Book entity to convert.
     * @return The BookDetails model containing book information.
     */
    public BookDetails convertBookToBookDetails(Book book) {
        BookDetails bookDetails = new BookDetails();
        bookDetails.setBookId(book.getBookId());
        bookDetails.setCategory(String.valueOf(book.getCategory()));
        bookDetails.setDescription(book.getDescription());
        bookDetails.setEdition(book.getEdition());
        bookDetails.setAuthor(book.getAuthor());
        bookDetails.setISBN(book.getISBN());
        bookDetails.setLanguage(book.getLanguage());
        bookDetails.setStatus(String.valueOf(book.getStatus()));
        bookDetails.setCurrentPrice(book.getCurrentPrice());
        bookDetails.setQuantity(book.getQuantity());
        bookDetails.setPublisher(book.getPublisher());
        bookDetails.setOriginalPrice(book.getOriginalPrice());
        bookDetails.setTitle(book.getTitle());
        bookDetails.setAuthor(book.getAuthor());
        bookDetails.setPublicationYear(book.getPublicationYear());
        bookDetails.setCondition(String.valueOf(book.getCondition()));

        return bookDetails;
    }
}