import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    private boolean _isWhiteTurn;
    private static boolean _castleSmall;
    private static boolean _castleBig;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            BoardGUI boardGUI = new BoardGUI(game);
            boardGUI.loadGUI();            
        });
    }

    public Game() {
        Board board = new Board();
        _castleSmall = true;
        _castleBig = true;
        calculateAllMoves();
        int test = 9;
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
}
