import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SaveGame {

    public static void saveGame(List<Move> moveQueue) {
        int saveNumber = 1;
        String fileName = "chess_save_" + saveNumber + ".txt";
        File file = new File(fileName);
        while (file.exists()) {
            saveNumber++;
            fileName = "chess_save_" + saveNumber + ".txt";
            file = new File(fileName);
        }
        try {
            FileWriter writer = new FileWriter(file);
            for (Move move : moveQueue) {
                writer.write(move.toString());

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Move> loadGameMoves(String fileName) {
        ArrayList<Move> moveQueue = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(":");
            while (scanner.hasNext()) {
                String moveString = scanner.next();
                String[] moveFields = moveString.split(",");
                int currRow = Integer.parseInt(moveFields[0]);
                int currCol = Integer.parseInt(moveFields[1]);
                int destRow = Integer.parseInt(moveFields[2]);
                int destCol = Integer.parseInt(moveFields[3]);
                Move move = new Move(currRow, currCol, destRow, destCol);
                moveQueue.add(move);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moveQueue;
    }

    public static void loadGame(Game game, List<Move> moveQueue) {
        for (Move move : moveQueue) {
            Piece movingPiece = Board.board[move.getCurrRow()][move.getCurrCol()];
            game.performMove(movingPiece, move);
        }
    }
}
