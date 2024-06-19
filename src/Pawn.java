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
        int direction = this.getColor() ? -1 : 1; // true für Weiß (bewegt sich nach oben), false für Schwarz (bewegt sich nach unten)
    
        // Gerade Bewegung
        int straightRow = row + direction;
        // Prüfe, ob der gerade Weg frei ist
        if (straightRow >= 0 && straightRow < 8 && !isBlocked(straightRow, col)) {
            moves.add(new Move(row, col, straightRow, col));
    
            // Für den ersten Zug kann der Bauer zwei Felder vorrücken
            if ((this.getColor() && row == 6) || (!this.getColor() && row == 1)) {
                int jumpRow = row + (2 * direction);
                if (!isBlocked(jumpRow, col)) {
                    moves.add(new Move(row, col, jumpRow, col));
                } 
            }
        }
    
        // Diagonale Schlagbewegungen
        int[] attackCols = {col - 1, col + 1};
        for (int attackCol : attackCols) {
            if (attackCol >= 0 && attackCol < 8 && isOpponent(straightRow, attackCol)) {
                moves.add(new Move(row, col, straightRow, attackCol));
            }
        }
    
        this.setPossibleMoves(moves);
    }

    public boolean enPassantAvailable(Move move) {

        if (!(move.getMovingPiece() instanceof Pawn)) {
            return false;
        }
    
        Piece capturedPiece = move.getCapturedPiece();
    
        if (!(capturedPiece instanceof Pawn) || capturedPiece.getColor() == this.getColor()) {
            return false;
        }

        if (capturedPiece.getLastMoveDistance() != 2) {
            return false;
        }
        // Check if enemy pawn is next to ally pawn, while also fulfilling the condition that the enemy pawns last move had a distance of 2
        int destRow = move.getCurrRow();
        int destCol = move.getCurrCol();
        int capturedRow = move.getDestRow();
        int capturedCol = move.getDestCol();
        if (Math.abs(destRow - capturedRow) != 1 || Math.abs(destCol - capturedCol) != 1) {
            return false;
        }
        // If condition is met, en Passant must be available
        return true;
    }

    public String getName() {
        if (this.getColor()) {
            return "pawn_w";
        } else {
            return "pawn_b";
        }
    }
}
