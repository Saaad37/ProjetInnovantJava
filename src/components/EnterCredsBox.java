package components;

import javax.swing.*;
import java.awt.*;

public class EnterCredsBox extends JFrame {

    public EnterCredsBox(){
        this.setLayout(new GridLayout(4, 1, 20, 20));
        this.setPreferredSize(new Dimension(720, 480));
        this.setLocationRelativeTo(null);
        this.setTitle("Enter Credentials");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel l = new JLabel("Enter your MySQL username and password", SwingConstants.CENTER);
        l.setFont(new Font("Helvetica", Font.PLAIN, 26));

        JTextField username = new JTextField();



        this.add(l);
        this.add(username);
        this.pack();
    }


    public String getUsername(){
        return "";
    }

    public String getPassword(){
        return "";
    }

    public static void main(String[] args) {
        EnterCredsBox e = new EnterCredsBox();
        e.setVisible(true);
    }

}
