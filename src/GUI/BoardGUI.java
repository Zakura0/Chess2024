package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import Core.Board;
import Core.Game;
import Core.Move;
import Core.Pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

/**
 * Die Klasse BoardGUI erzeugt die Benutzerschnittstelle des Schachbretts,
 * worauf der Nutzer interagieren kann.
 * 
 * @author Gruppe 02
 */

public class BoardGUI extends JPanel {

    private final int tileSize = (int) (MainGUI.frameHeight * 0.095);
    private final int board = 8;
    private final Color beige = new Color(248, 231, 187);
    private final Color brown = new Color(150, 77, 34);
    private boolean pieceSelected;
    private int selectedRow;
    private int selectedCol;
    private Game game;
    public static Map<String, BufferedImage> pieceImages = new HashMap<>();
    private int xBoardOffset;
    private int yBoardOffset;
    
    /**
     * Konstruktor zur Ezeugung des Schachbretts
     * 
     * @param game Das aktuelle Spiel.
     */
    public BoardGUI(Game game) {
        this.game = game;
        setSize(new Dimension(800, 800));
        setBounds(xBoardOffset, yBoardOffset, board * tileSize, board * tileSize);
        yBoardOffset = 80;
        xBoardOffset = 55;
        pieceSelected = false;
        loadPieceImages();
        initializeListener();
        setVisible(true);
    }

    private void initializeListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;
                evaluateClick(row, col);
            }
        });
    }

    /**
     * Bewertet ob ein jeweiliger Klick gültig ist (gibt zum Beispiel "It's not your
     * turn" zurück wenn Schwarz gerade dran ist, aber Weiß spielen will).
     * Sollte die Auswahl gültig sein werden alle möglichen Züge für diese Figur
     * angezeigt.
     * Wenn ein Spieler einen möglichen Zug anklickt, wird dieser ausgeführt und das
     * Schachbrett aktualisiert sich visuell.
     * 
     * @param row Reihe des angeklickten Schachfeldes
     * @param col Zeile des angeklickten Schachfeldes
     */

    private void evaluateClick(int row, int col) {
        if (!pieceSelected) {
            // Erster Klick: Wähle das Piece aus und zeige mögliche Züge
            selectedRow = row;
            selectedCol = col;
            Piece piece = Board.board[selectedRow][selectedCol];
            if (piece == null) {
                return;
            }
            if (piece.getColor() != game.isWhiteTurn()) {
                MainGUI.infoLabel.setText("It's not your turn");
                repaintBoard();
                return;
            }
            // Prüfe ob die Figur überhaupt Züge hat
            List<Move> moves = piece.getPossibleMoves();
            if (moves.isEmpty()) {
                return;
            }
            // Zeige mögliche Züge an
            showMoves(moves, row, col);
            pieceSelected = true;
        } else {
            // Zweiter Klick: Führe den Zug aus, wenn er gültig ist
            Piece piece = Board.board[selectedRow][selectedCol];
            Move move = new Move(selectedRow, selectedCol, row, col);
            if (piece.getPossibleMoves().contains(move)) {
                game.performMove(piece, move);
                pieceSelected = false;
                paintComponent(getGraphics());
            } else if (Board.board[row][col] != null && Board.board[row][col] != piece &&
                    Board.board[row][col].getColor() == piece.getColor()) {
                pieceSelected = false;
                repaintBoard();
                evaluateClick(row, col);
            } else {
                pieceSelected = false;
                repaintBoard();
            }
        }

    }

    /**
     * Zeigt die möglichen Züge einer Figur auf dem Schachbrett an.
     * 
     * @param moves Liste der möglichen Züge
     * @param row   Reihe der Figur
     * @param col   Spalte der Figur
     */
    public void showMoves(List<Move> moves, int row, int col) {
        Piece piece = Board.board[row][col];
        for (Move move : moves) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            int targetRow = move.getDestRow();
            int targetCol = move.getDestCol();
            Piece opponent = Board.board[targetRow][targetCol];
            if (opponent != null && opponent.getColor() != piece.getColor()) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.setStroke(new BasicStroke(5));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                int centerX = targetCol * tileSize + tileSize / 2;
                int centerY = targetRow * tileSize + tileSize / 2;
                g2.setStroke(new BasicStroke(5));
                int attackCircle = (int)(tileSize * 0.9);
                g2.drawOval(centerX - attackCircle / 2, centerY - attackCircle / 2, attackCircle, attackCircle);
            } else {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.setStroke(new BasicStroke(5));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                int centerX = targetCol * tileSize + tileSize / 2;
                int centerY = targetRow * tileSize + tileSize / 2;
                int dot = (int)(tileSize * 0.9) / 3;
                g2.fillOval(centerX - dot / 2, centerY - dot / 2, dot, dot);
            }
        }
    }

    /**
     * Aktualisiert das Schachbrett visuell.
     */
    public void repaintBoard() {
        paintComponent(getGraphics());
    }

    /**
     * Erzeugt das Schachbrett, mit der jeweiligen Feldbezeichnung und nen
     * Schachfiguren
     * 
     * @param alphabet Gibt die Feldbeschriftungen des Schachbretts an.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String alphabet = "8765432bcdefgh";

        for (int i = 0; i < board; i++) { // i = row
            for (int j = 0; j < board; j++) { // j = col
                boolean isWhite = (i + j) % 2 == 0;
                g.setColor(isWhite ? beige : brown);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(isWhite ? brown : beige);
                if (i == 7 && j == 0) {
                    g.drawString("a", j * tileSize + 80, i * tileSize + 90);
                    g.drawString("1", j * tileSize + 4, i * tileSize + 20);
                } else if (i == 7) {
                    String character = alphabet.substring(0, 1);
                    alphabet = alphabet.substring(1, alphabet.length());
                    g.drawString(character, j * tileSize + 80, i * tileSize + 90);
                } else if (j == 0) {
                    String character = alphabet.substring(0, 1);
                    alphabet = alphabet.substring(1, alphabet.length());
                    g.drawString(character, j * tileSize + 4, i * tileSize + 20);
                }

                Piece piece = Board.board[i][j];
                if (piece != null) {
                    BufferedImage image = pieceImages.get(piece.getName());
                    if (image != null) {
                        g.drawImage(image, j * tileSize, i * tileSize, tileSize, tileSize, this);
                    }
                }
            }
        }
    }


        /**
         * getPreferredSize wird überschrieben um sicherzustellen,
         * dass das Schachbrett die richtige Größe hat, wenn es in der
         * Benutzeroberfläche angezeigt wird.
         * 
         * @return Die Größe des Schachbretts
         */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board * tileSize, board * tileSize);
    }

    /**
     * Lädt die Bilder der Schachfiguren.
     */
    private void loadPieceImages() {
        try {
            pieceImages.put("pawn_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-pawn.png")));
            pieceImages.put("rook_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-rook.png")));
            pieceImages.put("knight_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-knight.png")));
            pieceImages.put("bishop_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-bishop.png")));
            pieceImages.put("queen_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-queen.png")));
            pieceImages.put("king_w", ImageIO.read(getClass().getResourceAsStream("/IMG/white-king.png")));
            pieceImages.put("pawn_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-pawn.png")));
            pieceImages.put("rook_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-rook.png")));
            pieceImages.put("knight_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-knight.png")));
            pieceImages.put("bishop_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-bishop.png")));
            pieceImages.put("queen_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-queen.png")));
            pieceImages.put("king_b", ImageIO.read(getClass().getResourceAsStream("/IMG/black-king.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}