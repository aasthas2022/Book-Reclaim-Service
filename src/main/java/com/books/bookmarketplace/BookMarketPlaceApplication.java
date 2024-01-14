/**
 * BookMarketPlaceApplication.java
 * 
 * Main entry point of the Book Marketplace Spring Boot application.
 * The application serves as a platform for buying, selling, and managing textbooks
 * with a fixed pool of books that depreciate in price after each transaction.
 */

package com.books.bookmarketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class BookMarketPlaceApplication {

 /**
  * The main method that starts the Spring Boot application.
  *
  * @param args Command-line arguments (if any).
  */
	public static void main(String[] args) {
		SpringApplication.run(BookMarketPlaceApplication.class, args);
	}

}
