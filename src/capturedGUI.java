
import javax.swing.*;

import java.awt.*;
import java.util.List;

public class capturedGUI extends JPanel {

    private JPanel whiteCapturedPanel;
    private JPanel blackCapturedPanel;

    public capturedGUI() {

        setLayout(new BorderLayout());
        whiteCapturedPanel = new JPanel(new GridLayout(1, 16, 2, 2));
        blackCapturedPanel = new JPanel(new GridLayout(1, 16, 2, 2));

        whiteCapturedPanel.setOpaque(false);
        blackCapturedPanel.setOpaque(false);

        add(whiteCapturedPanel, BorderLayout.NORTH);
        add(blackCapturedPanel, BorderLayout.SOUTH);
    }

    public void updateCapturedPieces(List<Piece> whiteCaptured, List<Piece> blackCaptured) {
        whiteCapturedPanel.removeAll();
        blackCapturedPanel.removeAll();

        for (Piece piece : whiteCaptured) {
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getScaledInstance(7, 7, Image.SCALE_SMOOTH)));
            whiteCapturedPanel.add(pieceLabel);
        }

        for (Piece piece : blackCaptured) {
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getScaledInstance(7, 7, Image.SCALE_SMOOTH)));
            blackCapturedPanel.add(pieceLabel);
        }

        whiteCapturedPanel.revalidate();
        whiteCapturedPanel.repaint();
        blackCapturedPanel.revalidate();
        blackCapturedPanel.repaint();
    }
}