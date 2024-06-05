
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.BorderLayout;

public class BoardGUI extends JPanel {

    
    private static final int tileSize = 80; 
    private static final int board = 8; 
    private static final Color beige = new Color(248,231,187);
    private static final Color brown = new Color(150, 77, 34); 

    public BoardGUI(){
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

            BoardGUI board = new BoardGUI();
            JPanel boardFrame = new JPanel();
            boardFrame.setLayout(new GridBagLayout()); 
            boardFrame.add(board);
            boardFrame.setBorder(new EmptyBorder(50, 50, 50, 50));
            mainFrame.add(boardFrame, BorderLayout.WEST);

            mainFrame.setLocationRelativeTo(null); 
            mainFrame.setVisible(true);
    }
}