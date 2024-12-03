package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYDataset;

public class XYC extends ApplicationFrame {

    final static String title = "Surveillance Graph";
    ArrayList<Double[]> vals;

    public XYC(ArrayList<Double[]> vals) {

        super(title);
        JFreeChart xyChart = ChartFactory.createXYLineChart("Suivi", "t", "Pressure", createDataset(),
                PlotOrientation.HORIZONTAL, true, true, false);
        ChartPanel panel = new ChartPanel(xyChart);
        panel.setPreferredSize(new Dimension(500, 500));
        final XYPlot plot = xyChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesPaint(3, Color.MAGENTA);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesStroke(2, new BasicStroke(4.0f));
        renderer.setSeriesStroke(3, new BasicStroke(4.0f));
    }

    public XYDataset createDataset() {
        return null;
        // TODO
    }

}
