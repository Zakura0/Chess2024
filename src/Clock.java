import java.awt.*;

import javax.swing.*;
/**
 * Die Klasse Clock repräsentiert die grafische Uhr, die die verbleibende Zeit anzeigt.
 */
public class Clock extends JPanel {
	
	private JLabel time;
    public GUI gui;

	public Clock(int timeLimit) {
        time = new JLabel(timeLimit/60 + ":" + String.format("%02d", timeLimit%60));
        time.setFont(new Font("Arial", Font.BOLD, 75));
        time.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.add(time);
        setVisible(true);
	}
    
    public JLabel getTime() {
        return time;
    }

}