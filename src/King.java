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
    
        this.setPossibleMoves(moves);
    }

    private Move castleSmall() {
        int row;
        if (Game.isCastleSmall() == false) {
            if (this.getColor() == true) {
                row = 7;                //WHITE
            } else {
                row = 0;                //BLACK
            }
            if (!isBlocked(row, 5) || !isBlocked(row, 6)) {
                return new Move(row, 4, row, 6, row, 5);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Move castleBig() {
        int row;
        if (Game.isCastleBig() == false) {
            if (this.getColor() == true) {  
                row = 7;                //WHITE
            } else {
                row = 0;                //BLACK
            }
            if (!isBlocked(row, 1) || !isBlocked(row, 2) || !isBlocked(row, 3)) {
                return new Move(row, 4, row, 2, row, 3);
            } else {
                return null;
            }
        }
        return null;
    }

    public String getName() {
        if (this.getColor()) {
            return "king_w";
        } else {
            return "king_b";
        }
    }
}
