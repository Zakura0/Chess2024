import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class TakenPiecesGUI {

    Piece piece;
    layeredGlassPane = new JLayeredPane();
    
        layeredGlassPane.setVisible(true);
        int row = piece.getRow();
        int col = piece.getCol();
        boolean color = piece.getColor();
        transformPanel = new JPanel();
        List<JButton> buttons = setButtons(color);
        transformPanel.setLayout(new GridLayout(2, 2, 20, 20));
        transformPanel.setOpaque(false);
        for (JButton button : buttons) {
            button.setSize(new Dimension(40, 40));
            transformPanel.add(button);
        }
        int x = (row*80)-40+xBoardOffset;
        int y = (col*80)-40+yBoardOffset;
        transformPanel.setBounds(y, x, 160, 160);
        layers.add(transformPanel, JLayeredPane.POPUP_LAYER);
        layers.revalidate();
        layers.repaint();
    }
}

