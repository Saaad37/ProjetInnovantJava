package components;

import javax.swing.*;
import java.awt.*;

public class EnterCredsBox extends JFrame {

    public EnterCredsBox(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 480));
        this.setLocationRelativeTo(null);
        this.setTitle("Enter Credentials");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension entryTextDim = new Dimension(120, 35);

        JLabel l = new JLabel("Enter your MySQL username and password", SwingConstants.CENTER);
        l.setBounds(55, 25, 600, 50);
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
