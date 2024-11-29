package components;

import main.WindowPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graph extends JPanel implements Runnable {

    Window w;

    private final int winHeight = 500;
    private final int winWidth = 500;
    private final int arrowWidth = 5;
    private final int labelMarg = 10;
    private final int marg = 20;
    private final int margTot = marg + labelMarg;
    private final Color pointColors = new Color(150, 200, 80);
    private final int graphWidth = winWidth - 2 * marg;
    private final int graphHeight = winHeight - 2 * marg;

    int index;

    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Double[]> vals;
    Point origin = new Point(margTot, (winHeight - margTot));
    Point maxY = new Point(margTot, margTot);
    Point maxX = new Point(winWidth - margTot, winHeight - margTot);

    String labelAtY;
    String labelAtX;

    Thread thread;

    public Graph(String labelAtX, String labelAtY, ArrayList<Double[]> vals, int index) {
        this.labelAtX = labelAtX;
        this.labelAtY = labelAtY;
        this.vals = vals;
        this.index = index;
        setVals();
        this.setPreferredSize(new Dimension(winWidth, winHeight));
        this.setFont(new Font("Cambria Math", Font.BOLD, 20));
    }

    public void setVals() {
        for (int i = 0; i < vals.size(); i++) {
            points.add(new Point(Integer.parseInt(vals.get(i)[index].toString()), i));
        }
    }

    public void startThread() {
        thread = new Thread();
        thread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        // Drawing the x y axis
        g2.drawLine(origin.x, origin.y, margTot, margTot);
        g2.drawLine(origin.x, origin.y, winWidth - margTot, winHeight - margTot);

        // Drawing the arrow at the extremes of the axes

        // Arrow at X axis
        g2.drawLine(maxX.x, maxX.y, maxX.x - arrowWidth, maxX.y + arrowWidth);
        g2.drawLine(maxX.x, maxX.y, maxX.x - arrowWidth, maxX.y - arrowWidth);

        // Arrow at Y axis
        g2.drawLine(maxY.x, maxY.y, maxY.x - arrowWidth, maxY.y + arrowWidth);
        g2.drawLine(maxY.x, maxY.y, maxY.x + arrowWidth, maxY.y + arrowWidth);

        // Writing StringAtX axis
        g2.drawString(labelAtX, winWidth - marg, winHeight - margTot);
        // Writing StringAtY axis
        g2.drawString(labelAtY, marg, marg);

        g2.setColor(pointColors);
        g2.setStroke(new BasicStroke(3f));

        // Drawing Points
        for (Point p : points) {
            g2.draw((Shape) p);
        }

    }

    @Override
    public void run() {

        double deltaT = 0;
        long finalTime = System.nanoTime();
        long currentTime = 0;
        int interval = 1000000000 / 60;

        while (thread != null) {

            currentTime = System.nanoTime();

            deltaT += (currentTime - finalTime) / interval;
            finalTime = currentTime;

            if (deltaT >= 1) {
                repaint();
            }

        }
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}