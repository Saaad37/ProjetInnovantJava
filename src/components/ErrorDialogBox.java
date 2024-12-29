package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class ErrorDialogBox extends JDialog {

    public ErrorDialogBox(String ErrorMessage){
        SoundSystem soundSys = new SoundSystem("/assets/ErrorSound.wav");
        soundSys.playSound(0);
        JLabel errorLabel = new JLabel(ErrorMessage, SwingConstants.CENTER);
        Font font = new Font("Helvetica", Font.PLAIN, 20);
        errorLabel.setFont(font);
        Image icon = new ImageIcon(this.getClass().getResource("/assets/errorIcon.png")).getImage();
        Button b = new Button(new Rectangle(45, 80, 80, 30), "Ok", Color.WHITE, Color.LIGHT_GRAY);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                soundSys.stopSound();
                dispose();
            }
        });

        int textWidth = (int) font.getStringBounds(ErrorMessage, new FontRenderContext(new AffineTransform(), true, true)).getWidth();

        this.setTitle("Error");
        this.setSize(new Dimension(textWidth + 50, 120));
        this.add(errorLabel);
        this.add(b);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(icon);
        this.setLayout(new GridLayout(2, 1));
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
}
