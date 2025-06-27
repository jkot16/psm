import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartDrawer {
    public static void drawEnergies(Simulation sim) {
        double[] t = sim.getTimes();
        double[] ep = sim.getPotential();
        double[] ek = sim.getKinetic();
        double[] et = sim.getTotal();
        XYSeries s1 = new XYSeries("Ep");
        XYSeries s2 = new XYSeries("Ek");
        XYSeries s3 = new XYSeries("Et");
        for (int i = 0; i < t.length; i++) {
            s1.add(t[i], ep[i]);
            s2.add(t[i], ek[i]);
            s3.add(t[i], et[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "String Energies", "Time", "Energy", dataset,
                PlotOrientation.VERTICAL, true, true, false
        );
        styleChart(chart, new Color[]{Color.MAGENTA, Color.ORANGE, Color.CYAN}, 3.0f);
        showChart(chart, "Energy vs Time");
    }

    public static void drawStringShapesMultiple(Simulation sim, double dt) {
        double maxTime = sim.getTimes()[sim.getTotalSteps()];
        XYSeriesCollection dataset = new XYSeriesCollection();
        int n = sim.getPointCount();
        double dx = sim.getSpatialStep();
        for (double t = 0; t <= maxTime; t += dt) {
            int idx = (int) (t / sim.getTimeStep());
            if (idx > sim.getTotalSteps()) idx = sim.getTotalSteps();
            XYSeries series = new XYSeries("t=" + String.format("%.2f", t));
            double[] y = sim.getYHistory()[idx];
            for (int i = 0; i <= n; i++) {
                series.add(i * dx, y[i]);
            }
            dataset.addSeries(series);
        }
        JFreeChart chart = ChartFactory.createXYLineChart(
                "String Shapes Over Time", "x", "u", dataset,
                PlotOrientation.VERTICAL, true, false, false
        );
        styleChart(chart, null, 2.0f);
        showChart(chart, "String Shapes");
    }

    private static void styleChart(JFreeChart chart, Color[] colors, float strokeWidth) {
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        int seriesCount = plot.getDataset().getSeriesCount();
        for (int i = 0; i < seriesCount; i++) {
            if (colors != null && i < colors.length) {
                renderer.setSeriesPaint(i, colors[i]);
            }
            renderer.setSeriesStroke(i, new BasicStroke(strokeWidth));
        }
        plot.setRenderer(renderer);
        chart.setAntiAlias(true);
    }

    private static void showChart(JFreeChart chart, String title) {
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Simulation sim = new Simulation(10, Math.PI, 0.01, 2 * Math.PI);
        sim.run();
        drawEnergies(sim);
        drawStringShapesMultiple(sim, 0.5);
    }
}
