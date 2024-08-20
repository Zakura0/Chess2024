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

public class GUI extends JFrame implements Runnable{
    private Dimension frameDim = new Dimension(1500, 900);
    private BoardGUI boardGUI;
    private Clock clockWhite;
    private Clock clockBlack;
    private JButton draw;
    private JButton save;
    private JButton load;
    private JButton reset;
    public static int timeWhite = 10*60;
    public static int timeBlack = 10*60;
    public static boolean startedClock;
    public static Thread counter;
    private Game game;
    public static JLabel infoLabel;
    public static JTextArea movesArea;

    public GUI(Game game) {
        this.game = game;
        setTitle("Chess");
        setSize(frameDim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        infoLabel = new JLabel("Welcome to Chess");
        boardGUI = new BoardGUI(game);
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
        add(boardGUI, BorderLayout.CENTER);


        Border matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 20,new Color(238,238,238));
        scrollPane.setBorder(matteBorder);
        Border emptyBorder = BorderFactory.createEmptyBorder(20, 20, 50, 20);
        
        this.getRootPane().setBorder(emptyBorder); 
        pack();
        setResizable(false);
        setVisible(true);
    }

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

    public void repaintBoard(){
        boardGUI.repaint();
    }

    @Override
	public void run() {	
		while(startedClock) {
			try  {
				Thread.sleep(1000);
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

    public void resetTimer(){
        int timeLimit = 10;
        timeLimit*=60;
        timeWhite = timeLimit; 
        timeBlack = timeLimit;
        infoLabel.setText("");
        clockWhite.getTime().setText(timeWhite/60 + ":" + String.format("%02d", timeWhite%60));
        clockBlack.getTime().setText(timeBlack/60 + ":" + String.format("%02d", timeBlack%60));
        counter = new Thread(this);
        startedClock = false;
    }
}
