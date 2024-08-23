package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import Core.Move;
import Core.Board;
import Core.Game;

public class MoveTest {

    Move move;
    Game game;

    @BeforeEach
    public void setUp() {
        move = new Move(3, 3, 0, 3, 0);
        GUI.MainGUI.unitTest = true;
        game = new Game();
    }

    /* 
     * Testet das Setzen und Abrufen der Transformation eines Moves.
     * - Eine Transformation wird auf den Wert 3 gesetzt.
     * Der Test ist POSITIV, wenn der Rückgabewert der Transformation 3 ist.
     */

    @Test
    public void testSetGetTransformation() {
        move.setTransformation(3);
        assertEquals(3, move.getTransformation());
    }

    /* 
     * Testet, ob das richtige Spielstück bewegt wird (positiver Fall).
     * - Prüft, ob das Spielstück an der aktuellen Position korrekt zurückgegeben wird.
     * Der Test ist POSITIV, wenn das Spielstück an der aktuellen Position das erwartete ist.
     */

    @Test
    public void testGetMovingPiecePositive() {
        assertEquals(Board.board[0][3], move.getMovingPiece());
    }

    /* 
     * Testet, ob das richtige Spielstück bewegt wird (negativer Fall).
     * - Prüft, ob das Spielstück an einer falschen Position nicht zurückgegeben wird.
     * Der Test ist POSITIV, wenn das Spielstück an der falschen Position nicht zurückgegeben wird.
     */

    @Test
    public void testGetMovingPieceNegative() {
        assertNotEquals(Board.board[2][2], move.getMovingPiece());
    }

    /* 
     * Testet das Abrufen der aktuellen Zeile des Moves.
     * - Prüft, ob die aktuelle Zeile korrekt zurückgegeben wird.
     * Der Test ist POSITIV, wenn die aktuelle Zeile 3 ist.
     */

    @Test
    public void testGetCurrRow() {
        assertEquals(3, move.getCurrRow());
    }

    /* 
     * Testet das Abrufen der aktuellen Spalte des Moves.
     * - Prüft, ob die aktuelle Spalte korrekt zurückgegeben wird.
     * Der Test ist POSITIV, wenn die aktuelle Spalte 3 ist.
     */

    @Test
    public void testGetCurrCol() {
        assertEquals(3, move.getCurrCol());
    }

    /* 
     * Testet das Abrufen der Zielspalte des Moves.
     * - Prüft, ob die Zielspalte korrekt zurückgegeben wird.
     * Der Test ist POSITIV, wenn die Zielreihe 3 ist.
     */

    @Test
    public void testGetDestRow() {
        assertEquals(0, move.getDestRow());
    }

    /* 
     * Testet das Abrufen der Zielspalte des Moves.
     * - Prüft, ob die Zielspalte korrekt zurückgegeben wird.
     * Der Test ist POSITIV, wenn die Zielspalte 3 ist.
     */

    @Test
    public void testGetDestCol() {
        assertEquals(3, move.getDestCol());
    }

    /* 
     * Testet die toString-Methode des Moves (positiver Fall).
     * - Prüft, ob der zurückgegebene String nicht null ist und der erwarteten Notation entspricht.
     * Der Test ist POSITIV, wenn die toString-Methode den String "3,3,0,3,0:" zurückgibt.
     */

    @Test
    public void testToStringPositive() {
        assertNotNull(move.toString());
        assertEquals("3,3,0,3,0:", move.toString());
    }

    /* 
     * Testet die toString-Methode des Moves (negativer Fall).
     * - Prüft, ob der zurückgegebene String nicht null ist und nicht der unerwarteten Notation entspricht.
     * Der Test ist POSITIV, wenn die toString-Methode nicht den String "3,3,0,3,1:" zurückgibt.
     */

    @Test
    public void testToStringNegative() {
        assertNotNull(move.toString());
        assertNotEquals("3,3,0,3,1:", move.toString());
    }

    /* 
     * Testet die Umwandlung der Startposition in algebraische Notation (positiver Fall).
     * - Prüft, ob die Startposition korrekt in algebraische Notation umgewandelt wird.
     * Der Test ist POSITIV, wenn die Startposition als "d5" zurückgegeben wird.
     */

    @Test
    public void testToAlgebraicNotationStartPositive() {
        assertEquals("d5", move.toAlgebraicNotationStart());
    }

    /* 
     * Testet die Umwandlung der Startposition in algebraische Notation (negativer Fall).
     * - Prüft, ob die Startposition nicht fälschlicherweise in eine andere Notation umgewandelt wird.
     * Der Test ist POSITIV, wenn die Startposition nicht als "e5" zurückgegeben wird.
     */

    @Test
    public void testToAlgebraicNotationStartNegative() {
        assertNotEquals("e5", move.toAlgebraicNotationStart());
    }

    /* 
     * Testet die Umwandlung der Zielposition in algebraische Notation (positiver Fall).
     * - Prüft, ob die Zielposition korrekt in algebraische Notation umgewandelt wird.
     * Der Test ist POSITIV, wenn die Zielposition als "d8" zurückgegeben wird.
     */

    @Test
    public void testToAlgebraicNotationEndPositive() {
        assertEquals("d8", move.toAlgebraicNotationEnd());
    }

    /* 
     * Testet die Umwandlung der Zielposition in algebraische Notation (negativer Fall).
     * - Prüft, ob die Zielposition nicht fälschlicherweise in eine andere Notation umgewandelt wird.
     * Der Test ist POSITIV, wenn die Zielposition nicht als "e8" zurückgegeben wird.
     */

    @Test
    public void testToAlgebraicNotationEndNegative() {
        assertNotEquals("e8", move.toAlgebraicNotationEnd());
    }
}
