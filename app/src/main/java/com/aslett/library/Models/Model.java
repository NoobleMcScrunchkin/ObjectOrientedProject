package com.aslett.library.Models;

import com.aslett.library.Main;
import com.aslett.library.Utils.DBField;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Model implements Cloneable {
    protected String table = "";

    protected ArrayList<DBField> fields = new ArrayList<DBField>();

    protected int ID = 0;

    public Model() {
        fields.add(new DBField("ID", "int"));
    }

    protected static Object findByField(Model instance, String dbfield, String dbvalue) {
        Model product = instance;
        Class<?> c = null;

        ResultSet rs = Main.db.query(String.format("SELECT * FROM %s WHERE %s = '%s'", product.table, dbfield, dbvalue));

        try {
            while (rs.next()) {
                for (int i = 0; i < product.fields.size(); i++) {
                    DBField field = product.fields.get(i);
                    String type = field.type;
                    Field f = null;
                    Object value = null;

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

                    if (f != null) {
                        try {
                            f.setAccessible(true);
                            f.set(product, value);
                        } catch (Exception e) {
                            System.out.println("Failed to set field " + field.field);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }

                rs = null;
            }
        }
        return product;
    }

    protected static Object find(Model instance, int ID) {
        return findByField(instance, "ID", Integer.toString(ID));
    }

    protected static ArrayList<Model> all(Model instance) {
        ArrayList<Model> products = new ArrayList<Model>();
        Class<?> c = null;

        ResultSet rs = Main.db.query(String.format("SELECT * FROM %s ", instance.table));

        try {
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
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }

                rs = null;
            }
        }

        return products;
    }

    public void createTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s (", this.table);

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                query += String.format("%s %s AUTO_INCREMENT PRIMARY KEY", field.field, field.type);
            } else {
                query += String.format("%s %s", field.field, field.type);
            }

            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        query += ");";

        Main.db.update(query);
    }

    public void dbInsert() {
        String query = String.format("INSERT INTO %s (", this.table);

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            query += field.field;

            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        query += ") VALUES (";

        Class<?> c = null;

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                query += "NULL";
            } else {
                Field f = null;

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

                if (f != null) {
                    try {
                        query += String.format("'%s'", f.get(this).toString());
                    } catch (Exception e) {
                    }
                }
            }

            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        query += ");";

        Main.db.update(query);
    }

    public void save() {
        String query = String.format("UPDATE %s SET ", this.table);

        Class<?> c = null;

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                continue;
            } else {
                Field f = null;

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

                if (f != null) {
                    try {
                        query += String.format("%s = '%s'", field.field, f.get(this).toString());
                    } catch (Exception e) {
                    }
                }
            }

            if (i < fields.size() - 1) {
                query += ", ";
            }
        }

        query += String.format(" WHERE ID = %d;", this.ID);

        Main.db.update(query);
    }

    public Integer getID() {
        return ID;
    }

    public String[] allProperties() {
        String[] properties = new String[fields.size()];

        Class<?> c = null;

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);
            Field f = null;

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

            if (f != null) {
                try {
                    f.setAccessible(true);
                    properties[i] = f.get(this).toString();
                } catch (Exception e) {
                }
            }
        }

        return properties;
    }
}
