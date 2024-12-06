package components;

import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ui.ApplicationFrame;

public class XYC extends ApplicationFrame {

    ArrayList<Double[]> vals;
    JFreeChart chart;
    ChartPanel chartPanel;

    public XYC(final String title, ArrayList<Double[]> vals) {
        super(title);
        this.vals = vals;
        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    public XYDataset createDataset() {
        TimeSeries series = new TimeSeries("Random Data");
        Second current = new Second();

        for (int i = 0; i < vals.size() - 1; i++) {
            double val = vals.get(i)[1];
            try {
                series.add(current, Double.valueOf(val));
                current = (Second) current.next();
            } catch (SeriesException e) {
                System.err.println("Error adding to series");
            }
        }

        return new TimeSeriesCollection(series);
    }

    public JFreeChart createChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(
                "Computing Test",
                "Seconds",
                "Value",
                dataset,
                false,
                true,
                false);
    }

    public void updateUI() {
        chartPanel.removeAll();
        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
        setContentPane(chartPanel);
        chartPanel.repaint();
    }
}