package Game.Pieces;

import java.util.ArrayList;
import java.util.List;

import Game.Move;

/**
 * Klasse für den Läufer
 */
public class Bishop extends Piece {

    public Bishop(int row, int col, boolean color) {
        super(row, col, color);
    }

    public void calculatePossibleMoves() {
        List<Move> moves = new ArrayList<>();
        int row = this.getRow();
        int col = this.getCol();
        // Richtungsvektoren nur für die vier Diagonalen
        int[][] directions = {
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
                Move move = new Move(row, col, targetRow, targetCol);    
                // Prüfe, ob die neue Position blockiert ist
                if (isBlocked(targetRow, targetCol)) {
                    // Wenn ein Gegner blockiert, füge den Schlag hinzu
                    if (isOpponent(targetRow, targetCol)) {
                        if (checkMoveValid(move)) {
                            moves.add(move);
                        }
                    }
                    break; // Breche die Schleife ab, da der Weg blockiert ist
                }
    
                // Füge den Zug hinzu, wenn nicht blockiert
                if (checkMoveValid(move)) {
                    moves.add(move);
                }
            }
        }
    
        this.setPossibleMoves(moves);
    }

    public String getName() {
        if (this.getColor()) {
            return "bishop_w";
        } else {
            return "bishop_b";
        }
    }

	@Override
	public String getAlgebraicNotation() {
		return "B";
	}
}
