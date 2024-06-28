import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    private boolean _smallCastle;
    private boolean _bigCastle;

    public King(int row, int col, boolean color, boolean dead) {
        super(row, col, color, dead);
        _smallCastle = true;
        _bigCastle = true;
    }

    public boolean getCastleSmall() {
        return _smallCastle;
    }

    public boolean getCastleBig() {
        return _bigCastle;
    }

    public void setCastleSmall(boolean isPossible) {
        _smallCastle = isPossible;
    }

    public void setCastleBig(boolean isPossible) {
        _bigCastle = isPossible;
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
        if (_smallCastle) {
            int targetRowKing = row;
            int targetColKing = col + 2;
            if (!isBlocked(row, 5) && !isBlocked(row, 6)) {
                if (isFriendlyRook(row, 7)) {
                    moves.add(new Move(row, col, targetRowKing, targetColKing));
                }
            }
            
        }
        if (_bigCastle) {
            int targetRowKing = row;
            int targetColKing = col - 2;
            if (!isBlocked(row, 1) && !isBlocked(row, 2) && !isBlocked(row, 3)) {
                if (isFriendlyRook(row, 0)) {
                    moves.add(new Move(row, col, targetRowKing, targetColKing));
                }
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
