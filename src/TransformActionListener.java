import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransformActionListener implements ActionListener {
    private String _piece;
    private Game _game;

    public TransformActionListener(String piece, Game game) {
        _piece = piece;
        _game = game;
    }

    public void actionPerformed(ActionEvent e) {
        _game.performTransform(_piece);
    }
}
