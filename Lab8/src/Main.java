import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.Arrays;

public class Main {

    // parametry Lorenza
    static final double A = 10.0, B = 25.0, C = 8.0 / 3.0;

    // dt i maks. liczba kroków
    static final double dt = 0.03;
    static final int    STEPS = 5_000;      // nie musi być 20 000
    static final double LIMIT = 100.0;      // próg „rozbiegania”

    public static void main(String[] args) {

        double[][] euler    = integrateEuler(1, 1, 1);
        double[][] midpoint = integrateMidpoint(1, 1, 1);
        double[][] rk4      = integrateRK4(1, 1, 1);

        SwingUtilities.invokeLater(() -> createAndShowChart(euler, midpoint, rk4));
    }

    /* ---------- rysowanie ---------- */

    private static void createAndShowChart(double[][] euler,
                                           double[][] midpoint,
                                           double[][] rk4) {

        XYSeries sEuler    = toSeries("Euler ("    + euler.length    + " steps)", euler);
        XYSeries sMidpoint = toSeries("Midpoint (" + midpoint.length + " steps)", midpoint);
        XYSeries sRK4      = toSeries("RK4 ("      + rk4.length      + " steps)", rk4);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(sEuler);
        dataset.addSeries(sMidpoint);
        dataset.addSeries(sRK4);

        // jaśniejszy motyw
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Lorenz system",
                "x", "z", dataset);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setAutoRangeIncludesZero(false);
        NumberAxis range  = (NumberAxis) plot.getRangeAxis();
        range.setAutoRangeIncludesZero(false);

        JFrame frame = new JFrame("Lorenz – Euler vs Midpoint vs RK4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static XYSeries toSeries(String name, double[][] data) {
        XYSeries s = new XYSeries(name);
        for (double[] p : data) s.add(p[0], p[2]);   // z(x)
        return s;
    }

    /* ---------- równania Lorenza ---------- */

    static double fx(double x, double y, double z) { return A * (y - x); }
    static double fy(double x, double y, double z) { return x * (B - z) - y; }
    static double fz(double x, double y, double z) { return x * y - C * z; }

    /* ---------- integratory ---------- */

    static double[][] integrateEuler(double x0, double y0, double z0) {
        double[][] buf = new double[STEPS][3];
        int n = 0;
        double x = x0, y = y0, z = z0;

        for (; n < STEPS; n++) {
            if (isUnstable(x, y, z)) break;
            buf[n][0] = x; buf[n][1] = y; buf[n][2] = z;

            double kx = fx(x, y, z);
            double ky = fy(x, y, z);
            double kz = fz(x, y, z);

            x += dt * kx;
            y += dt * ky;
            z += dt * kz;
        }
        return Arrays.copyOf(buf, n);
    }

    static double[][] integrateMidpoint(double x0, double y0, double z0) {
        double[][] buf = new double[STEPS][3];
        int n = 0;
        double x = x0, y = y0, z = z0;

        for (; n < STEPS; n++) {
            if (isUnstable(x, y, z)) break;
            buf[n][0] = x; buf[n][1] = y; buf[n][2] = z;

            double k1x = fx(x, y, z), k1y = fy(x, y, z), k1z = fz(x, y, z);
            double xm = x + 0.5 * dt * k1x;
            double ym = y + 0.5 * dt * k1y;
            double zm = z + 0.5 * dt * k1z;
            double k2x = fx(xm, ym, zm), k2y = fy(xm, ym, zm), k2z = fz(xm, ym, zm);

            x += dt * k2x; y += dt * k2y; z += dt * k2z;
        }
        return Arrays.copyOf(buf, n);
    }

    static double[][] integrateRK4(double x0, double y0, double z0) {
        double[][] buf = new double[STEPS][3];
        int n = 0;
        double x = x0, y = y0, z = z0;

        for (; n < STEPS; n++) {
            if (isUnstable(x, y, z)) break;
            buf[n][0] = x; buf[n][1] = y; buf[n][2] = z;

            double k1x = fx(x, y, z),    k1y = fy(x, y, z),    k1z = fz(x, y, z);
            double x2 = x + 0.5 * dt * k1x, y2 = y + 0.5 * dt * k1y, z2 = z + 0.5 * dt * k1z;
            double k2x = fx(x2, y2, z2),  k2y = fy(x2, y2, z2),  k2z = fz(x2, y2, z2);
            double x3 = x + 0.5 * dt * k2x, y3 = y + 0.5 * dt * k2y, z3 = z + 0.5 * dt * k2z;
            double k3x = fx(x3, y3, z3),  k3y = fy(x3, y3, z3),  k3z = fz(x3, y3, z3);
            double x4 = x + dt * k3x,      y4 = y + dt * k3y,      z4 = z + dt * k3z;
            double k4x = fx(x4, y4, z4),   k4y = fy(x4, y4, z4),   k4z = fz(x4, y4, z4);

            x += dt * (k1x + 2*k2x + 2*k3x + k4x) / 6.0;
            y += dt * (k1y + 2*k2y + 2*k3y + k4y) / 6.0;
            z += dt * (k1z + 2*k2z + 2*k3z + k4z) / 6.0;
        }
        return Arrays.copyOf(buf, n);
    }

    /* ---------- pomoc ---------- */

    private static boolean isUnstable(double x, double y, double z) {
        return Math.abs(x) > LIMIT || Math.abs(y) > LIMIT || Math.abs(z) > LIMIT
                || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z);
    }
}
