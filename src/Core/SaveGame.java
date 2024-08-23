package Core;

import java.util.ArrayList;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import Core.Pieces.Piece;
import GUI.MainGUI;

/**
 * Die Klasse SaveGame speichert und lädt ein Spiel.
 * @author Gruppe 02
 */
public class SaveGame {

    /**
     * Speichert das Spiel in einer Datei.
     * @param moveQueue Die Liste der Züge, die gespeichert werden sollen.
     */
    public static void createSaveFile(List<Move> moveQueue, JFrame gui) {
        try {
            // Open a dialog to save the text file
            FileDialog dialog = new FileDialog(gui, "Save Game", FileDialog.SAVE);
            dialog.setFile("game.txt");
            dialog.setVisible(true);
            String filePath = dialog.getDirectory() + dialog.getFile();
            File file = new File(filePath);
            save(moveQueue, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Speichert die Züge in einer Datei. Public nur für Testzwecke.
     * 
     * @param moveQueue Die Liste der Züge, die gespeichert werden sollen.
     * 
     * @param file Die Datei, in die die Züge gespeichert werden sollen.
     */
    public static void save(List<Move> moveQueue, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            for (Move move : moveQueue) {
                writer.write(move.toString());
            }
            writer.write("*" + MainGUI.timeWhite + "," + MainGUI.timeBlack);
            writer.write("*" + MainGUI.whitePlayer + "," + MainGUI.blackPlayer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt die Züge aus einer Datei.
     * 
     * @param fileName Der Name der Datei, aus der die Züge geladen werden sollen.
     * 
     * @return Die Liste der geladenen Züge.
     */
    public static ArrayList<Move> loadGameMoves(String fileName) {
        ArrayList<Move> moveQueue = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String[] moves_and_times = scanner.nextLine().split("\\*");
            String[] moves_strings = moves_and_times[0].split(":");
            for (String string : moves_strings) {
                String[] moveFields = string.split(",");
                int currRow = Integer.parseInt(moveFields[0]);
                int currCol = Integer.parseInt(moveFields[1]);
                int destRow = Integer.parseInt(moveFields[2]);
                int destCol = Integer.parseInt(moveFields[3]);
                int pieceType = Integer.parseInt(moveFields[4]);
                Move move = new Move(currRow, currCol, destRow, destCol, pieceType);
                moveQueue.add(move);
            }
            scanner.close();
            //String name1 
            int whiteTime = Integer.parseInt(moves_and_times[1].split(",")[0]);
            int blackTime = Integer.parseInt(moves_and_times[1].split(",")[1]);
            String whitePlayer = moves_and_times[2].split(",")[0];
            String blackPlayer = moves_and_times[2].split(",")[1];
            MainGUI.whitePlayer = whitePlayer;
            MainGUI.blackPlayer = blackPlayer;
            MainGUI.timeWhite = whiteTime;
            MainGUI.timeBlack = blackTime;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moveQueue;
    }

    /**
     * Stellt ein Spiel aus den geladenen Zügen wieder her.
     * 
     * @param game Das Spiel, in das die Züge geladen werden sollen.
     * 
     * @param moveQueue Die Liste der Züge, die geladen wurden
     */
    public static void loadGame(Game game, List<Move> moveQueue) {
        for (Move move : moveQueue) {
            Piece movingPiece = Board.board[move.getCurrRow()][move.getCurrCol()];
            game.performMove(movingPiece, move);
        }
    }
}
