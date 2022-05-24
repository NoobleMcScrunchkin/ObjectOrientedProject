package com.aslett.library;

import com.aslett.library.Models.Products.DVD;
import com.aslett.library.Utils.DB;

public class Main{
    public static DB db = new DB();

    public static void main(String[] args) {
        db.initDB();

        DVD dvd = DVD.find(2);
        System.out.println(dvd.name);
        // dvd.name = "updatedfromjava";
        // dvd.save();

        UI.begin();
    }
}