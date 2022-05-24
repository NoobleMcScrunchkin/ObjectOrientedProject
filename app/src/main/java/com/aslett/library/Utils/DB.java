package com.aslett.library.Utils;

import java.sql.*;

import com.aslett.library.Models.*;
import com.aslett.library.Models.Products.*;

import io.github.cdimascio.dotenv.*;

public class DB {
    private String address;

    public DB() {
        try {
            Dotenv dotenv = Dotenv.configure().load();

            String host = dotenv.get("DB_HOST", "localhost");
            String name = dotenv.get("DB_NAME", "library");
            String user = dotenv.get("DB_USER", "root");
            String pass = dotenv.get("DB_PASS", "root");

            this.address = "jdbc:mysql://" + host + "/" + name + "?" + "user=" + user + "&password=" + pass;
        } catch (DotenvException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void initDB() {
        Book book = new Book();
        book.createTable();
        DVD dvd = new DVD();
        dvd.createTable();
        Loan loan = new Loan();
        loan.createTable();
        User user = new User();
        user.createTable();
    }

    public ResultSet query(String query) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(this.address);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            return rs;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
    }

    public void update(String query) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(this.address);

            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
