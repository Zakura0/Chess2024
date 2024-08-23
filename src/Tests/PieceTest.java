package Tests;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

import Core.Pieces.*;
import Core.Game;
import Core.Board;
import Core.Move;

public class PieceTest {

    Piece concretePiece;

    Pawn pawn_w;
    Bishop bishop_w;
    Knight knight_w;
    Rook rook_w;
    Queen queen_w;
    King king_w;

    Pawn pawn_b;
    Bishop bishop_b;
    Knight knight_b;
    Rook rook_b;
    Queen queen_b;
    King king_b;

    List<Move> moves;

    Game game;
    Board board;

    @BeforeEach
    public void setUp() {
        concretePiece = new Queen(3, 4, true);        // Konkrete Instanz zum testen einer abstrakten Klasse
        GUI.MainGUI.unitTest = true;                                    // Ermoeglicht das Umgehen des Startdialogs
        game = new Game();                                          // Hier wird die Startaufstellung generiert
        Board.board[3][4] = concretePiece;

        pawn_w = (Pawn) Board.board[6][3];
        bishop_w = (Bishop) Board.board[7][2];
        knight_w = (Knight) Board.board[7][1];
        rook_w = (Rook) Board.board[7][0];
        queen_w = (Queen) Board.board[7][3];
        king_w = (King) Board.board[7][4];

        pawn_b = (Pawn) Board.board[1][4];
        bishop_b = (Bishop) Board.board[0][2];
        knight_b = (Knight) Board.board[0][1];
        rook_b = (Rook) Board.board[0][0];
        queen_b = (Queen) Board.board[0][3];
        king_b = (King) Board.board[0][4];

        moves = new ArrayList<>();
    }


    /*
     * Testen der implementierten Methoden der abstrakten Klasse Piece
     */

     /*
      * Testet das Bewegen einer Spielfigur auf eine neue Position (positiver Fall).
      * Der Test ist POSITIV, wenn die Figur korrekt auf die neue Position bewegt wird.
      */

    @Test
    public void testMovePositive() {
        concretePiece.move(4, 4);
        assertEquals(4, concretePiece.getRow());
        assertEquals(4, concretePiece.getCol());
    }

    /*
     * Testet das Bewegen einer Spielfigur (negativer Fall).
     * Der Test ist POSITIV, wenn die Figur nicht auf der alten Position bleibt.
     */

    @Test
    public void testMoveNegative() {
        concretePiece.move(4, 4);
        assertNotEquals(3, concretePiece.getRow());
        assertNotEquals(3, concretePiece.getCol());
    }

    /*
     * Testet das Abrufen einer Figur von einer bestimmten Position (positiver Fall).
     * Der Test ist POSITIV, wenn die Figur an der angegebenen Position eine Dame ist.
     */

    @Test
    public void testGetPiecePositive() {
        assertTrue(Piece.getPiece(3, 4) instanceof Queen);
    }

    /*
     * Testet das Abrufen einer Figur von einer bestimmten Position (negativer Fall).
     * Der Test ist POSITIV, wenn die Figur an der angegebenen Position keine Dame ist.
     */

    @Test
    public void testGetPieceNegative() {
        assertFalse(Piece.getPiece(2, 2) instanceof Queen);
    }

    /*
     * Testet das Abrufen einer Figur von einer leeren Position.
     * Der Test ist POSITIV, wenn null zurückgegeben wird.
     */

    @Test
    public void testGetPieceNull() {
        assertNull(Piece.getPiece(5, 5));
    }

    /*
     * Testet das Abrufen der Spalte einer Figur.
     * Der Test ist POSITIV, wenn die Spalte der Figur korrekt zurückgegeben wird.
     */

    @Test
    @Order(1)
    public void testGetCol() {
        assertEquals(4, concretePiece.getCol());
    }

    /*
     * Testet das Abrufen der Reihe einer Figur.
     * Der Test ist POSITIV, wenn die Reihe der Figur korrekt zurückgegeben wird.
     */

    @Test
    @Order(2)
    public void testGetRow() {
        assertEquals(3, concretePiece.getRow());
    }

    /*
     * Testet das Abrufen der Farbe einer Figur.
     * Der Test ist POSITIV, wenn die Farbe der Figur korrekt zurückgegeben wird.
     */

    @Test
    @Order(3)
    public void testGetColor() {
        assertEquals(true, concretePiece.getColor());
    }

    /*
     * Testet das Berechnen der möglichen Züge, wenn keine Züge gesetzt wurden.
     * Der Test ist POSITIV, wenn die Liste der möglichen Züge leer ist.
     */

