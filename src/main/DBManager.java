package main;

import components.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JTextField firstNameField;
    JTextField lastNameField;
    String[] columnNames;
    ArrayList<String[]> totalData;
    Window f;


    public DBManager(WindowPanel wp){
        this.wp = wp;
        columnNames = new String[] {"UUID", "First Name", "Last Name", "Date Created", "Limit Depth"};
        totalData = new ArrayList<>();

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/SurveillanceSystem", username, Creds.password);
            System.out.println("Connection Established successfully !");
            fetchUsers();
            printAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchUsers(){
        try {
            initComponents();

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

            f.pack();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAllUsersWindow(){

    }


    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        table = new JTable();
        JLabel firstNameTxt = new JLabel("First name:");
        JLabel lastNameTxt = new JLabel("Last name:");
        f = new Window("Users");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setPreferredSize(new Dimension(500, 480));
        firstNameField= new JTextField();
        lastNameField = new JTextField();

        firstNameTxt.setBounds(new Rectangle(10, 20, 180, 35));
        lastNameTxt.setBounds(new Rectangle(10, 60, 180, 35));
        firstNameField.setBounds(new Rectangle(100, 20, 380 ,35));
        lastNameField.setBounds(new Rectangle(100, 60, 380, 35));


        table.setModel(new DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "UUID", "First Name", "Last Name", "Date Created", "Max Depth"
                }
        ){
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        table.setColumnSelectionAllowed(true);
        table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        table.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
        table.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        GroupLayout layout = new GroupLayout(f.getContentPane());
        f.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(132, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
        );

        f.add(firstNameField);
        f.add(lastNameField);
        f.add(firstNameTxt);
        f.add(lastNameTxt);

        f.pack();
    }
    
    public int getMaxDepth(int uuid){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT maxDepth FROM users WHERE uuid=" + uuid);
            int maxDepth = 0;
            while(rs.next()){
             maxDepth = rs.getInt("maxDepth");
            }
            return maxDepth;
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
            stmt.close();
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
            stmt.close();
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
            // INSERT INTO users(uuid, first_name, last_name) VALUES(uuid, 'first_name','last_name');
            stmt.close();
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    private void changeMaxDepth(int uuid ,double maxDepth){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE " + tableName + " SET maxDepth=" + maxDepth + " WHERE uuid="+ uuid  );
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
