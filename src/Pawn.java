import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(int row, int col, boolean color) {
        super(row, col, color);
    }

    public void calculatePossibleMoves() {
        List<Move> moves = new ArrayList<>();
        int row = this.getRow();
        int col = this.getCol();
        int direction = this.getColor() ? -1 : 1; // true für Weiß (bewegt sich nach oben), false für Schwarz (bewegt
                                                  // sich nach unten)

        // Gerade Bewegung
        int straightRow = row + direction;
        // Prüfe, ob der gerade Weg frei ist
        Move move = new Move(row, col, straightRow, col);
        if (straightRow >= 0 && straightRow < 8 && !isBlocked(straightRow, col)) {
            if (checkMoveValid(move)) {
                moves.add(move);
            }
            // Für den ersten Zug kann der Bauer zwei Felder vorrücken
            if ((this.getColor() && row == 6) || (!this.getColor() && row == 1)) {
                int jumpRow = row + (2 * direction);
                move = new Move(row, col, jumpRow, col);
                if (!isBlocked(jumpRow, col)) {
                    if (checkMoveValid(move)) {
                        moves.add(move);
                    }
                }
            }
        }

        // Diagonale Schlagbewegungen
        int[] attackCols = { col - 1, col + 1 };
        for (int attackCol : attackCols) {
            if (attackCol >= 0 && attackCol < 8 && straightRow >= 0 && straightRow < 8) {
                move = new Move(row, col, straightRow, attackCol);
                if (isOpponent(straightRow, attackCol)) {
                    if (checkMoveValid(move)) {
                        moves.add(move);
                    }
                }
            }
        }

        // en Passant
        /*
        if (enPassant(row, col) == true) {
            moves.add(new Move(row, col, row + direction, col + 1));
            moves.add(new Move(row, col, row + direction, col - 1));
        } */

        this.setPossibleMoves(moves);
    }

    public boolean enPassant(int row, int col) {


        if ((this.getColor() && row == 4) || (!this.getColor() && row == 3)) {
            Piece[] adjacentPieces = { getPiece(row, col - 1), getPiece(row, col + 1) };
            for (Piece piece : adjacentPieces) {
                if (piece != null && piece.getColor() != this.getColor() && piece instanceof Pawn) {
                    Game g = new Game();
                    Move m = new Move(0,0,0,0);
                    if (g.getQueue() != null && m.getMovingPiece() == piece && Math.abs(m.getCurrRow() - m.getDestRow()) == 2) {
                        return true;  
                    }
                }
            }
        }
        return false;
    }

    public String getName() {
        if (this.getColor()) {
            return "pawn_w";
        } else {
            return "pawn_b";
        }
    }
}
