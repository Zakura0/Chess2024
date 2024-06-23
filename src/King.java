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
                if (isBlocked(targetRow, targetCol)) {
                    if (isOpponent(targetRow, targetCol)) {
                        moves.add(new Move(row, col, targetRow, targetCol));
                    }
                }
                else
                {
                    moves.add(new Move(row, col, targetRow, targetCol));
                }
            }
        }
        if (Game.isCastleSmall()) {
            int targetRowKing = row;
            int targetColKing = col + 2;
            int targetRowRook = row;
            int targetColRook = col - 2;
            if (!isBlocked(row, 5) && !isBlocked(row, 6)) {
                moves.add(new Move(row, col, targetRowKing, targetColKing, targetRowRook, targetColRook));
            }
            
        }
        if (Game.isCastleBig()) {
            int targetRowKing = row;
            int targetColKing = col - 2;
            int targetRowRook = row;
            int targetColRook = col + 3;
            if (!isBlocked(row, 1) && !isBlocked(row, 2) && !isBlocked(row, 3)) {
                moves.add(new Move(row, col, targetRowKing, targetColKing, targetRowRook, targetColRook));
            }
        }
    
        this.setPossibleMoves(moves);
    }
    
    public String getName() {
        if (this.getColor()) {
            return "king_w";
        } else {
            return "king_b";
        }
    }
}
