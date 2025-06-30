import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import java.awt.Color;

public final class LorenzPlot {

    public static void show(String title,double[][]... curves){
        XYSeriesCollection ds=new XYSeriesCollection();
        String[] names={"Euler","Midpoint","RK4"};
        for(int i=0;i<curves.length;i++) ds.addSeries(toSeries(names[i],curves[i]));

        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart chart=ChartFactory.createXYLineChart(title,"x","z",ds);
        XYPlot p=chart.getXYPlot();
        p.setBackgroundPaint(Color.WHITE);
        p.setDomainGridlinePaint(Color.LIGHT_GRAY);
        p.setRangeGridlinePaint(Color.LIGHT_GRAY);
        ((NumberAxis)p.getDomainAxis()).setAutoRangeIncludesZero(false);
        ((NumberAxis)p.getRangeAxis()).setAutoRangeIncludesZero(false);

        JFrame f=new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ChartPanel(chart));
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }

    private static XYSeries toSeries(String name,double[][] d){
        XYSeries s=new XYSeries(name);
        for(double[] p:d) s.add(p[0],p[2]);
        return s;
    }

    private LorenzPlot(){}
}
