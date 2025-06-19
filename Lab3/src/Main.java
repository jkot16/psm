public class Main {

    public static final double gravity = 9.8;
    public static final double pendulumRadius = 1.0;
    public static final double timeStep = 0.1;
    public static final double totalSimulationTime = 20;
    public static final double initialAngle = Math.toRadians(60);
    public static final double initialAngularVelocity = 0.0;

    public static void main(String[] args) {

        System.out.println("██╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗   ██╗███████╗██████╗     ███████╗██╗   ██╗██╗     ███████╗██████╗     ███╗   ███╗███████╗████████╗██╗  ██╗ ██████╗ ██████╗ \n" +
                "██║████╗ ████║██╔══██╗██╔══██╗██╔═══██╗██║   ██║██╔════╝██╔══██╗    ██╔════╝██║   ██║██║     ██╔════╝██╔══██╗    ████╗ ████║██╔════╝╚══██╔══╝██║  ██║██╔═══██╗██╔══██╗\n" +
                "██║██╔████╔██║██████╔╝██████╔╝██║   ██║██║   ██║█████╗  ██║  ██║    █████╗  ██║   ██║██║     █████╗  ██████╔╝    ██╔████╔██║█████╗     ██║   ███████║██║   ██║██║  ██║\n" +
                "██║██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║╚██╗ ██╔╝██╔══╝  ██║  ██║    ██╔══╝  ██║   ██║██║     ██╔══╝  ██╔══██╗    ██║╚██╔╝██║██╔══╝     ██║   ██╔══██║██║   ██║██║  ██║\n" +
                "██║██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝ ╚████╔╝ ███████╗██████╔╝    ███████╗╚██████╔╝███████╗███████╗██║  ██║    ██║ ╚═╝ ██║███████╗   ██║   ██║  ██║╚██████╔╝██████╔╝\n" +
                "╚═╝╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝   ╚═══╝  ╚══════╝╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝    ╚═╝     ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ \n" +
                "                                                                                                                                                                     ");
        solveImprovedEuler(initialAngle, initialAngularVelocity, timeStep, totalSimulationTime);


        System.out.println("██████╗ ██╗  ██╗██╗  ██╗    ███╗   ███╗███████╗████████╗██╗  ██╗ ██████╗ ██████╗ \n" +
                "██╔══██╗██║ ██╔╝██║  ██║    ████╗ ████║██╔════╝╚══██╔══╝██║  ██║██╔═══██╗██╔══██╗\n" +
                "██████╔╝█████╔╝ ███████║    ██╔████╔██║█████╗     ██║   ███████║██║   ██║██║  ██║\n" +
                "██╔══██╗██╔═██╗ ╚════██║    ██║╚██╔╝██║██╔══╝     ██║   ██╔══██║██║   ██║██║  ██║\n" +
                "██║  ██║██║  ██╗     ██║    ██║ ╚═╝ ██║███████╗   ██║   ██║  ██║╚██████╔╝██████╔╝\n" +
                "╚═╝  ╚═╝╚═╝  ╚═╝     ╚═╝    ╚═╝     ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ \n");

        solveRK4(initialAngle, initialAngularVelocity, timeStep, totalSimulationTime);
    }

    public static double computeAngularRate(double currentAngle, double currentAngularVelocity) {
        return currentAngularVelocity;
    }

    public static double computeAngularAcceleration(double currentAngle, double currentAngularVelocity) {
        return -(gravity / pendulumRadius) * Math.sin(currentAngle);
    }

    public static void solveImprovedEuler(double initialPendulumAngle, double initialPendulumAngularVelocity, double simulationTimeStep, double simulationDuration) {
        System.out.println("time\tangle\t\tangVelocity\tpotentialE\tkineticE\ttotalEnergy");
        double currentAngle = initialPendulumAngle;
        double currentAngularVelocity = initialPendulumAngularVelocity;
        double simulationTime = 0.0;
        while (simulationTime <= simulationDuration) {
            double pendulumPotentialEnergy = gravity * (1 - Math.cos(currentAngle));
            double pendulumKineticEnergy = 0.5 * (currentAngularVelocity * currentAngularVelocity);
            double pendulumTotalEnergy = pendulumPotentialEnergy + pendulumKineticEnergy;
            System.out.printf("%.1f  \t%.6f\t%.6f\t%.6f\t%.6f\t%.6f%n", simulationTime, currentAngle, currentAngularVelocity, pendulumPotentialEnergy, pendulumKineticEnergy, pendulumTotalEnergy);
            double k1AngleRate = computeAngularRate(currentAngle, currentAngularVelocity);
            double k1AngularAcceleration = computeAngularAcceleration(currentAngle, currentAngularVelocity);
            double estimatedAngle = currentAngle + simulationTimeStep * k1AngleRate;
            double estimatedAngularVelocity = currentAngularVelocity + simulationTimeStep * k1AngularAcceleration;
            double k2AngleRate = computeAngularRate(estimatedAngle, estimatedAngularVelocity);
            double k2AngularAcceleration = computeAngularAcceleration(estimatedAngle, estimatedAngularVelocity);
            currentAngle += simulationTimeStep * 0.5 * (k1AngleRate + k2AngleRate);
            currentAngularVelocity += simulationTimeStep * 0.5 * (k1AngularAcceleration + k2AngularAcceleration);
            simulationTime += simulationTimeStep;
        }
    }

    public static void solveRK4(double initialPendulumAngle, double initialPendulumAngularVelocity, double simulationTimeStep, double simulationDuration) {
        System.out.println("time\tangle\t\tangVelocity\tpotentialE\tkineticE\ttotalEnergy");
        double currentAngle = initialPendulumAngle;
        double currentAngularVelocity = initialPendulumAngularVelocity;
        double simulationTime = 0.0;
        while (simulationTime <= simulationDuration) {
            double pendulumPotentialEnergy = gravity * (1 - Math.cos(currentAngle));
            double pendulumKineticEnergy = 0.5 * (currentAngularVelocity * currentAngularVelocity);
            double pendulumTotalEnergy = pendulumPotentialEnergy + pendulumKineticEnergy;
            System.out.printf("%.1f  \t%.6f\t%.6f\t%.6f\t%.6f\t%.6f%n", simulationTime, currentAngle, currentAngularVelocity, pendulumPotentialEnergy, pendulumKineticEnergy, pendulumTotalEnergy);
            double incrementAngleK1 = simulationTimeStep * computeAngularRate(currentAngle, currentAngularVelocity);
            double incrementAngularVelocityK1 = simulationTimeStep * computeAngularAcceleration(currentAngle, currentAngularVelocity);
            double incrementAngleK2 = simulationTimeStep * computeAngularRate(currentAngle + 0.5 * incrementAngleK1, currentAngularVelocity + 0.5 * incrementAngularVelocityK1);
            double incrementAngularVelocityK2 = simulationTimeStep * computeAngularAcceleration(currentAngle + 0.5 * incrementAngleK1, currentAngularVelocity + 0.5 * incrementAngularVelocityK1);
            double incrementAngleK3 = simulationTimeStep * computeAngularRate(currentAngle + 0.5 * incrementAngleK2, currentAngularVelocity + 0.5 * incrementAngularVelocityK2);
            double incrementAngularVelocityK3 = simulationTimeStep * computeAngularAcceleration(currentAngle + 0.5 * incrementAngleK2, currentAngularVelocity + 0.5 * incrementAngularVelocityK2);
            double incrementAngleK4 = simulationTimeStep * computeAngularRate(currentAngle + incrementAngleK3, currentAngularVelocity + incrementAngularVelocityK3);
            double incrementAngularVelocityK4 = simulationTimeStep * computeAngularAcceleration(currentAngle + incrementAngleK3, currentAngularVelocity + incrementAngularVelocityK3);
            currentAngle += (incrementAngleK1 + 2.0 * incrementAngleK2 + 2.0 * incrementAngleK3 + incrementAngleK4) / 6.0;
            currentAngularVelocity += (incrementAngularVelocityK1 + 2.0 * incrementAngularVelocityK2 + 2.0 * incrementAngularVelocityK3 + incrementAngularVelocityK4) / 6.0;
            simulationTime += simulationTimeStep;
        }
    }
}
