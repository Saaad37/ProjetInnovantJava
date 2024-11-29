package components;

import java.awt.Dimension;

import javax.swing.*;

public class Graph extends JPanel {

    private final int winWidth = 500;
    private final int winHeight = 500;

    public Graph() {
        this.setPreferredSize(new Dimension(winHeight, winWidth));
    }

}