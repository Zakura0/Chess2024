import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    private boolean _isWhiteTurn;
    private boolean _isWhiteCheck; //Später für UI
    private boolean _isBlackCheck; //Später für UI
    public static List<Piece> whiteAlive;
    public static List<Piece> blackAlive;
    public static List<Piece> whiteDead;
    public static List<Piece> blackDead;
    public static King whiteKing;
    public static King blackKing;
    private static BoardGUI boardGUI;
    private static Pawn transformingPawn;
    private static Clock clock = new Clock();
    public static List<Move> moveQueue;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            boardGUI = new BoardGUI(game);
            boardGUI.loadGUI();
        });
    }

    public Game() {
        whiteAlive = new ArrayList<Piece>();
        blackAlive = new ArrayList<Piece>();
        whiteDead = new ArrayList<Piece>();
        blackDead = new ArrayList<Piece>();
        moveQueue = new ArrayList<Move>();
        _isWhiteCheck = false;
        _isBlackCheck = false;
        _isWhiteTurn = true;
        Board.initializeBoard();
        whiteKing = (King) Board.board[7][4];
        blackKing = (King) Board.board[0][4];
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

    public static boolean checkForCheck(boolean color) {
        List<Piece> kingPieces = color ? whiteAlive : blackAlive;
        int KingRow = kingPieces.get(0).getRow();
        int KingCol = kingPieces.get(0).getCol();
        return tileUnderAttack(KingRow, KingCol, color);
    }

    public static boolean tileUnderAttack(int row, int col, boolean color) {
        String opponentKnight = color ? "knight_b" : "knight_w";
        String opponentPawn = color ? "pawn_b" : "pawn_w";
        String opponentRook = color ? "rook_b" : "rook_w";
        String opponentBishop = color ? "bishop_b" : "bishop_w";
        String opponentQueen = color ? "queen_b" : "queen_w";
    
        // Knight moves
        int[][] movesVectorsKnight = {
            { -2, -1 }, { -2, 1 },
            { 2, -1 }, { 2, 1 }, 
            { -1, -2 }, { 1, -2 }, 
            { -1, 2 }, { 1, 2 } 
        };
    
        for (int[] moveVector : movesVectorsKnight) {
            int targetRow = row + moveVector[0];
            int targetCol = col + moveVector[1];
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                Piece piece = Board.board[targetRow][targetCol];
                if (piece != null && !piece.getColor() == color && piece.getName().equals(opponentKnight)) {
                    return true;
                }
            }
        }
    
        // Pawn moves
        int pawnDirection = color ? -1 : 1;
        int straightRow = row + pawnDirection;
        int[] attackCols = { col - 1, col + 1 };
    
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
            if (checkDirectionForOpponent(row, col, direction, color, opponentRook, opponentQueen)) {
                return true;
            }
        }
    
        // Bishop and Queen moves
        int[][] directionsBishop = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        for (int[] direction : directionsBishop) {
            if (checkDirectionForOpponent(row, col, direction, color, opponentBishop, opponentQueen)) {
                return true;
            }
        }
    
        return false;
    }
    
    private static boolean checkDirectionForOpponent(int row, int col, int[] direction, boolean color, String opponentPiece1, String opponentPiece2) {
        int dRow = direction[0];
        int dCol = direction[1];
        int targetRow = row + dRow;
        int targetCol = col + dCol;
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
        if (Clock.started == false) {
            Clock.started = true; Clock.counter.start(); 
        }
		if (Clock.isWhite) {
            Clock.isWhite = false;
        } 
        else {
            Clock.isWhite = true;
        }
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()];
        killPiece(destPiece);
        piece.move(move.getDestRow(), move.getDestCol());
        addMoveToQueue(move);
        if (piece instanceof Pawn) {
            checkTransform((Pawn) piece);
        }
        checkEnPassant(piece, move, destPiece);
        performCastleMove(piece, move);
        checkForCastleMove(piece);
        calculateAllMoves();
        if(checkForCheck(!piece.getColor())) {
            String color = piece.getColor() ? "black" : "white";
            setCheck(!piece.getColor());
            Clock.winner.setText("The " + color + " king is in check!"); //Später für UI
            if (checkForMateOrStalemate(!piece.getColor()))
            {
                if (Clock.isWhite = true){
                    Clock.winner.setText("White won!");
                    Clock.draw.setEnabled(false);
                }
                else {
                    Clock.winner.setText("Black won!");
                    Clock.draw.setEnabled(false);
                }
            }
        }
        else if (checkForMateOrStalemate(!piece.getColor()))
        {
            Clock.winner.setText("Stalemate!");
            Clock.draw.setEnabled(false);
        }
        else {
            setUncheck(!piece.getColor());
        }
        changeTurn();        
    }

    private boolean checkForMateOrStalemate(boolean color) {
        List<Piece> kingPieces = color ? whiteAlive : blackAlive;
        for (Piece piece : kingPieces) {
            if (piece.getPossibleMoves().size() > 0) {
                return false;
            }            
        }
        return true;   
    }

    private void killPiece(Piece piece) {
        if (piece != null) {
            if (piece.getColor()) {
                whiteAlive.remove(piece);
                whiteDead.add(piece);
            } else {
                blackAlive.remove(piece);
                blackDead.add(piece);
            }
        }        
    }

    public void setCheck(boolean color) {
        if (color) {
            _isWhiteCheck = true;
        } else {
            _isBlackCheck = true;
        }
    }

    public void setUncheck(boolean color) {
        if (color) {
            _isWhiteCheck = false;
        } else {
            _isBlackCheck = false;
        }
    }

    private void checkForCastleMove(Piece piece) {
        if (piece instanceof King) {
            if (piece.getColor()) {
                whiteKing.setCastleBig(false);
                whiteKing.setCastleSmall(false);
            } else {
                blackKing.setCastleBig(false);
                blackKing.setCastleSmall(false);
            }
        } else if (piece instanceof Rook) {
            System.out.println("Is Rook move");
            if (piece.getColor()) {
                if (piece.getCol() == 0) {
                    whiteKing.setCastleBig(false);
                } else if (piece.getCol() == 7) {
                    whiteKing.setCastleSmall(false);
                }
            } else {
                if (piece.getCol() == 0) {
                    blackKing.setCastleBig(false);
                } else if (piece.getCol() == 7) {
                    blackKing.setCastleSmall(false);
                }
            }
        }
    }

    private void performCastleMove(Piece piece, Move move) {
        int blackRow = 0;
        int whiteRow = 7;
        Piece whiteLeftRook = Board.board[whiteRow][0];
        Piece whiteRightRook = Board.board[whiteRow][7]; 
        Piece blackLeftRook = Board.board[blackRow][0]; 
        Piece blackRightRook = Board.board[blackRow][7]; 

        if (piece instanceof King && piece.getColor() == true) {
            if (move.getDestCol() == 2) {
                whiteLeftRook.move(whiteRow, 3);
            } else if (move.getDestCol() == 6) {
                whiteRightRook.move(whiteRow, 5);
            }
        } else if (piece instanceof King && piece.getColor() == false) {
            if (move.getDestCol() == 2) {
                blackLeftRook.move(blackRow, 3);
            } else if (move.getDestCol() == 6) {
                blackRightRook.move(blackRow, 5);
            }
        }
    }

    private void checkEnPassant(Piece piece, Move move, Piece destPiece) {
        if (piece instanceof Pawn) {
            if (move.getCurrCol() != move.getDestCol() && destPiece == null){
                int direction = move.getCurrCol() < move.getDestCol() ? -1 : 1;
                killPiece(Board.board[move.getDestRow() + direction][move.getDestCol()]);
                Board.board[move.getDestRow() + direction][move.getDestCol()] = null;
            }
        }
    }

    public static void addMoveToQueue(Move move) {
        moveQueue.add(move);
    }

    public static void clearQueue() {
        moveQueue.clear();
    }

    public static Move getLastMove() {
        if (moveQueue.isEmpty()) {
            return null;
        }
        return moveQueue.get(moveQueue.size() - 1);
    }


    private void checkTransform(Pawn piece) {
        if (piece.getRow() == 0) {
            boardGUI.showTransform(piece);
        } else if (piece.getRow() == 7) {
            boardGUI.showTransform(piece);
        }
    }

    public static void performTransform(String piece) {
        Pawn p = transformingPawn;
        if (piece.equals("rook")) {
            Board.board[p.getRow()][p.getCol()] = new Rook(p.getRow(), p.getCol(), p.getColor());
        } else if (piece.equals("bishop")) {
            Board.board[p.getRow()][p.getCol()] = new Bishop(p.getRow(), p.getCol(), p.getColor());
        } else if (piece.equals("knight")) {
            Board.board[p.getRow()][p.getCol()] = new Knight(p.getRow(), p.getCol(), p.getColor());
        } else if (piece.equals("queen")) {
            Board.board[p.getRow()][p.getCol()] = new Queen(p.getRow(), p.getCol(), p.getColor());
        }
        boardGUI.hideTransform();
        transformingPawn = null;
    }
}
