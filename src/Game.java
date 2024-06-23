import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    private boolean _isWhiteTurn;
    private static boolean _castleSmallWhite;
    private static boolean _castleBigWhite;
    private static boolean _castleSmallBlack;
    private static boolean _castleBigBlack;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            BoardGUI boardGUI = new BoardGUI(game);
            boardGUI.loadGUI();            
        });
    }

    public Game() {
        Board board = new Board();
        _isWhiteTurn = true;
        _castleSmallWhite = true;
        _castleBigWhite = true;
        _castleSmallBlack = true;
        _castleBigBlack = true;
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
        if (_castleSmallBlack || _castleSmallWhite) {
            return true;
        }
        return false;
    }

    public static boolean isCastleBig() {
        if (_castleBigBlack || _castleSmallBlack) {
            return true;
        }
        return false;
    }

    public void performMove(Piece piece, Move move) {
        if (piece.getColor() == _isWhiteTurn) {
            changeTurn();
        }
        checkCastling(piece);
        piece.move(move.getDestRow(), move.getDestCol());
        calculateAllMoves();
    }

    private void checkCastling(Piece piece) {
        int king = 4;
        int leftRook = 0;
        int rightRook = 7;
        if (piece.getColor()) {
            if (piece.getCol() == king) {
                _castleSmallWhite = false;
                _castleBigWhite = false;
            } else { 
                if (piece.getCol() == leftRook) {
                    _castleBigWhite = false;
                }
                if (piece.getCol() == rightRook) {
                    _castleSmallWhite = false;
                }
            }
        } else {
            if (piece.getCol() == king) {
                _castleSmallBlack = false;
                _castleBigBlack = false;
            } else { 
                if (piece.getCol() == leftRook) {
                    _castleBigBlack = false;
                }
                if (piece.getCol() == rightRook) {
                    _castleSmallBlack = false;
                }
            }
        }
    }

}
