package components;

import java.awt.Rectangle;

import javax.swing.JButton;

public class Button extends JButton{

    public Button(Rectangle bounds, String buttonName){

        this.setBounds(bounds);
        this.setText(buttonName);
        this.setLayout(null);
    }
}