package components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.BasicStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class CG extends org.jfree.chart.ui.ApplicationFrame {

    ArrayList<Double[]> vals;
    ChartPanel chartPanel;
    XYPlot plot;
    JFreeChart xylineChart;

    public CG(ArrayList<Double[]> vals) {
        super("Suivi");
        this.vals = vals;
        if (!vals.isEmpty()) {
            xylineChart = ChartFactory.createXYLineChart(
                    "Suivi",
                    "t",
                    "Values",
                    createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            chartPanel = new ChartPanel(xylineChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
            plot = xylineChart.getXYPlot();
            addRenderer(plot, chartPanel);
        } else {
            xylineChart = ChartFactory.createXYLineChart(
                    "Suivi",
                    "t",
                    "Values",
                    null,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            chartPanel = new ChartPanel(xylineChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
            plot = xylineChart.getXYPlot();
            addRenderer(plot, chartPanel);
        }
    }

    private void addRenderer(XYPlot plot, ChartPanel chartPanel) {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesPaint(3, Color.MAGENTA);
        renderer.setSeriesStroke(0, new BasicStroke(5.0f));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesStroke(2, new BasicStroke(3.0f));
        renderer.setSeriesStroke(3, new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        XYSeries depth = new XYSeries("Depth");
        XYSeries pressure = new XYSeries("Pressure");
        XYSeries PaN2 = new XYSeries("PaN2");
        XYSeries PaO2 = new XYSeries("PaO2");

        double depthVal = vals.getLast()[0];
        double pressureVal = vals.getLast()[1];
        double PaN2Val = vals.getLast()[2];
        double PaO2Val = vals.getLast()[3];

        double t = Double.valueOf(vals.size() - 1);

        XYDataItem depthItem = new XYDataItem(t, depthVal);
        XYDataItem pressureItem = new XYDataItem(t, pressureVal);
        XYDataItem PaN2Item = new XYDataItem(t, PaN2Val);
        XYDataItem PaO2Item = new XYDataItem(t, PaO2Val);

        depth.add(depthItem);
        pressure.add(pressureItem);
        PaN2.add(PaN2Item);
        PaO2.add(PaO2Item);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(depth);
        dataset.addSeries(pressure);
        dataset.addSeries(PaN2);
        dataset.addSeries(PaO2);
        return dataset;
    }

    public void updateUI() {
        chartPanel.removeAll();
        xylineChart = ChartFactory.createXYLineChart(
                "Suivi",
                "t",
                "Values",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        plot = xylineChart.getXYPlot();
        chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        addRenderer(plot, chartPanel);
        chartPanel.repaint();
    }

    public void resetAll() {
        chartPanel.removeAll();
        xylineChart = ChartFactory.createXYLineChart(
                "Suivi",
                "t",
                "Values",
                null,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(xylineChart);
        plot = xylineChart.getXYPlot();
        chartPanel.setPreferredSize(new Dimension(560, 367));
        addRenderer(plot, chartPanel);
        chartPanel.repaint();
    }
}