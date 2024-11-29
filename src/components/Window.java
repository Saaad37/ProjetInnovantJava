package components;

import javax.swing.*;

public class Window extends JFrame{

    public Window(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Surveillance system");
        this.setFocusable(true);
        this.setResizable(false);
        this.setVisible(true);
    }
}
