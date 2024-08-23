package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Core.Board;
import Core.Game;
import Core.Move;
import Core.Pieces.*;

public class GameTest {

    Game game;

    @BeforeEach
    public void setUp() {
        GUI.MainGUI.unitTest = true;
        game = new Game();
    }

    @Test
    public void testIsWhiteTurnTrue() {
        assertTrue(game.isWhiteTurn());
    }

    @Test
    public void testIsWhiteTurnFalse() {
        Pawn pawn_w = (Pawn) Board.board[6][3];
        game.performMove(pawn_w, new Move(7, 3, 6, 3));
        assertFalse(game.isWhiteTurn());
    }

    @Test
    public void testCheckForCheckTrue() {
        Pawn pawn_w1 = (Pawn) Board.board[6][4];
        Pawn pawn_w2 = (Pawn) Board.board[6][3];
        Pawn pawn_b = (Pawn) Board.board[1][2];
        Queen queen_b = (Queen) Board.board[0][3];
        game.performMove(pawn_w1, new Move(6, 4, 5, 4));
        game.performMove(pawn_b, new Move(1, 2, 2, 2));
        game.performMove(pawn_w2, new Move(6, 3, 5, 3));
        game.performMove(queen_b, new Move(0, 3, 3, 0));
        assertTrue(Game.checkForCheck(true));
    }

    @Test
    public void testCheckForCheckFalse() {
        assertFalse(Game.checkForCheck(true));
    }

    @Test
    public void testTileUnderAttackKNPositive() {
        Knight knight_w = (Knight) Board.board[7][1];
        Pawn pawn_b = (Pawn) Board.board[1][1];
        game.performMove(knight_w, new Move(7, 1, 5, 0));
        game.performMove(pawn_b, new Move(1, 1, 2, 1));
        game.performMove(knight_w, new Move(5, 0, 4, 2));
        assertTrue(Game.tileUnderAttack(2, 1, false)); // Das von schwarz besetzte Feld kann vom weissen springer angegriffen werden
    }

    @Test
    public void testTileUnderAttackPPositive() {
        Pawn pawn_w = (Pawn) Board.board[6][0];
        Pawn pawn_b = (Pawn) Board.board[1][1];
        game.performMove(pawn_w, new Move(6, 0, 4, 0));
        game.performMove(pawn_b, new Move(1, 1, 3, 1));
        assertTrue(Game.tileUnderAttack(3, 1, false)); // Das von schwarz besetzte Feld kann vom weissen pawn angegriffen werden
    }

    @Test
    public void testTileUnderAttackRQPositive() {
        Board.board[6][0] = null;
        Board.board[6][2] = null;
        Board.board[4][0] = new Pawn(4, 0, false);
        assertTrue(Game.tileUnderAttack(4, 0, false));
    }

    @Test
    public void testTileUnderAttackBQPositive() {
        Board.board[6][3] = null;
        Board.board[6][4] = null;
        Board.board[5][3] = new Pawn(5, 3, false);
        assertTrue(Game.tileUnderAttack(5, 3, false));
    }

    @Test
    public void testTileUnderAttackNegative() {
        assertFalse(Game.tileUnderAttack(4, 4, true)); // Wird zurzeit nicht bedroht, da Feld leer
    }

    @Test
    public void testPerformMoveCastling() {
        King king_w = (King) Board.board[7][4];
        Rook rook_w = (Rook) Board.board[7][7];
        Move move = new Move(7, 4, 7, 6); // King from e1 to g1
        game.performMove(king_w, move);

        assertEquals(king_w, Board.board[7][6]);
        assertEquals(rook_w, Board.board[7][5]);
        assertEquals("0-0", game.getAlgebraicNotation());
    }

    @Test
    public void testPerformMoveEnPassant() {
        Pawn pawn_b = (Pawn) Board.board[1][4];
        Board.board[3][3] = new Pawn(3, 3, true);
        Pawn pawn_w = (Pawn) Board.board[3][3]; 
        pawn_b.move(3, 4); // Move black pawn to e5
        Move move = new Move(3, 3, 2, 4); // White pawn captures en passant on e6
        game.performMove(pawn_w, move);

        assertNull(Board.board[3][4]);
        assertEquals(pawn_w, Board.board[2][4]);
        assertEquals("e6 e.p.", game.getAlgebraicNotation());
    }

    @Test
    public void testPerformMove_Checkmate() {
        Pawn pawn_w1 = (Pawn) Board.board[6][5];
        Pawn pawn_w2 = (Pawn) Board.board[6][6];
        Pawn pawn_b = (Pawn) Board.board[1][4];
        Queen queen_b = (Queen) Board.board[0][3];
        game.performMove(pawn_w1, new Move(6, 5, 5, 5));
        game.performMove(pawn_b, new Move(1, 4, 2, 4));
        game.performMove(pawn_w2, new Move(6, 6, 4, 6));
        game.performMove(queen_b, new Move(0, 3, 4, 7));

        assertEquals("Qh4#", game.getAlgebraicNotation());
    }

    @Test
    public void testPerformMove_Check() {
        Pawn pawn_w1 = (Pawn) Board.board[6][5];
        Pawn pawn_w2 = (Pawn) Board.board[6][0];
        Pawn pawn_b = (Pawn) Board.board[1][4];
        Queen queen_b = (Queen) Board.board[0][3];
        game.performMove(pawn_w1, new Move(6, 5, 5, 5));
        game.performMove(pawn_b, new Move(1, 4, 2, 4));
        game.performMove(pawn_w2, new Move(6, 0, 4, 0));
        game.performMove(queen_b, new Move(0, 3, 4, 7));

        assertEquals("Qh4+", game.getAlgebraicNotation());
    }

    @Test
    public void testPerformMove_PawnPromotion() {
        Board.board[0][0] = null;
        Board.board[1][0] = null;
        Board.board[1][0] = new Pawn(1, 0, true);
        Pawn pawn_w = (Pawn) Board.board[1][0];
        Move move = new Move(1, 0, 0, 0, 1); // Promote pawn on e8
        //move.setTransformation(1); // Promote to Queen
        game.performMove(pawn_w, move);

        assertTrue(Board.board[0][0] instanceof Queen);
        assertEquals("a8=Q", game.getAlgebraicNotation());
    }

    @Test
    public void testAddMoveToQueue() {
        Move move = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move);
        assertFalse(Game.moveQueue.isEmpty());
    }

    @Test
    public void testClearQueuePositive() {
        Move move = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move);
        Game.clearQueue();
        assertTrue(Game.moveQueue.isEmpty());
    }

    @Test
    public void testGetLastMovePositive() {
        Move move1 = new Move(1, 1, 3, 1);
        Move move2 = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move1);
        Game.addMoveToQueue(move2);
        assertEquals(move2, Game.getLastMove());;
    }

    @Test
    public void testGetLastMoveNegative() {
        assertNull(Game.getLastMove());
    }

    @Test
    public void testResetGame() {
        game.resetGame();
        assertTrue(Game.whiteDead.isEmpty());
        assertTrue(Game.blackDead.isEmpty());
        assertTrue(Game.moveQueue.isEmpty());
        assertEquals("", game.getAlgebraicNotation());
        assertTrue(Game.isWhite);
    }

    @Test
    public void testGetAlgebraicNotationEmpty() {
        assertEquals("", game.getAlgebraicNotation());
    }
    
    @Test
    public void testSetGetAlgebraicNotation() {
        game.setAlgebraicNotation("a3;b6;c3");
        assertEquals("c3", game.getAlgebraicNotation());
    }

}
