package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Core.Board;
import Core.Game;
import Core.Pieces.*;

public class BoardTest {

    Game game;

    /*
     * Testet, ob das Bord richtig initialisiert wird.
     * - Es werden einzelne Felder überprüft die gesetzt sein sollten, nach dem das Board initialisiert wird
     * Der Test ist POSITIV solbald die überprüften Felder korrekt gesetzt sind.
     */
    
    @Test
    public void testInitializeBoard() {
        GUI.MainGUI.unitTest = true;
        game = new Game();
        assertFalse(Game.whiteAlive.isEmpty());
        assertFalse(Game.blackAlive.isEmpty());
        assertTrue(Board.board[1][0] instanceof Pawn);
        assertTrue(Board.board[7][0] instanceof Rook);
        assertTrue(Board.board[7][1] instanceof Knight);
        assertTrue(Board.board[7][2] instanceof Bishop);

    }

}
