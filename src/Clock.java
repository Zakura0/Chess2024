import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(6, 1));
		rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		draw = new JButton("Draw");
        draw.addActionListener(this);

		
		JLabel white = new JLabel("White");
		JLabel black = new JLabel("Black");
		whitetime = new JLabel(p1time/600 + ":" + String.format("%02d", p1time%60));
		blacktime = new JLabel(p2time/600 + ":" + String.format("%02d", p2time%60));
		winner = new JLabel("");
		
		rightPanel.add(black);
		rightPanel.add(blacktime);
		rightPanel.add(winner);
		rightPanel.add(whitetime);
		rightPanel.add(white);
		rightPanel.add(draw);
		
		add(rightPanel, BorderLayout.EAST);
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