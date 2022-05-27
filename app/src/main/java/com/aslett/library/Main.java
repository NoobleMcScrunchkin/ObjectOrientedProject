package com.aslett.library;

import com.aslett.library.Models.User;
import com.aslett.library.Utils.*;

public class Main {
    public static DB db = new DB();
    public static User currentUser = null;

    public static void main(String[] args) {
        db.initDB();

        UI.begin();
    }
}