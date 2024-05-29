

public class Game {
    public BoardGUI window; 
    public static void main(String[] args) throws Exception {
        new Game();
    }

    public Game(){
        window = new BoardGUI(this);
    }
}