    @Test
    public void testCalculatePossibleMovesWhenNotSet() {
        assertNotNull(concretePiece.getPossibleMoves());
        assertTrue(concretePiece.getPossibleMoves().isEmpty());
    }

    /*
     * Testet das Berechnen der möglichen Züge, wenn Züge gesetzt wurden.
     * Der Test ist POSITIV, wenn die Liste der möglichen Züge nicht leer ist und den gesetzten Zügen entspricht.
     */

    @Test
    public void testCalculatePossibleMovesWhenSet() {
        Move move = new Move(3, 4, 3, 3);
        concretePiece.getPossibleMoves().add(move);
        assertFalse(concretePiece.getPossibleMoves().isEmpty());
        assertEquals(move, concretePiece.getPossibleMoves().get(0));
    }

    /*
     * Testet das Setzen der möglichen Züge (positiver Fall).
     * Der Test ist POSITIV, wenn die Liste der möglichen Züge korrekt gesetzt wird.
     */

    @Test
    public void testSetPossibleMovesPositive() {
        Move move1 = new Move(3, 4, 3, 3);
        Move move2 = new Move(3, 4, 3, 5);
        moves.add(move1);
        moves.add(move2);
        
        concretePiece.setPossibleMoves(moves);
        assertFalse(concretePiece.getPossibleMoves().isEmpty());
        assertEquals(moves, concretePiece.getPossibleMoves());
    }

    /*
     * Testet das Setzen der möglichen Züge mit einer null-Liste.
     * Der Test ist POSITIV, wenn die Liste der möglichen Züge null ist.
     */

    @Test
    public void testSetPossibleMovesNull() {
        concretePiece.setPossibleMoves(null);
        assertNull(concretePiece.getPossibleMoves());
    }

    /*
     * Testen der abstrakten Methoden
     * Aus Trivialitaetsgruenden werden getName und getAlgebraicNotation nur einmal getestest
     */

     /*
     * Testet das Abrufen des Namens der Figur.
     * Der Test ist POSITIV, wenn der Name korrekt zurückgegeben wird.
     */

     @Test
    public void testGetName() {
        assertEquals("queen_w", concretePiece.getName());
    }

    /*
     * Testet das Abrufen der algebraischen Notation für eine Nicht-Bauer-Figur.
     * Der Test ist POSITIV, wenn die Notation korrekt ist.
     */

    @Test
    public void testGetAlgebraicNotationNonPawn() {
        assertEquals("Q", concretePiece.getAlgebraicNotation());
    }

    /*
     * Testet das Abrufen der algebraischen Notation für einen Bauern.
     * Der Test ist POSITIV, wenn die Notation für den Bauern korrekt ist (leere Zeichenkette).
     */

    @Test
    public void testGetAlgebraicNotationPawn() {
        assertEquals("", pawn_w.getAlgebraicNotation());
    }

    /*
     * Pawn
     */

     /*
     * Testet das Berechnen der möglichen Züge eines Bauern von der Startposition.
     * Der Test ist POSITIV, wenn die möglichen Züge des Bauern korrekt berechnet werden.
     */

    @Test
    public void testCalculatePossibleMovesStartPos_P() {
        moves.add(new Move(6, 3, 5, 3)); // Normaler Zug, das Feld vor pawn_w ist frei
        moves.add(new Move(6, 3, 4, 3)); // Jump (2 Felder), da kein Zug ausgefuehrt wurde, moeglich
        pawn_w.calculatePossibleMoves();
        assertNotNull(pawn_w.getPossibleMoves());
        assertEquals(moves, pawn_w.getPossibleMoves());
    }

    /*
     * Testet das Berechnen der möglichen Züge eines Bauern, einschließlich Diagonal- und En-Passant-Zügen.
     * Der Test ist POSITIV, wenn die möglichen Züge des Bauern korrekt berechnet werden.
     */

    @Test
    public void testCalculatePossibleMovesStraightDiagonalEnPassant_P() {
        game.performMove(pawn_w, new Move(6, 3, 4, 3));
        game.performMove(knight_b, new Move(0, 1, 2, 2));
        game.performMove(pawn_w, new Move(4, 3, 3, 3));
        game.performMove(pawn_b, new Move(1, 4, 3, 4));
        moves.add(new Move(3, 3, 2, 3)); // Normaler Zug, das Feld vor pawn_w ist frei
        moves.add(new Move(3, 3, 2, 2)); // Diagonal Schlagen links, knight_b ist diagonal links von pawn_w
        moves.add(new Move(3, 3, 2, 4)); // EnPassant rechts, pawn_b ist rechts neben pawn_w
        pawn_w.calculatePossibleMoves();
        assertNotNull(pawn_w.getPossibleMoves());
        assertEquals(moves, pawn_w.getPossibleMoves());
    }

