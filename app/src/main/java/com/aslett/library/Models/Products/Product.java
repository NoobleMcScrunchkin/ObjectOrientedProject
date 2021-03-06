package com.aslett.library.Models.Products;

import com.aslett.library.Models.Model;
import com.aslett.library.Utils.DBField;

// Class representing a product in the library
public abstract class Product extends Model {
    public String name = "";
    public String description = "";
    public String image = "";
    public int quantity = 0;

    // Base product class
    public Product(String name, String description, String image, int quantity) {
        super();
        this.name = name;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        fields.add(new DBField("name", "text"));
        fields.add(new DBField("description", "text"));
        fields.add(new DBField("image", "longtext"));
        fields.add(new DBField("quantity", "int"));
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getAvailable() {
        return quantity;
    }

    public String getEarliestReturn() {
        return "";
    }
}
