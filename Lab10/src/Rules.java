public class Rules {
    private int surviveMin;
    private int surviveMax;
    private int reviveExact;

    public Rules(int surviveMin, int surviveMax, int reviveExact) {
        this.surviveMin = surviveMin;
        this.surviveMax = surviveMax;
        this.reviveExact = reviveExact;
    }

    public boolean shouldSurvive(int neighbors) {
        return neighbors >= surviveMin && neighbors <= surviveMax;
    }

    public boolean shouldRevive(int neighbors) {
        return neighbors == reviveExact;
    }
}
