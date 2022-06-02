package com.aslett.library.Models;

import java.util.ArrayList;
import com.aslett.library.Utils.DBField;
import com.aslett.library.Utils.AuthController;

// User Model
public class User extends Model {
    public String username;
    public String password;
    public int isAdmin;
    public int approved;

    // Create a new User
    public User(String username, String password, boolean isAdmin) {
        this(username, password, isAdmin, false);
    }

    // Create a new User, defaulting to not an admin
    public User(String username, String password) {
        this(username, password, false);
    }

    public User(String username, String password, boolean isAdmin, boolean isApproved) {
        super();
        addFields();

        try {
            this.password = AuthController.createHash(password);
        } catch (Exception e) {
            this.password = "";
            System.out.println(e.getMessage());
        }

        this.username = username;
        this.isAdmin = isAdmin ? 1 : 0;
        this.approved = isApproved ? 1: 0;

        this.dbInsert();
    }

    // Template User
    public User() {
        super();
        addFields();
    }

    // Set DB Column info
    public void addFields() {
        this.table = "users";
        fields.add(new DBField("username", "text"));
        fields.add(new DBField("password", "text"));
        fields.add(new DBField("isAdmin", "int"));
        fields.add(new DBField("approved", "int"));
    }

    // Get by ID
    public static User find(int ID) {
        User user = (User) Model.find(new User(), ID);
        return user;
    }

    // Get by field
    public static ArrayList<User> findByField(String field, String value) {
        ArrayList<Model> userModels = (ArrayList<Model>) Model.findByField(new User(), field, value);
        ArrayList<User> users = new ArrayList<User>();
        for (Model user : userModels) {
            users.add((User) user);
        }
        return users;
    }

    // Get all Users
    public static ArrayList<User> all() {
        ArrayList<Model> userModels = (ArrayList<Model>) Model.all(new User());
        ArrayList<User> users = new ArrayList<User>();
        for (Model user : userModels) {
            users.add((User) user);
        }
        return users;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin == 1;
    }

    public boolean isApproved() {
        return approved == 1;
    }

    public String getAdminStr() {
        return isAdmin() ? "Yes" : "No";
    }

    public String getApprovedStr() {
        return isApproved() ? "Yes" : "No";
    }
}
