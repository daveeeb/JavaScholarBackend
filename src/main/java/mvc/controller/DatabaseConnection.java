package mvc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Javier David Barraza Ureña ID 3303
 * Manages the connection to the MySQL database.
 */

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/scholar_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Establishes and returns a database connection.
     *
     * @return A valid JDBC Connection.
     * @throws SQLException if the connection fails.
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
