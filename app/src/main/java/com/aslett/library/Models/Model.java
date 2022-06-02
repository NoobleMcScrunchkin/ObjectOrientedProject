package com.aslett.library.Models;

import com.aslett.library.Main;
import com.aslett.library.Utils.DBField;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class to connect an object to a database.
 * Allows for the creation of a table for the object.
 * Will set properties on the object automaticaly with correct naming and takes
 * into account inheritance.
 * Every database table should have a class that inherits this
 */
public abstract class Model implements Cloneable {
    // Init Variables
    public String table = "";
    public ArrayList<DBField> fields = new ArrayList<DBField>();
    protected int ID = 0;

    /**
     * Constructor for a model.
     * Adds an ID by default to the object.
     */
    public Model() {
        fields.add(new DBField("ID", "int"));
    }

    // Find rows in the database by querying a field.
    protected static ArrayList<Model> findByField(Model instance, String dbfield, String dbvalue) {
        // Init Variables
        ArrayList<Model> models = new ArrayList<Model>();
        Class<?> c = null;
        Model model = null;

        // Get Results from DB
        Map<String, Object> results = Main.db
                .query(String.format("SELECT * FROM %s WHERE %s = '%s'", instance.table, dbfield, dbvalue));

        ResultSet rs = (ResultSet) results.get("resultset");
        Statement stmt = (Statement) results.get("statement");
        Connection conn = (Connection) results.get("connection");

        try {
            // Iterate over each row and each property in the result set
            while (rs.next()) {
                // New instance of model
                try {
                    model = (Model) instance.clone();
                } catch (Exception e) {
                    System.out.println("Clone error");
                    return null;
                }
                for (int i = 0; i < model.fields.size(); i++) {
                    DBField field = model.fields.get(i);
                    String type = field.type;
                    Field f = null;
                    Object value = null;

                    // Get the type of the property
                    switch (type) {
                        case "int":
                            value = rs.getInt(field.field);
                            break;
                        case "date":
                            value = rs.getDate(field.field);
                            break;
                        case "text":
                        case "longtext":
                            value = rs.getString(field.field);
                            break;
                        default:
                            break;
                    }

                    // Determine which class the property belongs to
                    c = model.getClass();
                    while (f == null) {
                        try {
                            f = c.getDeclaredField(field.field);
                        } catch (Exception e) {
                            c = c.getSuperclass();
                            if (c.getSimpleName().equals("Object")) {
                                break;
                            }
                        }
                    }

                    // If the appropriate class is found, set the property on the class.
                    if (f != null) {
                        try {
                            f.setAccessible(true);
                            f.set(model, value);
                        } catch (Exception e) {
                            System.out.println("Failed to set field " + field.field);
                        }
                    }
                }
                models.add(model);
            }
        } catch (SQLException ex) {
            // Display error information
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // Cleanup Variables
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }

                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }

                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                }

                conn = null;
            }
        }

        // Return the results
        return models;
    }

    // Find row by ID
    protected static Model find(Model instance, int ID) {
        return findByField(instance, "ID", Integer.toString(ID)).get(0);
    }

    // Get all rows from table
    protected static ArrayList<Model> all(Model instance) {
        // Init Variables
        ArrayList<Model> products = new ArrayList<Model>();
        Class<?> c = null;

        // Get Results from DB
        Map<String, Object> results = Main.db.query(String.format("SELECT * FROM %s ", instance.table));

        ResultSet rs = (ResultSet) results.get("resultset");
        Statement stmt = (Statement) results.get("statement");
        Connection conn = (Connection) results.get("connection");

        try {
            // Iterate over each row and each property in the result set
            while (rs.next()) {
                Model b = null;
                try {
                    b = (Model) instance.clone();
                } catch (Exception e) {
                    System.out.println("Clone error");
                    return null;
                }

                for (int i = 0; i < b.fields.size(); i++) {
                    DBField field = b.fields.get(i);
                    String type = field.type;
                    Field f = null;
                    Object value = null;

                    // Get the type of the property
                    switch (type) {
                        case "int":
                            value = rs.getInt(field.field);
                            break;
                        case "date":
                            value = rs.getDate(field.field);
                            break;
                        case "text":
                        case "longtext":
                            value = rs.getString(field.field);
                            break;
                        default:
                            break;
                    }

                    // Determine which class the property belongs to
                    c = instance.getClass();
                    while (f == null) {
                        try {
                            f = c.getDeclaredField(field.field);
                        } catch (Exception e) {
                            c = c.getSuperclass();
                            if (c.getSimpleName().equals("Object")) {
                                break;
                            }
                        }
                    }

                    // If the appropriate class is found, set the property on the class.
                    if (f != null) {
                        try {
                            f.setAccessible(true);
                            f.set(b, value);
                        } catch (Exception e) {
                            System.out.println("Failed to set field " + field.field);
                        }
                    }
                }
                products.add(b);
            }
        } catch (SQLException ex) {
            // Display error information
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // Cleanup Variables
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }

                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }

                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                }

                conn = null;
            }
        }

        // Return the results
        return products;
    }

    // Create DB table if necessary
    public void createTable() {
        // Create Query
        String query = String.format("CREATE TABLE IF NOT EXISTS %s (", this.table);

        // Add fields to query
        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            // If field is ID make sure it is primary key.
            if (field.field == "ID") {
                query += String.format("%s %s AUTO_INCREMENT PRIMARY KEY", field.field, field.type);
            } else {
                query += String.format("%s %s", field.field, field.type);
            }

            // Add comma if not last field
            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        // Close query
        query += ");";

        // Execute query
        Main.db.update(query);
    }

    // Insert row into DB table
    public void dbInsert() {
        // Create Query
        String query = String.format("INSERT INTO %s (", this.table);

        // Add fields to query
        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            query += field.field;

            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        query += ") VALUES (";

        Class<?> c = null;

        // Add values to query
        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                query += "NULL";
            } else {
                Field f = null;

                // Determine which class the property belongs to
                c = this.getClass();
                while (f == null) {
                    try {
                        f = c.getDeclaredField(field.field);
                    } catch (Exception e) {
                        c = c.getSuperclass();
                        if (c.getSimpleName().equals("Object")) {
                            break;
                        }
                    }
                }

                // If the appropriate class is found, add the value to the query
                if (f != null) {
                    try {
                        Object value = f.get(this);
                        if (value != null) {
                            query += String.format("'%s'", value.toString());
                        } else {
                            query += "NULL";
                        }
                    } catch (Exception e) {
                    }
                }
            }

            // Add comma if not last field
            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        // Close query
        query += ");";

        // Execute query
        Main.db.update(query);
    }

    // Update row in DB table
    public void save() {
        // Create Query
        String query = String.format("UPDATE %s SET ", this.table);

        Class<?> c = null;

        // Add field and values to query
        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            // Ignore ID
            if (field.field == "ID") {
                continue;
            } else {
                Field f = null;

                // Determine which class the property belongs to
                c = this.getClass();
                while (f == null) {
                    try {
                        f = c.getDeclaredField(field.field);
                    } catch (Exception e) {
                        c = c.getSuperclass();
                        if (c.getSimpleName().equals("Object")) {
                            break;
                        }
                    }
                }

                // If the appropriate class is found, add the field and value to the query
                if (f != null) {
                    try {
                        Object value = f.get(this);
                        if (value != null) {
                            query += String.format("%s = '%s'", field.field, value.toString());
                        } else {
                            query += String.format("%s = NULL", field.field);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            // Add comma if not last field
            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        // Close query
        query += String.format(" WHERE ID = %d;", this.ID);

        // Execute query
        Main.db.update(query);
    }

    // Get all the values for an object
    public String[] allProperties() {
        String[] properties = new String[fields.size()];

        Class<?> c = null;

        // Get all the values for each field
        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);
            Field f = null;

            // Determine which class the property belongs to
            c = this.getClass();
            while (f == null) {
                try {
                    f = c.getDeclaredField(field.field);
                } catch (Exception e) {
                    c = c.getSuperclass();
                    if (c.getSimpleName().equals("Object")) {
                        break;
                    }
                }
            }

            // If the appropriate class is found, add the value to the array
            if (f != null) {
                try {
                    f.setAccessible(true);
                    properties[i] = f.get(this).toString();
                } catch (Exception e) {
                }
            }
        }

        // Return the array
        return properties;
    }

    // Getters
    public Integer getID() {
        return ID;
    }
}
