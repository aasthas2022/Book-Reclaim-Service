/**
 * UserController.java
 * <p>
 * This class serves as the controller for managing user-related operations in the Book Marketplace application.
 * It handles incoming HTTP requests related to users, including retrieving, adding, updating, deleting users,
 * and other user-related operations. This controller interacts with the UserService to perform these actions.
 * <p>
 * Endpoints:
 * - GET /users/          : Retrieve all users.
 * - GET /users/getUserById?userId={id} : Retrieve a user by their ID.
 * - GET /users/searchUsers?keyword={searchTerm} : Search for a user by username or email.
 * - POST /users/addUser   : Add a new user.
 * - PUT /users/updateUser?userId={id} : Update an existing user.
 * - DELETE /users/deleteUser?userId={id} : Delete a user by their ID.
 * - GET /users/purchasedBooks?userId={id} : Retrieve books purchased by a user.
 * - GET /users/booksSoldByUser?userId={id} : Retrieve books sold by a user.
 * - PUT /users/addFunds?userId={id}&funds={amount} : Add funds to a user's account.
 * <p>
 * This class uses validation annotations to ensure the correctness of incoming data and
 * includes error handling for various exceptional cases. It logs events and errors using a Logger.
 */
package com.books.bookmarketplace.controller;

import com.books.bookmarketplace.entity.User;
import com.books.bookmarketplace.errorhandler.UserAlreadyExistsException;
import com.books.bookmarketplace.errorhandler.UserNotFoundException;
import com.books.bookmarketplace.errorhandler.ValidationException;
import com.books.bookmarketplace.model.BookDetails;
import com.books.bookmarketplace.model.UserDetails;
import com.books.bookmarketplace.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for managing user-related operations.
 * This class handles requests related to users,
 * including retrieving, adding, updating, deleting users,
 * and other user-related operations.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET Request to retrieve all users.
     *
     * @return A list of UserDetails representing all users.
     */
    @GetMapping("/")
    public ResponseEntity<List<UserDetails>> getAllUsers() {
        try {
            List<UserDetails> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET Request to retrieve a user by user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A UserDetails object representing the user.
     */
    @GetMapping("/getUserById")
    public ResponseEntity<?> getUserById(@Valid @RequestParam(name = "userId") @Positive Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
        }

        try {
            logger.log(Level.INFO, "Fetching user with ID: " + userId);
            UserDetails user = userService.getUserById(userId);
            logger.log(Level.INFO, "Retrieved user: " + user.getUsername());
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET Request to search for a user by username or email.
     *
     * @param keyword The username or email to search for.
     * @return A UserDetails object representing the user found.
     */
    @GetMapping("/searchUsers")
    public ResponseEntity<UserDetails> searchUsers(@Valid @RequestParam(name = "keyword") String keyword) {
        if (keyword.isBlank()) {
            throw new ValidationException(Collections.singletonList("Search term cannot be blank."));
        }
        try {
            logger.log(Level.INFO, "Fetching user with username/email: " + keyword);
            UserDetails user = userService.searchUsersByEMailOrUserName(keyword);
            logger.log(Level.INFO, "Retrieved " + user.getFirstName() + " " + user.getLastName());
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST Request to add a new user.
     *
     * @param user          The User object representing the new user to be added.
     * @param bindingResult A BindingResult object for validation errors.
     * @return A ResponseEntity containing the newly added User object.
     */
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            User newUser = userService.addUser(user);
            logger.log(Level.INFO, "Added new user: " + newUser.getUsername());
            return ResponseEntity.ok().body(newUser);
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding a new user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT Request to update a user.
     *
     * @param userId        The ID of the user to be updated.
     * @param user          The User object with updated information.
     * @param bindingResult A BindingResult object for validation errors.
     * @return A ResponseEntity containing the updated User object.
     */
    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestParam(name = "userId") @Positive Long userId, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            User updatedUser = userService.updateUser(user, userId);
            logger.log(Level.INFO, "Updated the user: " + updatedUser.getUsername());
            return ResponseEntity.ok().body(updatedUser);
        } catch (UserNotFoundException | UserAlreadyExistsException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE Request to delete a user by user ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return A ResponseEntity with a message indicating the deletion status.
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@Valid @RequestParam(name = "userId") Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "Deleting user with ID: " + userId);
            userService.deleteUser(userId);
            logger.log(Level.INFO, "User with ID " + userId + " deleted successfully.");
            return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET Request to retrieve books purchased by a user.
     *
     * @param userId The ID of the user for whom purchased books should be retrieved.
     * @return A list of BookDetails representing the books purchased by the user.
     */
    @GetMapping("/purchasedBooks")
    public ResponseEntity<List<BookDetails>> purchasedBooks(@Valid @RequestParam(name = "userId") Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
            }
            List<BookDetails> purchasedBooks = userService.getPurchasedBooksByUser(userId);

            if (purchasedBooks.isEmpty()) {
                logger.log(Level.INFO, "No books available");
                return ResponseEntity.noContent().build();
            } else {
                logger.log(Level.INFO, "Retrieved " + purchasedBooks.size() + " books purchased by user");
                return ResponseEntity.ok().body(purchasedBooks);
            }
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET Request to retrieve books sold by a user.
     *
     * @param userId The ID of the user for whom sold books should be retrieved.
     * @return A list of BookDetails representing the books sold by the user.
     */
    @GetMapping("/booksSoldByUser")
    public ResponseEntity<List<BookDetails>> booksSoldByUser(@Valid @RequestParam(name = "userId") Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
            }
            List<BookDetails> soldBooks = userService.getBooksSoldByUser(userId);

            if (soldBooks.isEmpty()) {
                logger.log(Level.INFO, "No books sold by the user");
                return ResponseEntity.noContent().build();
            } else {
                logger.log(Level.INFO, "Retrieved " + soldBooks.size() + " books sold by user");
                return ResponseEntity.ok().body(soldBooks);
            }
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT Request to add funds to a user's account.
     *
     * @param userId The ID of the user for whom funds should be added.
     * @param funds  The amount of funds to add to the user's account.
     * @return A ResponseEntity containing the updated User object with added funds.
     */
    @PutMapping("/addFunds")
    public ResponseEntity<User> addFundsToUser(@Valid @RequestParam(name = "userId") Long userId, @RequestParam(name = "funds") Double funds) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid user ID. Please provide a positive numeric value."));
        }
        if (funds == null || funds <= 0.0) {
            throw new ValidationException(Collections.singletonList("Invalid amount. Please provide a non-negative value."));
        }
        try {
            User updatedUser = userService.addFundsToUser(userId, funds);
            logger.log(Level.INFO, "Added funds to user: " + updatedUser.getUsername());
            return ResponseEntity.ok().body(updatedUser);
        } catch (UserNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}