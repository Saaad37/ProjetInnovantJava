package components;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CG extends JPanel {

    ArrayList<Double[]> vals;
    int index;
    JFrame frame;
    DefaultCategoryDataset dataset;

    public CG(ArrayList<Double[]> vals, int index, JFrame frame) {
        this.vals = vals;
        this.index = index;
        this.frame = frame;
        initUI();
    }

    private void initUI() {
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
        frame.setContentPane(chartPanel);
    }

    public void updateUI() {
        JFreeChart chart = createChart(updateDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.setContentPane(chartPanel);
    }

    public DefaultCategoryDataset updateDataset() {
        if (!vals.isEmpty()) {
            for (int i = 0; i < vals.size(); i++) {
                dataset.addValue(vals.get(i)[index], "1", "1");
            }
        }
        return dataset;
    }

    public DefaultCategoryDataset createDataset() {
        dataset = new DefaultCategoryDataset();
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createLineChart(
                "Values",
                "t",
                "Pressure",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
    }
}
