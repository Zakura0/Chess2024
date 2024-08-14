import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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

        // GridBagLayout layout = new GridBagLayout();
        // GridBagConstraints gbc = new GridBagConstraints();
        // setLayout(layout);

        setLayout(new BorderLayout());

        infoLabel = new JLabel("Welcome to Chess");
        boardGUI = new BoardGUI(game);
        clockWhite = new Clock(timeWhite);
        clockBlack = new Clock(timeBlack);
        counter = new Thread(this);
        initButtons();

        //add clocks to upper right corner
        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new GridLayout(2, 1));
        clockPanel.add(clockBlack);
        clockPanel.add(clockWhite);
        add(clockPanel, BorderLayout.EAST);
        // gbc.gridx = 1;
        // gbc.gridy = 0;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // add(clockPanel, gbc);

        // //add board to center
        // gbc.gridx = 0;
        // gbc.gridy = 0;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // add(boardGUI, gbc);
        add(boardGUI, BorderLayout.CENTER);

        //add buttons to lower left corner
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        buttonPanel.add(draw);
        buttonPanel.add(save);
        buttonPanel.add(load);
        buttonPanel.add(reset);
        buttonPanel.setPreferredSize(new Dimension(1, 120));
        add(buttonPanel, BorderLayout.SOUTH);
        // gbc.gridx = 0;
        // gbc.gridy = 1;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // add(buttonPanel, gbc);

        // //add info label to lower right corner
        // gbc.gridx = 1;
        // gbc.gridy = 1;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // add(infoLabel, gbc);
        add(infoLabel, BorderLayout.NORTH);

        // //add moves area to upper left corner 
        // gbc.gridx = 0;
        // gbc.gridy = 0;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        // add(scrollPane, gbc);
        movesArea = new JTextArea();
        movesArea.setEditable(false);
        Font font = new Font("Arial", Font.PLAIN, 25); // Erstelle ein Schriftobjekt mit Schriftart, Stil und Größe
        movesArea.setFont(font); // Wende das Schriftobjekt auf das JTextArea an
        JScrollPane scrollPane = new JScrollPane(movesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.WEST);
        
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
