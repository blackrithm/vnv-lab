package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("Alice");
    }

    @Test
    void testMemberBorrowingLimit() {
        assertEquals(0, member.getBorrowedBooks());
        member.borrowBook();
        assertEquals(1, member.getBorrowedBooks());
        member.borrowBook();
        assertEquals(2, member.getBorrowedBooks());
        member.borrowBook();
        assertEquals(3, member.getBorrowedBooks());
    }

    @Test
    void testExceedingBorrowLimit() {
        member.borrowBook();
        member.borrowBook();
        member.borrowBook();
        assertThrows(IllegalStateException.class, () -> member.borrowBook());
    }

    @Test
    void testReturningABookByMember() {
        member.borrowBook();
        member.borrowBook();
        assertEquals(2, member.getBorrowedBooks());
        member.returnBook();
        assertEquals(1, member.getBorrowedBooks());
    }

    @Test
    void testReturningABookWhenMemberHasNoneBorrowed() {
        assertThrows(IllegalStateException.class, () -> member.returnBook());
    }
}