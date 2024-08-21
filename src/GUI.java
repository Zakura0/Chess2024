import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

/**
 * In GUI werden neben dem Schachbrett die Haupt-GUI Komponenten dargestellt.
 * @author Gruppe 02
 * 
 */
public class GUI extends JFrame implements Runnable{
    private Dimension frameDim = new Dimension(1500, 900);
    private BoardGUI boardGUI;
    private capturedGUI capturedGUI;
    private String whitePlayer;
    private String blackPlayer;
    private Clock clockWhite;
    private Clock clockBlack;
    private JButton draw;
    private JButton save;
    private JButton load;
    private JButton reset;
    public static int timeWhite;
    public static int timeBlack;
    public static boolean startedClock;
    public static Thread counter;
    private Game game;
    public static JLabel infoLabel;
    public static JTextArea movesArea;
    public static boolean unitTest = false;

    public GUI(Game game) {
        this.game = game;
        setTitle("Chess");
        setSize(frameDim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        {
            if (!unitTest) {
                openNewGameDialog();
            }
            else {
                whitePlayer = "White";
                blackPlayer = "Black";
                timeWhite = 10;
                timeBlack = 10;
            }
        }        
        timeWhite *= 60;
        timeBlack *= 60;

        infoLabel = new JLabel("Welcome to Chess");
        boardGUI = new BoardGUI(game);
        capturedGUI = new capturedGUI();
        clockWhite = new Clock(timeWhite);
        clockBlack = new Clock(timeBlack);
        counter = new Thread(this);
        initButtons();

        JPanel blackClockPanel = new JPanel();
        blackClockPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcB = new GridBagConstraints();
        gbcB.insets = new Insets(0, 50, 0, 50); 
        blackClockPanel.add(clockBlack, gbcB);

        JPanel whiteClockPanel = new JPanel();
        whiteClockPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcW = new GridBagConstraints();
        gbcW = new GridBagConstraints();
        gbcW.insets = new Insets(5, 50, 0, 50);
        whiteClockPanel.add(clockWhite, gbcW);


        Dimension buttonSize = new Dimension(150, 30);
        draw.setPreferredSize(buttonSize);
        save.setPreferredSize(buttonSize);
        load.setPreferredSize(buttonSize);
        reset.setPreferredSize(buttonSize);


        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        eastPanel.add(blackClockPanel, gbc);
        gbc.gridy = 1;
        eastPanel.add(draw, gbc);
        gbc.gridy = 2;
        eastPanel.add(save, gbc);
        gbc.gridy = 3;
        eastPanel.add(load, gbc);
        gbc.gridy = 4;
        eastPanel.add(reset, gbc);
        gbc.gridy = 5;
        eastPanel.add(whiteClockPanel, gbc);
        add(eastPanel, BorderLayout.EAST);
        add(infoLabel, BorderLayout.NORTH);

        movesArea = new JTextArea();
        movesArea.setEditable(false);
        Font font = new Font("Arial", Font.PLAIN, 25); // Erstelle ein Schriftobjekt mit Schriftart, Stil und Größe
        movesArea.setFont(font); // Wende das Schriftobjekt auf das JTextArea an
        JScrollPane scrollPane = new JScrollPane(movesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        add(scrollPane, BorderLayout.WEST);

        /*JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2));
        centerPanel.add(boardGUI);
        centerPanel.add(capturedGUI); 
        */
        add(boardGUI ,BorderLayout.CENTER);


        Border matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 20,new Color(238,238,238));
        scrollPane.setBorder(matteBorder);
        Border emptyBorder = BorderFactory.createEmptyBorder(20, 20, 50, 20);

        this.getRootPane().setBorder(emptyBorder); 
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void openNewGameDialog() {
        JTextField whitePlayerField = new JTextField();
        JTextField blackPlayerField = new JTextField();
        JTextField timeField = new JTextField();

        Object[] message = {
            "White Player:", whitePlayerField,
            "Black Player:", blackPlayerField,
            "Time (minutes):", timeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Chess", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            whitePlayer = whitePlayerField.getText();
            blackPlayer = blackPlayerField.getText();
            int time = Integer.parseInt(timeField.getText());
            timeWhite = time;
            timeBlack = time;
        }
    }

    /**
     * Die Methode initialisiert alle Buttons (Draw, Save, Load, Reset).
     * Hier bei stoppt das Schachspiel und die Uhr wenn auf "Draw" gedrückt wird. Das Spiel endet im Unentschieden.
     * "Save" erzeugt eine .txt Datei und speichert alle Informationen über das aktuelle Schachbrett (Position, Rocharde, Wer dran ist).
     * "Load" lädt eine gewisse Schachposition aus einer .txt Datei ("Save").
     * "Reset" setzt das Schachbrett und die Uhr zurück. 
     */
    private void initButtons(){
        draw = new JButton("Draw");
        draw.addActionListener(new ActionListener() {
            @Override
	        public void actionPerformed(ActionEvent e) {
		        startedClock = false;
                counter.interrupt();
		        infoLabel.setText("Draw");
	        }
        });

		save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveGame.saveGame(Game.moveQueue); 
            }
        });

