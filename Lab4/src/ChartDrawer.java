import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartDrawer {
    public static void main(String[] args) {
        List<Double> timeList = new ArrayList<>();
        List<Double> spherePositionList = new ArrayList<>();
        List<Double> sphereRotationList = new ArrayList<>();
        List<Double> otherPositionList = new ArrayList<>();
        List<Double> otherRotationList = new ArrayList<>();
        List<Double> sphereEnergyList = new ArrayList<>();
        List<Double> otherEnergyList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("simulation_data.csv"))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                timeList.add(Double.parseDouble(parts[0]));
                spherePositionList.add(Double.parseDouble(parts[1]));
                sphereRotationList.add(Double.parseDouble(parts[2]));
                otherPositionList.add(Double.parseDouble(parts[3]));
                otherRotationList.add(Double.parseDouble(parts[4]));
                sphereEnergyList.add(Double.parseDouble(parts[5]));
                otherEnergyList.add(Double.parseDouble(parts[6]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        XYSeries spherePositionSeries = new XYSeries("Sphere - Center of Mass");
        XYSeries otherPositionSeries = new XYSeries("Other object - Center of Mass");
        XYSeries sphereRotationSeries = new XYSeries("Sphere - Rotation Angle");
        XYSeries otherRotationSeries = new XYSeries("Other object - Rotation Angle");
        XYSeries sphereEnergySeries = new XYSeries("Sphere - Total Energy");
        XYSeries otherEnergySeries = new XYSeries("Other object - Total Energy");

        for (int i = 0; i < timeList.size(); i++) {
            spherePositionSeries.add(timeList.get(i), spherePositionList.get(i));
            otherPositionSeries.add(timeList.get(i), otherPositionList.get(i));
            sphereRotationSeries.add(timeList.get(i), sphereRotationList.get(i));
            otherRotationSeries.add(timeList.get(i), otherRotationList.get(i));
            sphereEnergySeries.add(timeList.get(i), sphereEnergyList.get(i));
            otherEnergySeries.add(timeList.get(i), otherEnergyList.get(i));
        }

        XYSeriesCollection positionDataset = new XYSeriesCollection();
        positionDataset.addSeries(spherePositionSeries);
        positionDataset.addSeries(otherPositionSeries);

        JFreeChart positionChart = ChartFactory.createXYLineChart(
                "Center of Mass vs. Time",
                "Time [s]",
                "Position [m]",
                positionDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYSeriesCollection rotationDataset = new XYSeriesCollection();
        rotationDataset.addSeries(sphereRotationSeries);
        rotationDataset.addSeries(otherRotationSeries);

        JFreeChart rotationChart = ChartFactory.createXYLineChart(
                "Rotation Angle vs. Time",
                "Time [s]",
                "Rotation Angle [rad]",
                rotationDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYSeriesCollection energyDataset = new XYSeriesCollection();
        energyDataset.addSeries(sphereEnergySeries);
        energyDataset.addSeries(otherEnergySeries);

        JFreeChart energyChart = ChartFactory.createXYLineChart(
                "Total Energy vs. Time",
                "Time [s]",
                "Energy [J]",
                energyDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot positionPlot = positionChart.getXYPlot();
        XYLineAndShapeRenderer positionRenderer = new XYLineAndShapeRenderer();
        positionRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        positionRenderer.setSeriesStroke(1, new BasicStroke(2.0f));
        positionPlot.setRenderer(positionRenderer);

        XYPlot rotationPlot = rotationChart.getXYPlot();
        XYLineAndShapeRenderer rotationRenderer = new XYLineAndShapeRenderer();
        rotationRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        rotationRenderer.setSeriesStroke(1, new BasicStroke(2.0f));
        rotationPlot.setRenderer(rotationRenderer);

        XYPlot energyPlot = energyChart.getXYPlot();
        XYLineAndShapeRenderer energyRenderer = new XYLineAndShapeRenderer();
        energyRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        energyRenderer.setSeriesStroke(1, new BasicStroke(2.0f));
        energyPlot.setRenderer(energyRenderer);

        SwingUtilities.invokeLater(() -> {
            JFrame frame1 = new JFrame("Center of Mass");
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.getContentPane().add(new ChartPanel(positionChart), BorderLayout.CENTER);
            frame1.pack();
            frame1.setLocationRelativeTo(null);
            frame1.setVisible(true);

            JFrame frame2 = new JFrame("Rotation Angle");
            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.getContentPane().add(new ChartPanel(rotationChart), BorderLayout.CENTER);
            frame2.pack();
            frame2.setLocationRelativeTo(null);
            frame2.setVisible(true);

            JFrame frame3 = new JFrame("Total Energy");
            frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame3.getContentPane().add(new ChartPanel(energyChart), BorderLayout.CENTER);
            frame3.pack();
            frame3.setLocationRelativeTo(null);
            frame3.setVisible(true);
        });
    }
}
