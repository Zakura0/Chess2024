
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

public class BoardGUI extends JPanel {

    private static final int tileSize = 80;
    private static final int board = 8;
    private static final Color beige = new Color(248, 231, 187);
    private static final Color brown = new Color(150, 77, 34);
    private boolean pieceSelected = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Game _game;
    private Map<String, BufferedImage> pieceImages = new HashMap<>();
    private static Map<String, Icon> pieceIcons = new HashMap<>();
    private JFrame mainFrame;
    private JPanel transformPanel;

    public BoardGUI(Game game) {
        _game = game;
        loadPieceImages();
        loadPieceIcons();
        initializeListener();
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
            showMoves(row, col);
            pieceSelected = true;
        } else {
            // Zweiter Klick: Führe den Zug aus, wenn er gültig ist
            Piece piece = Board.board[selectedRow][selectedCol];
            Move move = new Move(selectedRow, selectedCol, row, col);
            if (piece.getPossibleMoves().contains(move)) {
                _game.performMove(piece, move);
                pieceSelected = false;
                paintComponent(getGraphics());
            } else {
                pieceSelected = false;
                repaintBoard();
            }
        }

    }

    public void showMoves(int row, int col) {
        Piece piece = Board.board[row][col];
        List<Move> moves = piece.getPossibleMoves();
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

        for (int i = 0; i < board; i++) { // i = row
            for (int j = 0; j < board; j++) { // j = col
                boolean isWhite = (i + j) % 2 == 0;
                g.setColor(isWhite ? beige : brown);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);

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

    public void loadGUI() {
        mainFrame = new JFrame("Chess");
        mainFrame.setSize(1000, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        BoardGUI boardGui = new BoardGUI(_game);
        JPanel boardFrame = new JPanel();
        boardFrame.setLayout(new GridBagLayout());
        boardFrame.add(boardGui);
        boardFrame.setBorder(new EmptyBorder(50, 50, 50, 0));
        mainFrame.add(boardFrame, BorderLayout.WEST);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public void transform(Boolean color) {
        transformPanel = new JPanel();
        List<JButton> buttons = setButtons(color);
        transformPanel.setLayout(new GridLayout(2, 2));
        transformPanel.setBorder(setTransformBorder(color));
        for (JButton button : buttons) {
            transformPanel.add(button);
        }
        transformPanel.setVisible(true);
        mainFrame.add(transformPanel, BorderLayout.EAST);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void hideTransform() {
        transformPanel.setVisible(false);
        repaintBoard();
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

    private void loadPieceIcons() {
        for (Map.Entry<String, BufferedImage> entry : pieceImages.entrySet()) {
            pieceIcons.put(entry.getKey(), new ImageIcon(entry.getValue()));
        }
    }

    private EmptyBorder setTransformBorder(Boolean color) {
        EmptyBorder whiteBorder = new EmptyBorder(50, 0, 590, 50);
        EmptyBorder blackBorder = new EmptyBorder(590, 0, 50, 50);
        if (color) {
            return whiteBorder;
        } else {
            return blackBorder;
        }
    }

    private List<JButton> setButtons(boolean color) {
        JButton rook;
        JButton bishop;
        JButton knight;
        JButton queen;
        if (color) {
            rook = new JButton(pieceIcons.get("rook_w"));
            rook.addActionListener(new TransformActionListener("rook"));
            bishop = new JButton(pieceIcons.get("bishop_w"));
            bishop.addActionListener(new TransformActionListener("bishop"));
            knight = new JButton(pieceIcons.get("knight_w"));
            knight.addActionListener(new TransformActionListener("knight"));
            queen = new JButton(pieceIcons.get("queen_w"));
            queen.addActionListener(new TransformActionListener("queen"));
        } else {
            rook = new JButton(pieceIcons.get("rook_b"));
            rook.addActionListener(new TransformActionListener("rook"));
            bishop = new JButton(pieceIcons.get("bishop_b"));
            bishop.addActionListener(new TransformActionListener("bishop"));
            knight = new JButton(pieceIcons.get("knight_b"));
            knight.addActionListener(new TransformActionListener("knight"));
            queen = new JButton(pieceIcons.get("queen_b"));
            queen.addActionListener(new TransformActionListener("queen"));
        }
        List<JButton> buttons = new ArrayList<>();
        buttons.add(rook);
        buttons.add(bishop);
        buttons.add(knight);
        buttons.add(queen);
        return buttons;
    }
}