        load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ArrayList<Move> loadedmoveQueue = SaveGame.loadGameMoves(selectedFile.getAbsolutePath());
                    game.resetGame();
                    SaveGame.loadGame(game, loadedmoveQueue);
                }
            }
        });

        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter.interrupt();
                game.resetGame();
                resetTimer();
            }
        });
    }

    /**
     * Setzt die GUI zurück.
     */
    public void repaintBoard(){
        boardGUI.repaint();
    }

    /**
    *  Implementation der Uhr.
    * @param timeWhite Die Zeit für Weiß.
    * @param timeBlack Die Zeit für Schwarz.
    * @boolean startedClock = false Die Uhr fängt nicht an zu zählen, =true, Die Uhr fängt an zu zählen.
     */
    @Override
	public void run() {	
		while(startedClock) {
			try  {
				Thread.sleep(1000); //Uhr läuft auf einem anderen Thread um Synchronität und Funktionalität zu garantieren.
			}
			catch (InterruptedException e) {
                break;
            }
			//Wenn Weiß am Zug ist
			if (Game.isWhite) {	
                timeWhite--;			
				clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
				clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
				//Wenn Weiß keine Zeit mehr hat
				if(timeWhite == 0) {
					infoLabel.setText("Black won!");
					clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
					clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
					break;
				}
			}
			//Wenn Schwarz am Zug ist
			else {
				timeBlack--;
				clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
				clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
				//Wenn Schwarz keine Zeit mehr hat
				if(timeBlack == 0) {
					infoLabel.setText("White won!");
					clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
					clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
					break;
				}
			}
		}
		
		if (!startedClock) {
			clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
			clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
		}
	}
/**
 * Setzt die Uhr für beide Spieler zurück.
 * @param timeLimit Die gegebene Spielzeit für beide Spieler (10 Minuten default).
 * @param timeWhite Die Zeit für Weiß (hier = timelimit, da die Uhr zurückgesetzt wird).
 * @param timeBlack Die Zeit für Schwarz.
 * @param infoLabel JTextField welches gewisse Infos anzeigt wie "Black won!" oder hier: "Welcome to Chess".
 * @param startedClock = false Die Uhr fängt nicht an zu zählen wenn auf Reset gedrückt wird. 
 */
    public void resetTimer(){
        int timeLimit = 10;
        timeLimit*=60;
        timeWhite = timeLimit; 
        timeBlack = timeLimit;
        infoLabel.setText("Welcome to Chess");
        clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
        clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
        counter = new Thread(this);
        startedClock = false;
    }

    

    /*
     * Diese Methode behandelt die Bauernumwandlung.
     */
    public int getPromotionChoice(Move move) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Choose a piece for promotion:",
            "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(), game.isWhiteTurn());
                return 1;
            case 1:
                Board.board[move.getDestRow()][move.getDestCol()] = new Rook(move.getDestRow(), move.getDestCol(), game.isWhiteTurn());
                return 2;
            case 2:
                Board.board[move.getDestRow()][move.getDestCol()] = new Bishop(move.getDestRow(), move.getDestCol(), game.isWhiteTurn());
                return 3;
            case 3:
                Board.board[move.getDestRow()][move.getDestCol()] = new Knight(move.getDestRow(), move.getDestCol(), game.isWhiteTurn());
                return 4;
            default:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(), game.isWhiteTurn());
                return 1;
        }
    }
}
