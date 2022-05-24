package com.aslett.library.Models;

// import com.aslett.library.Utils.DBField;

public class Loan extends Model {
    public Loan() {
        super();
        this.table = "loans";
        this.dbInsert();
    }

    public void addFields() {
        this.table = "loans";
        // fields.add(new DBField("loan_date", "date"));
        // fields.add(new DBField("return_date", "date"));
        // fields.add(new DBField("returned", "int"));
        // fields.add(new DBField("customer_id", "int"));
        // fields.add(new DBField("product_id", "int"));
    }
}
