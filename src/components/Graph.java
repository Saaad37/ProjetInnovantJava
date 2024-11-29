package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.*;

public class Graph extends JPanel {

    private final int winWidth = 500;
    private final int winHeight = 500;
    private final int marg = 20;
    private final int labelMarg = 10;
    private final int margTot = marg + labelMarg;
    private final Color lineColor = new Color(15, 60, 54);
    private final int arrowWidth = 5;

    BasicStroke defaultStroke = new BasicStroke(2.25f);
    Point origin = new Point(margTot, winHeight - margTot);
    Point maxX = new Point(winWidth - margTot, winHeight - margTot);
    Point maxY = new Point(margTot, margTot);

    String labelAtX;
    String labelAtY;
    int index;
    ArrayList<Double[]> vals;
    ArrayList<Point> points = new ArrayList<>();

    public Graph(String labelAtX, String labelAtY, ArrayList<Double[]> vals, int index) {
        this.setPreferredSize(new Dimension(winHeight, winWidth));
        this.setFont(new Font("Cambira Math", Font.BOLD, 16));
        this.labelAtX = labelAtX;
        this.labelAtY = labelAtY;
        this.vals = vals;
        this.index = index;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.setStroke(defaultStroke);

        // Drawing x Axis
        g2.drawLine((int) origin.getX(), (int) origin.getY(), (int) maxX.getX(), (int) maxX.getY());
        // Drawing y Axis
        g2.drawLine((int) origin.getX(), (int) origin.getY(), (int) maxY.getX(), (int) maxY.getY());

        // Drawing the arrows on X and Y axes
        g2.drawLine(maxX.x, maxX.y, maxX.x - arrowWidth, maxX.y + arrowWidth);
        g2.drawLine(maxX.x, maxX.y, maxX.x - arrowWidth, maxX.y - arrowWidth);

        g2.drawLine(maxY.x, maxY.y, maxY.x - arrowWidth, maxY.y + arrowWidth);
        g2.drawLine(maxY.x, maxY.y, maxY.x + arrowWidth, maxY.y + arrowWidth);

        // Drawing Text
        g2.drawString(labelAtX, maxX.x + labelMarg, maxX.y + labelMarg);
        g2.drawString(labelAtY, maxY.x + labelMarg, maxY.y);

    }

}