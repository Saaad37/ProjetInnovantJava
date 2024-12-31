package main;

import components.Button;
import components.ErrorDialogBox;
import components.Window;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DBManager {
    static final String username = "root";
    static final String tableName = "users";
    final Color defaultButCol = Color.WHITE;
    final Color onHoverCol = Color.LIGHT_GRAY;
    final int buttonRadius=0;
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
    components.Button useButton;
    components.Button searchButton;
    JComboBox<String> idsComboBox;
    boolean isProfileOpened;
    KeyListener kl;


    public DBManager(WindowPanel wp){
        this.wp = wp;
        totalData = new ArrayList<>();
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/SurveillanceSystem", username, Creds.password);
            con.setAutoCommit(false);
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


    public void searchedTable(int uuid){
        try {
            int c;
            PreparedStatement pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE uuid=" + uuid + " ORDER BY last_name ASC");
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


    private void searchedTable(String firstName, String lastName){
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE first_name LIKE '%" + firstName +
                    "%' AND last_name LIKE '%" + lastName + "%'");
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


    private void updateTable(){
        try {
            table.setRowSelectionAllowed(false);
            table.setColumnSelectionAllowed(false);
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
                table.setRowSelectionAllowed(true);
                table.setColumnSelectionAllowed(false);
            }
            pst.close();
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
            addButton = new Button(new Rectangle(40, 100, 60, 35), "Add", defaultButCol, onHoverCol, buttonRadius);
            updateButton = new Button(new Rectangle(110, 100, 80, 35), "Update", defaultButCol, onHoverCol, buttonRadius);
            delButton = new Button(new Rectangle(200, 100, 80, 35), "Delete", defaultButCol, onHoverCol, buttonRadius);
            useButton = new Button(new Rectangle(290, 100, 80, 35), "Use", defaultButCol, onHoverCol, buttonRadius);
            searchButton = new Button(new Rectangle(380, 100, 80, 35), "Search", defaultButCol, onHoverCol, buttonRadius);


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
                    }else if(!idsComboBox.getSelectedItem().equals("id")){
                        ErrorDialogBox e = new ErrorDialogBox("Unselect id please.");
                        e.setVisible(true);
                    }else if(table.getSelectedRow() >= 1){
                        ErrorDialogBox e = new ErrorDialogBox("Unselect row and enter first name and last name");
                        e.setVisible(true);
                    }
                    else{
                        insertUser(generateUUID(), firstName, lastName);
                        System.out.println("User added sucessefully !");
                        firstNameField.setText("");
                        lastNameField.setText("");
                        loadIds();
                        updateTable();
                    }
                }
            });

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String selectedId = idsComboBox.getSelectedItem().toString();
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    if(selectedId.equals("id") && firstName.isBlank() && lastName.isBlank() ) {
                        updateTable();
                    }else if(!selectedId.equals("id")){
                        searchedTable(Integer.parseInt(selectedId));
                    }else{
                        searchedTable(firstName, lastName);
                    }
                }
            });

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                        int uuid = getUserId();
                        if(uuid == 0 ) return;
                        System.out.println(uuid);
                        String firstName = firstNameField.getText();
                        String lastName = lastNameField.getText();
                        updateUser(uuid, firstName, lastName);
                        updateTable();

                        firstNameField.setText("");
                        lastNameField.setText("");
                }
            });

            delButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    int uuid = getUserId();
                    if(uuid == 0) return;
                    deleteUser(uuid);
                    updateTable();
                    loadIds();

                    firstNameField.setText("");
                    lastNameField.setText("");
                }
            });

            useButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    int uuid = getUserId();
                    if(uuid == 0) return;
                    setUserID(uuid);
                    setMaxValN2(Integer.parseInt(getProfile(uuid).getLast()));
                    wp.clearSavedDepth();
                    wp.setTesting(false);
                    System.out.println(getMaxDepth() + " " + getMaxValN2());
                    isProfileOpened = false;
                    f.dispose();
                }
            });

            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if(!e.getValueIsAdjusting()){
                        int r = table.getSelectedRow();
                            if(r >= 0){
                                System.out.println(Integer.parseInt(table.getValueAt(r, 0).toString()));
                                int uuid = Integer.parseInt(table.getValueAt(r, 0).toString());

                                firstNameField.setText(getProfile(uuid).get(0));
                                lastNameField.setText(getProfile(uuid).get(1));
                        }

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

            table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            table.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
            table.setFillsViewportHeight(true);
            jScrollPane1.setViewportView(table);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


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
            f.add(updateButton);
            f.add(delButton);
            f.add(useButton);
            f.add(searchButton);

            f.pack();
    }

    private void deleteUser(int uuid) {
        try {
            PreparedStatement pst = con.prepareStatement("DELETE FROM " + tableName + " WHERE uuid=" + uuid);
            pst.executeUpdate();
            con.commit();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void loadIds(){
        ArrayList<Integer> ids = getAllids();
        System.out.println(ids.toString());
        idsComboBox.removeAllItems();
        idsComboBox.addItem("id");
        for(int i = 0; i < ids.size();i++){
            idsComboBox.addItem(ids.get(i).toString());
        }

    }

    public void unselectRow(){
        kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int code = e.getKeyCode();
                if(code == KeyEvent.VK_ESCAPE || table.getSelectedRow() < 0){
                    table.setSelectionModel(null);
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        };
    }
    
    public ArrayList<String> getProfile(int uuid){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT first_name, last_name, profile_age, maxDepth FROM " + tableName +
                    " WHERE uuid=" + uuid);
            ArrayList<String> res = new ArrayList<>();
            String firstName = "";
            String lastName = "";
            String dateCreated = "";
            String maxDepth = "";
            while(rs.next()){
             firstName = rs.getString("first_name");
             lastName = rs.getString("last_name");
             dateCreated = rs.getString("profile_age");
             maxDepth = rs.getString("maxDepth");
             res.add(firstName);
             res.add(lastName);
             res.add(dateCreated);
             res.add(maxDepth);
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUser(int uuid, String firstName, String lastName){
        try {
            PreparedStatement pst  = con.prepareStatement("UPDATE users SET first_name='" + firstName + "', last_name='" + lastName + "' WHERE uuid=" + uuid);
            // UPDATE users SET first_name='firstName', last_name='lastName' WHERE uuid=uuid;
            pst.executeUpdate();
            con.commit();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    public boolean isProfilesOpen(){
        return isProfileOpened;
    }

    public void setProfileOpened(boolean profileOpened) {
        wp.profileSelected = profileOpened;
    }

    public void setMaxValN2(int maxDepth){
        double Pmax = ((wp.rhoSaltedWater * wp.g * maxDepth) + wp.P0Pasc) * Math.pow(10, -5);
        setMaxDepth(maxDepth);
        wp.maxValN2 = (Pmax * wp.XN2) * 750;
    }

    public void setMaxDepth(int maxDepth){
        wp.maxDepth = maxDepth;
    }

    public double getMaxDepth(){
        return wp.maxDepth;
    }

    public double getMaxValN2(){
        return wp.maxValN2;
    }

    public void setUserID(int uuid){
        wp.userId = uuid;
        setProfileOpened(true);
    }

    public void changeMaxDepth(int uuid ,double maxDepth){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE " + tableName + " SET maxDepth=" + maxDepth + " WHERE uuid="+ uuid  );
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private int getUserId(){
        if(table.getSelectedRow() < 0){
            ErrorDialogBox e = new ErrorDialogBox("Please Select a user..");
            e.setVisible(true);
            return 0;
        }else{
            return Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        }
    }


}
