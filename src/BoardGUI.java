
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

public class BoardGUI {

    public BoardGUI(Game g){
        JFrame window = new JFrame("Chess");
        window.setSize(1000, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(true);
        window.setBackground(Color.WHITE);
        window.setVisible(true);
    }

   public void createBoardGraphics(){
    for(int i = 0; i < 8; i++){ // i = file
        for(int j = 0; j < 8; j++){ // j = rank
            boolean isWhiteSquare = (i + j) % 2 != 0;
            int squareColor = (isWhiteSquare) ? 8 : 16; // 8 = white, 16 = black
            var position = new Vector<Float>();

            drawSquare(squareColor, position);
            }
        }
    }

    public void drawSquare(int squareColor, Vector<Float> position){
        Color color = (squareColor == 8)? Color.WHITE : Color.BLACK;
        Graphics2D g = new position.getGraphics();
        g.setColor(color);
        float x = (float) position.getX();
        float y = (float) position.getY();
        float width = 50.0f;
        float height = 50.0f;

        Rectangle2D square = new Rectangle2D.Float(x,y,width,height);
        g.fill(square);
    } 
}