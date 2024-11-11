import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RotatingCube extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private double angleX = 0;
    private double angleY = 0;
    private final double[][] cubeVertices = {
            {-1, -1, -1}, {-1, -1, 1}, {-1, 1, -1}, {-1, 1, 1},
            {1, -1, -1}, {1, -1, 1}, {1, 1, -1}, {1, 1, 1}
    };

    private final int[][] cubeEdges = {
            {0, 1}, {1, 3}, {3, 2}, {2, 0}, {4, 5}, {5, 7}, {7, 6}, {6, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    public RotatingCube() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        // Add key listener for arrow key events
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        angleY -= Math.PI / 30;
                        break;
                    case KeyEvent.VK_RIGHT:
                        angleY += Math.PI / 30;
                        break;
                    case KeyEvent.VK_UP:
                        angleX -= Math.PI / 30;
                        break;
                    case KeyEvent.VK_DOWN:
                        angleX += Math.PI / 30;
                        break;
                }
                repaint(); // Re-render the panel after key press
            }
        });
        setFocusable(true); // Focus for key events
    }

    private int[] project(double x, double y, double z) {
        // Perspective projection matrix
        double distance = 5; // View distance
        double factor = 200 / (distance + z); // Scale factor

        int screenX = (int) (x * factor + WIDTH / 2);
        int screenY = (int) (-y * factor + HEIGHT / 2);
        return new int[]{screenX, screenY};
    }

    private void rotateCube() {
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);

        for (int i = 0; i < cubeVertices.length; i++) {
            double[] vertex = cubeVertices[i];

            // Rotate around X axis
            double tempY = vertex[1] * cosX - vertex[2] * sinX;
            double tempZ = vertex[1] * sinX + vertex[2] * cosX;
            vertex[1] = tempY;
            vertex[2] = tempZ;

            // Rotate around Y axis
            double tempX = vertex[0] * cosY + vertex[2] * sinY;
            tempZ = -vertex[0] * sinY + vertex[2] * cosY;
            vertex[0] = tempX;
            vertex[2] = tempZ;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rotate the cube for each frame
        rotateCube();

        // Draw the cube edges
        g.setColor(Color.WHITE);
        for (int[] edge : cubeEdges) {
            int[] start = project(cubeVertices[edge[0]][0], cubeVertices[edge[0]][1], cubeVertices[edge[0]][2]);
            int[] end = project(cubeVertices[edge[1]][0], cubeVertices[edge[1]][1], cubeVertices[edge[1]][2]);

            g.drawLine(start[0], start[1], end[0], end[1]);
        }
    }

    public static void main(String[] args) {
        // Create the window frame
        JFrame frame = new JFrame("Rotating Cube");
        RotatingCube cube = new RotatingCube();
        frame.add(cube);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
