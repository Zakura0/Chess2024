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

    public void move(int rowKing, int colKing, int rowRook, int colRook) {
        
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

    List<Move> moveQueue = new ArrayList<>();

    public void addMoveToQueue(Move move) {
        this.moveQueue.add(move);
    }

    public List<Move> getQueue() {
        return this.moveQueue;
    }

    public void clearQueue() {
        this.moveQueue.clear();
    }
}

