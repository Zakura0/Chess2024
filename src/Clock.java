import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

public class Clock extends JPanel {
	
	private JLabel time;

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