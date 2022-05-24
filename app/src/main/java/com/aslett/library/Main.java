package com.aslett.library;

import com.aslett.library.Utils.DB;

public class Main{
    public static DB db = new DB();

    public static void main(String[] args) {
        db.initDB();

        UI.begin();
    }
}