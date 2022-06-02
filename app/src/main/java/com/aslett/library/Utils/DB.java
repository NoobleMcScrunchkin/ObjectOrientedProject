package com.aslett.library.Utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.aslett.library.Models.*;
import com.aslett.library.Models.Products.*;
import io.github.cdimascio.dotenv.*;

public class DB {
    private String address;

    // Setup DB Connection
    public DB() {
        try {
            Dotenv dotenv = Dotenv.configure().load();

            String host = dotenv.get("DB_HOST", "localhost");
            String name = dotenv.get("DB_NAME", "library");
            String user = dotenv.get("DB_USER", "root");
            String pass = dotenv.get("DB_PASS", "root");

            this.address = "jdbc:mysql://" + host + "/" + name + "?" + "user=" + user + "&password=" + pass;

            Connection conn = DriverManager.getConnection(this.address);
            boolean reachable = conn.isValid(10);

            if (!reachable) {
                System.out.println("Database is not reachable");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Database is not reachable");
            System.exit(0);
            return;
        }
    }

    // Create tables in DB;
    public void initDB() {
        Book book = new Book();
        book.createTable();
        DVD dvd = new DVD();
        dvd.createTable();
        Loan loan = new Loan();
        loan.createTable();
        User user = new User();
        user.createTable();
        if (User.findByField("username", "Admin").size() == 0) {
            User admin = new User("Admin", "Password01", true, true);
        }
    }

    // Execute Query with results on DB
    public Map<String, Object> query(String query) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(this.address);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            Map<String, Object> results = new HashMap<>();
            results.put("resultset", rs);
            results.put("statement", stmt);
            results.put("connection", conn);

            return results;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
    }

    // Execute Query without results on DB
    public void update(String query) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(this.address);

            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