    /*
     * Bishop
     */

     /*
     * Testet die möglichen Züge des Läufers (Bishop) in der Startposition
     * - Der Läufer sollte in der Startposition blockiert sein.
     * Der Test ist POSITIV, wenn keine möglichen Züge für den Läufer existieren.
     */

    @Test
    public void testCalculatePossibleMovesStartPos_B() {
        bishop_w.calculatePossibleMoves();
        assertTrue(bishop_w.getPossibleMoves().isEmpty());
    }

    /*
     * Testet die möglichen Züge des Läufers nach einigen Zügen
     * - Es werden mehrere Züge durchgeführt, um den Läufer zu befreien.
     * - Der Läufer bewegt sich dann in eine Position, von der aus er mehrere Züge machen kann.
     * Der Test ist POSITIV, wenn die berechneten Züge den erwarteten Zügen entsprechen.
     */

    @Test
    public void testCalculatePossibleMoves_B() {
        game.performMove(pawn_w, new Move(6, 3, 4, 3));
        game.performMove(pawn_b, new Move(1, 4, 3, 4));
        game.performMove(bishop_w, new Move(7, 2, 4, 5));
        moves.add(new Move(4, 5, 3, 4));    // Schlaegt hier, deshalb ist row2col7 nicht moeglich -> geblockt
        moves.add(new Move(4, 5, 3, 6));
        moves.add(new Move(4, 5, 2, 7));
        moves.add(new Move(4, 5, 5, 4));
        moves.add(new Move(4, 5, 6, 3));
        moves.add(new Move(4, 5, 7, 2));
        moves.add(new Move(4, 5, 5, 6));
        bishop_w.calculatePossibleMoves();
        assertNotNull(bishop_w.getPossibleMoves());
        assertEquals(moves, bishop_w.getPossibleMoves());
    }

    /*
     * Knight
     */

     /*
     * Testet die möglichen Züge des Springers (Knight) in der Startposition
     * - Der Springer kann zu Beginn zwei mögliche Züge machen.
     * Der Test ist POSITIV, wenn die berechneten Züge den erwarteten Zügen entsprechen.
     */

    @Test
    public void testCalculatePossibleMoves_KN() {
        moves.add(new Move(7, 1, 5, 0));
        moves.add(new Move(7, 1, 5, 2));
        knight_w.calculatePossibleMoves();
        assertEquals(moves, knight_w.getPossibleMoves());
    }

    /*
     * Rook
     */

    /*
     * Testet die möglichen Züge des Turms (Rook) in der Startposition
     * - Der Turm sollte in der Startposition blockiert sein.
     * Der Test ist POSITIV, wenn keine möglichen Züge für den Turm existieren.
     */

    @Test
    public void testCalculatePossibleMovesStartPos_R() {
        rook_w.calculatePossibleMoves();
        assertTrue(rook_w.getPossibleMoves().isEmpty());
    }

    /*
     * Testet die möglichen Züge des Turms nach einigen Zügen
     * - Der Turm wird befreit und bewegt sich in eine Position, von der aus er mehrere Züge machen kann.
     * Der Test ist POSITIV, wenn die berechneten Züge den erwarteten Zügen entsprechen.
     */

    @Test
    public void testCalculatePossibleMoves_R() {
        Piece rookPawn = Board.board[6][0];
        game.performMove(rookPawn, new Move(6, 1, 4, 1));
        game.performMove(pawn_b, new Move(1, 4, 3, 4));
        game.performMove(rook_w, new Move(7, 1, 5, 1));
        game.performMove(pawn_b, new Move(3, 4, 4, 4));
        game.performMove(rook_w, new Move(5, 1, 5, 4));
        moves.add(new Move(5, 4, 4, 4));
        moves.add(new Move(5, 4, 5, 3));
        moves.add(new Move(5, 4, 5, 2));
        moves.add(new Move(5, 4, 5, 1));
        moves.add(new Move(5, 4, 5, 0));
        moves.add(new Move(5, 4, 5, 5));
        moves.add(new Move(5, 4, 5, 6));
        moves.add(new Move(5, 4, 5, 7));
        rook_w.calculatePossibleMoves();
        assertEquals(moves, rook_w.getPossibleMoves());

    }

    /*
     * Queen
     */

