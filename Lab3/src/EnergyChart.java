import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class EnergyChart {
    private String chartTitle;
    private XYSeriesCollection dataset;

    public EnergyChart(String chartTitle, XYSeriesCollection dataset) {
        this.chartTitle = chartTitle;
        this.dataset = dataset;
    }

    public void displayChart() {
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Time", "Energy", dataset);
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, false);

        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesShapesVisible(1, true);

        Color semiTransparentGreen = new Color(0, 255, 0, 150);
        renderer.setSeriesPaint(2, semiTransparentGreen);
        renderer.setSeriesStroke(2, new BasicStroke(
                3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0f));
        renderer.setSeriesShapesVisible(2, false);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        chart.getTitle().setFont(new Font("Dialog", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("Dialog", Font.PLAIN, 14));

        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
        domainAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
        rangeAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));

        ChartFrame frame = new ChartFrame(chartTitle, chart);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
