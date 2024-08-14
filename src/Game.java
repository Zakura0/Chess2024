import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    public static boolean isWhite;
    public static List<Piece> whiteAlive;
    public static List<Piece> blackAlive;
    public static List<Piece> whiteDead;
    public static List<Piece> blackDead;
    public static King whiteKing;
    public static King blackKing;
    private GUI gui;
    public static List<Move> moveQueue;
    private Map<String, Integer> boardStates;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Game();
        });
    }

    public Game() {
        gui = new GUI(this);
        whiteAlive = new ArrayList<Piece>();
        blackAlive = new ArrayList<Piece>();
        whiteDead = new ArrayList<Piece>();
        blackDead = new ArrayList<Piece>();
        boardStates = new HashMap<String, Integer>();
        moveQueue = new ArrayList<Move>();
        isWhite = true;
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
        isWhite = !isWhite;
    }

    public boolean isWhiteTurn() {
        return isWhite;
    }

    public static boolean checkForCheck(boolean color) {
        King king = color ? whiteKing : blackKing;
        int KingRow = king.getRow();
        int KingCol = king.getCol();
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

    public int getPromotionChoice(Move move) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Choose a piece for promotion:",
            "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(), isWhite);
                return 1;
            case 1:
                Board.board[move.getDestRow()][move.getDestCol()] = new Rook(move.getDestRow(), move.getDestCol(), isWhite);
                return 2;
            case 2:
                Board.board[move.getDestRow()][move.getDestCol()] = new Bishop(move.getDestRow(), move.getDestCol(), isWhite);
                return 3;
            case 3:
                Board.board[move.getDestRow()][move.getDestCol()] = new Knight(move.getDestRow(), move.getDestCol(), isWhite);
                return 4;
            default:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(), isWhite);
                return 1;
        }
    }

    public void performMove(Piece piece, Move move) {
        if (GUI.startedClock == false) {
            GUI.startedClock = true;
            GUI.counter.start(); 
        }
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()];
        if (destPiece != piece)
        {
            killPiece(destPiece);
        }
        piece.move(move.getDestRow(), move.getDestCol());
        if (piece instanceof Pawn && (move.getDestRow() == 0 || move.getDestRow() == 7)) {
            if (move.getTransformation() != 0) {
                switch (move.getTransformation()) {
                    case 1:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(), isWhite);
                        break;
                    case 2:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Rook(move.getDestRow(), move.getDestCol(), isWhite);
                        break;
                    case 3:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Bishop(move.getDestRow(), move.getDestCol(), isWhite);    
                        break;
                    case 4:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Knight(move.getDestRow(), move.getDestCol(), isWhite);       
                        break;
                    default:
                        break;
                }
            }
            else{
                int choice = getPromotionChoice(move);
                move.setTransformation(choice);
            }
        }
        addMoveToQueue(move);    
        checkEnPassant(piece, move, destPiece);
        isCastleMove(piece, move);
        calculateAllMoves();
        if(checkForCheck(!piece.getColor())) {
            String color = piece.getColor() ? "black" : "white";
            GUI.infoLabel.setText("The " + color + " king is in check!"); //Später für UI
            if (checkForMateOrStalemate(!piece.getColor()))
            {
                if (isWhite = true){
                    GUI.infoLabel.setText("White won!");
                    GUI.counter.interrupt();
                }
                else {
                    GUI.infoLabel.setText("Black won!");
                    GUI.counter.interrupt();
                }
            }
        }
        else if (checkForMateOrStalemate(!piece.getColor()))
        {
            GUI.infoLabel.setText("Stalemate!");
            GUI.counter.interrupt();
        }
        else {
            GUI.infoLabel.setText("");
        }
        addBoardAsString();
        for (String key : boardStates.keySet()) {
            if (boardStates.get(key) == 3) {
                GUI.infoLabel.setText("Draw by repetition!");
                GUI.counter.interrupt();
            }
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

    private void isCastleMove(Piece piece, Move move) {
        performCastleMove(piece, move);
        if (piece instanceof King) {
            if (piece.getColor()) {
                whiteKing.setCastleBig(false);
                whiteKing.setCastleSmall(false);
            } else {
                blackKing.setCastleBig(false);
                blackKing.setCastleSmall(false);
            }
        } else if (piece instanceof Rook) {
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

        if (piece instanceof King && piece.getColor()) {
            if (move.getDestCol() == 2 && whiteLeftRook != null) {
                whiteLeftRook.move(whiteRow, 3);
            } else if (move.getDestCol() == 6 && whiteRightRook != null) {
                whiteRightRook.move(whiteRow, 5);
            }
        } else if (piece instanceof King && !piece.getColor()) {
            if (move.getDestCol() == 2 && blackLeftRook != null) {
                blackLeftRook.move(blackRow, 3);
            } else if (move.getDestCol() == 6 && blackRightRook != null) {
                blackRightRook.move(blackRow, 5);
            }
        }
    }

    private void checkEnPassant(Piece piece, Move move, Piece destPiece) {
        if (piece instanceof Pawn) {
            if (move.getCurrCol() != move.getDestCol() && destPiece == null){
                int direction = move.getCurrRow() < move.getDestRow() ? -1 : 1;
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

    private void addBoardAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = Board.board[i][j];
                if (piece != null) {
                    sb.append(piece.getName() + i + j + ",");
                }
            }
        }
        sb.append("cbw:" + whiteKing.getCastleBig() + ",csw:" + whiteKing.getCastleSmall() + ",");
        sb.append("cbb:" + blackKing.getCastleBig() + ",csb:" + blackKing.getCastleSmall());
        String boardState = sb.toString();
        boardStates.put(boardState, boardStates.getOrDefault(boardState, 0) + 1);
    }

    public void resetGame()
    {
        whiteAlive.clear();
        blackAlive.clear();
        whiteDead.clear();
        blackDead.clear();
        boardStates.clear();
        moveQueue.clear();
        isWhite = true;
        Board.initializeBoard();
        whiteKing = (King) Board.board[7][4];
        blackKing = (King) Board.board[0][4];
        calculateAllMoves();
        gui.repaintBoard();
    }

    public void saveGame()
    {
        SaveGame.saveGame(moveQueue);
    }
}
