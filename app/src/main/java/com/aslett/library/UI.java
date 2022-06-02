package com.aslett.library;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

import com.aslett.library.Models.Loan;
import com.aslett.library.Models.User;
import com.aslett.library.Models.Products.*;
import com.aslett.library.Utils.AuthController;
import com.aslett.library.Utils.DBField;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

// JavaFX UI Class
public class UI extends Application {
    private Stage stage;
    private TableView<Book> bookTable;
    private TableView<DVD> dvdTable;
    private TableView<Loan> loanTable;
    private TableView<User> userTable;

    // Entrypoint
    public static void begin() {
        launch();
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public void updateContent() {
        bookTable.getItems().clear();
        ArrayList<Book> bookList = Book.all();
        for (Book book : bookList) {
            bookTable.getItems().add(book);
        }

        dvdTable.getItems().clear();
        ArrayList<DVD> dvdList = DVD.all();
        for (DVD dvd : dvdList) {
            dvdTable.getItems().add(dvd);
        }

        loanTable.getItems().clear();
        ArrayList<Loan> loanList = Loan.all();
        for (Loan loan : loanList) {
            loanTable.getItems().add(loan);
        }

        userTable.getItems().clear();
        ArrayList<User> userList = User.all();
        for (User user : userList) {
            userTable.getItems().add(user);
        }
    }

    public Group createBooksTab() {
        // Create Group to be returned
        Group booksTab = new Group();

        // Create TableView
        bookTable = new TableView<>();

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
        TableColumn<Book, Integer> available = new TableColumn<>("Available");

        // Add Columns to table
        bookTable.getColumns()
                .addAll(Arrays.asList(name, desc, author, publisher, isbn, genre, language, year, quantity, available));

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
        available.setCellValueFactory(new PropertyValueFactory<Book, Integer>("available"));

        // Add books to the table
        ArrayList<Book> bookList = Book.all();
        for (Book book : bookList) {
            bookTable.getItems().add(book);
        }

        // Search Box
        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();

        // Handle text change event
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Find books where one of the properties match the search value
            bookTable.getItems().clear();
            for (Book book : bookList) {
                String[] properties = book.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        bookTable.getItems().add(book);
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

        Group sidebar = addBookSidebar(bookTable);

        // Set node locations
        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(bookTable, 0, 1);
        GridPane.setConstraints(sidebar, 2, 0);
        GridPane.setColumnSpan(bookTable, 2);
        GridPane.setRowSpan(sidebar, 2);

        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add nodes to grid and add grid to group
        grid.getChildren().addAll(searchLabel, searchField, bookTable, sidebar);
        booksTab.getChildren().add(grid);
        return booksTab;
    }

    public Group createDVDsTab() {
        Group dvdsTab = new Group();
        dvdTable = new TableView<>();

        TableColumn<DVD, String> name = new TableColumn<>("Name");
        TableColumn<DVD, String> desc = new TableColumn<>("Description");
        TableColumn<DVD, String> director = new TableColumn<>("Director");
        TableColumn<DVD, String> genre = new TableColumn<>("Genre");
        TableColumn<DVD, Integer> year = new TableColumn<>("Year");
        TableColumn<DVD, String> certificate = new TableColumn<>("Certificate");
        TableColumn<DVD, String> company = new TableColumn<>("Company");
        TableColumn<DVD, Integer> runtime = new TableColumn<>("Runtime");
        TableColumn<DVD, Integer> quantity = new TableColumn<>("Quantity");
        TableColumn<DVD, Integer> available = new TableColumn<>("Available");

        dvdTable.getColumns()
                .addAll(Arrays.asList(name, desc, director, genre, year, certificate, company, runtime, quantity,
                        available));

        name.setCellValueFactory(new PropertyValueFactory<DVD, String>("name"));
        desc.setCellValueFactory(new PropertyValueFactory<DVD, String>("description"));
        director.setCellValueFactory(new PropertyValueFactory<DVD, String>("director"));
        genre.setCellValueFactory(new PropertyValueFactory<DVD, String>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("year"));
        certificate.setCellValueFactory(new PropertyValueFactory<DVD, String>("certificate"));
        company.setCellValueFactory(new PropertyValueFactory<DVD, String>("company"));
        runtime.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("runtime"));
        quantity.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("quantity"));
        available.setCellValueFactory(new PropertyValueFactory<DVD, Integer>("available"));

        ArrayList<DVD> dvdList = DVD.all();
        for (DVD dvd : dvdList) {
            dvdTable.getItems().add(dvd);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            dvdTable.getItems().clear();
            for (DVD dvd : dvdList) {
                String[] properties = dvd.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        dvdTable.getItems().add(dvd);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        Group sidebar = addDVDSidebar(dvdTable);

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(dvdTable, 0, 1);
        GridPane.setConstraints(sidebar, 2, 0);
        GridPane.setColumnSpan(dvdTable, 2);
        GridPane.setRowSpan(sidebar, 2);

        dvdTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, dvdTable, sidebar);
        dvdsTab.getChildren().add(grid);
        return dvdsTab;
    }

    public Group createLoansTab() {
        Group loansTab = new Group();
        loanTable = new TableView<>();

        TableColumn<Loan, String> loanDate = new TableColumn<>("Loan Date");
        TableColumn<Loan, String> returnDate = new TableColumn<>("Return Date");
        TableColumn<Loan, Integer> returned = new TableColumn<>("Returned");
        TableColumn<Loan, String> customerName = new TableColumn<>("Customer Name");
        TableColumn<Loan, String> customerPhone = new TableColumn<>("Customer Phone Number");
        TableColumn<Loan, Integer> productID = new TableColumn<>("Product ID");
        TableColumn<Loan, String> productType = new TableColumn<>("Product Type");

        loanTable.getColumns()
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
            loanTable.getItems().add(loan);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loanTable.getItems().clear();
            for (Loan loan : loanList) {
                String[] properties = loan.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        loanTable.getItems().add(loan);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        Group sidebar = addLoansSidebar(loanTable);

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(loanTable, 0, 1);
        GridPane.setConstraints(sidebar, 2, 1);
        GridPane.setColumnSpan(loanTable, 2);

        loanTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, loanTable, sidebar);
        loansTab.getChildren().add(grid);
        return loansTab;
    }

    public Group createUsersTab() {
        Group loansTab = new Group();
        userTable = new TableView<>();

        TableColumn<User, String> username = new TableColumn<>("Username");
        TableColumn<User, String> admin = new TableColumn<>("Admin");
        TableColumn<User, String> approved = new TableColumn<>("Approved");

        userTable.getColumns()
                .addAll(Arrays.asList(username, admin, approved));

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        admin.setCellValueFactory(new PropertyValueFactory<User, String>("adminStr"));
        approved.setCellValueFactory(new PropertyValueFactory<User, String>("approvedStr"));

        ArrayList<User> userList = User.all();
        for (User user : userList) {
            userTable.getItems().add(user);
        }

        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            userTable.getItems().clear();
            for (User user : userList) {
                String[] properties = user.allProperties();
                for (String property : properties) {
                    if (property.toLowerCase().contains(newValue.toLowerCase())) {
                        userTable.getItems().add(user);
                        break;
                    }
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(4, 4, 4, 4));
        grid.setVgap(5);
        grid.setHgap(5);

        Group sidebar = addUsersSidebar();

        GridPane.setConstraints(searchLabel, 0, 0);
        GridPane.setConstraints(searchField, 1, 0);
        GridPane.setConstraints(userTable, 0, 1);
        GridPane.setConstraints(sidebar, 2, 1);
        GridPane.setColumnSpan(userTable, 2);

        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        grid.getChildren().addAll(searchLabel, searchField, userTable, sidebar);
        loansTab.getChildren().add(grid);
        return loansTab;
    }

    public Group addDVDSidebar(TableView<?> table) {
        Group sidebarGroup = new Group();

        GridPane grid = addProductSidebar(table);

        Integer row = 4;

        HashMap<String, Label> labels = new HashMap<String, Label>();
        HashMap<String, TextField> inputfields = new HashMap<String, TextField>();

        DVD dvd = new DVD();
        for (DBField field : dvd.fields) {
            if (field.field == "ID") {
                continue;
            }
            if (field.field == "image") {
                labels.put(field.field, new Label("Image (Base64 Encoded)"));
            } else {
                labels.put(field.field, new Label(toTitleCase(field.field)));
            }
            grid.add(labels.get(field.field), 0, row);

            inputfields.put(field.field, new TextField());
            grid.add(inputfields.get(field.field), 1, row);
            row++;
        }

        Button addBookBtn = new Button("Add Book");
        grid.add(addBookBtn, 0, row);

        addBookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (DBField field : dvd.fields) {
                    if (field.field == "ID") {
                        continue;
                    }
                    if (inputfields.get(field.field).getText() == "") {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Please fill in all fields");
                        alert.showAndWait();
                        return;
                    }
                }
                try {
                    new DVD(inputfields.get("name").getText(), inputfields.get("description").getText(),
                            inputfields.get("image").getText(), inputfields.get("director").getText(),
                            inputfields.get("genre").getText(), Integer.parseInt(inputfields.get("year").getText()),
                            inputfields.get("certificate").getText(), inputfields.get("company").getText(),
                            Integer.parseInt(inputfields.get("runtime").getText()),
                            Integer.parseInt(inputfields.get("quantity").getText()));
                } catch (Exception f) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Invalid input");
                    alert.showAndWait();
                }
                updateContent();
            }
        });

        sidebarGroup.getChildren().add(grid);

        return sidebarGroup;
    }

    public Group addBookSidebar(TableView<?> table) {
        Group sidebarGroup = new Group();

        GridPane grid = addProductSidebar(table);

        Integer row = 4;

        HashMap<String, Label> labels = new HashMap<String, Label>();
        HashMap<String, TextField> inputfields = new HashMap<String, TextField>();

        Book book = new Book();
        for (DBField field : book.fields) {
            if (field.field == "ID") {
                continue;
            }
            if (field.field == "image") {
                labels.put(field.field, new Label("Image (Base64 Encoded)"));
            } else {
                labels.put(field.field, new Label(toTitleCase(field.field)));
            }
            grid.add(labels.get(field.field), 0, row);

            inputfields.put(field.field, new TextField());
            grid.add(inputfields.get(field.field), 1, row);
            row++;
        }

        Button addBookBtn = new Button("Add Book");
        grid.add(addBookBtn, 0, row);

        addBookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (DBField field : book.fields) {
                    if (field.field == "ID") {
                        continue;
                    }
                    if (inputfields.get(field.field).getText() == "") {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Please fill in all fields");
                        alert.showAndWait();
                        return;
                    }
                }
                try {
                    new Book(inputfields.get("name").getText(), inputfields.get("description").getText(),
                            inputfields.get("image").getText(), inputfields.get("author").getText(),
                            inputfields.get("publisher").getText(), inputfields.get("isbn").getText(),
                            inputfields.get("genre").getText(), inputfields.get("language").getText(),
                            Integer.parseInt(inputfields.get("year").getText()),
                            Integer.parseInt(inputfields.get("quantity").getText()));
                } catch (Exception f) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Invalid input");
                    alert.showAndWait();
                }
                updateContent();
            }
        });

        sidebarGroup.getChildren().add(grid);

        return sidebarGroup;
    }

    public GridPane addProductSidebar(TableView<?> table) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Username input
        Label customerLabel = new Label("Customer Name:");
        grid.add(customerLabel, 0, 0);

        TextField customerField = new TextField();
        grid.add(customerField, 1, 0);

        // Password Input
        Label phoneLabel = new Label("Phone Number:");
        grid.add(phoneLabel, 0, 1);

        TextField phoneField = new TextField();
        grid.add(phoneField, 1, 1);

        Button createLoanBtn = new Button("Create Loan");
        grid.add(createLoanBtn, 0, 2);

        createLoanBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Product product = (Product) table.getSelectionModel().getSelectedItem();
                if (product == null) {
                    return;
                }
                String customerName = customerField.getText();
                String phoneNumber = phoneField.getText();
                if (customerName.isEmpty() || phoneNumber.isEmpty()) {
                    return;
                }

                java.util.Date date = new java.util.Date();
                Date loanDate = new Date(date.getTime());
                LocalDateTime dateReturn = LocalDateTime.from(date.toInstant()).plusDays(7);
                Date loanDateReturn = Date.valueOf(dateReturn.toLocalDate());

                new Loan(loanDate, loanDateReturn, 0, customerName, phoneNumber, product.getID(), product.table);
            }
        });

        Button viewBtn = new Button("View");
        grid.add(viewBtn, 0, 3);

        viewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Product product = (Product) table.getSelectionModel().getSelectedItem();
                if (product == null) {
                    return;
                }

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);

                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(5);
                grid.setVgap(5);
                grid.setPadding(new Insets(25, 25, 25, 25));

                ImageView imageView = new ImageView();
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(product.image);
                    ByteArrayInputStream is = new ByteArrayInputStream(imageBytes);
                    imageView.setImage(new Image(is));
                } catch (Exception f) {
                }
                imageView.setPreserveRatio(true);
                imageView.fitHeightProperty().set(400);
                grid.add(imageView, 0, 0);

                Text[] textArr = new Text[product.fields.size()];
                int row = 0;
                for (int i = 0; i < product.fields.size(); i++) {
                    if (product.fields.get(i).field == "ID" || product.fields.get(i).field == "image") {
                        continue;
                    }
                    textArr[i] = new Text();
                    textArr[i]
                            .setText(toTitleCase(product.fields.get(i).field) + ": " + product.allProperties()[i]);
                    grid.add(textArr[i], 0, row + 1);
                    row++;
                }
                Text availableText = new Text();
                availableText.setText("Available: " + product.getAvailable());
                grid.add(availableText, 0, row + 1);
                row++;

                Text earliestReturnText = new Text();
                earliestReturnText.setText("Next return: " + product.getEarliestReturn());
                grid.add(earliestReturnText, 0, row + 1);
                row++;

                Button saveBtn = new Button("Save");
                grid.add(saveBtn, 0, row + 1);

                saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("Save");
                        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Document", "*.txt"),
                                new ExtensionFilter("All Files", "*.*"));

                        File file = chooser.showSaveDialog(stage);
                        PrintWriter outFile = null;
                        try {
                            outFile = new PrintWriter(file);
                        } catch (FileNotFoundException f) {
                            f.printStackTrace();
                        }

                        for (int i = 0; i < product.fields.size(); i++) {
                            if (product.fields.get(i).field == "ID" || product.fields.get(i).field == "image") {
                                continue;
                            }
                            outFile.println(
                                    toTitleCase(product.fields.get(i).field) + ": " + product.allProperties()[i]);
                        }
                        outFile.println("Available: " + product.getAvailable());
                        outFile.println("Next return: " + product.getEarliestReturn());
                        outFile.close();
                    }
                });

                dialogVbox.getChildren().add(grid);

                Scene dialogScene = new Scene(dialogVbox);
                dialog.setScene(dialogScene);
                dialog.show();

            }
        });

        return grid;
    }

    public Group addLoansSidebar(TableView<Loan> table) {
        Group sideBarGroup = new Group();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button returnBtn = new Button("Mark as returned");
        grid.add(returnBtn, 0, 0);

        returnBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Loan loan = (Loan) table.getSelectionModel().getSelectedItem();
                if (loan == null) {
                    return;
                }
                loan.returned = 1;
                loan.save();
                updateContent();
            }
        });

        sideBarGroup.getChildren().add(grid);
        return sideBarGroup;
    }

    public Group addUsersSidebar() {
        Group sideBarGroup = new Group();

        if (!Main.currentUser.isAdmin()) {
            return sideBarGroup;
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button setAdminBtn = new Button("Toggle Admin");

        setAdminBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                User user = (User) userTable.getSelectionModel().getSelectedItem();
                if (user == null || user.getID() == Main.currentUser.getID()) {
                    return;
                }
                user.isAdmin = user.isAdmin() ? 0 : 1;
                user.save();
                updateContent();
            }
        });

        grid.add(setAdminBtn, 0, 0);

        Button approveBtn = new Button("Approve User");

        approveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                User user = (User) userTable.getSelectionModel().getSelectedItem();
                if (user == null || user.getID() == Main.currentUser.getID()) {
                    return;
                }
                user.approved = 1;
                user.save();
                updateContent();
            }
        });

        grid.add(approveBtn, 0, 1);
        sideBarGroup.getChildren().add(grid);
        return sideBarGroup;
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

        Tab usersTab = new Tab("Users", createUsersTab());
        tabPane.getTabs().add(usersTab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateContent();
        });

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
        Button registerBtn = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signInBtn);
        hbBtn.getChildren().add(registerBtn);
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

        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ArrayList<User> users = User.findByField("username", usernameField.getText());
                if (users.size() > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Registration Failed");
                    alert.setContentText("Username already exists");
                    alert.showAndWait();
                } else {
                    User user = new User(usernameField.getText(), passwordField.getText());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Registration Successful");
                    alert.setHeaderText("Registration Successful");
                    alert.setContentText("You will be able to login once an admin has approved your account");
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
