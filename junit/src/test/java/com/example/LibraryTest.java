package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("1984", "Orwell");
        book2 = new Book("Brave New World", "Huxley");
        library.addBook(book1);
        library.addBook(book2);
    }

    @Test
    void testFindingABookInTheLibrary() {
        Book found = library.findBook("1984");
        assertNotNull(found);
        assertEquals("1984", found.getTitle());
        Book notFound = library.findBook("Nonexistent");
        assertNull(notFound);
    }

    @Test
    void testCheckingBookAvailabilityInLibrary() {
        assertTrue(library.isBookAvailable("1984"));
        book1.borrow();
        assertFalse(library.isBookAvailable("1984"));
        assertFalse(library.isBookAvailable("Nonexistent"));
    }
}