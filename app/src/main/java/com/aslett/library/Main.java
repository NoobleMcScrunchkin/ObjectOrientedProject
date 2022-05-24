package com.aslett.library;

import com.aslett.library.Models.User;
import com.aslett.library.Utils.*;

public class Main {
    public static DB db = new DB();
    public static User currentUser = null;

    public static void main(String[] args) {
        db.initDB();

        // User user = new User("kieran", "test");
        User user = User.find(2);
        System.out.println(user.username);
        try {
            if (HashingController.validatePassword("test1", user.password)) {
                currentUser = user;
            } else {
                currentUser = null;
            }
        } catch (Exception e) {
            currentUser = null;
            System.out.println(e.getMessage());
        }

        UI.begin();
    }
}