package Game.Pieces;

import java.util.ArrayList;
import java.util.List;

import Game.Move;
/**
 * Die Klasse Knight repräsentiert den Springer im Schachspiel.
 * 
 * @see Piece
 */
public class Knight extends Piece {
    public Knight(int row, int col, boolean color) {
        super(row, col, color);
    }

    /*
     * @see Piece#calculatePossibleMoves()
     */
    public void calculatePossibleMoves() {
        List<Move> moves = new ArrayList<>();
        int row = this.getRow();
        int col = this.getCol();
        int[][] movesVectors = {
                { -2, -1 }, { -2, 1 }, // Oben links, Oben rechts
                { 2, -1 }, { 2, 1 }, // Unten links, Unten rechts
                { -1, -2 }, { 1, -2 }, // Links oben, Links unten
                { -1, 2 }, { 1, 2 } // Rechts oben, Rechts unten
        };

        for (int[] moveVector : movesVectors) {
            int targetRow = row + moveVector[0];
            int targetCol = col + moveVector[1];
            Move move = new Move(row, col, targetRow, targetCol);
            // Prüfe, ob die neue Position innerhalb des Bretts liegt
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                // Prüfe, ob die neue Position blockiert ist oder ein Gegner steht
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

        this.setPossibleMoves(moves);
    }

    public String getName() {
        if (this.getColor()) {
            return "knight_w";
        } else {
            return "knight_b";
        }
    }

	@Override
	public String getAlgebraicNotation() {
		return "N";
	}
}
