import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    private boolean _isWhiteTurn;
    private static boolean _castleSmallWhite;
    private static boolean _castleBigWhite;
    private static boolean _castleSmallBlack;
    private static boolean _castleBigBlack;
    private static boolean _isWhiteCheck;
    private static boolean _isBlackCheck;
    public static List<Piece> whitePieces;
    public static List<Piece> blackPieces;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            BoardGUI boardGUI = new BoardGUI(game);
            boardGUI.loadGUI();
        });
    }

    public Game() {
        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();
        _isWhiteCheck = false;
        _isBlackCheck = false;
        _isWhiteTurn = true;
        _castleSmallWhite = true;
        _castleBigWhite = true;
        _castleSmallBlack = true;
        _castleBigBlack = true;
        Board.initializeBoard();
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
        if (_castleBigBlack || _castleBigWhite) {
            return true;
        }
        return false;
    }

    public boolean checkKingInCheck(boolean color) {
        List<Piece> kingPieces = color ? whitePieces : blackPieces;
        String opponentKnight = color ? "knight_b" : "knight_w";
        String opponentPawn = color ? "pawn_b" : "pawn_w";
        String opponentRook = color ? "rook_b" : "rook_w";
        String opponentBishop = color ? "bishop_b" : "bishop_w";
        String opponentQueen = "queen_" + (color ? "b" : "w");
    
        int KingRow = kingPieces.get(0).getRow();
        int KingCol = kingPieces.get(0).getCol();
    
        // Knight moves
        int[][] movesVectorsKnight = {
            { -2, -1 }, { -2, 1 },
            { 2, -1 }, { 2, 1 }, 
            { -1, -2 }, { 1, -2 }, 
            { -1, 2 }, { 1, 2 } 
        };
    
        for (int[] moveVector : movesVectorsKnight) {
            int targetRow = KingRow + moveVector[0];
            int targetCol = KingCol + moveVector[1];
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                Piece piece = Board.board[targetRow][targetCol];
                if (piece != null && !piece.getColor() == color && piece.getName().equals(opponentKnight)) {
                    return true;
                }
            }
        }
    
        // Pawn moves
        int pawnDirection = color ? -1 : 1;
        int straightRow = KingRow + pawnDirection;
        int[] attackCols = { KingCol - 1, KingCol + 1 };
    
        for (int attackCol : attackCols) {
            if (straightRow >= 0 && straightRow < 8 && attackCol >= 0 && attackCol < 8) {
                Piece piece = Board.board[straightRow][attackCol];
                if (piece != null && !piece.getColor() == color && piece.getName().equals(opponentPawn)) {
                    return true;
                }
            }
        }
    
        // Rook and Queen moves
        int[][] directionsRook = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directionsRook) {
            if (checkDirectionForOpponent(KingRow, KingCol, direction, color, opponentRook, opponentQueen)) {
                return true;
            }
        }
    
        // Bishop and Queen moves
        int[][] directionsBishop = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        for (int[] direction : directionsBishop) {
            if (checkDirectionForOpponent(KingRow, KingCol, direction, color, opponentBishop, opponentQueen)) {
                return true;
            }
        }
    
        return false;
    }
    
    private boolean checkDirectionForOpponent(int KingRow, int KingCol, int[] direction, boolean color, String opponentPiece1, String opponentPiece2) {
        int dRow = direction[0];
        int dCol = direction[1];
        int targetRow = KingRow + dRow;
        int targetCol = KingCol + dCol;
        while (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
            Piece piece = Board.board[targetRow][targetCol];
            if (piece != null) {
                if (!piece.getColor() == color && (piece.getName().equals(opponentPiece1) || piece.getName().equals(opponentPiece2))) {
                    return true;
                }
                break;
            }
            targetRow += dRow;
            targetCol += dCol;
        }
        return false;
    }

    public void performMove(Piece piece, Move move) {
        int startRow = piece.getRow();
        int startCol = piece.getCol();
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()];
        piece.move(move.getDestRow(), move.getDestCol());
        if(checkKingInCheck(piece.getColor())) {
            piece.move(startRow, startCol);
            Board.board[move.getDestRow()][move.getDestCol()] = destPiece;
            System.out.println("Invalid move, friendly king would be in check");
            return;
        }
        if(checkKingInCheck(!piece.getColor())) {
            String color = piece.getColor() ? "black" : "white";
            setCheck(!piece.getColor());
            System.out.println("The " + color + " king is in check!");
            checkForMate();
        }
        if (piece.getColor() == _isWhiteTurn) {
            changeTurn();
        }
        checkCastling(piece);
        piece.move(move.getDestRow(), move.getDestCol());
        calculateAllMoves();
    }

    private void checkForMate() {
        boolean isWhiteMate = true;
        boolean isBlackMate = true;
        for (Piece piece : whitePieces) {
            if (!piece.getPossibleMoves().isEmpty()) {
                isWhiteMate = false;
                break;
            }
        }
        for (Piece piece : blackPieces) {
            if (!piece.getPossibleMoves().isEmpty()) {
                isBlackMate = false;
                break;
            }
        }
        if (isWhiteMate) {
            System.out.println("White is in checkmate!");
        }
        if (isBlackMate) {
            System.out.println("Black is in checkmate!");
        }
    }

    public static void setCheck(boolean color) {
        if (color) {
            _isWhiteCheck = true;
        } else {
            _isBlackCheck = true;
        }
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
