import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransformActionListener implements ActionListener {
    private String _piece;

    public TransformActionListener(String piece) {
        _piece = piece;
    }

    public void actionPerformed(ActionEvent e) {
        Game.performTransform(_piece);
    }
}
