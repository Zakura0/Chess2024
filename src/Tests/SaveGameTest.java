package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Core.Game;
import Core.Board;
import Core.Move;
import Core.SaveGame;
import Core.Pieces.Pawn;
import Core.Pieces.Piece;
import GUI.GUI;

public class SaveGameTest {

    String testFileName;

    @BeforeEach
    public void setUp() {
        GUI.unitTest = true;
        GUI.timeWhite = 0;
        GUI.timeBlack = 0;

        testFileName = "chess_save_1.txt";

        File savedFile = new File("chess_save_1.txt");
        savedFile.delete();

        
    }

    @AfterEach
    public void tearDown() {
        File savedFile = new File("chess_save_1.txt");
        savedFile.delete();
    }

    @Test
    public void testSaveGame() throws IOException {
        List<Move> moveQueue = new ArrayList<>();
        moveQueue.add(new Move(1, 0, 3, 0, 1));
        GUI.timeWhite = 300;
        GUI.timeBlack = 300;

        SaveGame.saveGame(moveQueue);

        File savedFile = new File("chess_save_1.txt");
        assertTrue(savedFile.exists());

        String content = Files.readString(savedFile.toPath());
        assertEquals("1,0,3,0,1:*300,300", content);

        savedFile.delete();
    }

    @Test
    public void testLoadGameMoves() throws IOException {
        File file = new File(testFileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("1,0,3,0,1:*300,300");
        }

        ArrayList<Move> moves = SaveGame.loadGameMoves(testFileName);

        assertEquals(1, moves.size());
        Move loadedMove = moves.get(0);
        assertEquals(1, loadedMove.getCurrRow());
        assertEquals(0, loadedMove.getCurrCol());
        assertEquals(3, loadedMove.getDestRow());
        assertEquals(0, loadedMove.getDestCol());
        assertEquals(1, loadedMove.getTransformation());
        assertEquals(300, GUI.timeWhite);
        assertEquals(300, GUI.timeBlack);
    }

    @Test
    public void testLoadGame() {
        Game game = new Game();
        Piece piece = new Pawn(1, 0, false);
        Board.board[1][0] = piece;

        List<Move> moveQueue = new ArrayList<>();
        moveQueue.add(new Move(1, 0, 3, 0, 1));

        SaveGame.loadGame(game, moveQueue);

        assertEquals(Board.board[3][0], piece);
    }
}

