import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Graph extends JPanel {

    WindowPanel wp;
    double maxVal;
    double minVal;
    ArrayList<Point> arr;
    ArrayList<Double[]> vals;
    int index;

    int marg = 30;
    int labelMarg = 15;
    int totalMarg = marg + labelMarg;
    Color lineColor = new Color(44,102,230,180);
    Color pointColor = new Color(100,100, 100, 180);
    Color gridColor = new Color(200,200,200,200);
    static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    int pointWidth = 4;
    int YDivisions = 10;

    double xScale;
    double yScale;

/*
    public Graph(WindowPanel wp, ArrayList<Double> vals, int index){
//        this.wp = wp;
//        this.vals = vals;
//        this.index = index;
        arr = new ArrayList<>();
//        for(int i = 0;i != vals.size() - 1;i++){
//            arr.add(new Point(i, Integer.parseInt(vals.get(i)[index].toString())));
//        }
    }
*/

    public Graph(ArrayList<Double[]> val, int index){
            this.index = index;
            this.vals = val;
    }
    @Override
    public void paintComponents(Graphics g){
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        xScale = ((double) this.getWidth() - 2 * marg) / (arr.size() - 1);
        yScale = ((double) this.getHeight() - 2 * marg) / (getMax() - 1);


        ArrayList<Point> graphPoints  = new ArrayList<Point>();
        for(int i =0;i < vals.size();i++){
            int x1 = (int) (i*xScale + marg + labelMarg);
            int y1 = (int) ((int) ((getMax() - vals.get(i)[index])) * yScale + marg);
            graphPoints.add(new Point(x1,y1));
        }

        // Draw White Background
        g2.setColor(Color.WHITE);
        g2.fillRect(marg + labelMarg, marg, getWidth() - (2*marg) - labelMarg, getHeight() - 2 * marg - labelMarg);
        g2.setColor(Color.BLACK);

        // Hatch marks and grid lines for y axis
        for(int i = 0; i < YDivisions + 1;i++){
            int x0 = marg + labelMarg;
            int x1 = pointWidth + marg + labelMarg;
            int y0 = getHeight() - ((i * (getHeight() - marg * 2 - labelMarg)) / YDivisions + marg + labelMarg);
            int y1 = y0;
            if(!vals.isEmpty()){
                g2.setColor(gridColor);
                g2.drawLine(marg + labelMarg + 1 + pointWidth, y0, getWidth() - marg, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMin() + (getMax() -getMin()) * ((i * 1.0) /  YDivisions)) * 100)) /100.0 + "";
                FontMetrics fontMet = g2.getFontMetrics();
                int labelWidth = fontMet.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (fontMet.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // Hatch marks and grid lines for x axis
        for(int i = 0;i < vals.size();i++){
            if(vals.size() > 1){
              int x0 = i * (getWidth() - marg * 2 - labelMarg) / (vals.size() - 1) + marg + labelMarg;
              int x1 = x0;
              int y0 = getHeight() - marg - labelMarg;
              int y1 = y0 - pointWidth;
              if((i % ((int) ((vals.size() /20.0)) + 1)) == 0){
                  g2.setColor(gridColor);
                  g2.drawLine(x0, getHeight() - marg - labelMarg - 1 - pointWidth, x1, marg);
                  g2.setColor(Color.BLACK);
                  String xLabel = i + "";
                  FontMetrics fontMet = g2.getFontMetrics();
                  int labelWidth = fontMet.stringWidth(xLabel);
                  g2.drawString(xLabel, x0 -labelWidth / 2, y0 + fontMet.getHeight() + 3);
              }
              g2.drawLine(x0, y0, x1, y1);
            }
        }

        // Create x and y axis
        g2.drawLine(marg + labelMarg, getHeight() - marg - labelMarg, marg + labelMarg, marg);
        g2.drawLine(marg + labelMarg, getHeight() - marg - labelMarg, getWidth() - marg, getHeight());

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for(int i = 0;i < graphPoints.size() - 1;i++){
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1,y1,x2,y2);
        }
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (Point graphPoint : graphPoints) {
            int x = graphPoint.x - pointWidth / 2;
            int y = graphPoint.y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    public double getMax(){
        maxVal = Double.MIN_VALUE;

        for(Point p : arr){
            if(p.getX() > maxVal){
                maxVal = p.getX();
            }
        }
        return maxVal;
    }

    public double getMin(){
        minVal = Double.MAX_VALUE;
        for(Point p : arr){
            if(p.getX() < minVal){
                minVal = p.getX();
            }
        }
        return minVal;
    }

    public void setScores(ArrayList<Double[]> vals){
        this.vals = vals;
        invalidate();
        this.repaint();
    }

    public ArrayList<Double[]> getVals(){
        return vals;
    }

    public static void createAndShowGui(){
        ArrayList<Double[]> val = new ArrayList<>();
        Random rand = new Random();
        int maxDataPoints = 40;
        int maxVal = 10;
        for(int i = 0; i < maxDataPoints; i++){
            val.add(new Double[] {(double) rand.nextDouble() * maxVal, (double) rand.nextDouble() * maxVal,
                 (double) rand.nextDouble() * maxVal, (double) rand.nextDouble() * maxVal});
        }
        Graph panel = new Graph(val, 0);
        panel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGui();
            }
        });
    }

}
