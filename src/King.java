import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(int row, int col, boolean color) {
        super(row, col, color);
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
            int targetRow = row + dRow;
            int targetCol = col + dCol;
    
            // Prüfe, ob die neue Position außerhalb des Bretts liegt
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                // Prüfe, ob die neue Position blockiert ist
                if (!isBlocked(targetRow, targetCol) || isOpponent(targetRow, targetCol)) {
                    // Füge den Zug hinzu, wenn nicht blockiert oder ein Gegner geschlagen werden kann
                    moves.add(new Move(row, col, targetRow, targetCol));
                }
            }
        }
    
        this.setPossibleMoves(moves);
    }
}
