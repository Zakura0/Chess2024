import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(int row, int col, boolean color) {
        super(row, col, color);
    }

    public void setPossibleMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
    
        while(row != 1) {
            row--;
            moves.add(new Move())
        }

        this._possibleMoves = moves;
    }

    public List<Move> getPossibleMoves() {
        return this._possibleMoves;
    }
}
