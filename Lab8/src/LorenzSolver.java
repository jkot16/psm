public final class LorenzSolver {

    public static final double SIGMA= 10.0;
    public static final double RHO = 25.0;
    public static final double BETA  = 8.0 / 3.0;

    public static final double TIME_STEP = 0.03;
    public static final int MAX_STEPS = 5_000;
    public static final double DIVERGENCE_LIMIT = 100.0;

    public static void main(String[] args) {
        double[][] eulerPath = integrateEuler   (1.0, 1.0, 1.0);
        double[][] midpointPath = integrateMidpoint(1.0, 1.0, 1.0);
        double[][] rk4Path = integrateRK4     (1.0, 1.0, 1.0);

        printSummary("Euler   ", eulerPath);
        printSummary("Midpoint", midpointPath);
        printSummary("RK4     ", rk4Path);
    }

    private static void printSummary(String label, double[][] trajectory) {
        double[] last = trajectory[trajectory.length - 1];
        System.out.printf(
                "%s  steps=%d  final=(%.6f, %.6f, %.6f)%n",
                label, trajectory.length, last[0], last[1], last[2]
        );
    }

    public static double[][] integrateEuler(double x0, double y0, double z0)   { return solve(x0, y0, z0, Method.EULER); }
    public static double[][] integrateMidpoint(double x0, double y0, double z0){ return solve(x0, y0, z0, Method.MIDPOINT); }
    public static double[][] integrateRK4(double x0, double y0, double z0)     { return solve(x0, y0, z0, Method.RK4); }

    private enum Method { EULER, MIDPOINT, RK4 }

    private static double[][] solve(double x0, double y0, double z0, Method method) {
        double[][] path = new double[MAX_STEPS][3];
        int step = 0;
        double x = x0, y = y0, z = z0;

        for (; step < MAX_STEPS; step++) {
            if (isDiverging(x, y, z)) break;
            path[step][0] = x;
            path[step][1] = y;
            path[step][2] = z;

            switch (method) {
                case EULER -> {
                    double dx = fx(x, y, z);
                    double dy = fy(x, y, z);
                    double dz = fz(x, y, z);
                    x += TIME_STEP * dx;
                    y += TIME_STEP * dy;
                    z += TIME_STEP * dz;
                }
                case MIDPOINT -> {
                    double k1x = fx(x, y, z);
                    double k1y = fy(x, y, z);
                    double k1z = fz(x, y, z);

                    double midX = x + 0.5 * TIME_STEP * k1x;
                    double midY = y + 0.5 * TIME_STEP * k1y;
                    double midZ = z + 0.5 * TIME_STEP * k1z;

                    double k2x = fx(midX, midY, midZ);
                    double k2y = fy(midX, midY, midZ);
                    double k2z = fz(midX, midY, midZ);

                    x += TIME_STEP * k2x;
                    y += TIME_STEP * k2y;
                    z += TIME_STEP * k2z;
                }
                case RK4 -> {
                    double k1x = fx(x, y, z);
                    double k1y = fy(x, y, z);
                    double k1z = fz(x, y, z);

                    double x2 = x + 0.5 * TIME_STEP * k1x;
                    double y2 = y + 0.5 * TIME_STEP * k1y;
                    double z2 = z + 0.5 * TIME_STEP * k1z;
                    double k2x = fx(x2, y2, z2);
                    double k2y = fy(x2, y2, z2);
                    double k2z = fz(x2, y2, z2);

                    double x3 = x + 0.5 * TIME_STEP * k2x;
                    double y3 = y + 0.5 * TIME_STEP * k2y;
                    double z3 = z + 0.5 * TIME_STEP * k2z;
                    double k3x = fx(x3, y3, z3);
                    double k3y = fy(x3, y3, z3);
                    double k3z = fz(x3, y3, z3);

                    double x4 = x + TIME_STEP * k3x;
                    double y4 = y + TIME_STEP * k3y;
                    double z4 = z + TIME_STEP * k3z;
                    double k4x = fx(x4, y4, z4);
                    double k4y = fy(x4, y4, z4);
                    double k4z = fz(x4, y4, z4);

                    x += TIME_STEP * (k1x + 2 * k2x + 2 * k3x + k4x) / 6.0;
                    y += TIME_STEP * (k1y + 2 * k2y + 2 * k3y + k4y) / 6.0;
                    z += TIME_STEP * (k1z + 2 * k2z + 2 * k3z + k4z) / 6.0;
                }
            }
        }
        return java.util.Arrays.copyOf(path, step);
    }

    private static double fx(double x, double y, double z) { return SIGMA * (y - x); }
    private static double fy(double x, double y, double z) { return x * (RHO - z) - y; }
    private static double fz(double x, double y, double z) { return x * y - BETA * z; }

    private static boolean isDiverging(double x, double y, double z) {
        return Math.abs(x) > DIVERGENCE_LIMIT || Math.abs(y) > DIVERGENCE_LIMIT || Math.abs(z) > DIVERGENCE_LIMIT
                || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z);
    }

    private LorenzSolver() {}
}
