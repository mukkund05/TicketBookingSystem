package com.hexaware.util;

import java.io.IOException;
import java.util.Properties;


public class DBPropertyUtil {
    
    public static String getConnectionString(String propertyFileName) {
        Properties props = new Properties();
        try (var input = DBPropertyUtil.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            if (input == null) {
                throw new IOException("Property file '" + propertyFileName + "' not found in the classpath. Ensure it is placed in src/main/resources or src.");
            }
            props.load(input);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            if (url == null || user == null || password == null) {
                throw new IOException("Missing required properties in '" + propertyFileName + "'. Ensure db.url, db.user, and db.password are defined.");
            }
            return url + "?user=" + user + "&password=" + password;
        } catch (IOException e) {
            throw new RuntimeException("Error reading database properties: " + e.getMessage(), e);
        }
    }
}