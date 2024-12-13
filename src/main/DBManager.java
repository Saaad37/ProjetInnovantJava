package main;

import components.Window;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DBManager {
    static final String username = "root";
    static final String tableName = "users";
    Connection con;
    WindowPanel wp;
    JTable table;
    Font font = new Font("Helvetica", Font.PLAIN, 20);
    String[] columnNames;
    ArrayList<String[]> totalData;


    public DBManager(WindowPanel wp){
        this.wp = wp;
        columnNames = new String[] {"UUID", "First Name", "Last Name", "Date Created", "Limit Depth"};
        totalData = new ArrayList<>();
        showAllUsersWindow();
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/SurveillanceSystem", username, Creds.password);
            System.out.println("Connection Established successfully !");
            printAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchUsers(){
        try {
            int c;
            PreparedStatement pst = con.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            DefaultTableModel dtm = (DefaultTableModel)  table.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                String[] data = new String[] {};
                for(int i = 0; i < rsmd.getColumnCount();i++){
                    data = (new String[] {rs.getString("uuid"), rs.getString("first_name"), rs.getString("last_name")
                    , rs.getString("profile_age"), rs.getString("maxDepth")});
                }
                totalData.add(data);
                dtm.addRow(data);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAllUsersWindow(){
        Window f = new Window("Users");
        table = new JTable();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setPreferredSize(new Dimension(720, 500));
        f.setBackground(Color.gray);
        f.add(table);



        f.pack();

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
                System.out.println("| uuid | " + rs.getInt("uuid"));
                System.out.println("        | first_name | " +  rs.getString("first_name"));
                System.out.println("        | last_name | " + rs.getString("last_name") );
            }
            System.out.println("+------------------------+------------------------+--------------------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int generateUUID(){
        Random rand = new Random();
        int uuid_len = rand.nextInt(1, 4);
        String uuid = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)).substring(5, 10);
        int uuidInt = Integer.parseInt(uuid);
        if(getAllids().contains(uuidInt)){
            generateUUID();
        }
        return uuidInt;
    }

    private void insertUser(int uuid, String firstName, String lastName){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO " + tableName + "(uuid, first_name, last_name) " +
                    "VALUES(" + uuid + ", '" + firstName + "', '" + lastName + "')");
            // INSERT INTO users(uuid, first_name, last_name) VALUES(9457, ''"
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeMaxDepth(int uuid ,double maxDepth){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE " + tableName + " SET maxDepth=" + maxDepth + " WHERE uuid="+ uuid  );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
