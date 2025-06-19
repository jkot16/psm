import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartMain {

    public static final double gravity = 10;
    public static final double pendulumRadius = 1.0;
    public static final double timeStep = 0.1;
    public static final double totalSimulationTime = 20;
    public static final double initialAngle = Math.toRadians(60);
    public static final double initialAngularVelocity = 0.0;

    public static void main(String[] args) {
        System.out.println("██████╗ ██████╗  ██████╗ ██╗     ███████╗██████╗  ██████╗ ██████╗ ");
        System.out.println("██╔══██╗██╔══██╗██╔════╝ ██║     ██╔════╝██╔══██╗██╔═══██╗██╔══██╗");
        System.out.println("██████╔╝██████╔╝██║  ███╗██║     █████╗  ██████╔╝██║   ██║██║  ██║");
        System.out.println("██╔═══╝ ██╔══██╗██║   ██║██║     ██╔══╝  ██╔══██╗██║   ██║██║  ██║");
        System.out.println("██║     ██║  ██║╚██████╔╝███████╗███████╗██║  ██║╚██████╔╝██████╔╝");
        System.out.println("╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ");

        XYSeriesCollection heunDataset = solvePendulumHeun(
                initialAngle, initialAngularVelocity, timeStep, totalSimulationTime
        );

        System.out.println();
        System.out.println("██████╗ ██╗  ██║███████╗████████╗██║  ██║ ██████╗ ██████╗ ");
        System.out.println("██╔══██╗██║  ██║██╔════╝╚══██╔══╝██║  ██║██╔═══██╗██╔══██╗");
        System.out.println("██████╔╝███████║█████╗     ██║   ███████║██║   ██║██║  ██║");
        System.out.println("██╔══██╗██╔══██║██╔══╝     ██║   ██╔══██║██║   ██║██║  ██║");
        System.out.println("██║  ██║██║  ██║███████╗   ██║   ██║  ██║╚██████╔╝██████╔╝");
        System.out.println("╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ");


        XYSeriesCollection rk4Dataset = solvePendulumRK4(
                initialAngle, initialAngularVelocity, timeStep, totalSimulationTime
        );


        EnergyChart heunChart = new EnergyChart("Improved Euler Method", heunDataset);
        EnergyChart rk4Chart = new EnergyChart("RK4 Method", rk4Dataset);

        heunChart.displayChart();
        rk4Chart.displayChart();
    }


    public static double computeAngularRate(double angle, double angularVelocity) {
        return angularVelocity;
    }

    public static double computeAngularAcceleration(double angle, double angularVelocity) {
        return -(gravity / pendulumRadius) * Math.sin(angle);
    }

    public static XYSeriesCollection solvePendulumHeun(double initialAngle,
                                                       double initialAngularVelocity,
                                                       double timeStep,
                                                       double totalSimulationTime) {
        System.out.println("time\tangle\t\tangularVelocity\tpotentialEnergy\tkineticEnergy\ttotalEnergy");

        XYSeries potentialEnergySeries = new XYSeries("Potential Energy");
        XYSeries kineticEnergySeries   = new XYSeries("Kinetic Energy");
        XYSeries totalEnergySeries     = new XYSeries("Total Energy");

        double angle = initialAngle;
        double angularVelocity = initialAngularVelocity;
        double currentTime = 0.0;

        while (currentTime <= totalSimulationTime) {
            double potentialEnergy = gravity * (1 - Math.cos(angle));
            double kineticEnergy = 0.5 * (angularVelocity * angularVelocity);
            double totalEnergy = potentialEnergy + kineticEnergy;


            System.out.printf("%.3f\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f%n", currentTime, angle, angularVelocity, potentialEnergy, kineticEnergy, totalEnergy);

            potentialEnergySeries.add(currentTime, potentialEnergy);
            kineticEnergySeries.add(currentTime, kineticEnergy);
            totalEnergySeries.add(currentTime, totalEnergy);


            double k1Angle = computeAngularRate(angle, angularVelocity);
            double k1AngularVelocity = computeAngularAcceleration(angle, angularVelocity);


            double predictedAngle = angle + timeStep * k1Angle;
            double predictedAngularVelocity = angularVelocity + timeStep * k1AngularVelocity;


            double k2Angle = computeAngularRate(predictedAngle, predictedAngularVelocity);
            double k2AngularVelocity = computeAngularAcceleration(predictedAngle, predictedAngularVelocity);


            angle += timeStep * 0.5 * (k1Angle + k2Angle);
            angularVelocity += timeStep * 0.5 * (k1AngularVelocity + k2AngularVelocity);

            currentTime += timeStep;
        }


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(potentialEnergySeries);
        dataset.addSeries(kineticEnergySeries);
        dataset.addSeries(totalEnergySeries);

        return dataset;
    }

    public static XYSeriesCollection solvePendulumRK4(double initialAngle,
                                                      double initialAngularVelocity,
                                                      double timeStep,
                                                      double totalSimulationTime) {
        System.out.println("time\tangle\t\tangularVelocity\tpotentialEnergy\tkineticEnergy\ttotalEnergy");

        XYSeries potentialEnergySeries = new XYSeries("Potential Energy");
        XYSeries kineticEnergySeries = new XYSeries("Kinetic Energy");
        XYSeries totalEnergySeries = new XYSeries("Total Energy");

        double angle = initialAngle;
        double angularVelocity = initialAngularVelocity;
        double currentTime = 0.0;

        while (currentTime <= totalSimulationTime) {
            double potentialEnergy = gravity * (1 - Math.cos(angle));
            double kineticEnergy = 0.5 * (angularVelocity * angularVelocity);
            double totalEnergy = potentialEnergy + kineticEnergy;

            System.out.printf("%.3f\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f%n", currentTime, angle, angularVelocity, potentialEnergy, kineticEnergy, totalEnergy);

            potentialEnergySeries.add(currentTime, potentialEnergy);
            kineticEnergySeries.add(currentTime, kineticEnergy);
            totalEnergySeries.add(currentTime, totalEnergy);


            double k1Angle = timeStep * computeAngularRate(angle, angularVelocity);
            double k1AngularVelocity = timeStep * computeAngularAcceleration(angle, angularVelocity);

            double k2Angle = timeStep * computeAngularRate(
                    angle + 0.5 * k1Angle,
                    angularVelocity + 0.5 * k1AngularVelocity
            );
            double k2AngularVelocity = timeStep * computeAngularAcceleration(
                    angle + 0.5 * k1Angle,
                    angularVelocity + 0.5 * k1AngularVelocity
            );

            double k3Angle = timeStep * computeAngularRate(
                    angle + 0.5 * k2Angle,
                    angularVelocity + 0.5 * k2AngularVelocity
            );
            double k3AngularVelocity = timeStep * computeAngularAcceleration(
                    angle + 0.5 * k2Angle,
                    angularVelocity + 0.5 * k2AngularVelocity
            );

            double k4Angle = timeStep * computeAngularRate(
                    angle + k3Angle,
                    angularVelocity + k3AngularVelocity
            );
            double k4AngularVelocity = timeStep * computeAngularAcceleration(
                    angle + k3Angle,
                    angularVelocity + k3AngularVelocity
            );

            angle += (k1Angle + 2.0 * k2Angle + 2.0 * k3Angle + k4Angle) / 6.0;
            angularVelocity += (k1AngularVelocity + 2.0 * k2AngularVelocity
                    + 2.0 * k3AngularVelocity + k4AngularVelocity) / 6.0;

            currentTime += timeStep;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(potentialEnergySeries);
        dataset.addSeries(kineticEnergySeries);
        dataset.addSeries(totalEnergySeries);

        return dataset;
    }
}
