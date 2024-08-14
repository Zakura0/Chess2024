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
            writer.write("*" + GUI.timeWhite + "," + GUI.timeBlack);
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
            int whiteTime = Integer.parseInt(moves_and_times[1].split(",")[0]);
            int blackTime = Integer.parseInt(moves_and_times[1].split(",")[1]);
            GUI.timeWhite = whiteTime;
            GUI.timeBlack = blackTime;
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
