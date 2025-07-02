import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Stack;

public class Main extends JPanel {
    private String lSystemString;
    private final int iterationCount;
    private final double rotationAngleDegrees = 25.0;
    private final int lineSegmentLength = 9;

    public Main(int iterationCount) {
        this.iterationCount = iterationCount;
        generateLSystemString();
    }

    private void generateLSystemString() {
        lSystemString = "X";
        for (int i = 0; i < iterationCount; i++) {
            StringBuilder nextString = new StringBuilder();
            for (char symbol : lSystemString.toCharArray()) {
                if (symbol == 'X') {
                    nextString.append("F+[[X]-X]-F[-FX]+X");
                } else if (symbol == 'F') {
                    nextString.append("FF");
                } else {
                    nextString.append(symbol);
                }
            }
            lSystemString = nextString.toString();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        Stack<AffineTransform> transformStack = new Stack<>();

        AffineTransform initialTransform = new AffineTransform();
        initialTransform.translate(getWidth() / 2.0, getHeight());
        graphics2D.setTransform(initialTransform);

        for (char command : lSystemString.toCharArray()) {
            switch (command) {
                case 'F':
                    graphics2D.drawLine(0, 0, 0, -lineSegmentLength);
                    graphics2D.translate(0, -lineSegmentLength);
                    break;
                case '+':
                    graphics2D.rotate(Math.toRadians(rotationAngleDegrees));
                    break;
                case '-':
                    graphics2D.rotate(-Math.toRadians(rotationAngleDegrees));
                    break;
                case '[':
                    transformStack.push(graphics2D.getTransform());
                    break;
                case ']':
                    graphics2D.setTransform(transformStack.pop());
                    break;
            }
        }
    }

    public static void main(String[] args) {
        int defaultIterations = 5;

        JFrame window = new JFrame("Fractal Plant");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(new Main(defaultIterations));
        window.setSize(1200, 800);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
