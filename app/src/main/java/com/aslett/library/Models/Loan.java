package com.aslett.library.Models;

import java.sql.Date;
import java.util.ArrayList;

import com.aslett.library.Utils.DBField;

// Loans
public class Loan extends Model {
    public Date loanDate;
    public Date returnDate;
    public int returned;
    public String customerName;
    public String customerPhone;
    public int productID;
    public String productType;

    // Create a new Loan
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

    // Template Loan
    public Loan() {
        super();
        addFields();
    }

    // Set DB Column info
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

    // Get by ID
    public static Loan find(int ID) {
        Loan loan = (Loan) Model.find(new Loan(), ID);
        return loan;
    }
    
    // Get by field
    public static ArrayList<Loan> findByField(String field, String value) {
        ArrayList<Model> loanModels = (ArrayList<Model>) Model.findByField(new Loan(), field, value);
        ArrayList<Loan> loans = new ArrayList<Loan>();
        for (Model loan : loanModels) {
            loans.add((Loan) loan);
        }
        return loans;
    }

    // Get all loans
    public static ArrayList<Loan> all() {
        ArrayList<Model> loanModels = (ArrayList<Model>) Model.all(new Loan());
        ArrayList<Loan> loans = new ArrayList<Loan>();
        for (Model loan : loanModels) {
            loans.add((Loan) loan);
        }
        return loans;
    }

    // Getters
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
