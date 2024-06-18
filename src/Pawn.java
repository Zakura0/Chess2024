import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(int row, int col, boolean color) {
        super(row, col, color);
    }

    public void setPossibleMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
    
        

        this._possibleMoves = moves;
    }

    public List<Move> getPossibleMoves() {
        return this._possibleMoves;
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
