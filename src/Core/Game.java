package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import Core.Pieces.Bishop;
import Core.Pieces.King;
import Core.Pieces.Knight;
import Core.Pieces.Pawn;
import Core.Pieces.Piece;
import Core.Pieces.Queen;
import Core.Pieces.Rook;
import GUI.GUI;

/**
 * Die Game Klasse ist die Hauptklasse des Spiels und verwaltet die Spiellogik.
 * Sie erstellt außerdem die Benutzeroberfläche und initialisiert das Spiel.
 * @author Gruppe 02
 */
public class Game {
    public static boolean isWhite;
    public static List<Piece> whiteAlive;
    public static List<Piece> blackAlive;
    public static List<Piece> whiteDead;
    public static List<Piece> blackDead;
    private static King whiteKing;
    private static King blackKing;
    private String algebraic;
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
        algebraic = "";
        Board.initializeBoard();
        whiteKing = (King) Board.board[7][4];
        blackKing = (King) Board.board[0][4];
        calculateAllMoves();
    }

    private void calculateAllMoves() {
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

    /**
     * Diese Methode überprüft, ob das Feld unter Angriff steht,
     * indem alle möglichen Felder die dieses Feld angreifen könnten, überprüft
     * werden.
     * 
     * @param row   Zeile des Feldes
     * 
     * @param col   Spalte des Feldes
     * 
     * @param color Farbe des Königs
     * 
     * @return true, wenn das Feld unter Angriff steht, sonst false
     * 
     */
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

    /**
     * Diese Methode überprüft, ob ein Gegner in einer bestimmten Richtung ist.
     * 
     * @param row       Zeile des Feldes
     * 
     * @param col       Spalte des Feldes
     * 
     * @param direction Richtung in der überprüft wird
     */
    private static boolean checkDirectionForOpponent(int row, int col, int[] direction, boolean color,
            String opponentPiece1, String opponentPiece2) {
        int dRow = direction[0];
        int dCol = direction[1];
        int targetRow = row + dRow;
        int targetCol = col + dCol;
        while (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
            Piece piece = Board.board[targetRow][targetCol];
            if (piece != null) {
                if (!piece.getColor() == color
                        && (piece.getName().equals(opponentPiece1) || piece.getName().equals(opponentPiece2))) {
                    return true;
                }
                break;
            }
            targetRow += dRow;
            targetCol += dCol;
        }
        return false;
    }

    /**
     * Diese Methode führt einen Zug aus und aktualisiert das Spiel.
     * Hier wird auch überprüft, ob der König im Schach steht oder ob ein Schachmatt
     * oder Patt vorliegt.
     * Außerdem werden auch Rochade, En-Passant und Bauernumwandlung berücksichtigt.
     * 
     * @param piece Die ausgewählte Figur
     * 
     * @param move  Der ausgewählte Zug
     * 
     * 
     */
    public void performMove(Piece piece, Move move) {
        String anEnd = move.toAlgebraicNotationEnd();
        String anPiece = piece.getAlgebraicNotation();
        String anWholeMove = anPiece + anEnd; // Algebraic Notation
        if (GUI.startedClock == false) {
            GUI.startedClock = true;
            GUI.counter.start();
        }
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()]; // Falls der Move ein Schlag ist
        if (destPiece != piece && destPiece != null) {
            killPiece(destPiece);
            anWholeMove = anPiece + "x" + anEnd;
            gui.updateCapturedPieces(destPiece.getColor());
        }
        piece.move(move.getDestRow(), move.getDestCol()); // Führe den Zug aus
        if (piece instanceof Pawn && (move.getDestRow() == 0 || move.getDestRow() == 7)) { // Bauernumwandlung
            if (move.getTransformation() != 0) {
                switch (move.getTransformation()) {
                    case 1:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(),
                                move.getDestCol(), isWhite);
                        break;
                    case 2:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Rook(move.getDestRow(),
                                move.getDestCol(), isWhite);
                        break;
                    case 3:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Bishop(move.getDestRow(),
                                move.getDestCol(), isWhite);
                        break;
                    case 4:
                        Board.board[move.getDestRow()][move.getDestCol()] = new Knight(move.getDestRow(),
                                move.getDestCol(), isWhite);
                        break;
                    default:
                        break;
                }
            } else {
                int choice = gui.getPromotionChoice(move);
                move.setTransformation(choice);
                String transformChoice = "";
                switch (choice) {
                    case 1:
                        transformChoice = "Q";
                        break;
                    case 2:
                        transformChoice = "R";
                        break;
                    case 3:
                        transformChoice = "B";
                        break;
                    case 4:
                        transformChoice = "K";
                        break;
                }
                anWholeMove += "=" + transformChoice;
            }
        }
        addMoveToQueue(move); // Füge den Zug zur Queue hinzu
        boolean ep = checkEnPassant(piece, move, destPiece); // En-Passant
        if (ep) {
            anWholeMove += " e.p.";
        }
        boolean castle = isCastleMove(piece, move); // Rochade
        if (castle && anEnd.contains("c")) {
            anWholeMove = "0-0-0";
        } else if (castle && anEnd.contains("g")) {
            anWholeMove = "0-0";
        }
        calculateAllMoves(); // Berechne alle möglichen Züge
        if (checkForCheck(!piece.getColor())) { // Überprüfe ob der König im Schach steht
            String color = piece.getColor() ? "black" : "white";
            GUI.infoLabel.setText("The " + color + " king is in check!"); 
            anWholeMove += "+";
            if (checkForMateOrStalemate(!piece.getColor())) { // Überprüfe ob Schachmatt oder Patt vorliegt
                anWholeMove = anWholeMove.substring(0, anWholeMove.length() - 1);
                anWholeMove += "#";
                if (isWhite = true) {
                    GUI.infoLabel.setText("White won!");
                    GUI.counter.interrupt();
                } else {
                    GUI.infoLabel.setText("Black won!");
                    GUI.counter.interrupt();
                }
            }
        } else if (checkForMateOrStalemate(!piece.getColor())) {
            GUI.infoLabel.setText("Stalemate!");
            GUI.counter.interrupt();
        } else {
            GUI.infoLabel.setText(" ");
        }
        addBoardAsString(); // Füge den aktuellen Boardstate zur Map hinzu
        for (String key : boardStates.keySet()) {
            if (boardStates.get(key) == 3) { // Überprüfe auf Draw durch Wiederholung
                GUI.infoLabel.setText("Draw by repetition!");
                GUI.counter.interrupt();
            }
        }
        algebraic += anWholeMove + ";"; // Füge den Zug zur Algebraic Notation hinzu
        GUI.movesArea.append(anWholeMove + "\n"); // Füge den Zug zur GUI hinzu
        changeTurn(); // Wechsel den Zug
    }

    /**
     * Diese Methode überprüft ob ein Schachmatt oder Patt vorliegt.
     * 
     * @param color Farbe des Königs
     */
    private boolean checkForMateOrStalemate(boolean color) {
        List<Piece> kingPieces = color ? whiteAlive : blackAlive;
        for (Piece piece : kingPieces) {
            if (piece.getPossibleMoves().size() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Diese Methode entfernt eine Figur vom Brett und fügt sie in die Liste der
     * toten Figuren hinzu.
     * 
     * @param piece Die zu entfernende Figur
     * 
     */
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

    /**
     * Diese Methode überprüft ob ein Rochadezug ausgeführt wird.
     * 
     * @param piece Die ausgewählte Figur
     * 
     * @param move  Der ausgewählte Zug
     */
    private boolean isCastleMove(Piece piece, Move move) {
        if (performCastleMove(piece, move)) {
            return true;
        }
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
        return false;
    }

    /**
     * Diese Methode führt den Rochadezug aus.
     * 
     * @param piece Die ausgewählte Figur
     * 
     * @param move  Der ausgewählte Zug
     */
    private boolean performCastleMove(Piece piece, Move move) {
        int blackRow = 0;
        int whiteRow = 7;
        Piece whiteLeftRook = Board.board[whiteRow][0];
        Piece whiteRightRook = Board.board[whiteRow][7];
        Piece blackLeftRook = Board.board[blackRow][0];
        Piece blackRightRook = Board.board[blackRow][7];

        if (piece instanceof King && piece.getColor()) {
            if (move.getDestCol() == 2 && whiteLeftRook != null && whiteKing.getCastleBig()) {
                whiteLeftRook.move(whiteRow, 3);
                return true;
            } else if (move.getDestCol() == 6 && whiteRightRook != null && whiteKing.getCastleSmall()) {
                whiteRightRook.move(whiteRow, 5);
                return true;
            }
        } else if (piece instanceof King && !piece.getColor()) {
            if (move.getDestCol() == 2 && blackLeftRook != null && blackKing.getCastleBig()) {
                blackLeftRook.move(blackRow, 3);
                return true;
            } else if (move.getDestCol() == 6 && blackRightRook != null && blackKing.getCastleSmall()) {
                blackRightRook.move(blackRow, 5);
                return true;
            }
        }
        return false;
    }

    /**
     * Diese Methode überprüft ob ein En-Passant Zug ausgeführt wird.
     * 
     * @param piece     Die ausgewählte Figur
     * 
     * @param move      Der ausgewählte Zug
     * 
     * @param destPiece Die Figur die geschlagen wird
     */
    private boolean checkEnPassant(Piece piece, Move move, Piece destPiece) {
        if (piece instanceof Pawn) {
            if (move.getCurrCol() != move.getDestCol() && destPiece == null) {
                int direction = move.getCurrRow() < move.getDestRow() ? -1 : 1;
                killPiece(Board.board[move.getDestRow() + direction][move.getDestCol()]);
                gui.updateCapturedPieces(Board.board[move.getDestRow() + direction][move.getDestCol()].getColor());
                Board.board[move.getDestRow() + direction][move.getDestCol()] = null;
                return true;
            }
        }   
        return false;
    }

    /**
     * Diese Methode fügt einen Zug zur Queue hinzu.
     * 
     * @param move Der ausgewählte Zug
     */
    public static void addMoveToQueue(Move move) {
        moveQueue.add(move);
    }

    /**
     * Diese Methode entfernt alle Züge aus der Queue.
     */
    public static void clearQueue() {
        moveQueue.clear();
    }

    /**
     * Diese Methode gibt den letzten Zug aus der Queue zurück.
     * Dies ist für En-Passant notwendig.
     */
    public static Move getLastMove() {
        if (moveQueue.isEmpty()) {
            return null;
        }
        return moveQueue.get(moveQueue.size() - 1);
    }

    /**
     * Diese Methode fügt den aktuellen Boardstate zur Map hinzu.
     */
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

    /**
     * Diese Methode setzt das Spiel zurück.
     */
    public void resetGame() {
        whiteAlive.clear();
        blackAlive.clear();
        whiteDead.clear();
        blackDead.clear();
        boardStates.clear();
        moveQueue.clear();
        algebraic = "";
        GUI.movesArea.setText("");
        GUI.infoLabel.setText("Welcome to Chess");
        isWhite = true;
        Board.initializeBoard();
        whiteKing = (King) Board.board[7][4];
        blackKing = (King) Board.board[0][4];
        calculateAllMoves();
        gui.repaintBoard();
    }

    /**
     * Diese Methode lädt ein Spiel.
     */
    public void saveGame() {
        SaveGame.saveGame(moveQueue);
    }
}
