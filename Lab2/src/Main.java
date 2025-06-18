import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * SAMPLE INPUT to test the simulation:
 * projectile mass [kg]: 2
 * drag coefficient k (0=vacuum, >0=with air resistance): 0,05
 * initial speed [m/s]: 30
 * launch angle [degrees]: 40
 * time step dt [s]: 0,02
 * maximum simulation time [s]: 15
 *
 * ATTENTION - Even though the maximum simulation time is set to 15 seconds,
 * the program will stop after around 3 seconds with given sample data.
 * That is because from logical perspective we should end the loop as soon as
 * the projectile height goes below 0 - meaning it landed. It can be observed
 * in the printed output.
 */

public class Main {
    private static final double GRAVITY = 9.81;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter projectile mass [kg]: ");
        double projectileMass = scanner.nextDouble();

        System.out.print("Enter drag coefficient (>0=with air resistance, 0=vacuum): ");
        double dragCoefficient = scanner.nextDouble();

        System.out.print("Enter initial speed [m/s]: ");
        double initialSpeed = scanner.nextDouble();

        System.out.print("Enter launch angle [degrees]: ");
        double launchAngleDegrees = scanner.nextDouble();

        System.out.print("Enter time step [s]: ");
        double timeStep = scanner.nextDouble();

        System.out.print("Enter maximum simulation time [s]: ");
        double maxSimulationTime = scanner.nextDouble();

        scanner.close();

        double launchAngleRadians = Math.toRadians(launchAngleDegrees);

        double initialVx = initialSpeed * Math.cos(launchAngleRadians);
        double initialVy = initialSpeed * Math.sin(launchAngleRadians);

        System.out.println("███████╗██╗   ██╗██╗     ███████╗██████╗ ███████╗    ███╗   ███╗███████╗████████╗██╗  ██╗ ██████╗ ██████╗ \n" +
                "██╔════╝██║   ██║██║     ██╔════╝██╔══██╗██╔════╝    ████╗ ████║██╔════╝╚══██╔══╝██║  ██║██╔═══██╗██╔══██╗\n" +
                "█████╗  ██║   ██║██║     █████╗  ██████╔╝███████╗    ██╔████╔██║█████╗     ██║   ███████║██║   ██║██║  ██║\n" +
                "██╔══╝  ██║   ██║██║     ██╔══╝  ██╔══██╗╚════██║    ██║╚██╔╝██║██╔══╝     ██║   ██╔══██║██║   ██║██║  ██║\n" +
                "███████╗╚██████╔╝███████╗███████╗██║  ██║███████║    ██║ ╚═╝ ██║███████╗   ██║   ██║  ██║╚██████╔╝██████╔╝\n" +
                "╚══════╝ ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ \n" +
                "                                                                                                          ");
        simulateWithEulerMethod(projectileMass, dragCoefficient, initialVx, initialVy, timeStep, maxSimulationTime);

