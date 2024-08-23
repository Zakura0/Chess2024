package GUI;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


import javax.swing.*;
import javax.swing.border.Border;

import Core.Board;
import Core.Game;
import Core.Move;
import Core.SaveGame;
import Core.Pieces.Bishop;
import Core.Pieces.Knight;
import Core.Pieces.Piece;
import Core.Pieces.Queen;
import Core.Pieces.Rook;

import java.awt.Color;

/**
 * In GUI werden neben dem Schachbrett die Haupt-GUI Komponenten dargestellt.
 * 
 * @author Gruppe 02
 * 
 */
public class MainGUI extends JFrame implements Runnable {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private BoardGUI boardGUI;
    private JPanel capturedWhite;
    private JPanel capturedBlack;
    private String whitePlayer;
    private String blackPlayer;
    private ClockGUI clockWhite;
    private ClockGUI clockBlack;
    private JButton draw;
    private JButton reset;
    public static int timeLimit;
    public static int timeWhite;
    public static int timeBlack;
    public static boolean startedClock;
    public static Thread counter;
    private Game game;
    public static JLabel infoLabel;
    public static JTextArea movesArea;
    public static boolean unitTest = false;

    /**
     * Konstruktor zur Erzeugung der GUI.
     * @param game
     */
    public MainGUI(Game game) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.game = game;
        setTitle("Chess");
        setPreferredSize(new Dimension((int) (screenSize.width * 0.9), (int) (screenSize.height * 0.92)));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        {
            if (!unitTest) {
                openNewGameDialog();
            } else {
                whitePlayer = "White";
                blackPlayer = "Black";
                timeLimit = 10;
                timeWhite = 10;
                timeBlack = 10;
            }
        }
        timeWhite *= 60;
        timeBlack *= 60;

        infoLabel = new JLabel("Welcome to Chess");
        JLabel blackPlayerLabel = new JLabel(blackPlayer);
        JLabel whitePlayerLabel = new JLabel(whitePlayer);
        boardGUI = new BoardGUI(game);
        clockWhite = new ClockGUI(timeWhite);
        clockBlack = new ClockGUI(timeBlack);
        counter = new Thread(this);
        initButtons();
        initMenu();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();

        gbcMain.insets = new Insets(0,0,0,0);
        gbcMain.anchor = GridBagConstraints.LINE_START;

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        mainPanel.add(infoLabel, gbcMain);

       /*  gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        mainPanel.add(boardGUI, gbcMain); */
        

        JPanel blackClockPanel = new JPanel();
        blackClockPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcB = new GridBagConstraints();
        gbcB.insets = new Insets(0, (int) (screenSize.width * 0.0245), (int) (screenSize.width * 0.07), 0);
        blackClockPanel.add(clockBlack, gbcB);

        JPanel whiteClockPanel = new JPanel();
        whiteClockPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcW = new GridBagConstraints();
        gbcW.insets = new Insets((int) (screenSize.width * 0.07), (int) (screenSize.width * 0.0245), 0, 0);
        whiteClockPanel.add(clockWhite, gbcW);

        JPanel capturedBlackPanel = new JPanel();
        capturedBlack = new JPanel(new GridLayout(1, 16, 1, 1));
        capturedBlackPanel.setLayout(new GridBagLayout());
        capturedBlackPanel.setPreferredSize(new Dimension((int) (screenSize.width * 0.3), (int) (screenSize.height * 0.07)));
        GridBagConstraints gbcCB = new GridBagConstraints();
        gbcCB.insets = new Insets(15, 0, 0, 350);
        capturedBlackPanel.add(capturedBlack, gbcCB);

        JPanel capturedWhitePanel = new JPanel();
        capturedWhite = new JPanel(new GridLayout(1, 16, 1, 1));
        capturedWhitePanel.setLayout(new GridBagLayout());
        capturedWhitePanel.setPreferredSize(new Dimension((int) (screenSize.width * 0.3), (int) (screenSize.height * 0.07)));
        GridBagConstraints gbcCW = new GridBagConstraints();
        gbcCW.insets = new Insets(0, 0, 15, 350);
        capturedWhitePanel.add(capturedWhite, gbcCW);

        //JPanel spacePanel = new JPanel();
        //spacePanel.setPreferredSize(new Dimension((int) (screenSize.width * 0.15), (int) (screenSize.height * 0.05)));

        Dimension buttonSize = new Dimension((int)(screenSize.width* 0.12),(int)(screenSize.height * 0.1) );
        draw.setPreferredSize(buttonSize);
        reset.setPreferredSize(buttonSize);

