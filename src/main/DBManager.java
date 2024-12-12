package main;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DBManager {
    static final String username = "root";
    static final String tableName = "users";
    Connection con;
    WindowPanel wp;

    public DBManager(){
//        this.wp = wp;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/SurveillanceSystem", username, Creds.password);
            System.out.println("Connection Established successfully !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Integer> getAllids(){
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid FROM " + tableName);
            while(rs.next()){
                ids.add(rs.getInt("uuid"));
            }
            return ids;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void printAllUsers(){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            System.out.println("+----------------------+------------------------------+--------------------\n");
            while(rs.next()){
                System.out.println("| uuid | " + rs.getInt("uuid") + "\n");
                System.out.println("| first_name | " +  rs.getString("first_name") + "\n");
                System.out.println("| last_name | " + rs.getString("last_name") + "\n");
            }
            System.out.println("+------------------------+------------------------+--------------------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int generateUUID(){
        Random rand = new Random();
        int uuid_len = rand.nextInt(1, 4);
        String uuid = String.valueOf(rand.nextInt(9) * uuid_len);
        int uuidInt = Integer.parseInt(uuid);
        if(getAllids().contains(uuid)){
            generateUUID();
        }
        return uuidInt;
    }

    private void InsertUser(int uuid, String firstName, String lastName){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO " + tableName + "(uuid, first_name, last_name) " +
                    "VALUES(" + uuid + ", '" + firstName + "', '" + lastName + "'");
            // INSERT INTO users(uuid, first_name, last_name) VALUES(9457, ''"
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeMaxDepth(double maxDepth, int uuid){
        try {
            Statement stmt = con.createStatement();
            stmt.executeQuery("UPDATE " + tableName + " SET maxDepth=" + maxDepth + " WHERE uuid="+ uuid  );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        DBManager db = new DBManager();

        db.InsertUser(db.generateUUID(), "Saad", "Norelyaqine");
        System.out.println(db.getAllids().toString());

    }


}
