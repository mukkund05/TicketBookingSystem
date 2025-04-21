package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
  
    public static Connection getConnection() throws SQLException {
        String connectionString = DBPropertyUtil.getConnectionString("db.properties");
        return DriverManager.getConnection(connectionString);
    }
}



