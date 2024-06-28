import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(int row, int col, boolean color, boolean dead) {
        super(row, col, color, dead);
    }

    public void calculatePossibleMoves() {
        List<Move> moves = new ArrayList<>();
        int row = this.getRow();
        int col = this.getCol();
        // Richtungsvektoren für oben, unten, links, rechts, und die vier Diagonalen
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Gerade
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonal
        };
    
        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];
            int targetRow = row;
            int targetCol = col;
    
            while (true) {
                targetRow += dRow;
                targetCol += dCol;
    
                // Prüfe, ob die neue Position außerhalb des Bretts liegt
                if (targetRow < 0 || targetRow >= 8 || targetCol < 0 || targetCol >= 8) {
                    break;
                }
    
                // Prüfe, ob die neue Position blockiert ist
                if (isBlocked(targetRow, targetCol)) {
                    // Wenn ein Gegner blockiert, füge den Schlag hinzu
                    if (isOpponent(targetRow, targetCol)) {
                        moves.add(new Move(row, col, targetRow, targetCol));
                    }
                    break; // Breche die Schleife ab, da der Weg blockiert ist
                }
    
                // Füge den Zug hinzu, wenn nicht blockiert
                moves.add(new Move(row, col, targetRow, targetCol));
            }
        }
    
        this.setPossibleMoves(moves);
    }

    public String getName() {
        if (this.getColor()) {
            return "queen_w";
        } else {
            return "queen_b";
        }
    }
}