        JPanel drawPanel = new JPanel();
        drawPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcDraw = new GridBagConstraints();
        gbcDraw.insets = new Insets(0, (int) (screenSize.width * 0.031),(int) (screenSize.height * 0.02) , 0);
        drawPanel.add(draw, gbcDraw);

        JPanel resetPanel = new JPanel();
        resetPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcReset = new GridBagConstraints();
        gbcReset.insets = new Insets(0, (int) (screenSize.width * 0.031), 0, 0);
        resetPanel.add(reset, gbcReset);

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        Font PlayerFont = new Font("Arial", Font.PLAIN, 18);
        JLabel label = new JLabel();
        label.setFont(PlayerFont);
        label.setHorizontalAlignment(JLabel.CENTER);
        whitePlayerLabel.setPreferredSize(new Dimension((int) (screenSize.width * 0.105), (int) (screenSize.height * 0.033)));
        blackPlayerLabel.setPreferredSize(new Dimension((int) (screenSize.width * 0.035), (int) (screenSize.height * 0.033)));
        whitePlayerLabel.setFont(PlayerFont);
        blackPlayerLabel.setFont(PlayerFont);


        JPanel whitePlayerPanel = new JPanel();
        whitePlayerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcWP = new GridBagConstraints();
        gbcWP.insets = new Insets(0, (int) (screenSize.width * 0.12), 0, 0);
        whitePlayerPanel.add(whitePlayerLabel, gbcWP);

        JPanel blackPlayerPanel = new JPanel();
        blackPlayerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcBP = new GridBagConstraints();
        gbcBP.insets = new Insets(0, (int) (screenSize.width * 0.05), 0, 0);
        blackPlayerPanel.add(blackPlayerLabel, gbcBP);


        gbc.gridx = 0;
        gbc.gridy = 0;
        eastPanel.add(blackPlayerPanel, gbc);
        gbc.gridy = 1;
        eastPanel.add(blackClockPanel, gbc);
        gbc.gridy = 2;
        eastPanel.add(drawPanel, gbc);
        gbc.gridy = 3;
        eastPanel.add(resetPanel, gbc);
        gbc.gridy = 4;
        eastPanel.add(whiteClockPanel, gbc);
        gbc.gridy = 5;
        eastPanel.add(whitePlayerPanel, gbc);

        gbcMain.gridx = 2;
        gbcMain.gridy = 1;
        mainPanel.add(eastPanel, gbcMain); 

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.add(capturedWhitePanel);
        middlePanel.add(boardGUI);
        middlePanel.add(capturedBlackPanel);
        
        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        mainPanel.add(middlePanel, gbcMain);

        /*gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        mainPanel.add(capturedWhitePanel, gbcMain);

        gbcMain.gridx = 1;
        gbcMain.gridy = 2;
        mainPanel.add(capturedBlackPanel, gbcMain); */



