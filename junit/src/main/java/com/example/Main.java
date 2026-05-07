package com.example;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Member alice = new Member("Alice");
        Book book = new Book("1984", "Orwell");
        library.registerMember(alice);
        library.addBook(book);
        System.out.println("Is '1984' available? " + library.isBookAvailable("1984"));
    }
}