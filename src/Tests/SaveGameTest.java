package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import GUI.MainGUI;

public class SaveGameTest {

    String testFileName;

    @BeforeEach
    public void setUp() {
        MainGUI.unitTest = true;
        MainGUI.timeWhite = 0;
        MainGUI.timeBlack = 0;

        testFileName = "chess_save_1.txt";

        File savedFile = new File("chess_save_1.txt");
        savedFile.delete();

        
    }

    @AfterEach
    public void tearDown() {
        File savedFile = new File("chess_save_1.txt");
        savedFile.delete();
    }

    /*
     * Testet, ob die Züge in eine Datei richtig eingelesen werden
     * - Führt eine Move im Spiel aus
     * - Speichert die Züge mit saveGame in der Textdatei
     * Der Test ist POSITIV, wenn der gerade ausgeführte (und alle anderen) Züge
     * in der Text Datei gespeichert werden
     */

    @Test
    public void testSaveGame() throws IOException {
        List<Move> moveQueue = new ArrayList<>();
        moveQueue.add(new Move(1, 0, 3, 0, 1));
        MainGUI.timeWhite = 300;
        MainGUI.timeBlack = 300;
        Path filePath = Paths.get("chess_save.txt");
        Files.createFile(filePath);
        File file = new File("chess_save.txt");
        
        SaveGame.save(moveQueue, file);
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        file.delete();
        assertEquals("1,0,3,0,1:*300,300*White,Black", content);

        
    }

    /*
     * Testet, ob die Züge aus einer Datei richtig ausgelesen werden
     * - Es wird eine Datei mit einem Zug erstellt
     * - Die Datei wird mit loadGameMoves ausgelesen
     * Der Test ist POSITIV, wenn nach dem Laden der Move korrekt in der moveQueue abgespeichert wird
     */

    @Test
    public void testLoadGameMoves() throws IOException {
        File file = new File(testFileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("1,0,3,0,1:*300,300*White,Black");
        }

        ArrayList<Move> moves = SaveGame.loadGameMoves(testFileName);

        assertEquals(1, moves.size());
        Move loadedMove = moves.get(0);
        assertEquals(1, loadedMove.getCurrRow());
        assertEquals(0, loadedMove.getCurrCol());
        assertEquals(3, loadedMove.getDestRow());
        assertEquals(0, loadedMove.getDestCol());
        assertEquals(1, loadedMove.getTransformation());
        assertEquals(300, MainGUI.timeWhite);
        assertEquals(300, MainGUI.timeBlack);
        assertEquals("White", MainGUI.whitePlayer);
        assertEquals("Black", MainGUI.blackPlayer);
    }

    /* 
     * Testet, ob ein Spiel korrekt geladen wird, basierend auf geg. einer moveQueue
     * - Es wird ein Move Objekt zu der moveQueue hinzugefügt.
     * - Die moveQueue wird mit loadGame geladen
     * Der Test ist POSITIV, wenn nach dem Laden der moveQueue die Felder richtig gesetzt sind
     */

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

