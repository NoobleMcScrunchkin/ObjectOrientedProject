package com.aslett.library;

import java.util.ArrayList;
import java.util.Arrays;
import com.aslett.library.Models.Loan;
import com.aslett.library.Models.Products.*;
import com.aslett.library.Utils.AuthController;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// JavaFX UI Class
public class UI extends Application {
    private Stage stage;

    // Entrypoint
    public static void begin() {
        launch();
    }

    public Group createBooksTab() {
        // Create Group to be returned
        Group booksTab = new Group();

        // Create TableView
        TableView<Book> tableView = new TableView<>();

        // Create Table Columns
        TableColumn<Book, String> name = new TableColumn<>("Name");
        TableColumn<Book, String> desc = new TableColumn<>("Description");
        TableColumn<Book, String> author = new TableColumn<>("Author");
        TableColumn<Book, String> publisher = new TableColumn<>("Publisher");
        TableColumn<Book, String> isbn = new TableColumn<>("ISBN");
        TableColumn<Book, String> genre = new TableColumn<>("Genre");
        TableColumn<Book, String> language = new TableColumn<>("Language");
        TableColumn<Book, Integer> year = new TableColumn<>("Year");
        TableColumn<Book, Integer> quantity = new TableColumn<>("Quantity");

        // Add Columns to table
        tableView.getColumns()
                .addAll(Arrays.asList(name, desc, author, publisher, isbn, genre, language, year, quantity));

        // Set property on object to be used by table column
        name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        publisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        genre.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        language.setCellValueFactory(new PropertyValueFactory<Book, String>("language"));
        year.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        quantity.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));

        // Add books to the table
        ArrayList<Book> bookList = Book.all();
        for (Book book : bookList) {
            tableView.getItems().add(book);
        }

        // Search Box
        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();

        // Handle text change event
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Find books where one of the properties match the search value
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

        // Set grid options
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        // Set node locations
        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(tableView, 0, 1);
        GridPane.setColumnSpan(tableView, 2);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add nodes to grid and add grid to group
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

    // Main Content Scene
    public Scene createMainScene() {
        // Create Tab based navigation pane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        // Add tabs to navigation pane
        Tab booksTab = new Tab("Books", createBooksTab());
        tabPane.getTabs().add(booksTab);

        Tab dvdsTab = new Tab("DVDs", createDVDsTab());
        tabPane.getTabs().add(dvdsTab);

        Tab loansTab = new Tab("Loans", createLoansTab());
        tabPane.getTabs().add(loansTab);

        // Create main scene
        VBox vBox = new VBox(tabPane);
        Scene scene = new Scene(vBox);

        return scene;
    }

    // Login Scene
    public Scene createLoginScene() {
        // Create grid to place content
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Username input
        Label usernameLabel = new Label("User Name:");
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        // Password Input
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        // Sign in Button
        Button signInBtn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signInBtn);
        grid.add(hbBtn, 1, 3);

        // Handle Button Press
        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Attempt Login
                if (AuthController.attemptLogin(usernameField.getText(), passwordField.getText())) {
                    // Move to main scene
                    stage.setScene(createMainScene());
                } else {
                    // Display error if invalid credentials
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Login Failed");
                    alert.setContentText("Invalid username or password");
                    alert.showAndWait();
                }
            }
        });

        // return the scene
        Scene scene = new Scene(grid, 300, 275);
        return scene;
    }

    // Create Stage
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Library System");

        stage.setScene(createLoginScene());

        stage.show();
    }
}
