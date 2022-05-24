package com.aslett.library.Models;

import java.sql.Date;
import java.util.ArrayList;

import com.aslett.library.Utils.DBField;

public class Loan extends Model {
    public Date loanDate;
    public Date returnDate;
    public int returned;
    public String customerName;
    public String customerPhone;
    public int productID;
    public String productType;

    public Loan(Date loanDate, Date returnDate, int returned, String customerName, String customerPhone, int productID,
            String productType) {
        super();
        addFields();

        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.productID = productID;
        this.productType = productType;

        this.dbInsert();
    }

    public Loan() {
        super();
        addFields();
    }

    public void addFields() {
        this.table = "loans";
        fields.add(new DBField("loanDate", "date"));
        fields.add(new DBField("returnDate", "date"));
        fields.add(new DBField("returned", "int"));
        fields.add(new DBField("customerName", "text"));
        fields.add(new DBField("customerPhone", "text"));
        fields.add(new DBField("productID", "int"));
        fields.add(new DBField("productType", "text"));
    }

    public static Loan find(int ID) {
        Loan book = (Loan) Model.find(new Loan(), ID);
        return book;
    }

    public static ArrayList<Loan> all() {
        ArrayList<Model> products = (ArrayList<Model>) Model.all(new Loan());
        ArrayList<Loan> loans = new ArrayList<Loan>();
        for (Model product : products) {
            loans.add((Loan) product);
        }
        return loans;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getReturned() {
        return returned;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductType() {
        return productType;
    }
}
