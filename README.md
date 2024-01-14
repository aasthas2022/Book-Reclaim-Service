
# Book Marketplace Application

![Java](https://img.shields.io/badge/Java-11-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![Database](https://img.shields.io/badge/MySQL-lightgrey?logo=mysql&style=plastic&logoColor=white&labelColor=blue)

# Table of Contents
1. [Database Setup](#database-setup)
2. [Project Setup](#project-setup)
3. [Book Marketplace Application](#book-marketplace-application)
   - [Overview](#overview)
   - [Features](#features)
   - [Endpoints](#endpoints)
      - [User Management](#user-management)
      - [Book Management](#book-management)
4. [Technologies Used](#technologies-used)
5. [Project Structure](#project-structure)
6. [Custom Exceptions](#custom-exceptions)
7. [Exception Handling](#exception-handling)
8. [API Documentation](#api-documentation)

## Database Setup

The application uses MySQL as its database system. To set up the database, follow these steps:

1. **Install MySQL**: Install MySQL on your system if you haven't already.

2. **Database Configuration**: Open `application.properties` in the `resources` directory and configure your MySQL database connection properties, such as `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password`.

3. **Schema Creation**: The application automatically creates the necessary schema and tables on startup, thanks to Spring Data JPA. You can also manually create the schema using SQL scripts (script.sql) in the `resources` directory if needed.

4. **Database Initialization**: You can initialize the database with sample data or specific setup scripts, if required.

## Project Setup

To set up the project and run it locally, follow these steps:

1. **Clone the Repository**: Clone the project repository to your local machine.

2. **Import into IDE**: Import the project into your preferred Java IDE (e.g., IntelliJ IDEA or Eclipse).

3. **Build and Run**: Build the project and run it as a Spring Boot application.

4. **Access the API**: The API will be accessible at the defined port (usually 8080) by default. You can explore and test the endpoints using Swagger or Postman, as mentioned in the API Documentation section.

## Overview

The Book Marketplace Application is a web-based platform that enables users to buy and sell books for students. It offers a range of features for managing user accounts, listing books for sale, purchasing books, and handling various book-related operations. This README provides an in-depth overview of the application's capabilities, components, and usage.

## Features

The Book Marketplace Application offers the following key features:

Fixed size pool of books in the inventory, to be sold, bought back and resold to students. Price of book depreciates by 10% at each transaction.

- User Management:
  - Retrieve all users.
  - Retrieve a user by their ID.
  - Search for a user by username or email.
  - Add a new user.
  - Update an existing user.
  - Delete a user by their ID.
  - Retrieve books purchased by a user.
  - Retrieve books sold by a user.
  - Add funds to a user's account.

- Book Management:
  - Retrieve all books or books matching specific criteria.
  - Retrieve a book by its ISBN.
  - Search for books based on a keyword.
  - Add a new book to the marketplace.
  - Update an existing book.
  - Delete a book by its ID.
  - Purchase a book by a user.
  - Sell a book by a user.
  - Sell a book by ISBN.

## Endpoints

### User Management

- **Retrieve All Users**: GET `/users/` - Retrieve a list of all users.
- **Retrieve User by ID**: GET `/users/getUserById?userId={id}` - Retrieve a user by their unique ID.
- **Search Users**: GET `/users/searchUsers?keyword={searchTerm}` - Search for a user by username or email.
- **Add User**: POST `/users/addUser` - Register a new user.
- **Update User**: PUT `/users/updateUser?userId={id}` - Update an existing user.
- **Delete User**: DELETE `/users/deleteUser?userId={id}` - Delete a user by their ID.
- **Retrieve Purchased Books**: GET `/users/purchasedBooks?userId={id}` - Retrieve books purchased by a user.
- **Retrieve Books Sold by User**: GET `/users/booksSoldByUser?userId={id}` - Retrieve books sold by a user.
- **Add Funds to User's Account**: PUT `/users/addFunds?userId={id}&funds={amount}` - Add funds to a user's account.

### Book Management

- **Retrieve All Books**: GET `/books/` : Retrieve all books or books matching specific criteria.
- **Retrieve Book by ISBN**: GET `/books/getBookByISBN?isbn={isbn}` : Retrieve a book by its ISBN.
- **Retrieve Book by Book ID**: GET `/books/getBookById?bookId={bookId}` : Retrieve a book by its ID.
- **Retrieve Book by Category**: GET `/books/getBooksByCategory?category={category}` : Retrieve books by category.
- **Search Books by title, author or category**: GET `/books/searchBooks?keyword={searchTerm}` : Search for books based on a keyword.
- **Add Book**: POST `/books/addBook`          : Add a new book to the marketplace.
- **Update Book**: PUT `/books/updateBook?bookId={bookId}`    : Update an existing book.
- **Delete Book**: DELETE `/books/deleteBook?bookId={bookId}` : Delete a book by its ID.
- **Buy Book**: POST `/books/buyBook`         : Purchase a book by a user.
- **Sell Book**: POST `/books/sellBook`         : Sell back a book owned by a user.
- **Sell Book by ISBN**: POST `/books/sellBookByISBN`   : Sell a book by ISBN if exists or add a new book to the inventory for sale.

## Technologies Used

- **Java**: The primary programming language.
- **Spring Boot**: The framework for building Java applications.
- **Spring Data JPA**: For simplified data access and database interaction.
- **Spring MVC**: For building web applications and RESTful APIs.
- **Lombok**: To reduce boilerplate code.
- **Restful API**
- **MySQL**

## Project Structure

The application is organized as follows:

- `com.books.bookmarketplace.controller`: Contains controller classes for handling HTTP requests and defining API endpoints.
- `com.books.bookmarketplace.entity`: Defines JPA entity classes for users, books, and transactions.
- `com.books.bookmarketplace.model`: Defines model classes for simplified data representation.
- `com.books.bookmarketplace.repository`: Provides repository interfaces for database operations.
- `com.books.bookmarketplace.service`: Contains service classes for handling the logic behind the API calls.
- `com.books.bookmarketplace.errorhandler`: Contains custom exception classes and global exception handling.
- `resources`: Configuration files, including application properties and database setup.

## Custom Exceptions

The application includes custom exception classes with specific purposes:

- `UserNotFoundException`: Thrown when a user is not found.
- `UserAlreadyExistsException`: Thrown when a user already exists.
- `BookNotFoundException`: Thrown when a book is not found.
- `BookAlreadyExistsException`: Thrown when a book already exists.
- `InventoryFullException`: Thrown when the inventory is full.
- `ValidationException`: Thrown for validation errors.

## Exception Handling

The `GlobalExceptionHandler` class handles all custom exceptions and other exceptions to provides meaningful error responses. It maps different exceptions to appropriate HTTP status codes and custom error messages.

## API Documentation

The API is documented using Swagger and Postman, which provides interactive documentation for all endpoints in swagger.yml file and BookApplication.postman_collection.json.