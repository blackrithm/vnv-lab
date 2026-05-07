package com.example;

public class Member {
    private String name;
    private int borrowedBooks;
    public Member(String name) {
        this.name = name;
        this.borrowedBooks = 0;
    }
    public String getName() { return name; }
    public int getBorrowedBooks() { return borrowedBooks; }
    public void borrowBook() {
        if (borrowedBooks >= 3) {
            throw new IllegalStateException("Cannot borrow more than 3 books");
        }
        borrowedBooks++;
    }
    public void returnBook() {
        if (borrowedBooks == 0) {
            throw new IllegalStateException("No books to return");
        }
        borrowedBooks--;
    }
}