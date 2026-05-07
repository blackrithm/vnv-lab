package com.example;

public class Book {
    private String title;
    private String author;
    private boolean isBorrowed;
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return isBorrowed; }
    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
        } else {
            throw new IllegalStateException("Book already borrowed");
        }
    }
    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
        } else {
            throw new IllegalStateException("Book was not borrowed");
        }
    }
}
