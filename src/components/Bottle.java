package components;

import main.WindowPanel;

import javax.swing.*;
import java.awt.*;

public class Bottle {
    private JFrame root;
    private Rectangle BotBounds;
    private Color col;
    private int originX;
    private int originY;
    private int width;
    private int height;
    private int cap;
    private WindowPanel wp;
    private double Penv;


    public Bottle(JFrame f, Rectangle bounds, Color col, int capacityL, WindowPanel wp){
        this.root = f;
        this.BotBounds = bounds;
        this.col = col;
        this.originX = (int) this.BotBounds.getX();
        this.originY = (int) this.BotBounds.getY();
        this.width = (int) this.BotBounds.getWidth();
        this.height = (int) this.BotBounds.getHeight();
        this.cap = capacityL;
        this.wp = wp;
        this.Penv = wp.getSavedValues().getLast()[1];
        this.draw((Graphics2D) root.getGraphics());
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.BLACK);
        g2.draw(BotBounds);
//        g2.drawRect(this.width/2 - this.width/10, );

        root.paintAll((Graphics) g2);
    }
}
