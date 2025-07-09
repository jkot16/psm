public class Game {
    private int rows, cols;
    private boolean[][] board;
    private Rules rules;

    public Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new boolean[rows][cols];
        this.rules = new Rules(2, 3, 3);
    }

    public void setInitialState(boolean[][] state) {
        for (int i = 0; i < rows; i++)
            System.arraycopy(state[i], 0, board[i], 0, cols);
    }

    public void nextGeneration() {
        boolean[][] next = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbors = countLiveNeighbors(i, j);

                if (board[i][j]) {
                    next[i][j] = rules.shouldSurvive(neighbors);
                } else {
                    next[i][j] = rules.shouldRevive(neighbors);
                }
            }
        }

        board = next;
    }

    private int countLiveNeighbors(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = (x + dx + rows) % rows;
                int ny = (y + dy + cols) % cols;
                if (board[nx][ny]) count++;
            }
        return count;
    }

    public void setRules(int surviveMin, int surviveMax, int reviveExact) {
        this.rules = new Rules(surviveMin, surviveMax, reviveExact);
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] ? "O" : ".");
            }
            System.out.println();
        }
        System.out.println();
    }
}
