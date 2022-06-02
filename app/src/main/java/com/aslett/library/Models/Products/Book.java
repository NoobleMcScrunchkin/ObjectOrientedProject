package com.aslett.library.Models.Products;

import com.aslett.library.Models.Loan;
import com.aslett.library.Models.Model;
import com.aslett.library.Utils.DBField;

import java.sql.Date;
import java.util.ArrayList;

// Book Product
public class Book extends Product {
    public String author;
    public String publisher;
    public String isbn;
    public String genre;
    public String language;
    public Integer year;

    // Create a new book
    public Book(String name, String description, String image, String author, String publisher, String isbn,
            String genre, String language, Integer year, Integer quantity) {
        super(name, description, image, quantity);
        this.addFields();

        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.genre = genre;
        this.language = language;
        this.year = year;

        this.dbInsert();
    }

    // Template Book
    public Book() {
        super("", "", "", 0);
        addFields();
    }

    // Set DB Column info
    public void addFields() {
        this.table = "books";
        fields.add(new DBField("author", "text"));
        fields.add(new DBField("publisher", "text"));
        fields.add(new DBField("isbn", "text"));
        fields.add(new DBField("genre", "text"));
        fields.add(new DBField("language", "text"));
        fields.add(new DBField("year", "int"));
    }

    // Get by ID
    public static Book find(int ID) {
        Book book = (Book) Model.find(new Book(), ID);
        return book;
    }

    // Get by field
    public static ArrayList<Book> findByField(String field, String value) {
        ArrayList<Model> products = (ArrayList<Model>) Model.findByField(new Book(), field, value);
        ArrayList<Book> books = new ArrayList<Book>();
        for (Model product : products) {
            books.add((Book) product);
        }
        return books;
    }

    // Get all books
    public static ArrayList<Book> all() {
        ArrayList<Model> products = (ArrayList<Model>) Model.all(new Book());
        ArrayList<Book> books = new ArrayList<Book>();
        for (Model product : products) {
            books.add((Book) product);
        }
        return books;
    }

    // Getters
    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getAvailable() {
        ArrayList<Loan> loans = Loan.findByField("productType", "books");
        int loanCount = 0;
        for (Loan loan : loans) {
            if (loan.productID == this.ID && loan.returned == 0) {
                loanCount++;
            }
        }
        return quantity - loanCount;
    }

    public String getEarliestReturn() {
        ArrayList<Loan> loans = Loan.findByField("productType", "books");
        java.util.Date earliestReturn = null;
        for (Loan loan : loans) {
            if (loan.productID != this.ID || loan.returned == 1) {
                continue;
            }
            java.util.Date sqlDate = loan.getReturnDate();
            if (earliestReturn == null) {
                earliestReturn = sqlDate;
                continue;
            }

            if (sqlDate != null && sqlDate.compareTo(earliestReturn) < 0) {
                earliestReturn = sqlDate;
            }
        }
        if (earliestReturn != null) {
            return earliestReturn.toString();
        } else {
            return "No next return date";
        }
    }
}
