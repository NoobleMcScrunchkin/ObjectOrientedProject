package com.aslett.library.Models.Products;

import com.aslett.library.Models.Model;
import com.aslett.library.Utils.DBField;
import java.util.ArrayList;

public class DVD extends Product {
    public String director;
    public String genre;
    public Integer year;
    public String certificate;
    public String company;
    public Integer runtime;

    public DVD(String name, String description, String image, String director, String genre, int year,
            String certificate, String company, int runtime, int quantity) {
        super(name, description, image, quantity);
        addFields();

        this.director = director;
        this.genre = genre;
        this.year = year;
        this.certificate = certificate;
        this.company = company;
        this.runtime = runtime;

        super.dbInsert();
    }

    public DVD() {
        super("", "", "", 0);
        addFields();
    }

    public void addFields() {
        this.table = "dvds";
        fields.add(new DBField("director", "text"));
        fields.add(new DBField("genre", "text"));
        fields.add(new DBField("year", "int"));
        fields.add(new DBField("certificate", "text"));
        fields.add(new DBField("company", "text"));
        fields.add(new DBField("runtime", "int"));
    }

    public static DVD find(int ID) {
        DVD dvd = (DVD) Model.find(new DVD(), ID);
        return dvd;
    }

    public static ArrayList<DVD> all() {
        ArrayList<Model> products = (ArrayList<Model>) Model.all(new DVD());
        ArrayList<DVD> dvds = new ArrayList<DVD>();
        for (Model product : products) {
            dvds.add((DVD) product);
        }
        return dvds;
    }

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
