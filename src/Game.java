import javax.swing.SwingUtilities;

public class Game {
    public BoardGUI window;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardGUI boardGUI = new BoardGUI();
            boardGUI.loadGUI();
        });
    }
}
