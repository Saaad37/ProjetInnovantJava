package components;

import main.WindowPanel;

import javax.swing.*;
import java.awt.*;

public class EnterCredsBox extends JFrame {

    JTextField username;
    JPasswordField password;
    ImageIcon user = new ImageIcon(this.getClass().getResource("/assets/user.png"));

    public EnterCredsBox(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 480));
        this.setLocationRelativeTo(null);
        this.setTitle("Enter Credentials");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        user.setImage(WindowPanel.resizeImage(user, 25, 25).getImage());
        JLabel userL = new JLabel(user);
        userL.setBounds(85, 125, 25, 25);

        JLabel l = new JLabel("Enter your MySQL username and password", SwingConstants.CENTER);
        l.setBounds(55, 25, 600, 50);
        l.setFont(new Font("Helvetica", Font.PLAIN, 26));

        username = new JTextField();
        username.setFont(new Font("Arial", Font.PLAIN, 20));
        username.setBounds(120, 120, 500, 35);

        password = new JPasswordField();
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setBounds(120, 200, 500, 35);


        this.add(l);
        this.add(userL);
        this.add(username);
        this.add(password);

        this.pack();
    }


    public String getUsername(){
        return username.getText();
    }

    public char[] getPassword(){
        return password.getPassword();
    }

    public static void main(String[] args) {
        EnterCredsBox e = new EnterCredsBox();
        e.setVisible(true);
    }

}