     /*
     * Testet die möglichen Züge der Dame (Queen) in der Startposition
     * - Die Dame sollte in der Startposition blockiert sein.
     * Der Test ist POSITIV, wenn keine möglichen Züge für die Dame existieren.
     */

    @Test
    public void testCalculatePossibleMovesStartPos_Q() {
        queen_w.calculatePossibleMoves();
        assertTrue(queen_w.getPossibleMoves().isEmpty());
    }

    /*
     * Testet die möglichen Züge der Dame nach einigen Zügen
     * - Die Dame wird befreit und bewegt sich in eine Position, von der aus sie mehrere Züge machen kann.
     * Der Test ist POSITIV, wenn die berechneten Züge den erwarteten Zügen entsprechen.
     */

    @Test
    public void testCalculatePossibleMoves_Q() {
        game.performMove(pawn_w, new Move(6, 3, 4, 3));
        game.performMove(pawn_b, new Move(1, 4, 2, 4));
        game.performMove(queen_w, new Move(7, 3, 5, 3));
        moves.add(new Move(5, 3, 6, 3));
        moves.add(new Move(5, 3, 7, 3));
        moves.add(new Move(5, 3, 5, 2));
        moves.add(new Move(5, 3, 5, 1));
        moves.add(new Move(5, 3, 5, 0));
        moves.add(new Move(5, 3, 5, 4));
        moves.add(new Move(5, 3, 5, 5));
        moves.add(new Move(5, 3, 5, 6));
        moves.add(new Move(5, 3, 5, 7));
        moves.add(new Move(5, 3, 4, 2));
        moves.add(new Move(5, 3, 3, 1));
        moves.add(new Move(5, 3, 2, 0));
        moves.add(new Move(5, 3, 4, 4));
        moves.add(new Move(5, 3, 3, 5));
        moves.add(new Move(5, 3, 2, 6));
        moves.add(new Move(5, 3, 1, 7));
        queen_w.calculatePossibleMoves();
        assertEquals(moves, queen_w.getPossibleMoves());
    }

    /*
     * King
     */

     /*
     * Testet das Setzen und Abrufen der Rochadeoptionen des Königs (Castle Big)
     * - Die große Rochade-Option (Castle Big) wird deaktiviert.
     * Der Test ist POSITIV, wenn die Rochade-Option korrekt gesetzt und abgerufen wird.
     */

    @Test
    public void testGetSetCastleBig() {
        king_w.setCastleBig(false);
        assertFalse(king_w.getCastleBig());
    }

    /*
     * Testet das Setzen und Abrufen der Rochadeoptionen des Königs (Castle Small)
     * - Die kleine Rochade-Option (Castle Small) wird deaktiviert.
     * Der Test ist POSITIV, wenn die Rochade-Option korrekt gesetzt und abgerufen wird.
     */

    @Test
    public void testGetSetCastleSmall() {
        king_w.setCastleSmall(false);
        assertFalse(king_w.getCastleSmall());
    }

    /*
     * Testet die möglichen Züge des Königs (King) in der Startposition
     * - Der König sollte in der Startposition blockiert sein.
     * Der Test ist POSITIV, wenn keine möglichen Züge für den König existieren.
     */

    @Test
    public void testCalculatePossibleMovesStartPos_K() {
        king_w.calculatePossibleMoves();
        assertTrue(king_w.getPossibleMoves().isEmpty());
    }

    /*
     * Testet die möglichen Züge des Königs nach einigen Zügen
     * - Der König wird befreit und bewegt sich in eine Position, von der aus er mehrere Züge machen kann.
     * Der Test ist POSITIV, wenn die berechneten Züge den erwarteten Zügen entsprechen.
     */

    @Test
    public void testCalculatePossibleMoves_K() {
        Piece kingPawn = Board.board[6][4];
        Piece kingKnight = Board.board[7][6];
        Piece kingBishop = Board.board[7][5];
        game.performMove(kingPawn, new Move(6, 4, 5, 4));
        game.performMove(pawn_b, new Move(1, 4, 3, 4));
        game.performMove(kingKnight, new Move(7, 6, 5, 7));
        game.performMove(knight_b, new Move(0, 1, 2, 1));
        game.performMove(kingBishop, new Move(7, 5, 5, 3));
        moves.add(new Move(7, 4, 6, 4));
        moves.add(new Move(7, 4, 7, 5));
        moves.add(new Move(7, 4, 7, 6));
        king_w.calculatePossibleMoves();
        assertEquals(moves, king_w.getPossibleMoves());
    }
}
