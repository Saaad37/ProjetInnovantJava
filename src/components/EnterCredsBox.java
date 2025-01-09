package components;

import main.DBManager;
import main.WindowPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class EnterCredsBox extends JFrame {

    JTextField username;
    JPasswordField password;
    ImageIcon user = new ImageIcon(this.getClass().getResource("/assets/user.png"));
    ImageIcon pass = new ImageIcon(this.getClass().getResource("/assets/password.png"));
    components.Button b;
    String root;
    String encryptedPassword;
    JFrame f = this;

    public EnterCredsBox(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 420));
        this.setLocationRelativeTo(null);
        this.setTitle("Enter Credentials");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        b = new Button(new Rectangle(300, 320, 120, 50), "Enter", Color.WHITE
                ,Color.LIGHT_GRAY);

        user.setImage(WindowPanel.resizeImage(user, 25, 25).getImage());
        JLabel userL = new JLabel(user);
        userL.setBounds(85, 125, 25, 25);

        pass.setImage(WindowPanel.resizeImage(pass, 25, 25).getImage());
        JLabel passL = new JLabel(pass);
        passL.setBounds(85, 205, 25, 25);

        JLabel l = new JLabel("Enter your MySQL username and password", SwingConstants.CENTER);
        l.setBounds(55, 25, 600, 50);
        l.setFont(new Font("Helvetica", Font.PLAIN, 26));

        username = new JTextField();
        username.setFont(new Font("Arial", Font.PLAIN, 20));
        username.setBounds(120, 120, 500, 35);

        password = new JPasswordField();
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setBounds(120, 200, 500, 35);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setRoot(username.getText());
                System.out.println("root: " + getRoot());
                System.out.println("password: " + getPassword());
                if(DBManager.tryConn(getRoot(), getPassword())){
                    ErrorDialogBox d = new ErrorDialogBox("Connected sucessfully");
                    d.setVisible(true);
                }else{
                    ErrorDialogBox e = new ErrorDialogBox("Cannot connect.");
                    e.setVisible(true);
                    try {
                        e.wait(2500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    e.dispose();
                    username.setText("");
                    password.setText("");
                }
            }
        });


        this.add(l);
        this.add(userL);
        this.add(passL);
        this.add(username);
        this.add(password);
        this.add(b);

        this.pack();
    }


    public String getPassword(){return new String(password.getPassword());}

    public String getRoot(){return root;}

    public void setRoot(String root) {this.root = root;}
}
