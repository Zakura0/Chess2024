import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(int row, int col, boolean color) {
        super(row, col, color);
    }

    public void setPossibleMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
    
        

        this._possibleMoves = moves;
    }

    public List<Move> getPossibleMoves() {
        return this._possibleMoves;
    }
}
