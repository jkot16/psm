class Simulation {
    private int pointCount;
    private double spatialStep;
    private double timeStep;
    private int totalSteps;
    private double[][] yHistory;
    private double[] times, potential, kinetic, total;

    public Simulation(int pointCount, double length, double timeStep, double totalTime) {
        this.pointCount = pointCount;
        this.spatialStep = length / pointCount;
        this.timeStep = timeStep;
        this.totalSteps = (int) (totalTime / timeStep);
        this.times = new double[totalSteps + 1];
        this.potential = new double[totalSteps + 1];
        this.kinetic = new double[totalSteps + 1];
        this.total = new double[totalSteps + 1];
        this.yHistory = new double[totalSteps + 1][pointCount + 1];
    }

    public void run() {
        double[] y = new double[pointCount + 1];
        double[] v = new double[pointCount + 1];
        double[] a = new double[pointCount + 1];
        double[] yHalf = new double[pointCount + 1];
        double[] vHalf = new double[pointCount + 1];


        for (int i = 0; i <= pointCount; i++) {
            y[i] = Math.sin(i * spatialStep);
            v[i] = 0;
        }
        yHistory[0] = y.clone();
        times[0] = 0;
        kinetic[0] = 0;
        double ep0 = 0;
        for (int i = 0; i < pointCount; i++) {
            double diff = y[i + 1] - y[i];
            ep0 += (diff * diff) / (2 * spatialStep);
        }
        potential[0] = ep0;
        total[0] = ep0;


        for (int step = 1; step <= totalSteps; step++) {
            times[step] = step * timeStep;

            for (int i = 1; i < pointCount; i++) {
                a[i] = (y[i - 1] - 2 * y[i] + y[i + 1]) / (spatialStep * spatialStep);
            }

            for (int i = 1; i < pointCount; i++) {
                vHalf[i] = v[i] + a[i] * timeStep / 2;
                yHalf[i] = y[i] + v[i] * timeStep / 2;
            }
            yHalf[0] = yHalf[pointCount] = vHalf[0] = vHalf[pointCount] = 0;

            for (int i = 1; i < pointCount; i++) {
                double aHalf = (yHalf[i - 1] - 2 * yHalf[i] + yHalf[i + 1]) / (spatialStep * spatialStep);
                v[i] += aHalf * timeStep;
                y[i] += vHalf[i] * timeStep;
            }
            y[0] = y[pointCount] = v[0] = v[pointCount] = 0;


            yHistory[step] = y.clone();


            double ekVal = 0;
            for (int i = 0; i <= pointCount; i++) {
                ekVal += (spatialStep * v[i] * v[i]) / 2;
            }
            double epVal = 0;
            for (int i = 0; i < pointCount; i++) {
                double diff = y[i + 1] - y[i];
                epVal += (diff * diff) / (2 * spatialStep);
            }
            kinetic[step] = ekVal;
            potential[step] = epVal;
            total[step] = ekVal + epVal;
        }
    }

    public int getPointCount() { return pointCount; }
    public double getSpatialStep() { return spatialStep; }
    public double getTimeStep() { return timeStep; }
    public double[] getTimes() { return times; }
    public double[] getPotential() { return potential; }
    public double[] getKinetic() { return kinetic; }
    public double[] getTotal() { return total; }
    public double[][] getYHistory() { return yHistory; }
    public int getTotalSteps() { return totalSteps; }
}

public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(10, Math.PI, 0.01, 2 * Math.PI);
        sim.run();
        System.out.println("Time\tPotentialEnergy\tKineticEnergy\tTotalEnergy");
        double[] t = sim.getTimes();
        double[] ep = sim.getPotential();
        double[] ek = sim.getKinetic();
        double[] et = sim.getTotal();
        for (int i = 0; i < t.length; i++) {
            System.out.printf("%f\t%f\t%f\t%f%n", t[i], ep[i], ek[i], et[i]);
        }
    }
}