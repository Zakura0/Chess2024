
import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    private boolean _smallCastle;
    private boolean _bigCastle;

    public King(int row, int col, boolean color) {
        super(row, col, color);
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
            Move move = new Move(row, col, targetRow, targetCol);    
            // Prüfe, ob die neue Position außerhalb des Bretts liegt
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                // Prüfe, ob die neue Position blockiert ist
                if (isBlocked(targetRow, targetCol)) {
                    if (isOpponent(targetRow, targetCol)) {
                        if (checkMoveValid(move)) {
                            moves.add(move);
                        }
                    }
                }
                else
                {
                    if (checkMoveValid(move)) {
                        moves.add(move);
                    }
                }
            }
        }
        if (_smallCastle) {
            int targetRowKing = row;
            int targetColKing = col + 2;
            boolean castleUnderAttack = false;
            if (!isBlocked(row, 5) && !isBlocked(row, 6)) {
                for (int i = 4; i < 7; i++) {
                    if (Game.tileUnderAttack(row, i, this.getColor())) {
                        castleUnderAttack = true;
                    }
                }
                if (isFriendlyRook(row, 7) && !castleUnderAttack) {
                    moves.add(new Move(row, col, targetRowKing, targetColKing));
                }
            }
            
        }
        if (_bigCastle) {
            int targetRowKing = row;
            int targetColKing = col - 2;
            boolean castleUnderAttack = false;
            if (!isBlocked(row, 1) && !isBlocked(row, 2) && !isBlocked(row, 3)) {
                for (int i = 2; i < 5; i++) {
                    if (Game.tileUnderAttack(row, i, this.getColor())) {
                        castleUnderAttack = true;
                    }
                }
                if (isFriendlyRook(row, 0) && !castleUnderAttack) {
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

	@Override
	public String getAlgebraicNotation() {
		return "K";
	}
}
