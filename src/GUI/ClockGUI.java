package GUI;

import java.awt.*;

import javax.swing.*;

/**
 * Clock erzeugt die GUI einer Uhr.
 * 
 * @author Gruppe 02
 */
public class ClockGUI extends JPanel {

    private JLabel time;

    /**
     * Konstruktor zur Erzeugung der Uhr
     * @param timeLimit Das gesetzte Zeitlimit
     */
    public ClockGUI(int timeLimit) {
        time = new JLabel(timeLimit / 60 + ":" + String.format("%02d", timeLimit % 60));
        time.setFont(new Font("Arial", Font.BOLD, 75));
        time.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        time.setPreferredSize(new Dimension((int)(MainGUI.screenSize.width * 0.15), (int) (MainGUI.screenSize.height * 0.12)));
        time.setHorizontalAlignment(JLabel.CENTER);
        this.add(time);
        setVisible(true);
    }
    /**
     * Get-Method für Time
     * @return time
     */
    public JLabel getTime() {
        return time;
    }

    /**
     * Setzt die Farbe der Uhr
     * @param color
     */
    public void setClockColor(Color color){
        time.setForeground(color);
    }

}