        movesArea = new JTextArea();
        movesArea.setEditable(false);
        Font font = new Font("Arial", Font.PLAIN, 25); 
        movesArea.setFont(font); 
        JScrollPane scrollPane = new JScrollPane(movesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (screenSize.width * 0.15), (int) (screenSize.height * 0.711)));
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarGUI());


        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        mainPanel.add(scrollPane, gbcMain);

        add(mainPanel);

        Border matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 20, new Color(238, 238, 238));
        scrollPane.setBorder(matteBorder);

        pack();
        setResizable(false);
        setVisible(true);
    }
    /**
     * Erzeugung des Eingabefensters, wo Spielernamen und Zeit eingegeben werden kann.
     * 
     */

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
            if (whitePlayerField.getText().equals("") ||
                    blackPlayerField.getText().equals("") || timeField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                openNewGameDialog();
            } else if (timeField.getText().matches("[0-9]+") == false) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                openNewGameDialog();
            } else {
                whitePlayer = whitePlayerField.getText();
                blackPlayer = blackPlayerField.getText();
                int time = Integer.parseInt(timeField.getText());
                timeLimit = time;
                timeWhite = time;
                timeBlack = time;
            }
        } else {
            System.exit(0);
        }
    }


    /**
     * Initializierung der MenuBar mit Save und Load Funktion von Schachpositionen.
     */
    private void initMenu() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                SaveGame.saveGame(Game.moveQueue);
            }
        });

        JMenuItem load = new JMenuItem("Load");
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

        fileMenu.add(save);
        fileMenu.add(load);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }
    /**
     * Die Methode initialisiert alle Buttons (Draw, Save, Load, Reset).
     * Hier bei stoppt das Schachspiel und die Uhr wenn auf "Draw" gedrückt wird.
     * Das Spiel endet im Unentschieden.
     * "Save" erzeugt eine .txt Datei und speichert alle Informationen über das
     * aktuelle Schachbrett (Position, Rocharde, Wer dran ist).
     * "Load" lädt eine gewisse Schachposition aus einer .txt Datei ("Save").
     * "Reset" setzt das Schachbrett und die Uhr zurück.
     */
    private void initButtons() {
        draw = new JButton("Draw");
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startedClock = false;
                counter.interrupt();
                infoLabel.setText("Draw");
            }
        });

        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter.interrupt();
                game.resetGame();
                resetTimer();
                capturedWhite.removeAll();
                capturedBlack.removeAll();

            }
        });
    }

    /**
     * Setzt die GUI zurück.
     */
    public void repaintBoard() {
        boardGUI.repaint();
    }

    /**
     * Implementation der Uhr.
     * 
     * @boolean startedClock = false Die Uhr fängt nicht an zu zählen, =true, Die Uhr fängt an zu zählen.
     */
    @Override
    public void run() {
        while (startedClock) {
            try {
                Thread.sleep(1000); // Uhr läuft auf einem anderen Thread um Synchronität und Funktionalität zu
                                    // garantieren.
            } catch (InterruptedException e) {
                break;
            }
            // Wenn Weiß am Zug ist
            if (Game.isWhite) {
                timeWhite--;
                clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
                clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
                clockWhite.setClockColor(Color.RED);
                clockBlack.setClockColor(Color.BLACK);

                // Wenn Weiß keine Zeit mehr hat
                if (timeWhite == 0) {
                    infoLabel.setText("Black won!");
                    clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
                    clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
                    break;
                }
            }
            // Wenn Schwarz am Zug ist
            else {
                timeBlack--;
                clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
                clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
                clockBlack.setClockColor(Color.RED);
                clockWhite.setClockColor(Color.BLACK);
                // Wenn Schwarz keine Zeit mehr hat
                if (timeBlack == 0) {
                    infoLabel.setText("White won!");
                    clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
                    clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
                    break;
                }
            }
        }

        if (!startedClock) {
            clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
            clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
        }
    }

    /**
     * Aktualisiert die Anzeige der geschlagenen Figuren.
     * @param white boolean zur Unterscheidung der Farbe einer geschlagenen Figur
     */
    public void updateCapturedPieces(boolean white) {

        JPanel panel = white ? capturedWhite : capturedBlack;
        List<Piece> capturedPieces = white ? Game.whiteDead : Game.blackDead;
        panel.removeAll();
        for (Piece piece : capturedPieces) {
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH)));
            panel.add(pieceLabel);
        }

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Setzt die Uhr für beide Spieler zurück.
     * 
     */
    public void resetTimer() {
        int timeLimit = MainGUI.timeLimit;
        timeLimit *= 60;
        timeWhite = timeLimit;
        timeBlack = timeLimit;
        infoLabel.setText("Welcome to Chess");
        clockWhite.getTime().setText(timeWhite / 60 + ":" + String.format("%02d", timeWhite % 60));
        clockBlack.getTime().setText(timeBlack / 60 + ":" + String.format("%02d", timeBlack % 60));
        counter = new Thread(this);
        startedClock = false;
    }

    /**
     * Diese Methode behandelt die Bauernumwandlung.
     * 
     * @param move Der Zug, der die Bauernumwandlung auslöst.
     */
    public int getPromotionChoice(Move move) {
        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose a piece for promotion:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(),
                        game.isWhiteTurn());
                return 1;
            case 1:
                Board.board[move.getDestRow()][move.getDestCol()] = new Rook(move.getDestRow(), move.getDestCol(),
                        game.isWhiteTurn());
                return 2;
            case 2:
                Board.board[move.getDestRow()][move.getDestCol()] = new Bishop(move.getDestRow(), move.getDestCol(),
                        game.isWhiteTurn());
                return 3;
            case 3:
                Board.board[move.getDestRow()][move.getDestCol()] = new Knight(move.getDestRow(), move.getDestCol(),
                        game.isWhiteTurn());
                return 4;
            default:
                Board.board[move.getDestRow()][move.getDestCol()] = new Queen(move.getDestRow(), move.getDestCol(),
                        game.isWhiteTurn());
                return 1;
        }
    }
}