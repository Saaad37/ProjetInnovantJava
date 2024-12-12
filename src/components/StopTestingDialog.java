package components;

import main.WindowPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StopTestingDialog extends JDialog {

    public StopTestingDialog(String mess, WindowPanel wp){
        ArrayList<Double> arr = wp.getMaxDepthTest();
        JLabel errorLabel = new JLabel(mess, SwingConstants.CENTER);
        errorLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        Image icon = new ImageIcon(this.getClass().getResource("/assets/errorIcon.png")).getImage();
        Button abort = new Button(new Rectangle(45, 80, 80, 30), "Abort");
        Button lose = new Button(null, "I am Sure");

        abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });

        lose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                arr.clear();
                wp.setTesting(false);
                dispose();
            }
        });

        this.setTitle("Error");
        this.setSize(new Dimension(250, 120));
        this.add(errorLabel);
        this.add(abort);
        this.add(lose);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(icon);
        this.setLayout(new GridLayout(2, 2));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }


}
