package com.aslett.library.Models.Products;

import com.aslett.library.Models.Model;
import com.aslett.library.Utils.DBField;
import java.util.ArrayList;

public class Book extends Product {
    public String author;
    public String publisher;
    public String isbn;
    public String genre;
    public String language;
    public Integer year;

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

    public Book() {
        super("", "", "", 0);
        addFields();
    }

    public void addFields() {
        this.table = "books";
        fields.add(new DBField("author", "text"));
        fields.add(new DBField("publisher", "text"));
        fields.add(new DBField("isbn", "text"));
        fields.add(new DBField("genre", "text"));
        fields.add(new DBField("language", "text"));
        fields.add(new DBField("year", "int"));
    }

    public static Book find(int ID) {
        Book book = (Book) Model.find(new Book(), ID);
        return book;
    }

    public static ArrayList<Book> all() {
        ArrayList<Model> products = (ArrayList<Model>) Model.all(new Book());
        ArrayList<Book> books = new ArrayList<Book>();
        for (Model product : products) {
            books.add((Book) product);
        }
        return books;
    }

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
}
