
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

public class BoardGUI extends JPanel {

    private final int tileSize = 80;
    private final int board = 8;
    private final Color beige = new Color(248, 231, 187);
    private final Color brown = new Color(150, 77, 34);
    private boolean pieceSelected;
    private int selectedRow;
    private int selectedCol;
    private Game _game;
    private Map<String, BufferedImage> pieceImages = new HashMap<>();
    private Map<String, ImageIcon> transformIcons = new HashMap<>();
    private Map<String, ImageIcon> takenPiecesIcons = new HashMap<>();
    private int xBoardOffset;
    private int yBoardOffset;
    public BoardGUI(Game game) {
        _game = game;
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

    private void evaluateClick(int row, int col) {
        if (!pieceSelected) {
            // Erster Klick: Wähle das Piece aus und zeige mögliche Züge
            selectedRow = row;
            selectedCol = col;
            Piece piece = Board.board[selectedRow][selectedCol];
            if (piece == null) {
                return;
            }
            if (piece.getColor() != _game.isWhiteTurn()) {
                System.out.println("It's not your turn!");
                repaintBoard();
                return;
            }
            List<Move> moves = piece.getPossibleMoves();
            if (moves.isEmpty()) {
                return;
            }
            showMoves(moves, row, col);
            pieceSelected = true;
        } else {
            // Zweiter Klick: Führe den Zug aus, wenn er gültig ist
            Piece piece = Board.board[selectedRow][selectedCol];
            Move move = new Move(selectedRow, selectedCol, row, col);
            if (piece.getPossibleMoves().contains(move)) {
                _game.performMove(piece, move);
                pieceSelected = false;
                paintComponent(getGraphics());
            }            
            else if (Board.board[row][col] != null && Board.board[row][col] != piece &&
                        Board.board[row][col].getColor() == piece.getColor()) {
                pieceSelected = false;
                repaintBoard();
                evaluateClick(row, col);
            }
            else {
                pieceSelected = false;
                repaintBoard();
            }
        }

    }

    public void showMoves(List<Move> moves, int row, int col) {
        Piece piece = Board.board[row][col];        
        for (Move move : moves) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            int targetRow = move.getDestRow();
            int targetCol = move.getDestCol();
            Piece opponent = Board.board[targetRow][targetCol];
            if (opponent != null && opponent.getColor() != piece.getColor()) {
                g2.setColor(new Color(0, 0,0, 100)); 
                g2.setStroke(new BasicStroke(5));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                int centerX = targetCol * tileSize + tileSize / 2;
                int centerY = targetRow * tileSize + tileSize / 2;
                g2.setStroke(new BasicStroke(5));
                g2.drawOval(centerX - 37, centerY - 37, 75, 75);
            } else {
                g2.setColor(new Color(0, 0, 0, 100)); 
                g2.setStroke(new BasicStroke(5));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f)); 
                int centerX = targetCol * tileSize + tileSize / 2;
                int centerY = targetRow * tileSize + tileSize / 2;
                g2.fillOval(centerX - 12, centerY - 12, 25, 25);
            }
        }
    }    

    public void repaintBoard() {
        paintComponent(getGraphics());
    }

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
                if ( i == 7 && j == 0){
                    g.drawString("a", j * tileSize + 70 , i * tileSize + 70);
                    g.drawString("1", j * tileSize + 4, i  * tileSize + 20);
                } 
                else if ( i == 7){
                    String character = alphabet.substring(0, 1);
                    alphabet = alphabet.substring(1, alphabet.length());
                    g.drawString(character, j * tileSize + 70, i * tileSize + 70);
                }
                else if ( j == 0){
                    String character = alphabet.substring(0,1);
                    alphabet = alphabet.substring(1, alphabet.length());
                    g.drawString(character, j * tileSize + 4 , i * tileSize + 20);
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board * tileSize, board * tileSize);
    }


    private void loadPieceImages() {
        try {
            pieceImages.put("pawn_w", ImageIO.read(new File("src/figures_img/white-pawn.png")));
            pieceImages.put("rook_w", ImageIO.read(new File("src/figures_img/white-rook.png")));
            pieceImages.put("knight_w", ImageIO.read(new File("src/figures_img/white-knight.png")));
            pieceImages.put("bishop_w", ImageIO.read(new File("src/figures_img/white-bishop.png")));
            pieceImages.put("queen_w", ImageIO.read(new File("src/figures_img/white-queen.png")));
            pieceImages.put("king_w", ImageIO.read(new File("src/figures_img/white-king.png")));
            pieceImages.put("pawn_b", ImageIO.read(new File("src/figures_img/black-pawn.png")));
            pieceImages.put("rook_b", ImageIO.read(new File("src/figures_img/black-rook.png")));
            pieceImages.put("knight_b", ImageIO.read(new File("src/figures_img/black-knight.png")));
            pieceImages.put("bishop_b", ImageIO.read(new File("src/figures_img/black-bishop.png")));
            pieceImages.put("queen_b", ImageIO.read(new File("src/figures_img/black-queen.png")));
            pieceImages.put("king_b", ImageIO.read(new File("src/figures_img/black-king.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void showTransform(Piece piece) {
    //     layeredGlassPane.setVisible(true);
    //     int row = piece.getRow();
    //     int col = piece.getCol();
    //     boolean color = piece.getColor();
    //     transformPanel = new JPanel();
    //     List<JButton> buttons = setButtons(color);
    //     transformPanel.setLayout(new GridLayout(2, 2, 20, 20));
    //     transformPanel.setOpaque(false);
    //     for (JButton button : buttons) {
    //         button.setSize(new Dimension(40, 40));
    //         transformPanel.add(button);
    //     }
    //     int x = (row*80)-40+xBoardOffset;
    //     int y = (col*80)-40+yBoardOffset;
    //     transformPanel.setBounds(y, x, 160, 160);
    //     layers.add(transformPanel, JLayeredPane.POPUP_LAYER);
    //     layers.revalidate();
    //     layers.repaint();
    // }

    // public void hideTransform() {
    //     layeredGlassPane.setVisible(false);
    //     transformPanel.removeAll();
    //     if (transformPanel != null) {
    //         layers.remove(transformPanel);
    //         layers.revalidate();
    //         layers.repaint();
    //     }
    // }

    // private void loadPieceIcons(int width, int height, Map<String, ImageIcon> icons) {
    //     for (Map.Entry<String, BufferedImage> entry : pieceImages.entrySet()) {
    //         Image img = entry.getValue().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    //         icons.put(entry.getKey(), new ImageIcon(img));
    //     }
    // }

    // private List<JButton> setButtons(boolean color) {
    //     JButton rook;
    //     JButton bishop;
    //     JButton knight;
    //     JButton queen;
    //     if (color) {
    //         rook = new JButton(transformIcons.get("rook_w"));
    //         rook.addActionListener(new TransformActionListener("rook", _game));
    //         bishop = new JButton(transformIcons.get("bishop_w"));
    //         bishop.addActionListener(new TransformActionListener("bishop", _game));
    //         knight = new JButton(transformIcons.get("knight_w"));
    //         knight.addActionListener(new TransformActionListener("knight", _game));
    //         queen = new JButton(transformIcons.get("queen_w"));
    //         queen.addActionListener(new TransformActionListener("queen", _game));
    //     } else {
    //         rook = new JButton(transformIcons.get("rook_b"));
    //         rook.addActionListener(new TransformActionListener("rook", _game));
    //         bishop = new JButton(transformIcons.get("bishop_b"));
    //         bishop.addActionListener(new TransformActionListener("bishop", _game));
    //         knight = new JButton(transformIcons.get("knight_b"));
    //         knight.addActionListener(new TransformActionListener("knight", _game));
    //         queen = new JButton(transformIcons.get("queen_b"));
    //         queen.addActionListener(new TransformActionListener("queen", _game));
    //     }
    //     List<JButton> buttons = Arrays.asList(rook, bishop, knight, queen);
    //     return buttons;
    // }

}