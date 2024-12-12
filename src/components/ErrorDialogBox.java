package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ErrorDialogBox extends JDialog {

    public ErrorDialogBox(String ErrorMessage){
        JLabel errorLabel = new JLabel(ErrorMessage, SwingConstants.CENTER);
        errorLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        Image icon = new ImageIcon(this.getClass().getResource("/assets/errorIcon.png")).getImage();
        Button b = new Button(new Rectangle(45, 80, 80, 30), "Ok");

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });

        this.setTitle("Error");
        this.setSize(new Dimension(250, 120));
        this.add(errorLabel);
        this.add(b);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(icon);
        this.setLayout(new GridLayout(2, 1));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
}
