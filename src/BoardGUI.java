
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.*;

public class BoardGUI extends JPanel {

    
    private static final int tileSize = 80; 
    private static final int board = 8; 
    private static final Color beige = new Color(248,231,187);
    private static final Color brown = new Color(150, 77, 34);
    private boolean pieceSelected = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Game _game;

    public BoardGUI(Game game) {
        _game = game;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;
                if (!pieceSelected) {
                    // Erster Klick: Wähle das Piece aus und zeige mögliche Züge
                    selectedRow = row;
                    selectedCol = col;
                    showMoves(row, col);
                    pieceSelected = true;
                } else {
                    // Zweiter Klick: Führe den Zug aus, wenn er gültig ist
                    performMove(selectedRow, selectedCol, row, col);
                    pieceSelected = false; // Zurücksetzen für den nächsten Zug
                }
            }
        });
    }

    //Debigging purposes
    public void showMoves(int row, int col) {
        Piece piece = Board.board[row][col];
        List<Move> moves = piece.getPossibleMoves();
        for (Move move : moves) {
            Graphics g = getGraphics();
            g.setColor(Color.GREEN);
            g.fillRect(move.getDestCol() * tileSize, move.getDestRow() * tileSize, tileSize, tileSize);
            System.out.println("This " + piece + " Can Move to: " + move.getDestRow() + ", " + move.getDestCol());
        }
    }
    //Debigging purposes
    private void performMove(int startRow, int startCol, int destRow, int destCol) {
        Piece piece = Board.board[startRow][startCol];
        Move move = new Move(startRow, startCol, destRow, destCol);
        if (piece.getPossibleMoves().contains(move)) {
            piece.move(move.getDestRow(), move.getDestCol());
            paintComponent(getGraphics());
            _game.calculateAllMoves();
        }
        System.out.println("Moved " + piece + " from " + startRow + ", " + startCol + " to " + destRow + ", " + destCol);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < board; i++) { // i = row
            for (int j = 0; j < board; j++) { // j = col
                boolean isWhite = (i + j) % 2 == 0;
                g.setColor(isWhite ? beige : brown);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board * tileSize, board * tileSize);
    }


    public void loadGUI() {
        JFrame mainFrame = new JFrame("Chess");
            mainFrame.setSize(1000, 800); 
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            mainFrame.setLayout(new BorderLayout()); 

            BoardGUI board = new BoardGUI(_game);
            JPanel boardFrame = new JPanel();
            boardFrame.setLayout(new GridBagLayout()); 
            boardFrame.add(board);
            boardFrame.setBorder(new EmptyBorder(50, 50, 50, 50));
            mainFrame.add(boardFrame, BorderLayout.WEST);

            mainFrame.setLocationRelativeTo(null); 
            mainFrame.setVisible(true);
    }
}