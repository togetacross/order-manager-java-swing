package com.mycompany.DAO;

import com.mycompany.CustomExceptions.CustomDatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create Database connection
 */
public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/offer";
    private static final String USER = "root";
    private static final String PASSWORD  = "1234";

    /**
     * Create database connection
     *
     * @return database connection
     * @throws CustomDatabaseException if connection failed
     */
    public static Connection getConnection() throws CustomDatabaseException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

}
