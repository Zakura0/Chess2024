
import java.util.ArrayList;
import java.util.List;


public abstract class Piece {

    private int _row;
    private int _col;
    private boolean _color;
    private List<Move> _possibleMoves;

    public Piece(int row, int col, boolean color){ 
        this._row = row;
        this._col = col;
        this._color = color;
        this._possibleMoves = new ArrayList<>();
    }

    public void move(int row, int col){
        Board.board[this._row][this._col] = null;
        Board.board[row][col] = this;
        this._row = row;
        this._col = col;
    }

    public static Piece getPiece(int row, int col) {
        return Board.board[row][col];
    }

    public int getCol(){
        return this._col;
    }

    public int getRow(){
        return this._row;
    }

    public boolean getColor(){
        return this._color;
    }

    public List<Move> getPossibleMoves(){
        return this._possibleMoves;
    }

    public void setPossibleMoves(List<Move> moves){
        this._possibleMoves = moves;
    }

    protected boolean checkMoveValid(Move move){
        int startRow = move.getCurrRow();
        int startCol = move.getCurrCol();
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()];
        this.move(move.getDestRow(), move.getDestCol());
        if(Game.checkForCheck(this.getColor())) {
            this.move(startRow, startCol);
            Board.board[move.getDestRow()][move.getDestCol()] = destPiece;
            return false;
        }
        else {
            this.move(startRow, startCol);
            Board.board[move.getDestRow()][move.getDestCol()] = destPiece;
            return true;
        }
    }

    public abstract void calculatePossibleMoves();

    public abstract String getName();

    protected boolean isBlocked(int row, int col) {
        if(Board.board[row][col] != null) {
            return true;
        }
        return false;
    }

    protected boolean isOpponent(int row, int col) {
        if(Board.board[row][col] != null) {
            if(Board.board[row][col]._color != this._color) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFriendlyRook(int row, int col) {
        if (Board.board[row][col] instanceof Rook && !isOpponent(row, col)) {
            return true;
        } else {
            return false;
        }
    }


}

