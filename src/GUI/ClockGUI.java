package GUI;
import java.awt.*;

import javax.swing.*;

/**
 * Clock erzeugt die GUI einer Uhr. 
 * @author Gruppe 02
 */
public class ClockGUI extends JPanel {
	
	private JLabel time;
    public GUI gui;

	public ClockGUI(int timeLimit) {
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