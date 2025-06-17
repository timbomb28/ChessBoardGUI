import javax.swing.*;
import java.awt.*;

import java.util.HashSet;
import java.util.Set;
import java.awt.Point;

public class ChessboardGUI extends JPanel {
    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    private int[] queens = new int[BOARD_SIZE];
    private Set<Point> highlightedThreats = new HashSet<>();
    private Point currentTest = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("N-Queens Visualizer");
        ChessboardGUI gui = new ChessboardGUI();
        frame.add(gui);
        frame.setSize(TILE_SIZE * BOARD_SIZE + 16, TILE_SIZE * BOARD_SIZE + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Thread(gui::startSolving).start();
    }

    private void startSolving() {
        solve(0);
    }

    private boolean solve(int row) {
        if (row == BOARD_SIZE) {
            highlightedThreats.clear();
            currentTest = null;
            repaint();
            return true; // Zeige nur die erste Lösung
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            currentTest = new Point(col, row);
            repaint();
            sleep(300);

            if (isSafe(row, col)) {
                queens[row] = col;
                highlightedThreats.clear();
                currentTest = null;
                repaint();
                sleep(300);
                if (solve(row + 1)) {
                    return true;
                }
            } else {
                highlightedThreats = getThreatsFromQueen(row, col);
                repaint();
                sleep(300);
            }
        }
        queens[row] = -1; // Backtracking
        highlightedThreats.clear();
        currentTest = null;
        repaint();
        sleep(300);
        return false;
    }

    private Set<Point> getThreatsFromQueen(int row, int col) {
        Set<Point> threats = new HashSet<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            if (r != row) threats.add(new Point(col, r)); // gleiche Spalte
            int diagRight = col + (r - row);
            int diagLeft = col - (r - row);
            if (diagRight >= 0 && diagRight < BOARD_SIZE && r != row) {
                threats.add(new Point(diagRight, r));
            }
            if (diagLeft >= 0 && diagLeft < BOARD_SIZE && r != row) {
                threats.add(new Point(diagLeft, r));
            }
        }
        return threats;
    }

    private boolean isSafe(int row, int col) {
        for (int i = 0; i < row; i++) {
            int qCol = queens[i];
            if (qCol == col || Math.abs(qCol - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                g.setColor((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Aktuell getestetes Feld hervorheben
        if (currentTest != null) {
            g.setColor(new Color(0, 255, 0, 100));
            g.fillRect(currentTest.x * TILE_SIZE, currentTest.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Bedrohte Felder hervorheben
        if (!highlightedThreats.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255, 0, 0, 120));
            for (Point p : highlightedThreats) {
                g2.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
            g2.dispose();
        }

        // Damen zeichnen
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 36));
        for (int row = 0; row < BOARD_SIZE; row++) {
            int col = queens[row];
            if (col != -1) {
                g.drawString("♛", col * TILE_SIZE + 12, row * TILE_SIZE + 44);
            }
        }
    }

    public ChessboardGUI() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            queens[i] = -1;
        }
    }
}