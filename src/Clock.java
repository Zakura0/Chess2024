import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Clock extends JPanel implements Runnable, ActionListener {
	
	private static JLabel whitetime, blacktime;
	private static int p1time, p2time;
	public static Thread counter;
	public static boolean started, isWhite = true;
    public static JLabel winner;
    public static JButton draw;

	public Clock() {
		setLayout(new BorderLayout());
		initializeClock();
		started = false;
	}

	private void initializeClock() {
		int timeLimit = 10;									
		timeLimit*=600;
		counter = new Thread(this);
		p1time = timeLimit; 
		p2time = timeLimit;	
		
		
		JPanel black = new JPanel(new GridBagLayout());
		JPanel white = new JPanel(new GridBagLayout());

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		draw = new JButton("Draw");
        draw.addActionListener(this);

		whitetime = new JLabel(p1time/600 + ":" + String.format("%02d", p1time%60));
		whitetime.setFont(new Font("Arial", Font.BOLD, 75));
		whitetime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		blacktime = new JLabel(p2time/600 + ":" + String.format("%02d", p2time%60));
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

		add(eastPanel, BorderLayout.EAST);
		add(draw, BorderLayout.SOUTH);
	}

	@Override
	public void run() {	
		while(started) {
			try  {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {}
			//Wenn Weiß am Zug ist
			if (isWhite) {
				p1time--;
				whitetime.setText(p1time/600 + ":" + String.format("%02d", p1time%60));
				blacktime.setText(p2time/600 + ":" + String.format("%02d", p2time%60));
				//Wenn Weiß keine Zeit mehr hat
				if(p1time == 0) {
					winner.setText("Black won!");
					whitetime.setText(p1time/600 + ":" + String.format("%02d", p1time%60));
					blacktime.setText(p2time/600 + ":" + String.format("%02d", p2time%60));
					break;
				}
			}
			//Wenn Schwarz am Zug ist
			else {
				p2time--;
				whitetime.setText(p1time/600 + ":" + String.format("%02d", p1time%60));
				blacktime.setText(p2time/600 + ":" + String.format("%02d", p2time%60));
				//Wenn Schwarz keine Zeit mehr hat
				if(p2time == 0) {
					winner.setText("White won!");
					whitetime.setText(p1time/600 + ":" + String.format("%02d", p1time%60));
					blacktime.setText(p2time/600 + ":" + String.format("%02d", p2time%60));
					break;
				}
			}
		}
		
		if (!started) {
			whitetime.setText(p1time/600 + ":" + String.format("%02d", p1time%60));
			blacktime.setText(p2time/600 + ":" + String.format("%02d", p2time%60));
		}
	}

	//Wenn Draw Button gedrückt wird, erscheint ein Draw Text
	@Override
	public void actionPerformed(ActionEvent e) {
		started = false;
		winner.setText("Draw");
	}
}