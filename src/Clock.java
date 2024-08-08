import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.*;

public class Clock extends JPanel implements Runnable, ActionListener {
	
	private static JLabel whitetime, blacktime;
	private static int p1time, p2time;
	public static Thread counter;
	public static boolean started, isWhite = true;
    public static JLabel winner;
    public static JButton draw;
	private Game game;

	public Clock(Game game) {
		setLayout(new BorderLayout());
		this.game = game;
		initializeClock();
		started = false;
	}

	private void initializeClock() {
        int timeLimit = 10;
        timeLimit*=60;
        counter = new Thread(this);
        p1time = timeLimit; 
        p2time = timeLimit;


        JPanel black = new JPanel(new GridBagLayout());
        JPanel white = new JPanel(new GridBagLayout());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        draw = new JButton("Draw");
        draw.addActionListener(this);

		JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveGame.saveGame(Game.moveQueue);
            }
        });
        JButton load = new JButton("load");

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

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter.interrupt();
                game.resetGame();
                resetTimer();
            }
        });

        whitetime = new JLabel(p1time/60 + ":" + String.format("%02d", p1time%60));
        whitetime.setFont(new Font("Arial", Font.BOLD, 75));
        whitetime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        blacktime = new JLabel(p2time/60 + ":" + String.format("%02d", p2time%60));
        blacktime.setFont(new Font("Arial", Font.BOLD, 75));
        blacktime.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        winner = new JLabel("");

        // Hab versucht die Uhr mit EmptyBorder und gbc jz zu setzen, führt beides zu Problemen.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 100);
        gbc.anchor = GridBagConstraints.LINE_START;
        black.add(blacktime, gbc);
        gbc.gridy = 1;
        black.add(winner, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,0, 0, 100 );
        gbc.anchor = GridBagConstraints.LINE_END;
        white.add(whitetime, gbc);

        eastPanel.add(black);
        eastPanel.add(white);
		eastPanel.add(save);
		eastPanel.add(load);
		eastPanel.add(reset);

        add(eastPanel, BorderLayout.EAST);
        add(draw, BorderLayout.SOUTH);
    }

	@Override
	public void run() {	
		while(started) {
			try  {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
                break;
            }
			//Wenn Weiß am Zug ist
			if (isWhite) {
				p1time--;
				whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
				blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
				//Wenn Weiß keine Zeit mehr hat
				if(p1time == 0) {
					winner.setText("Black won!");
					whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
					blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
					break;
				}
			}
			//Wenn Schwarz am Zug ist
			else {
				p2time--;
				whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
				blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
				//Wenn Schwarz keine Zeit mehr hat
				if(p2time == 0) {
					winner.setText("White won!");
					whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
					blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
					break;
				}
			}
		}
		
		if (!started) {
			whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
			blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
		}
	}

	//Wenn Draw Button gedrückt wird, erscheint ein Draw Text
	@Override
	public void actionPerformed(ActionEvent e) {
		started = false;
        Clock.counter.interrupt();
		winner.setText("Draw");
	}

    public void resetTimer(){
        int timeLimit = 10;
        timeLimit*=60;
        p1time = timeLimit; 
        p2time = timeLimit;
        winner.setText("");
        whitetime.setText(p1time/60 + ":" + String.format("%02d", p1time%60));
        blacktime.setText(p2time/60 + ":" + String.format("%02d", p2time%60));
        counter = new Thread(this);
        started = false;
    }
}