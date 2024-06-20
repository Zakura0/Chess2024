import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    private boolean _isWhiteTurn;
    private static boolean _castleSmall;
    private static boolean _castleBig;
    private static boolean _isWhiteCheck;
    private static boolean _isBlackCheck;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            BoardGUI boardGUI = new BoardGUI(game);
            boardGUI.loadGUI();            
        });
    }

    public Game() {
        Board.initializeBoard();
        _isWhiteCheck = false;
        _isBlackCheck = false;
        _isWhiteTurn = true;
        _castleSmall = true;
        _castleBig = true;
        calculateAllMoves();
    }

    public void calculateAllMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = Board.board[i][j];
                if (piece == null) {
                    continue;
                }
                piece.calculatePossibleMoves();
            }
        }
    }

    private void changeTurn() {
        _isWhiteTurn = !_isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return _isWhiteTurn;
    }

    public static boolean isCastleSmall() {
        return _castleSmall;
    }

    public static boolean isCastleBig() {
        return _castleBig;
    }

    public void performMove(Piece piece, Move move) {
        if (piece.getColor() == _isWhiteTurn) {
            changeTurn();
        }
        piece.move(move.getDestRow(), move.getDestCol());
        calculateAllMoves();
        if (_isWhiteCheck) {
            System.out.println("White is in check");
            _isWhiteCheck = false;
        } 
        else if (_isBlackCheck) {
            System.out.println("Black is in check");
            _isBlackCheck = false;
        }
    }

    public static void setCheck(boolean color)
    {
        if(color)
        {
            _isWhiteCheck = true;
        }
        else
        {
            _isBlackCheck = true;
        }
    }

}
