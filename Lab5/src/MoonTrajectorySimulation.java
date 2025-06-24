import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MoonTrajectorySimulation extends JFrame {
    static final double gravitationalConstant = 6.6743e-11;
    static final double massSun = 1.989e30;
    static final double massEarth = 5.972e24;
    static final double massMoon = 7.347e22;
    static final double earthSunDistance = 1.5e11;
    static final double earthMoonDistance = 3.844e8;
    static final double timeStep = 60;
    static final double simulationDuration = 3.05e7;
    static final int printInterval = 10000;
    ArrayList<Point.Double> earthTrajectory = new ArrayList<>();
    ArrayList<Point.Double> moonTrajectory = new ArrayList<>();
    Point.Double currentEarthVelocity;
    Point.Double currentMoonVelocity;

    public MoonTrajectorySimulation() {
        simulateMotion();
        System.out.println("Final Earth Position: " + earthTrajectory.get(earthTrajectory.size()-1));
        System.out.println("Final Earth Velocity: " + currentEarthVelocity);
        System.out.println("Final Moon Position: " + moonTrajectory.get(moonTrajectory.size()-1));
        System.out.println("Final Moon Velocity: " + currentMoonVelocity);
        setTitle("Moon Trajectory Simulation (Improved Euler - MidPoint)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new TrajectoryPanel(earthTrajectory, moonTrajectory));
        setSize(1000,800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void simulateMotion() {
        double earthPosX = earthSunDistance;
        double earthPosY = 0;
        double earthVelX = 0;
        double earthVelY = Math.sqrt(gravitationalConstant * massSun / earthSunDistance);
        double moonPosX = earthPosX + earthMoonDistance;
        double moonPosY = 0;
        double moonVelX = 0;
        double moonVelY = earthVelY + Math.sqrt(gravitationalConstant * massEarth / earthMoonDistance);
        int numSteps = (int)(simulationDuration / timeStep);
        for (int i = 0; i < numSteps; i++) {
            earthTrajectory.add(new Point.Double(earthPosX, earthPosY));
            moonTrajectory.add(new Point.Double(moonPosX, moonPosY));
            double[] earthAccel = computeEarthAcceleration(earthPosX, earthPosY);
            double k1_earthPosX = timeStep * earthVelX;
            double k1_earthPosY = timeStep * earthVelY;
            double k1_earthVelX = timeStep * earthAccel[0];
            double k1_earthVelY = timeStep * earthAccel[1];
            double earthPosXMid = earthPosX + 0.5 * k1_earthPosX;
            double earthPosYMid = earthPosY + 0.5 * k1_earthPosY;
            double earthVelXMid = earthVelX + 0.5 * k1_earthVelX;
            double earthVelYMid = earthVelY + 0.5 * k1_earthVelY;
            double[] earthAccelMid = computeEarthAcceleration(earthPosXMid, earthPosYMid);
            double k2_earthPosX = timeStep * earthVelXMid;
            double k2_earthPosY = timeStep * earthVelYMid;
            double k2_earthVelX = timeStep * earthAccelMid[0];
            double k2_earthVelY = timeStep * earthAccelMid[1];
            earthPosX = earthPosX + k2_earthPosX;
            earthPosY = earthPosY + k2_earthPosY;
            earthVelX = earthVelX + k2_earthVelX;
            earthVelY = earthVelY + k2_earthVelY;
            double[] moonAccel = computeMoonAcceleration(moonPosX, moonPosY, earthPosX, earthPosY);
            double k1_moonPosX = timeStep * moonVelX;
            double k1_moonPosY = timeStep * moonVelY;
            double k1_moonVelX = timeStep * moonAccel[0];
            double k1_moonVelY = timeStep * moonAccel[1];
            double moonPosXMid = moonPosX + 0.5 * k1_moonPosX;
            double moonPosYMid = moonPosY + 0.5 * k1_moonPosY;
            double moonVelXMid = moonVelX + 0.5 * k1_moonVelX;
            double moonVelYMid = moonVelY + 0.5 * k1_moonVelY;
            double[] moonAccelMid = computeMoonAcceleration(moonPosXMid, moonPosYMid, earthPosX, earthPosY);
            double k2_moonPosX = timeStep * moonVelXMid;
            double k2_moonPosY = timeStep * moonVelYMid;
            double k2_moonVelX = timeStep * moonAccelMid[0];
            double k2_moonVelY = timeStep * moonAccelMid[1];
            moonPosX = moonPosX + k2_moonPosX;
            moonPosY = moonPosY + k2_moonPosY;
            moonVelX = moonVelX + k2_moonVelX;
            moonVelY = moonVelY + k2_moonVelY;
            if(i % printInterval == 0) {
                System.out.println("Step " + i);
                System.out.println("Earth Pos: (" + earthPosX + ", " + earthPosY + ") Earth Vel: (" + earthVelX + ", " + earthVelY + ")");
                System.out.println("Moon Pos: (" + moonPosX + ", " + moonPosY + ") Moon Vel: (" + moonVelX + ", " + moonVelY + ")");
            }
        }
        currentEarthVelocity = new Point.Double(earthVelX, earthVelY);
        currentMoonVelocity = new Point.Double(moonVelX, moonVelY);
    }

    double[] computeEarthAcceleration(double posX, double posY) {
        double distance = Math.sqrt(posX * posX + posY * posY);
        double accelFactor = -gravitationalConstant * massSun / (distance * distance * distance);
        return new double[]{accelFactor * posX, accelFactor * posY};
    }

    double[] computeMoonAcceleration(double posMoonX, double posMoonY, double posEarthX, double posEarthY) {
        double distanceMoonToSun = Math.sqrt(posMoonX * posMoonX + posMoonY * posMoonY);
        double accelFactorSun = -gravitationalConstant * massSun / (distanceMoonToSun * distanceMoonToSun * distanceMoonToSun);
        double accelSunX = accelFactorSun * posMoonX;
        double accelSunY = accelFactorSun * posMoonY;
        double deltaX = posMoonX - posEarthX;
        double deltaY = posMoonY - posEarthY;
        double distanceEarthMoon = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double accelFactorEarth = -gravitationalConstant * massEarth / (distanceEarthMoon * distanceEarthMoon * distanceEarthMoon);
        double accelEarthX = accelFactorEarth * deltaX;
        double accelEarthY = accelFactorEarth * deltaY;
        return new double[]{accelSunX + accelEarthX, accelSunY + accelEarthY};
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MoonTrajectorySimulation::new);
    }
}

class TrajectoryPanel extends JPanel {
    ArrayList<Point.Double> earthTrajectory;
    ArrayList<Point.Double> moonTrajectory;

    public TrajectoryPanel(ArrayList<Point.Double> earthTrajectory, ArrayList<Point.Double> moonTrajectory) {
        this.earthTrajectory = earthTrajectory;
        this.moonTrajectory = moonTrajectory;
    }

    protected void paintComponent(Graphics graphicsObj) {
        super.paintComponent(graphicsObj);
        Graphics2D g2d = (Graphics2D)graphicsObj;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        g2d.translate(panelWidth/2, panelHeight/2);
        double drawingScale = 2e-9;
        double moonOrbitExaggeration = 75;
        int sunPixelRadius = 50;
        g2d.setColor(Color.YELLOW);
        g2d.fill(new Ellipse2D.Double(-sunPixelRadius/2.0, -sunPixelRadius/2.0, sunPixelRadius, sunPixelRadius));
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < earthTrajectory.size()-1; i++) {
            Point.Double previousEarthPos = earthTrajectory.get(i);
            Point.Double currentEarthPos = earthTrajectory.get(i+1);
            int x1 = (int)(previousEarthPos.x * drawingScale);
            int y1 = (int)(previousEarthPos.y * drawingScale);
            int x2 = (int)(currentEarthPos.x * drawingScale);
            int y2 = (int)(currentEarthPos.y * drawingScale);
            g2d.drawLine(x1, y1, x2, y2);
        }
        g2d.setColor(Color.RED);
        for (int i = 0; i < moonTrajectory.size()-1; i++) {
            Point.Double previousEarthPos = earthTrajectory.get(i);
            Point.Double currentEarthPos = earthTrajectory.get(i+1);
            Point.Double previousMoonPos = moonTrajectory.get(i);
            Point.Double currentMoonPos = moonTrajectory.get(i+1);
            double earthX1 = previousEarthPos.x * drawingScale;
            double earthY1 = previousEarthPos.y * drawingScale;
            double earthX2 = currentEarthPos.x * drawingScale;
            double earthY2 = currentEarthPos.y * drawingScale;
            double moonX1 = previousMoonPos.x * drawingScale;
            double moonY1 = previousMoonPos.y * drawingScale;
            double moonX2 = currentMoonPos.x * drawingScale;
            double moonY2 = currentMoonPos.y * drawingScale;
            double deltaX1 = moonX1 - earthX1;
            double deltaY1 = moonY1 - earthY1;
            double deltaX2 = moonX2 - earthX2;
            double deltaY2 = moonY2 - earthY2;
            int drawnMoonX1 = (int)(earthX1 + deltaX1 * moonOrbitExaggeration);
            int drawnMoonY1 = (int)(earthY1 + deltaY1 * moonOrbitExaggeration);
            int drawnMoonX2 = (int)(earthX2 + deltaX2 * moonOrbitExaggeration);
            int drawnMoonY2 = (int)(earthY2 + deltaY2 * moonOrbitExaggeration);
            g2d.drawLine(drawnMoonX1, drawnMoonY1, drawnMoonX2, drawnMoonY2);
        }
        if (!moonTrajectory.isEmpty() && !earthTrajectory.isEmpty()) {
            Point.Double lastEarthPos = earthTrajectory.get(earthTrajectory.size()-1);
            int finalEarthX = (int)(lastEarthPos.x * drawingScale);
            int finalEarthY = (int)(lastEarthPos.y * drawingScale);
            g2d.setColor(Color.BLUE);
            int earthMarkerSize = 8;
            g2d.fill(new Ellipse2D.Double(finalEarthX - earthMarkerSize/2.0, finalEarthY - earthMarkerSize/2.0, earthMarkerSize, earthMarkerSize));
            Point.Double lastMoonPos = moonTrajectory.get(moonTrajectory.size()-1);
            double earthPosX = lastEarthPos.x * drawingScale;
            double earthPosY = lastEarthPos.y * drawingScale;
            double moonPosX = lastMoonPos.x * drawingScale;
            double moonPosY = lastMoonPos.y * drawingScale;
            double diffX = moonPosX - earthPosX;
            double diffY = moonPosY - earthPosY;
            moonPosX = earthPosX + diffX * moonOrbitExaggeration;
            moonPosY = earthPosY + diffY * moonOrbitExaggeration;
            g2d.setColor(Color.RED);
            int moonMarkerSize = 6;
            g2d.fill(new Ellipse2D.Double(moonPosX - moonMarkerSize/2.0, moonPosY - moonMarkerSize/2.0, moonMarkerSize, moonMarkerSize));
        }
        if (!moonTrajectory.isEmpty() && !earthTrajectory.isEmpty()) {
            Point.Double startEarthPos = earthTrajectory.get(0);
            Point.Double startMoonPos = moonTrajectory.get(0);
            double startEarthX = startEarthPos.x * drawingScale;
            double startEarthY = startEarthPos.y * drawingScale;
            double startMoonX = startMoonPos.x * drawingScale;
            double startMoonY = startMoonPos.y * drawingScale;
            double startDiffX = startMoonX - startEarthX;
            double startDiffY = startMoonY - startEarthY;
            int simulationStartX = (int)(startEarthX + startDiffX * moonOrbitExaggeration);
            int simulationStartY = (int)(startEarthY + startDiffY * moonOrbitExaggeration);
            g2d.setColor(Color.GREEN);
            g2d.fillRect(simulationStartX - 5, simulationStartY - 5, 10, 10);
            Point.Double endEarthPos = earthTrajectory.get(earthTrajectory.size()-1);
            Point.Double endMoonPos = moonTrajectory.get(moonTrajectory.size()-1);
            double endEarthX = endEarthPos.x * drawingScale;
            double endEarthY = endEarthPos.y * drawingScale;
            double endMoonX = endMoonPos.x * drawingScale;
            double endMoonY = endMoonPos.y * drawingScale;
            double endDiffX = endMoonX - endEarthX;
            double endDiffY = endMoonY - endEarthY;
            int simulationEndX = (int)(endEarthX + endDiffX * moonOrbitExaggeration);
            int simulationEndY = (int)(endEarthY + endDiffY * moonOrbitExaggeration);
            g2d.setColor(new Color(138,43,226));
            g2d.fillRect(simulationEndX - 5, simulationEndY - 5, 10, 10);
        }
        AffineTransform originalTransform = g2d.getTransform();
        g2d.setTransform(new AffineTransform());
        g2d.setColor(new Color(255,255,255,220));
        g2d.fillRect(10,10,260,120);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(10,10,260,120);
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(20,20,10,10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Sun",40,30);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(20,35,10,10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Earth",40,45);
        g2d.setColor(Color.RED);
        g2d.fillRect(20,50,10,10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Moon",40,60);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(20,65,10,10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("SimulationStart",40,75);
        g2d.setColor(new Color(138,43,226));
        g2d.fillRect(20,80,10,10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("SimulationEnd",40,90);
        g2d.setTransform(originalTransform);
    }
}
