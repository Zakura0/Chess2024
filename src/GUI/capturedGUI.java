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

}