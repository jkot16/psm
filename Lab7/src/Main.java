import java.awt.*;
import javax.swing.*;

public class Main extends JPanel {
    private static final int GRID_SIZE=42;
    private static final double EPS=1e-6;

    private static final double TEMP_LEFT=-200;
    private static final double TEMP_BOTTOM=-300;
    private static final double TEMP_RIGHT=300;
    private static final double TEMP_TOP=200;

    private double[][] temperatureGrid;

    public Main() {
        temperatureGrid=new double[GRID_SIZE][GRID_SIZE];

        for(int x=0;x<GRID_SIZE;x++) {
            temperatureGrid[x][0]=TEMP_BOTTOM;
            temperatureGrid[x][GRID_SIZE-1]=TEMP_TOP;
            temperatureGrid[0][x]=TEMP_LEFT;
            temperatureGrid[GRID_SIZE-1][x]=TEMP_RIGHT;
        }

        for(int x=1;x<GRID_SIZE-1;x++)
            for(int y=1;y<GRID_SIZE-1;y++)
                temperatureGrid[x][y]=0.0;

        boolean converged=false;
        while(!converged) {
            converged=true;
            for(int x=1;x<GRID_SIZE-1;x++) {
                for(int y=1;y<GRID_SIZE-1;y++) {
                    double newVal=0.25*(
                            temperatureGrid[x+1][y]+
                                    temperatureGrid[x-1][y]+
                                    temperatureGrid[x][y+1]+
                                    temperatureGrid[x][y-1]
                    );
                    if(Math.abs(newVal-temperatureGrid[x][y])>EPS)
                        converged=false;
                    temperatureGrid[x][y]=newVal;
                }
            }
        }

        setPreferredSize(new Dimension(GRID_SIZE*10,GRID_SIZE*10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize=10;

        double minTemp=Double.POSITIVE_INFINITY,maxTemp=Double.NEGATIVE_INFINITY;
        for(int x=0;x<GRID_SIZE;x++)
            for(int y=0;y<GRID_SIZE;y++) {
                minTemp=Math.min(minTemp,temperatureGrid[x][y]);
                maxTemp=Math.max(maxTemp,temperatureGrid[x][y]);
            }

        Graphics2D g2=(Graphics2D)g;
        for(int x=0;x<GRID_SIZE;x++) {
            for(int y=0;y<GRID_SIZE;y++) {
                double val=temperatureGrid[x][y];
                float norm=(float)((val-minTemp)/(maxTemp-minTemp));
                float hue=0.66f*(1-norm);
                g2.setColor(Color.getHSBColor(hue,1f,1f));
                g2.fillRect(x*cellSize,(GRID_SIZE-1-y)*cellSize,cellSize,cellSize);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame=new JFrame("Temperature on Square Plate");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new Main());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
