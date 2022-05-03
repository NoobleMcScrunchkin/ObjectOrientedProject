package com.aslett.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = null;

        String host = "localhost";
        String name = "library";
        String user = "root";
        String pass = "root";

        try {
            dotenv = Dotenv.configure().load();

            host = dotenv.get("DB_HOST", "localhost");
            name = dotenv.get("DB_NAME", "library");
            user = dotenv.get("DB_USER", "root");
            pass = dotenv.get("DB_PASS", "root");
        } catch (DotenvException e) {
            System.out.println(e.getMessage());
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + name + "?" + "user=" + user + "&password=" + pass);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM test");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String testcol = rs.getString("testcol");

                System.out.format("%s, %s\n", id, testcol);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }

                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }
}