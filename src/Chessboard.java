public class Chessboard {
    private final int size = 8;
    private final int[] queens = new int[size]; // queens[row] = column
    private int solutionCount = 0;

    public static void main(String[] args) {
        Chessboard board = new Chessboard();
        board.solve();
    }

    public void solve() {
        solveRecursive(0);
        if (solutionCount == 0) {
            System.out.println("No solution found.");
        }
    }

    private void solveRecursive(int row) {
        if (row == size) {
            printBoard();
            solutionCount++;
            return;
        }

        for (int col = 0; col < size; col++) {
            if (isSafe(row, col)) {
                queens[row] = col;
                solveRecursive(row + 1);
            }
        }
    }

    private boolean isSafe(int row, int col) {
        for (int prevRow = 0; prevRow < row; prevRow++) {
            int prevCol = queens[prevRow];
            if (prevCol == col ||
                prevCol - prevRow == col - row ||
                prevCol + prevRow == col + row) {
                return false;
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.println("Solution " + (solutionCount + 1) + ":");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (queens[row] == col) {
                    System.out.print(" Q ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
