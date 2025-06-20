import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NQueens extends JPanel {
    private int size;
    private int[] queens;

    private Point currentTest = null;
    private Set<Point> highlightedThreats = new HashSet<>();

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Bitte gib N ein (N ≥ 4):");
        if (input == null) return;
        int n;
        try {
            n = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ungültige Eingabe.");
            return;
        }

        if (n < 4) {
            JOptionPane.showMessageDialog(null, "Für N < 4 gibt es keine Lösung.");
            return;
        }

        JFrame frame = new JFrame("N-Queens Lösung für N = " + n);
        NQueens panel = new NQueens(n);
        frame.add(panel);
        frame.setSize(n * 60 + 16, n * 60 + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(panel::solve).start();
    }

    public NQueens(int n) {
        this.size = n;
        this.queens = new int[n];
        Arrays.fill(queens, -1);
    }

    private void solve() {
        placeQueen(0);
    }

    private boolean placeQueen(int row) {
        if (row == size) return true;

        for (int col = 0; col < size; col++) {
            currentTest = new Point(col, row);
            if (isSafe(row, col)) {
                highlightedThreats.clear();
                queens[row] = col;
                repaint();
                sleep(200);
                if (placeQueen(row + 1)) return true;
            } else {
                highlightedThreats = getThreatsFromQueen(row, col);
                repaint();
                sleep(200);
            }
        }

        queens[row] = -1;
        currentTest = null;
        highlightedThreats.clear();
        repaint();
        sleep(200);
        return false;
    }

    private Set<Point> getThreatsFromQueen(int row, int col) {
        Set<Point> threats = new HashSet<>();
        for (int r = 0; r < size; r++) {
            if (r != row) threats.add(new Point(col, r));
        }
        for (int r = 0; r < size; r++) {
            int diff = r - row;
            if (col + diff >= 0 && col + diff < size && r != row)
                threats.add(new Point(col + diff, r));
            if (col - diff >= 0 && col - diff < size && r != row)
                threats.add(new Point(col - diff, r));
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
        int tileSize = getWidth() / size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                g.setColor((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }

        g.setColor(new Color(255, 0, 0, 100));
        for (Point p : highlightedThreats) {
            g.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
        }

        if (currentTest != null) {
            g.setColor(new Color(0, 255, 0, 100));
            g.fillRect(currentTest.x * tileSize, currentTest.y * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, tileSize));
        for (int row = 0; row < size; row++) {
            int col = queens[row];
            if (col != -1) {
                g.drawString("♛", col * tileSize + tileSize / 5, row * tileSize + tileSize * 4 / 5);
            }
        }
    }
}