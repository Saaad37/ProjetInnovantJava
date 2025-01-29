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
    private int maxCap;
    private WindowPanel wp;
    private double Penv;
    private double nMol;
    private final double R = 8.314;

    /*
     PV = nRT
     n = V/Vm

     V = m^3
     T = K
     n = mol
     P = Pascal

     T = T(C) + 273.15

     Vm = RT/Penv

     each second cap -= V

    */

    public Bottle(JFrame f, Rectangle bounds, Color col, int capacityL, WindowPanel wp){
        this.root = f;
        this.BotBounds = bounds;
        this.col = col;
        this.originX = (int) this.BotBounds.getX();
        this.originY = (int) this.BotBounds.getY();
        this.width = (int) this.BotBounds.getWidth();
        this.height = (int) this.BotBounds.getHeight();
        this.maxCap = capacityL;
        this.wp = wp;
        this.Penv = wp.getSavedValues().getLast()[1];
        this.draw((Graphics2D) root.getGraphics());
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.BLACK);
        g2.draw(BotBounds);
//        g2.drawRect(this.width/2 - this.width/10, );
        // TODO
        root.paintAll((Graphics) g2);
    }

    public void update(){
        //TODO
    }
}
