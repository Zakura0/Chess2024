public class Move {

    private int _curr_row;
    private int _curr_col;
    private int _dest_row;
    private int _dest_col;

    public Move(int curr_row, int curr_col, int dest_row, int dest_col) {
        _curr_row = curr_row;
        _curr_col = curr_col;
        _dest_row = dest_row;
        _dest_col = dest_col;
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
    
}