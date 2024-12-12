package main;

import java.sql.*;

public class DBManager {
    static final String username = "root";

    public DBManager(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/SurveillanceSystem", username, Creds.password);
            System.out.println("Connection Established successfully !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        DBManager db = new DBManager();

    }

}
