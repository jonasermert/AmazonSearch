package com.jonasermert.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;


public class DatabaseConnection {
    protected static Connection initializeDatabase()
            throws SQLException, ClassNotFoundException
    {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("Path to config.properties");

            props.load(in);
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String driver = props.getProperty("jdbc.driver");
        if (driver != null) {
            Class.forName(driver) ;
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        Connection con = DriverManager.getConnection(url, username, password);

        return con;
    }
}