        System.out.println("███╗   ███╗██╗██████╗ ██████╗  ██████╗ ██╗███╗   ██╗████████╗    ███╗   ███╗███████╗████████╗██╗  ██╗ ██████╗ ██████╗ \n" +
                "████╗ ████║██║██╔══██╗██╔══██╗██╔═══██╗██║████╗  ██║╚══██╔══╝    ████╗ ████║██╔════╝╚══██╔══╝██║  ██║██╔═══██╗██╔══██╗\n" +
                "██╔████╔██║██║██║  ██║██████╔╝██║   ██║██║██╔██╗ ██║   ██║       ██╔████╔██║█████╗     ██║   ███████║██║   ██║██║  ██║\n" +
                "██║╚██╔╝██║██║██║  ██║██╔═══╝ ██║   ██║██║██║╚██╗██║   ██║       ██║╚██╔╝██║██╔══╝     ██║   ██╔══██║██║   ██║██║  ██║\n" +
                "██║ ╚═╝ ██║██║██████╔╝██║     ╚██████╔╝██║██║ ╚████║   ██║       ██║ ╚═╝ ██║███████╗   ██║   ██║  ██║╚██████╔╝██████╔╝\n" +
                "╚═╝     ╚═╝╚═╝╚═════╝ ╚═╝      ╚═════╝ ╚═╝╚═╝  ╚═══╝   ╚═╝       ╚═╝     ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ \n" +
                "                                                                                                                      ");
        simulateWithMidpointMethod(projectileMass, dragCoefficient, initialVx, initialVy, timeStep, maxSimulationTime);
    }


    private static void simulateWithEulerMethod(double projectileMass, double dragCoefficient, double initialVx, double initialVy, double timeStep, double maxSimulationTime) {
        double posX = 0.0;
        double posY = 0.0;
        double velX = initialVx;
        double velY = initialVy;
        double currentTime = 0.0;

        List<Double> timeHistory = new ArrayList<>();
        List<Double> xHistory = new ArrayList<>();
        List<Double> yHistory = new ArrayList<>();
        List<Double> vxHistory = new ArrayList<>();
        List<Double> vyHistory = new ArrayList<>();

        timeHistory.add(currentTime);
        xHistory.add(posX);
        yHistory.add(posY);
        vxHistory.add(velX);
        vyHistory.add(velY);

        while (currentTime < maxSimulationTime && posY >= 0.0) {
            double speed = Math.sqrt(velX * velX + velY * velY);

            double accelX = - (dragCoefficient / projectileMass) * speed * velX;
            double accelY = - GRAVITY - (dragCoefficient / projectileMass) * speed * velY;

            velX += accelX * timeStep;
            velY += accelY * timeStep;

            posX += velX * timeStep;
            posY += velY * timeStep;

            currentTime += timeStep;

            timeHistory.add(currentTime);
            xHistory.add(posX);
            yHistory.add(posY);
            vxHistory.add(velX);
            vyHistory.add(velY);
        }

        for (int i = 0; i < timeHistory.size(); i++) {
            System.out.printf("t=%.3f  x=%.4f  y=%.4f  vx=%.4f  vy=%.4f%n", timeHistory.get(i), xHistory.get(i), yHistory.get(i), vxHistory.get(i), vyHistory.get(i)
            );
        }
    }


    private static void simulateWithMidpointMethod(double projectileMass, double dragCoefficient, double initialVx, double initialVy, double timeStep, double maxSimulationTime) {
        double posX = 0.0;
        double posY = 0.0;
        double velX = initialVx;
        double velY = initialVy;
        double currentTime = 0.0;

        List<Double> timeHistory = new ArrayList<>();
        List<Double> xHistory = new ArrayList<>();
        List<Double> yHistory = new ArrayList<>();
        List<Double> vxHistory = new ArrayList<>();
        List<Double> vyHistory = new ArrayList<>();

        timeHistory.add(currentTime);
        xHistory.add(posX);
        yHistory.add(posY);
        vxHistory.add(velX);
        vyHistory.add(velY);

        while (currentTime < maxSimulationTime && posY >= 0.0) {
            double speed = Math.sqrt(velX * velX + velY * velY);
            double accelX = - (dragCoefficient / projectileMass) * speed * velX;
            double accelY = - GRAVITY - (dragCoefficient / projectileMass) * speed * velY;

            double velXHalf = velX + accelX * timeStep / 2.0;
            double velYHalf = velY + accelY * timeStep / 2.0;
            double posXHalf = posX + velX * timeStep / 2.0;
            double posYHalf = posY + velY * timeStep / 2.0;

            double speedHalf = Math.sqrt(velXHalf * velXHalf + velYHalf * velYHalf);
            double accelXHalf = - (dragCoefficient / projectileMass) * speedHalf * velXHalf;
            double accelYHalf = - GRAVITY - (dragCoefficient / projectileMass) * speedHalf * velYHalf;

            velX += accelXHalf * timeStep;
            velY += accelYHalf * timeStep;
            posX += velXHalf * timeStep;
            posY += velYHalf * timeStep;


            currentTime += timeStep;

            timeHistory.add(currentTime);
            xHistory.add(posX);
            yHistory.add(posY);
            vxHistory.add(velX);
            vyHistory.add(velY);
        }

        for (int i = 0; i < timeHistory.size(); i++) {
            System.out.printf("t=%.3f  x=%.4f  y=%.4f  vx=%.4f  vy=%.4f%n", timeHistory.get(i), xHistory.get(i), yHistory.get(i), vxHistory.get(i), vyHistory.get(i));
        }
    }
}



