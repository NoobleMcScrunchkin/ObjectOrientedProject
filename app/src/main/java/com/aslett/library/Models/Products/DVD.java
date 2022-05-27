package com.aslett.library.Models.Products;

import com.aslett.library.Models.Model;
import com.aslett.library.Utils.DBField;
import java.util.ArrayList;

// DVD Product
public class DVD extends Product {
    public String director;
    public String genre;
    public Integer year;
    public String certificate;
    public String company;
    public Integer runtime;

    // Create a new DVD
    public DVD(String name, String description, String image, String director, String genre, int year,
            String certificate, String company, int runtime, int quantity) {
        super(name, description, image, quantity);
        this.addFields();

        this.director = director;
        this.genre = genre;
        this.year = year;
        this.certificate = certificate;
        this.company = company;
        this.runtime = runtime;

        this.dbInsert();
    }

    //Template DVD
    public DVD() {
        super("", "", "", 0);
        addFields();
    }

    // Set DB Column info
    public void addFields() {
        this.table = "dvds";
        fields.add(new DBField("director", "text"));
        fields.add(new DBField("genre", "text"));
        fields.add(new DBField("year", "int"));
        fields.add(new DBField("certificate", "text"));
        fields.add(new DBField("company", "text"));
        fields.add(new DBField("runtime", "int"));
    }

    // Get by ID
    public static DVD find(int ID) {
        DVD dvd = (DVD) Model.find(new DVD(), ID);
        return dvd;
    }

    // Get by field
    public static ArrayList<DVD> findByField(String field, String value) {
        ArrayList<Model> products = (ArrayList<Model>) Model.findByField(new DVD(), field, value);
        ArrayList<DVD> dvds = new ArrayList<DVD>();
        for (Model product : products) {
            dvds.add((DVD) product);
        }
        return dvds;
    }

    // Get all dvds
    public static ArrayList<DVD> all() {
        ArrayList<Model> products = (ArrayList<Model>) Model.all(new DVD());
        ArrayList<DVD> dvds = new ArrayList<DVD>();
        for (Model product : products) {
            dvds.add((DVD) product);
        }
        return dvds;
    }

    // Getters
    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getYear() {
        return year;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getCompany() {
        return company;
    }

    public Integer getRuntime() {
        return runtime;
    }
}
