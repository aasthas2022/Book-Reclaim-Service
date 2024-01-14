-- CREATE statement to create BookMarketPlace database/schema

CREATE DATABASE `bookmarketplace`;

-- Sample INSERT statements for Books table

INSERT INTO Books (ISBN, title, author, edition, publicationYear, language, quantity, publisher, originalPrice, currentPrice, description, category, conditionDescription, book_condition, book_status)
VALUES ('978-0-13-516630-7', 'Introduction to Java Programming', 'Daniel Liang', 11, 2023, 'English', 50, 'Pearson', 50.99, 50.99, 'Comprehensive guide to Java programming.', 'TECHNOLOGY', 'Brand new', 'NEW', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-1-59327-584-6', 'Python Crash Course', 'Eric Matthes', 2, 2022, 'English', 30, 'No Starch Press', 35.50, 35.50, 'Learn Python programming from scratch.', 'TECHNOLOGY', 'Slightly used', 'LIKE_NEW', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-316-31452-2', 'Educated: A Memoir', 'Tara Westover', 1, 2018, 'English', 25, 'Random House', 28.75, 28.75, 'Memoir about a woman who grows up in a strict and abusive household but eventually escapes to learn about the world.', 'BIOGRAPHY', 'Good condition', 'GOOD', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-525-57326-9', 'Dune', 'Frank Herbert', 1, 1965, 'English', 15, 'Chilton Books', 20.00, 20.00, 'Science fiction novel set in the distant future amidst a feudal interstellar society.', 'SCIENCE', 'Slightly worn cover', 'GOOD', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-1-101-14678-2', 'To Kill a Mockingbird', 'Harper Lee', 1, 1960, 'English', 20, 'HarperCollins', 22.50, 22.50, 'Classic novel depicting racial injustice in the American South.', 'FICTION', 'Very good condition', 'EXCELLENT', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-345-81602-3', '1984', 'George Orwell', 1, 1949, 'English', 12, 'Harcourt Brace and Company', 18.75, 18.75, 'Dystopian novel exploring themes of totalitarianism and surveillance.', 'FICTION', 'Aged pages', 'ACCEPTABLE', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-307-26666-7', 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 1, 2014, 'English', 18, 'Harper', 24.99, 24.99, 'Explores the history and impact of Homo sapiens on the world.', 'HISTORY', 'Like new', 'LIKE_NEW', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-1-59030-481-0', 'The Alchemist', 'Paulo Coelho', 25, 1993, 'English', 22, 'HarperOne', 19.95, 19.95, 'Philosophical novel about a young Andalusian shepherd in his journey to find a hidden treasure.', 'FICTION', 'Cover slightly torn', 'ACCEPTABLE', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-7432-3569-2', 'The Da Vinci Code', 'Dan Brown', 1, 2003, 'English', 10, 'Doubleday', 14.50, 14.50, 'Mystery thriller involving a murder at the Louvre Museum and clues in famous works of art.', 'MYSTERY', 'Well-maintained', 'LIKE_NEW', 'AVAILABLE');

INSERT INTO Books (ISBN, title, author, edition, publication_year, language, quantity, publisher, original_price, current_price, description, category, condition_description, book_condition, book_status)
VALUES ('978-0-553-57340-0', 'Jurassic Park', 'Michael Crichton', 1, 1990, 'English', 8, 'Alfred A. Knopf', 12.25, 12.25, 'Science fiction thriller about the cloning of dinosaurs for a theme park.', 'FICTION', 'Minor creases on cover', 'GOOD', 'AVAILABLE');

-- Sample INSERT statements for Users table

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('john_doe', 'john.doe@gmail.com', 'SecurePassword123!', 'John', 'Doe', '1234567890', 1000.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('alice_smith', 'alice.smith@yahoo.com', 'StrongPass456#', 'Alice', 'Smith', '9876543210', 750.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('bob_johnson', 'bob.johnson@gmail.com', 'Passw0rd789$', 'Bob', 'Johnson', '5551234567', 500.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('emily_brown', 'emily.brown@yahoo.com', 'SecurePass789!', 'Emily', 'Brown', '1231231234', 1200.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('michael_williams', 'michael.williams@gmail.com', 'ComplexP@ssw0rd', 'Michael', 'Williams', '9879879876', 850.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('sophia_davis', 'sophia.davis@yahoo.com', 'StrongPwd2022#', 'Sophia', 'Davis', '1112223333', 950.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('matthew_johnson', 'matthew.johnson@gmail.com', 'P@ssword12345', 'Matthew', 'Johnson', '9990001111', 1100.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('olivia_lee', 'olivia.lee@yahoo.com', 'L0ngP@ssw0rd!', 'Olivia', 'Lee', '7778889999', 900.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('daniel_wilson', 'daniel.wilson@gmail.com', 'SecureP@ss123', 'Daniel', 'Wilson', '3334445555', 700.00);

INSERT INTO Users (username, email, password, first_name, last_name, phone_number, funds)
VALUES ('ava_martinez', 'ava.martinez@yahoo.com', 'P@ssw0rd5678', 'Ava', 'Martinez', '5556667777', 1300.00);