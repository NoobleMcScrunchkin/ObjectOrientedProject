package com.aslett.library;

import java.util.ArrayList;
import java.util.Arrays;
import com.aslett.library.Models.Loan;
import com.aslett.library.Models.Products.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {
    public static void begin() {
        launch();
    }

    public Group createBooksTab() {
        Group booksTab = new Group();

        TableView<Book> tableView = new TableView<>();

        TableColumn<Book, String> name = new TableColumn<>("Name");
        TableColumn<Book, String> desc = new TableColumn<>("Description");
        TableColumn<Book, String> author = new TableColumn<>("Author");
        TableColumn<Book, String> publisher = new TableColumn<>("Publisher");
        TableColumn<Book, String> isbn = new TableColumn<>("ISBN");
        TableColumn<Book, String> genre = new TableColumn<>("Genre");
        TableColumn<Book, String> language = new TableColumn<>("Language");
        TableColumn<Book, Integer> year = new TableColumn<>("Year");
        TableColumn<Book, Integer> quantity = new TableColumn<>("Quantity");

        tableView.getColumns()
                .addAll(Arrays.asList(name, desc, author, publisher, isbn, genre, language, year, quantity));

        name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        publisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        genre.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        language.setCellValueFactory(new PropertyValueFactory<Book, String>("language"));
        year.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        quantity.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));

        ArrayList<Book> bookList = Book.all();
        for (Book book : bookList) {
            tableView.getItems().add(book);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.getItems().clear();
            for (Book book : bookList) {
                String[] properties = book.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        tableView.getItems().add(book);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(tableView, 0, 1);
        GridPane.setColumnSpan(tableView, 2);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, tableView);
        booksTab.getChildren().add(grid);
        return booksTab;
    }

    public Group createDVDsTab() {
        Group dvdsTab = new Group();
        TableView<DVD> tableView = new TableView<>();

        TableColumn<DVD, String> name = new TableColumn<>("Name");
        TableColumn<DVD, String> desc = new TableColumn<>("Description");
        TableColumn<DVD, String> director = new TableColumn<>("Director");
        TableColumn<DVD, String> genre = new TableColumn<>("Genre");
        TableColumn<DVD, Integer> year = new TableColumn<>("Year");
        TableColumn<DVD, String> certificate = new TableColumn<>("Certificate");
        TableColumn<DVD, String> company = new TableColumn<>("Company");
        TableColumn<DVD, Integer> runtime = new TableColumn<>("Runtime");
        TableColumn<DVD, Integer> quantity = new TableColumn<>("Quantity");

        tableView.getColumns()
                .addAll(Arrays.asList(name, desc, director, genre, year, certificate, company, runtime, quantity));

        name.setCellValueFactory(new PropertyValueFactory<DVD, String>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<DVD, String>("description"));
        director.setCellValueFactory(new PropertyValueFactory<DVD, String>("director"));
        genre.setCellValueFactory(new PropertyValueFactory<DVD, String>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("year"));
        certificate.setCellValueFactory(new PropertyValueFactory<DVD, String>("certificate"));
        company.setCellValueFactory(new PropertyValueFactory<DVD, String>("company"));
        runtime.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("runtime"));
        quantity.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("quantity"));

        ArrayList<DVD> dvdList = DVD.all();
        for (DVD dvd : dvdList) {
            tableView.getItems().add(dvd);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.getItems().clear();
            for (DVD dvd : dvdList) {
                String[] properties = dvd.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        tableView.getItems().add(dvd);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(tableView, 0, 1);
        GridPane.setColumnSpan(tableView, 2);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, tableView);
        dvdsTab.getChildren().add(grid);
        return dvdsTab;
    }

    public Group createLoansTab() {
        Group loansTab = new Group();
        TableView<Loan> tableView = new TableView<>();

        TableColumn<Loan, String> loanDate = new TableColumn<>("Loan Date");
        TableColumn<Loan, String> returnDate = new TableColumn<>("Return Date");
        TableColumn<Loan, Integer> returned = new TableColumn<>("Returned");
        TableColumn<Loan, String> customerName = new TableColumn<>("Customer Name");
        TableColumn<Loan, String> customerPhone = new TableColumn<>("Customer Phone Number");
        TableColumn<Loan, Integer> productID = new TableColumn<>("Product ID");
        TableColumn<Loan, String> productType = new TableColumn<>("Product Type");

        tableView.getColumns()
                .addAll(Arrays.asList(loanDate, returnDate, returned, customerName, customerPhone, productID,
                        productType));

        loanDate.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanDate"));
        returnDate.setCellValueFactory(new PropertyValueFactory<Loan, String>("returnDate"));
        returned.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("returned"));
        customerName.setCellValueFactory(new PropertyValueFactory<Loan, String>("customerName"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<Loan, String>("customerPhone"));
        productID.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("productID"));
        productType.setCellValueFactory(new PropertyValueFactory<Loan, String>("productType"));

        ArrayList<Loan> loanList = Loan.all();
        for (Loan loan : loanList) {
            tableView.getItems().add(loan);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.getItems().clear();
            for (Loan loan : loanList) {
                String[] properties = loan.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        tableView.getItems().add(loan);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(tableView, 0, 1);
        GridPane.setColumnSpan(tableView, 2);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, tableView);
        loansTab.getChildren().add(grid);
        return loansTab;
    }

    public void start(Stage primaryStage) {

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Tab booksTab = new Tab("Books", createBooksTab());
        tabPane.getTabs().add(booksTab);

        Tab dvdsTab = new Tab("DVDs", createDVDsTab());
        tabPane.getTabs().add(dvdsTab);

        Tab loansTab = new Tab("Loans", createLoansTab());
        tabPane.getTabs().add(loansTab);

        VBox vBox = new VBox(tabPane);
        Scene scene = new Scene(vBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library System");

        primaryStage.show();
    }
}
