public class Move {

    private int _curr;
    private int _dest;

    public Move(int curr, int dest) {
        _curr = curr;
        _dest = dest;
    }

    public int getCurr() {
        return _curr;
    }

    public int getDest() {
        return _dest;
    }
    
}
