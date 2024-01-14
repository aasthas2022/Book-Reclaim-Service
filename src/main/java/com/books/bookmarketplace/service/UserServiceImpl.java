/**
 * UserServiceImpl.java
 * <p>
 * This Java class serves as a service for managing users and user-related operations within the marketplace application.
 * Users play a pivotal role in the system, and this class provides a range of functionalities for handling user-related tasks such as user retrieval,
 * addition, update, and deletion of user profiles. It also facilitates the retrieval of user purchase and sales history and the addition of funds to user accounts.
 * <p>
 * The class is organized into the following key sections:
 * - Dependencies: Declarations and initialization of repository dependencies.
 * - Methods for Managing Users: Retrieving, adding, updating, and deleting user profiles.
 * - Methods for Retrieving User Transactions: Retrieving lists of purchased and sold books by a user.
 * - Adding Funds to User Accounts: Functionality to increase a user's account balance.
 * - Private Helper Methods: Conversion methods to transform entities to model representations.
 * <p>
 * This class's purpose is to ensure efficient management of users, their transactions, and account balances,
 * contributing to a seamless user experience and maintaining data consistency in the marketplace application.
 */

package com.books.bookmarketplace.service;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import com.books.bookmarketplace.entity.User;
import com.books.bookmarketplace.errorhandler.UserAlreadyExistsException;
import com.books.bookmarketplace.errorhandler.UserNotFoundException;
import com.books.bookmarketplace.model.BookDetails;
import com.books.bookmarketplace.model.TransactionDetails;
import com.books.bookmarketplace.model.UserDetails;
import com.books.bookmarketplace.repository.TransactionRepository;
import com.books.bookmarketplace.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing users and user-related operations.
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {

    // Define the UserRepository and TransactionRepository as dependencies for the UserServiceImpl class.
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor for UserServiceImpl that injects the UserRepository and TransactionRepository dependencies.
     *
     * @param userRepository        The UserRepository used for user-related data access.
     * @param transactionRepository The TransactionRepository used for transaction-related data access.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves a list of all users in the marketplace.
     *
     * @return List of user details for all users.
     */
    @Override
    public List<UserDetails> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToUserDetails).collect(Collectors.toList());
    }

    /**
     * Retrieves user details by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return User details for the user with the given ID.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public UserDetails getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for the given ID: " + userId));
        return convertToUserDetails(user);
    }

    /**
     * Searches for users by email or username based on a keyword.
     *
     * @param keyword The keyword to search for in user emails and usernames.
     * @return User details matching the search keyword.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public UserDetails searchUsersByEMailOrUserName(String keyword) {
        User user = userRepository.findUserByKeyword(keyword).orElseThrow(() -> new UserNotFoundException("User not found for the given email or username: " + keyword));
        return convertToUserDetails(user);
    }

    /**
     * Adds a new user to the marketplace.
     *
     * @param user The new user to add.
     * @return The added user.
     * @throws UserAlreadyExistsException if a user with the same email or username already exists.
     */
    @Override
    public User addUser(User user) {
        User existingUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (existingUser != null) {
            throw new UserAlreadyExistsException("A user with the same email or username already exists.");
        }
        return userRepository.save(user);
    }

    /**
     * Updates the information of an existing user.
     *
     * @param user The updated user information.
     * @param id   The ID of the user to update.
     * @return The updated user.
     * @throws UserNotFoundException      if the user does not exist.
     * @throws UserAlreadyExistsException if the user already exists with the same username or email id
     */
    @Override
    public User updateUser(User user, Long id) {

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            User userWithSameUserNameOrEmail = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
            if (userWithSameUserNameOrEmail != null && !userWithSameUserNameOrEmail.getUserId().equals(id)) {
                throw new UserAlreadyExistsException("An existing user with the same username or email already exists.");
            }
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setFunds(user.getFunds());
            existingUser.setUsername(user.getUsername());
            List<Transaction> updatedTransactions = new ArrayList<>();
            for (Transaction transaction : user.getTransactions()) {
                transaction.setUser(existingUser);
                updatedTransactions.add(transaction);
            }
            existingUser.setTransactions(updatedTransactions);
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found for the given id: " + id);
        }

    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to delete.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + id));
        userRepository.delete(user);
    }

    /**
     * Retrieves a list of books purchased by a user.
     *
     * @param id The ID of the user to retrieve purchased books for.
     * @return List of books purchased by the user.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public List<BookDetails> getPurchasedBooksByUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + id));
        List<Book> books = transactionRepository.findPurchasedBooksByUser(id);
        List<BookDetails> purchaseBooks = new ArrayList<>();
        for (Book book : books) {
            BookDetails bookDetails = convertBookToBookDetails(book);
            purchaseBooks.add(bookDetails);
        }
        return purchaseBooks;
    }

    /**
     * Retrieves a list of books sold by a user.
     *
     * @param id The ID of the user to retrieve books sold by.
     * @return List of books sold by the user.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public List<BookDetails> getBooksSoldByUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + id));

        List<Book> books = transactionRepository.findBooksSoldByUser(id);
        List<BookDetails> soldBooks = new ArrayList<>();
        for (Book book : books) {
            BookDetails bookDetails = convertBookToBookDetails(book);
            soldBooks.add(bookDetails);
        }
        return soldBooks;
    }


    /**
     * Adds funds to a user's account.
     *
     * @param userId The ID of the user to add funds to.
     * @param amount The amount to add to the user's funds.
     * @return The updated user with the added funds.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public User addFundsToUser(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found for the given id: " + userId));
        user.setFunds(user.getFunds() + amount);
        return userRepository.save(user);
    }

    /**
     * Converts a User entity to a UserDetails model.
     *
     * @param user The User entity to convert.
     * @return The UserDetails model representing the user.
     */
    private UserDetails convertToUserDetails(User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(user.getUserId());
        userDetails.setUsername(user.getUsername());
        userDetails.setEmail(user.getEmail());
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        userDetails.setPhoneNumber(user.getPhoneNumber());
        userDetails.setFunds(user.getFunds());

        List<TransactionDetails> transactionDetails = user.getTransactions().stream().map(this::convertToTransactionDetails).collect(Collectors.toList());
        userDetails.setTransactionDetails(transactionDetails);

        return userDetails;
    }

    /**
     * Converts a Transaction entity to a TransactionDetails model.
     *
     * @param transaction The Transaction entity to convert.
     * @return The TransactionDetails model representing the transaction.
     */
    private TransactionDetails convertToTransactionDetails(Transaction transaction) {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionId(transaction.getTransactionId());
        transactionDetails.setTransactionDate(transaction.getTransactionDate());
        transactionDetails.setTransactionAmount(transaction.getTransactionAmount());
        transactionDetails.setStatus(transaction.getStatus());
        transactionDetails.setTransactionNotes(transaction.getTransactionNotes());
        transactionDetails.setTransactionType(transaction.getTransactionType());

        return transactionDetails;
    }

    /**
     * Converts a Book object to a corresponding BookDetails object.
     *
     * @param book The Book object to be converted.
     * @return A BookDetails object representing the converted book information.
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