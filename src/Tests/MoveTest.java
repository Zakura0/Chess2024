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

    @Test
    public void testGetTransformation() {
        assertEquals(0, move.getTransformation());
    }

    @Test
    public void testSetTransformation() {
        move.setTransformation(3);
        assertEquals(3, move.getTransformation());
    }

    @Test
    public void testGetMovingPiecePositive() {
        assertEquals(Board.board[0][3], move.getMovingPiece());
    }

    @Test
    public void testGetMovingPieceNegative() {
        assertNotEquals(Board.board[2][2], move.getMovingPiece());
    }

    @Test
    public void testGetCurrRow() {
        assertEquals(3, move.getCurrRow());
    }

    @Test
    public void testGetCurrCol() {
        assertEquals(3, move.getCurrCol());
    }

    @Test
    public void testGetDestRow() {
        assertEquals(0, move.getDestRow());
    }

    @Test
    public void testGetDestCol() {
        assertEquals(3, move.getDestCol());
    }

    @Test
    public void testToStringPositive() {
        assertNotNull(move.toString());
        assertEquals("3,3,0,3,0:", move.toString());
    }

    @Test
    public void testToStringNegative() {
        assertNotNull(move.toString());
        assertNotEquals("3,3,0,3,1:", move.toString());
    }

    @Test
    public void testToAlgebraicNotationStartPositive() {
        assertEquals("d5", move.toAlgebraicNotationStart());
    }

    @Test
    public void testToAlgebraicNotationStartNegative() {
        assertNotEquals("e5", move.toAlgebraicNotationStart());
    }

    @Test
    public void testToAlgebraicNotationEndPositive() {
        assertEquals("d8", move.toAlgebraicNotationEnd());
    }

    @Test
    public void testToAlgebraicNotationEndNegative() {
        assertNotEquals("e8", move.toAlgebraicNotationEnd());
    }
}
