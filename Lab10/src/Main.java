import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main extends JPanel implements ActionListener, MouseListener {
    private static final int NUM_ROWS = 50;
    private static final int NUM_COLS = 50;
    private static final int CELL_SIZE = 10;

    private boolean[][] currentGeneration = new boolean[NUM_ROWS][NUM_COLS];
    private boolean[][] nextGeneration = new boolean[NUM_ROWS][NUM_COLS];

    private Set<Integer> birthCounts = new HashSet<>();
    private Set<Integer> survivalCounts = new HashSet<>();

    private Timer simulationTimer;
    private boolean isRunning = false;

    private JFrame mainFrame;
    private JButton startPauseButton;
    private JButton clearButton;
    private JButton randomizeButton;
    private JButton applyRulesButton;
    private JTextField rulesTextField;

    public Main() {
        parseRules("B3/S23");
        setPreferredSize(new Dimension(NUM_COLS * CELL_SIZE, NUM_ROWS * CELL_SIZE));
        setBackground(Color.WHITE);
        addMouseListener(this);
        simulationTimer = new Timer(100, this);

        mainFrame = new JFrame("Game of Life");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(this, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        startPauseButton = new JButton("Start");
        clearButton = new JButton("Clear");
        randomizeButton = new JButton("Randomize");
        rulesTextField = new JTextField("B3/S23", 6);
        applyRulesButton = new JButton("Apply Rules");

        startPauseButton.addActionListener(e -> startOrPauseSimulation());
        clearButton.addActionListener(e -> clearBoard());
        randomizeButton.addActionListener(e -> randomizeBoard());
        applyRulesButton.addActionListener(e -> parseRules(rulesTextField.getText().trim()));

        controlPanel.add(startPauseButton);
        controlPanel.add(clearButton);
        controlPanel.add(randomizeButton);
        controlPanel.add(new JLabel("Rules:"));
        controlPanel.add(rulesTextField);
        controlPanel.add(applyRulesButton);

        mainFrame.add(controlPanel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void startOrPauseSimulation() {
        isRunning = !isRunning;
        if (isRunning) {
            simulationTimer.start();
            startPauseButton.setText("Pause");
        } else {
            simulationTimer.stop();
            startPauseButton.setText("Start");
        }
    }

    private void clearBoard() {
        simulationTimer.stop();
        isRunning = false;
        startPauseButton.setText("Start");
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                currentGeneration[row][col] = false;
            }
        }
        repaint();
    }

    private void randomizeBoard() {
        Random rand = new Random();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                currentGeneration[row][col] = rand.nextDouble() < 0.2;
            }
        }
        repaint();
    }

    private void parseRules(String ruleString) {
        Set<Integer> births = new HashSet<>();
        Set<Integer> survives = new HashSet<>();
        ruleString = ruleString.toUpperCase();
        try {
            String[] parts = ruleString.split("/");
            for (String part : parts) {
                if (part.startsWith("B")) {
                    for (char c : part.substring(1).toCharArray()) {
                        births.add(Character.getNumericValue(c));
                    }
                } else if (part.startsWith("S")) {
                    for (char c : part.substring(1).toCharArray()) {
                        survives.add(Character.getNumericValue(c));
                    }
                }
            }
            if (births.isEmpty() || survives.isEmpty()) {
                throw new IllegalArgumentException();
            }
            birthCounts = births;
            survivalCounts = survives;
            System.out.println("Applied rules: " + ruleString);
            System.out.println(" - Birth when dead cell has exactly: " + birthCounts);
            System.out.println(" - Survival when live cell has exactly: " + survivalCounts);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Invalid rule format.\nUse B#/S#, e.g. B3/S23",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            rulesTextField.setText("B3/S23");
            parseRules("B3/S23");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                g.setColor(currentGeneration[row][col] ? Color.BLACK : Color.LIGHT_GRAY);
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.WHITE);
                g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                int livingNeighbors = countLivingNeighbors(row, col);
                if (currentGeneration[row][col]) {
                    nextGeneration[row][col] = survivalCounts.contains(livingNeighbors);
                } else {
                    nextGeneration[row][col] = birthCounts.contains(livingNeighbors);
                }
            }
        }
        boolean[][] temp = currentGeneration;
        currentGeneration = nextGeneration;
        nextGeneration = temp;
        repaint();
    }

    private int countLivingNeighbors(int row, int col) {
        int count = 0;
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue;
                int neighborRow = (row + dRow + NUM_ROWS) % NUM_ROWS;
                int neighborCol = (col + dCol + NUM_COLS) % NUM_COLS;
                if (currentGeneration[neighborRow][neighborCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / CELL_SIZE;
        int row = e.getY() / CELL_SIZE;
        if (row >= 0 && row < NUM_ROWS && col >= 0 && col < NUM_COLS) {
            currentGeneration[row][col] = !currentGeneration[row][col];
            repaint();
        }
    }

    public void mousePressed(MouseEvent e)  {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e)  {}
    public void mouseExited(MouseEvent e)   {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
