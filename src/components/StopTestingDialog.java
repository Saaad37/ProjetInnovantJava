package components;

import main.WindowPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextMeasurer;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class StopTestingDialog extends JDialog {

    public StopTestingDialog(String mess, WindowPanel wp){
        ArrayList<Double> arr = wp.getMaxDepthTest();
        SoundSystem soundSys = new SoundSystem("/assets/ErrorSound.wav");
        JLabel errorLabel = new JLabel(mess, SwingConstants.CENTER);
        Font font = new Font("Helvetica", Font.PLAIN, 20);
        errorLabel.setFont(font);
        Image icon = new ImageIcon(this.getClass().getResource("/assets/errorIcon.png")).getImage();
        Button abort = new Button(new Rectangle(45, 80, 80, 30), "Abort", Color.BLACK, Color.LIGHT_GRAY, 0);
        Button lose = new Button(new Rectangle(99, 99, 99, 99), "I am Sure", Color.BLACK, Color.LIGHT_GRAY,0);
        // The coordinates don't matter because i set up a layout

        soundSys.playSound(0);

        int textWidth = (int) font.getStringBounds(mess, new FontRenderContext(new AffineTransform(), true, true)).getWidth();

        abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                soundSys.stopSound();
                dispose();
            }
        });

        lose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                soundSys.stopSound();
                arr.clear();
                wp.setTesting(false);
                wp.setTestingState();
                dispose();
            }
        });



        this.setTitle("Error");
        this.setSize(new Dimension(textWidth + 50, 120));
        this.add(errorLabel);
        this.add(abort);
        this.add(lose);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setIconImage(icon);
        this.setLayout(new GridLayout(2, 1));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

}
