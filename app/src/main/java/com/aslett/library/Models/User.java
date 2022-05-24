package com.aslett.library.Models;

import java.util.ArrayList;

import com.aslett.library.Utils.DBField;
import com.aslett.library.Utils.HashingController;

public class User extends Model {
    public String username;
    public String password;
    public int isAdmin;

    public User(String username, String password, boolean isAdmin) {
        super();
        addFields();

        try {
            this.password = HashingController.createHash(password);
        } catch (Exception e) {
            this.password = "";
            System.out.println(e.getMessage());
        }

        this.username = username;
        this.isAdmin = isAdmin ? 1 : 0;

        this.dbInsert();
    }

    public User(String username, String password) {
        this(username, password, false);
    }

    public User() {
        super();
        addFields();
    }

    public void addFields() {
        this.table = "users";
        fields.add(new DBField("username", "text"));
        fields.add(new DBField("password", "text"));
        fields.add(new DBField("isAdmin", "int"));
    }

    public static User find(int ID) {
        User user = (User) Model.find(new User(), ID);
        return user;
    }

    public static User findByField(String field, String username) {
        User user = (User) Model.findByField(new User(), field, username);
        return user;
    }

    public static ArrayList<User> all() {
        ArrayList<Model> userModels = (ArrayList<Model>) Model.all(new User());
        ArrayList<User> users = new ArrayList<User>();
        for (Model user : userModels) {
            users.add((User) user);
        }
        return users;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin == 1;
    }
}
