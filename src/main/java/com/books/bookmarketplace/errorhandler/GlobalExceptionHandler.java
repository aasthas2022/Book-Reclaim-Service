package com.books.bookmarketplace.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler class that centralizes the handling of various exceptions that may occur during the
 * execution of Spring Boot controllers. It provides consistent responses for different types of exceptions.
 * <p>
 * This class serves as a central hub for capturing and managing exceptions thrown by controller methods within
 * a Spring Boot application. By using the `@ControllerAdvice` annotation, it defines a global exception handling
 * strategy for the entire application. Exception handling methods within this class handle specific exception types
 * and generate consistent error responses, including error messages and appropriate HTTP status codes.
 * <p>
 * The primary goals of this class are to:
 * - Ensure that error responses to clients are uniform and follow a standardized format.
 * - Simplify the process of handling various types of exceptions that can occur within the application.
 * - Promote best practices for error handling, making it easier for clients to understand and respond to errors.
 * - Centralize exception handling logic, which enhances code maintainability and organization.
 * <p>
 * Exception handling methods within this class are annotated with `@ExceptionHandler` for specific exception types.
 * When an exception to a particular type is thrown within a controller method, the corresponding handler method is
 * invoked to manage the exception and provide a meaningful response to the client.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle exceptions related to UserNotFoundException.
     *
     * @param ex The UserNotFoundException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        String error = ex.getError();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "User not found");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    /**
     * Handle exceptions related to UserAlreadyExistsException.
     *
     * @param ex The UserAlreadyExistsException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String error = ex.getError();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "User already exists");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    /**
     * Handle exceptions related to BookNotFoundException.
     *
     * @param ex The BookNotFoundException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        String error = ex.getError();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Book not found");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    /**
     * Handle exceptions related to BookAlreadyExistsException.
     *
     * @param ex The BookAlreadyExistsException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<Object> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        String error = ex.getError();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Book already exists");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    /**
     * Handle exceptions related to InventoryFullException.
     *
     * @param ex The InventoryFullException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(InventoryFullException.class)
    public ResponseEntity<Object> handleInventoryFullException(InventoryFullException ex) {
        String error = ex.getError();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Inventory Full");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    /**
     * Handle exceptions related to ValidationException.
     *
     * @param ex The ValidationException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        List<String> errors = ex.getErrors();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Validation errors occurred");
        responseBody.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    /**
     * Handle exceptions related to HttpMessageNotReadableException.
     *
     * @param ex The HttpMessageNotReadableException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String error = "Invalid enum category. Provide valid enum value which are defined";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Enum validation");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    /**
     * Handle exceptions related to MethodArgumentTypeMismatchException.
     *
     * @param ex The MethodArgumentTypeMismatchException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleBadRequestException(MethodArgumentTypeMismatchException ex) {
        String error = ex.getMessage();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Bad Request");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    /**
     * Handle exceptions related to MissingServletRequestParameterException.
     *
     * @param ex The MissingServletRequestParameterException to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParametersException(MissingServletRequestParameterException ex) {
        String error = ex.getMessage();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Bad Request");
        responseBody.put("errors", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}