package main;

import components.Button;
import components.ErrorDialogBox;
import components.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
    JScrollPane jScrollPane1;
    ArrayList<String[]> totalData;
    Window f;
    components.Button addButton;
    components.Button delButton;
    components.Button updateButton;
    components.Button searchButton;
    JComboBox<String> idsComboBox;
    boolean isProfileOpened;


    public DBManager(WindowPanel wp){
        this.wp = wp;
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
        initComponents();
        updateTable();
    }


    private void updateTable(){
        try {
            int c;
            PreparedStatement pst = con.prepareStatement("SELECT * FROM " + tableName + " ORDER BY last_name ASC");
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
                f.repaint();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void initComponents() {

        f = new Window("Users");
        isProfileOpened = true;
        jScrollPane1 = new JScrollPane();
        table = new JTable();
        idsComboBox = new JComboBox<>();
        JLabel firstNameTxt = new JLabel("First name:");
        JLabel lastNameTxt = new JLabel("Last name:");
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        addButton = new Button(new Rectangle(40, 100, 70, 35), "Add");


        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {}

            @Override
            public void windowClosing(WindowEvent e) {
                isProfileOpened = false;
                e.getWindow().dispose();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {}
            @Override
            public void windowIconified(WindowEvent windowEvent) {}
            @Override
            public void windowDeiconified(WindowEvent windowEvent) {}
            @Override
            public void windowActivated(WindowEvent windowEvent) {}
            @Override
            public void windowDeactivated(WindowEvent windowEvent) {}
        });
        f.setPreferredSize(new Dimension(500, 480));
        f.setAlwaysOnTop(true);
        System.out.println(isProfileOpened);


        idsComboBox.setBounds(new Rectangle(350, 20, 100, 35));
        firstNameTxt.setBounds(new Rectangle(10, 20, 180, 35));
        lastNameTxt.setBounds(new Rectangle(10, 60, 180, 35));
        firstNameField.setBounds(new Rectangle(100, 20, 200 ,35));
        lastNameField.setBounds(new Rectangle(100, 60, 200, 35));

        loadIds();

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                if(firstName.isBlank() || lastName.isBlank()){
                    ErrorDialogBox d = new ErrorDialogBox("You must enter a first name and a last name");
                    d.setVisible(true);
                    firstNameField.setText("");
                    lastNameField.setText("");
                }else{
                    insertUser(generateUUID(), firstName, lastName);
                    System.out.println("User added sucessefully !");
                    firstNameField.setText("");
                    lastNameField.setText("");
                    loadIds();
                    updateTable();
                }

            }
        });

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
        f.add(addButton);
        f.add(idsComboBox);

        f.pack();
    }


    public void loadIds(){
        ArrayList<Integer> ids = getAllids();
        System.out.println(ids.toString());
        idsComboBox.removeAllItems();
        idsComboBox.addItem("id");
        for(int i = 0; i < ids.size() - 1;i++){
            idsComboBox.addItem(ids.get(i).toString());
        }

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


    public boolean isProfilesOpen(){
        return isProfileOpened;
    }

    private ArrayList<Integer> getAllids(){
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid FROM " + tableName + " ORDER BY last_name ASC");
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " ORDER BY last_name ASC");
            System.out.println("+----------------------+------------------------------+--------------------\n");
            while(rs.next()){
                System.out.println("        | uuid | " + rs.getInt("uuid"));
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
