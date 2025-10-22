package com.inventory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DB helper to obtain SQLite connection and initialize schema.
 */
public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:inventory.db";

    static {
        // Attempt to load driver (optional for newer JVMs)
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // ignore, driver should be on classpath via dependency
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Create tables if they do not exist.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "category TEXT,"
                + "price REAL NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "description TEXT"
                + ");";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}