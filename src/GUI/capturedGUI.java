package GUI;

import javax.swing.*;

import Core.Game;
import Core.Pieces.Piece;

import java.awt.*;
import java.util.List;

public class capturedGUI extends JPanel {

    private JPanel capturedPanel;

    public capturedGUI() {

        capturedPanel = new JPanel(new GridLayout(1, 16, 2, 2));
        capturedPanel.setOpaque(false);
        setVisible(true);
    }

    public void updateWhiteCapturedPieces(List<Piece> whiteCaptured) {
        capturedPanel.removeAll();

        for (Piece piece : whiteCaptured) {
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getScaledInstance(7, 7, Image.SCALE_SMOOTH)));
            capturedPanel.add(pieceLabel);
        }

        capturedPanel.revalidate();
        capturedPanel.repaint();

    }

    public void updateBlackCapturedPieces(List<Piece> blackCaptured){
        capturedPanel.removeAll();

        for (Piece piece : blackCaptured) {
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getScaledInstance(7, 7, Image.SCALE_SMOOTH)));
            capturedPanel.add(pieceLabel);
        }

        capturedPanel.revalidate();
        capturedPanel.repaint();
    }
}