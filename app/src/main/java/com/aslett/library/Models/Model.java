package com.aslett.library.Models;

import com.aslett.library.Main;
import com.aslett.library.Utils.DBField;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Model implements Cloneable {
    public String table = "";

    public ArrayList<DBField> fields = new ArrayList<DBField>();

    public int ID = 0;

    public Model() {
        fields.add(new DBField("ID", "int"));
    }

    protected static Object find(Model instance, int ID) {
        Model product = instance;
        Class<?> c = null;

        ResultSet rs = Main.db.query(String.format("SELECT * FROM %s WHERE ID = %d", product.table, ID));

        try {
            while (rs.next()) {
                for (int i = 0; i < product.fields.size(); i++) {
                    DBField field = product.fields.get(i);
                    String type = field.type;
                    Field f = null;
                    switch (type) {
                        case "int":
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
                                    f.set(product, rs.getInt(field.field));
                                } catch (Exception e) {
                                }
                            }
                            break;

                        case "text":
                        case "longtext":
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
                                    f.set(product, rs.getString(field.field));
                                } catch (Exception e) {
                                }
                            }
                            break;

                        default:
                            break;
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

    public static ArrayList<Model> all(Model instance) {
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

                    switch (type) {
                        case "int":
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
                                    f.set(b, rs.getInt(field.field));
                                } catch (Exception e) {
                                }
                            }

                            break;

                        case "text":
                        case "longtext":
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
                                    f.set(b, rs.getString(field.field));
                                } catch (Exception e) {
                                }
                            }

                            break;

                        default:
                            break;
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

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                query += "NULL";
            } else {
                Field f = null;

                try {
                    f = this.getClass().getDeclaredField(field.field);
                } catch (Exception e) {
                    try {
                        f = Model.class.getDeclaredField(field.field);
                    } catch (Exception ex) {
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

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);

            if (field.field == "ID") {
                continue;
            } else {
                Field f = null;

                try {
                    f = this.getClass().getDeclaredField(field.field);
                } catch (Exception e) {
                    try {
                        f = Model.class.getDeclaredField(field.field);
                    } catch (Exception ex) {
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

        for (int i = 0; i < fields.size(); i++) {
            DBField field = fields.get(i);
            Field f = null;
            try {
                f = this.getClass().getDeclaredField(field.field);
            } catch (Exception e) {
                try {
                    f = Model.class.getDeclaredField(field.field);
                } catch (Exception ex) {
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
