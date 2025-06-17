import javax.swing.*;
import java.awt.*;

public class ChessboardGUI extends JPanel {
    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    private int[] queens = new int[BOARD_SIZE];

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
            repaint();
            return true; // Zeige nur die erste Lösung
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            if (isSafe(row, col)) {
                queens[row] = col;
                repaint();
                sleep(300); // Animationsverzögerung
                if (solve(row + 1)) {
                    return true;
                }
            }
        }
        queens[row] = -1; // Backtracking
        repaint();
        sleep(300);
        return false;
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