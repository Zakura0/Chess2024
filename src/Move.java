public class Move {

    private int _curr_row;
    private int _curr_col;
    private int _dest_row;
    private int _dest_col;
    private int _transformation;

    public Move(int curr_row, int curr_col, int dest_row, int dest_col) {
        _curr_row = curr_row;
        _curr_col = curr_col;
        _dest_row = dest_row;
        _dest_col = dest_col;
        _transformation = 0;
    }

    public Move(int curr_row, int curr_col, int dest_row, int dest_col, int transformation) {
        _curr_row = curr_row;
        _curr_col = curr_col;
        _dest_row = dest_row;
        _dest_col = dest_col;
        _transformation = transformation;
    }

    public void setTransformation(int transformation) {
        _transformation = transformation;
    }

    public int getTransformation() {
        return _transformation;
    }

    public Piece getMovingPiece() {
        return Board.board[_dest_row][_dest_col];
    }

    public int getCurrRow() {
        return _curr_row;
    }

    public int getCurrCol() {
        return _curr_col;
    }

    public int getDestRow() {
        return _dest_row;
    }

    public int getDestCol() {
        return _dest_col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Move move = (Move) obj;
        return this.getDestRow() == move.getDestRow() &&
                this.getDestCol() == move.getDestCol() &&
                this.getCurrRow() == move.getCurrRow() &&
                this.getCurrCol() == move.getCurrCol();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getDestRow();
        result = 31 * result + this.getDestCol();
        result = 31 * result + this.getCurrRow();
        result = 31 * result + this.getCurrCol();
        return result;
    }

    @Override
    public String toString() {
        return _curr_row + "," + _curr_col + "," + _dest_row + "," + _dest_col + "," + _transformation + ":";
    }

}
