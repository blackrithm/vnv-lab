package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
   
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("1984", "Orwell");
    }

    @Test
    void testBorrowingABook() {
        assertFalse(book.isBorrowed());
        book.borrow();
        assertTrue(book.isBorrowed());
    }

    @Test
    void testBorrowingAnAlreadyBorrowedBook() {
        book.borrow();
        assertThrows(IllegalStateException.class, () -> book.borrow());
    }

    @Test
    void testReturningABook() {
        book.borrow();
        assertTrue(book.isBorrowed());
        book.returnBook();
        assertFalse(book.isBorrowed());
    }

    @Test
    void testReturningABookThatWasNotBorrowed() {
        assertThrows(IllegalStateException.class, () -> book.returnBook());
    }
}
