package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryUtilsTest {

    private Library library;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("1984", "Orwell");
        book2 = new Book("Brave New World", "Huxley");
        book3 = new Book("Fahrenheit 451", "Bradbury");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
    }

    @Test
    void testCountingAvailableBooksUsingLibraryUtils() {
        assertEquals(3, LibraryUtils.countAvailableBooks(library));
        book1.borrow();
        assertEquals(2, LibraryUtils.countAvailableBooks(library));
        book2.borrow();
        assertEquals(1, LibraryUtils.countAvailableBooks(library));
        book3.borrow();
        assertEquals(0, LibraryUtils.countAvailableBooks(library));
    }
}