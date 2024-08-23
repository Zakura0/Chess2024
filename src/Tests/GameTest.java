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

    /*
     * Testet, ob der Zug für die weißen Figuren am Anfang korrekt ist.
     * - Es wird überprüft, ob es am Anfang des Spiels der Zug der weißen Figuren ist.
     * Der Test ist POSITIV, wenn isWhiteTurn() true zurückgibt.
     */

    @Test
    public void testIsWhiteTurnTrue() {
        assertTrue(game.isWhiteTurn());
    }

    /*
     * Testet, ob nach einem Zug der weißen Figuren der Zug für die schwarzen Figuren korrekt ist.
     * - Ein Zug für eine weiße Figur wird ausgeführt.
     * Der Test ist POSITIV, wenn isWhiteTurn() false zurückgibt.
     */

    @Test
    public void testIsWhiteTurnFalse() {
        Pawn pawn_w = (Pawn) Board.board[6][3];
        game.performMove(pawn_w, new Move(7, 3, 6, 3));
        assertFalse(game.isWhiteTurn());
    }

    /*
     * Testet, ob ein Schach korrekt erkannt wird (positiver Fall).
     * - Verschiedene Züge werden ausgeführt, um die weiße Seite in Schach zu setzen.
     * Der Test ist POSITIV, wenn checkForCheck(true) true zurückgibt.
     */

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

    /*
     * Testet, ob kein Schach vorliegt (negativer Fall).
     * - Es wird ohne vorherige Züge geprüft, ob Schach vorliegt.
     * Der Test ist POSITIV, wenn checkForCheck(true) false zurückgibt.
     */

    @Test
    public void testCheckForCheckFalse() {
        assertFalse(Game.checkForCheck(true));
    }

    /*
     * Testet, ob ein Feld von einem Springer angegriffen wird (positiver Fall).
     * - Ein weißer Springer wird so bewegt, dass er ein schwarzes Feld bedroht.
     * Der Test ist POSITIV, wenn tileUnderAttack(2, 1, false) true zurückgibt.
     */

    @Test
    public void testTileUnderAttackKNPositive() {
        Knight knight_w = (Knight) Board.board[7][1];
        Pawn pawn_b = (Pawn) Board.board[1][1];
        game.performMove(knight_w, new Move(7, 1, 5, 0));
        game.performMove(pawn_b, new Move(1, 1, 2, 1));
        game.performMove(knight_w, new Move(5, 0, 4, 2));
        assertTrue(Game.tileUnderAttack(2, 1, false)); // Das von schwarz besetzte Feld kann vom weissen springer angegriffen werden
    }

    /*
     * Testet, ob ein Feld von einem Bauern angegriffen wird (positiver Fall).
     * - Ein weißer Bauer wird so bewegt, dass er ein schwarzes Feld bedroht.
     * Der Test ist POSITIV, wenn tileUnderAttack(3, 1, false) true zurückgibt.
     */

    @Test
    public void testTileUnderAttackPPositive() {
        Pawn pawn_w = (Pawn) Board.board[6][0];
        Pawn pawn_b = (Pawn) Board.board[1][1];
        game.performMove(pawn_w, new Move(6, 0, 4, 0));
        game.performMove(pawn_b, new Move(1, 1, 3, 1));
        assertTrue(Game.tileUnderAttack(3, 1, false)); // Das von schwarz besetzte Feld kann vom weissen pawn angegriffen werden
    }

    /*
     * Testet, ob ein Feld von einem Turm oder einer Dame angegriffen wird (positiver Fall).
     * - Ein schwarzer Bauer wird so platziert, dass er von einer Dame oder einem Turm angegriffen wird.
     * Der Test ist POSITIV, wenn tileUnderAttack(4, 0, false) true zurückgibt.
     */

    @Test
    public void testTileUnderAttackRQPositive() {
        Board.board[6][0] = null;
        Board.board[6][2] = null;
        Board.board[4][0] = new Pawn(4, 0, false);
        assertTrue(Game.tileUnderAttack(4, 0, false));
    }

    /*
     * Testet, ob ein Feld von einem Läufer oder einer Dame angegriffen wird (positiver Fall).
     * - Ein schwarzer Bauer wird so platziert, dass er von einer Dame oder einem Läufer angegriffen wird.
     * Der Test ist POSITIV, wenn tileUnderAttack(5, 3, false) true zurückgibt.
     */

    @Test
    public void testTileUnderAttackBQPositive() {
        Board.board[6][3] = null;
        Board.board[6][4] = null;
        Board.board[5][3] = new Pawn(5, 3, false);
        assertTrue(Game.tileUnderAttack(5, 3, false));
    }

    /*
     * Testet, ob ein Feld nicht bedroht wird (negativer Fall).
     * - Es wird überprüft, ob ein leeres Feld nicht bedroht wird.
     * Der Test ist POSITIV, wenn tileUnderAttack(4, 4, true) false zurückgibt.
     */

    @Test
    public void testTileUnderAttackNegative() {
        assertFalse(Game.tileUnderAttack(4, 4, true)); // Wird zurzeit nicht bedroht, da Feld leer
    }

    /*
     * Testet die Ausführung der Rochade.
     * - Der weiße König wird auf das Ziel für die Rochade bewegt.
     * Der Test ist POSITIV, wenn der König und der Turm an den richtigen Positionen stehen und die algebraische Notation "0-0" ist.
     */

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

    /*
     * Testet die Ausführung eines En-Passant-Zugs.
     * - Ein schwarzer Bauer wird in eine Position für En Passant gebracht und von einem weißen Bauern geschlagen.
     * Der Test ist POSITIV, wenn der schwarze Bauer entfernt wurde, der weiße Bauer richtig steht und die Notation "e6 e.p." ist.
     */

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

    /*
     * Testet die Ausführung eines Schachmatts.
     * - Züge werden ausgeführt, die zu einem Schachmatt führen.
     * Der Test ist POSITIV, wenn die algebraische Notation "Qh4#" ist.
     */

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


    /*
     * Testet die Ausführung eines Schach-Zugs.
     * - Züge werden ausgeführt, die zu einem Schach führen.
     * Der Test ist POSITIV, wenn die algebraische Notation "Qh4+" ist.
     */

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

    /*
     * Testet die Ausführung einer Bauernumwandlung.
     * - Ein weißer Bauer wird auf die gegnerische Grundreihe bewegt und in eine Dame umgewandelt.
     * Der Test ist POSITIV, wenn der Bauer erfolgreich in eine Dame umgewandelt wurde und die algebraische Notation "a8=Q" ist.
     */

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

    /*
     * Testet das Hinzufügen eines Zuges zur Zugwarteschlange.
     * - Ein Zug wird zur Warteschlange hinzugefügt.
     * Der Test ist POSITIV, wenn die Warteschlange nach dem Hinzufügen des Zuges nicht leer ist.
     */

    @Test
    public void testAddMoveToQueue() {
        Move move = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move);
        assertFalse(Game.moveQueue.isEmpty());
    }

    /*
     * Testet das Leeren der Zugwarteschlange.
     * - Ein Zug wird zur Warteschlange hinzugefügt und anschließend wird die Warteschlange geleert.
     * Der Test ist POSITIV, wenn die Warteschlange nach dem Leeren leer ist.
     */

    @Test
    public void testClearQueuePositive() {
        Move move = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move);
        Game.clearQueue();
        assertTrue(Game.moveQueue.isEmpty());
    }

    /*
     * Testet das Abrufen des letzten Zuges aus der Zugwarteschlange (positiver Fall).
     * - Zwei Züge werden zur Warteschlange hinzugefügt, und der letzte wird abgerufen.
     * Der Test ist POSITIV, wenn der abgerufene Zug der zuletzt hinzugefügte ist.
     */

    @Test
    public void testGetLastMovePositive() {
        Move move1 = new Move(1, 1, 3, 1);
        Move move2 = new Move(1, 1, 3, 1);
        Game.addMoveToQueue(move1);
        Game.addMoveToQueue(move2);
        assertEquals(move2, Game.getLastMove());;
    }

    /*
     * Testet das Abrufen des letzten Zuges aus der Zugwarteschlange (negativer Fall).
     * - Es wird versucht, den letzten Zug abzurufen, wenn die Warteschlange leer ist.
     * Der Test ist POSITIV, wenn null zurückgegeben wird.
     */

    @Test
    public void testGetLastMoveNegative() {
        assertNull(Game.getLastMove());
    }

    /*
     * Testet das Zurücksetzen des Spiels.
     * - Das Spiel wird zurückgesetzt, nachdem einige Züge ausgeführt wurden.
     * Der Test ist POSITIV, wenn die Liste der geschlagenen Figuren, die Zugwarteschlange und die Notation leer sind und weiß am Zug ist.
     */

    @Test
    public void testResetGame() {
        game.resetGame();
        assertTrue(Game.whiteDead.isEmpty());
        assertTrue(Game.blackDead.isEmpty());
        assertTrue(Game.moveQueue.isEmpty());
        assertEquals("", game.getAlgebraicNotation());
        assertTrue(Game.isWhite);
    }

    /*
     * Testet das Abrufen der algebraischen Notation (leerer Fall).
     * - Es wird versucht, die Notation abzurufen, wenn keine Züge gemacht wurden.
     * Der Test ist POSITIV, wenn die Notation leer ist.
     */

    @Test
    public void testGetAlgebraicNotationEmpty() {
        assertEquals("", game.getAlgebraicNotation());
    }

    /*
     * Testet das Setzen und Abrufen der algebraischen Notation.
     * - Eine Notation wird gesetzt und anschließend abgerufen.
     * Der Test ist POSITIV, wenn die abgerufene Notation der zuletzt gesetzten Notation entspricht.
     */
    
    @Test
    public void testSetGetAlgebraicNotation() {
        game.setAlgebraicNotation("a3;b6;c3");
        assertEquals("c3", game.getAlgebraicNotation());
    }

}
