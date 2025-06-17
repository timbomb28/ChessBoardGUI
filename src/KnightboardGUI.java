import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KnightboardGUI extends JPanel {
    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    private final List<Point> knights = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Knight Placement Animation");
        KnightboardGUI gui = new KnightboardGUI();
        frame.add(gui);
        frame.setSize(TILE_SIZE * BOARD_SIZE + 16, TILE_SIZE * BOARD_SIZE + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(gui::animateKnights).start();
    }

    private void animateKnights() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    knights.add(new Point(col, row));
                    repaint();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
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

        g.setColor(Color.BLUE);
        g.setFont(new Font("SansSerif", Font.BOLD, 36));
        for (Point knight : knights) {
            g.drawString("♞", knight.x * TILE_SIZE + 12, knight.y * TILE_SIZE + 44);
        }
    }
}
