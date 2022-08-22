import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimeZone;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.lang.Math;

public class ClockTest {
    private static JFrame frame = new JFrame("Java Clock");
    private static JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    private static JPanel[] clockPanels = new JPanel[4];
    private static TerminalClock[] terminalClocks = new TerminalClock[4];
    private static GraphicalClock[] graphicalClocks = new GraphicalClock[4];
    private static int[][] priorities = new int[1000][4];
    private static JComboBox<String> timeZoneSelector0 = new JComboBox<String>();
    private static JComboBox<String> timeZoneSelector1 = new JComboBox<String>();
    private static JComboBox<String> timeZoneSelector2 = new JComboBox<String>();
    private static JComboBox<String> timeZoneSelector3 = new JComboBox<String>();

    /**
     * The main method.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Get a list of time zones
        String[] timeZoneArray = TimeZone.getAvailableIDs();

        // Populate time zone selectors
        for (String tz : timeZoneArray) {
            timeZoneSelector0.addItem(tz);
            timeZoneSelector1.addItem(tz);
            timeZoneSelector2.addItem(tz);
            timeZoneSelector3.addItem(tz);
        }

        // Add actionListeners to the drop down menus
        timeZoneSelector0.addActionListener(new SelectorActionListener());
        timeZoneSelector1.addActionListener(new SelectorActionListener());
        timeZoneSelector2.addActionListener(new SelectorActionListener());
        timeZoneSelector3.addActionListener(new SelectorActionListener());

        // Create graphical clocks
        graphicalClocks[0] = new GraphicalClock(Color.RED, 1, "Asia/Tehran");
        graphicalClocks[1] = new GraphicalClock(Color.ORANGE, 2, "Canada/Eastern");
        graphicalClocks[2] = new GraphicalClock(Color.YELLOW, 3, "Europe/Istanbul");
        graphicalClocks[3] = new GraphicalClock(Color.GREEN, 4, "Hongkong");

        // Create terminal clocks
        terminalClocks[0] = new TerminalClock(1, "Asia/Tehran");
        terminalClocks[1] = new TerminalClock(2, "Canada/Eastern");
        terminalClocks[2] = new TerminalClock(3, "Europe/Istanbul");
        terminalClocks[3] = new TerminalClock(4, "Hongkong");

        // Set the current items in the lists as the current time zones of the clocks
        timeZoneSelector0.setSelectedItem(graphicalClocks[0].getTimeZoneString());
        timeZoneSelector1.setSelectedItem(graphicalClocks[1].getTimeZoneString());
        timeZoneSelector2.setSelectedItem(graphicalClocks[2].getTimeZoneString());
        timeZoneSelector3.setSelectedItem(graphicalClocks[3].getTimeZoneString());

        // Initialize clock panels
        for (int i = 0; i < clockPanels.length; i++) {
            clockPanels[i] = new JPanel(new BorderLayout(5, 5));
            Border clockPanelBorder = new LineBorder(Color.LIGHT_GRAY, 1);
            Border margin = new EmptyBorder(5, 5, 5, 5);
            clockPanels[i].setBorder(new CompoundBorder(clockPanelBorder, margin));
        }

        // Add selectors and clocks to the clock panels
        clockPanels[0].add(timeZoneSelector0, BorderLayout.NORTH);
        clockPanels[1].add(timeZoneSelector1, BorderLayout.NORTH);
        clockPanels[2].add(timeZoneSelector2, BorderLayout.NORTH);
        clockPanels[3].add(timeZoneSelector3, BorderLayout.NORTH);

        clockPanels[0].add(graphicalClocks[0], BorderLayout.CENTER);
        clockPanels[1].add(graphicalClocks[1], BorderLayout.CENTER);
        clockPanels[2].add(graphicalClocks[2], BorderLayout.CENTER);
        clockPanels[3].add(graphicalClocks[3], BorderLayout.CENTER);

        // Add GUI elements to mainPanel
        for (int i = 0; i < clockPanels.length; i++) {
            mainPanel.add(clockPanels[i]);
        }

        // Add everything to and setup the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.setSize(580, 300);
        frame.setVisible(true);

        // initialize random prirorities 
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                priorities[i][j] = (int) (Math.random() * 9) + 1;
            }
        }

        // set random priorities to test system
        while (true) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 4; j++) {
                    graphicalClocks[j].setThreadPriority(priorities[i][j]);
                    terminalClocks[j].setThreadPriority(priorities[i][j]);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * ActionListener for the time zone selectors.
     */
    private static class SelectorActionListener implements ActionListener {
        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> jcb = (JComboBox<String>) e.getSource();

            if (jcb == timeZoneSelector0) {
                graphicalClocks[0].setTimeZoneString(jcb.getSelectedItem().toString());
                terminalClocks[0].setTimeZoneString(graphicalClocks[0].getTimeZoneString());
            } else if (jcb == timeZoneSelector1) {
                graphicalClocks[1].setTimeZoneString(jcb.getSelectedItem().toString());
                terminalClocks[1].setTimeZoneString(graphicalClocks[1].getTimeZoneString());
            } else if (jcb == timeZoneSelector2) {
                graphicalClocks[2].setTimeZoneString(jcb.getSelectedItem().toString());
                terminalClocks[2].setTimeZoneString(graphicalClocks[2].getTimeZoneString());
            } else if (jcb == timeZoneSelector3) {
                graphicalClocks[3].setTimeZoneString(jcb.getSelectedItem().toString());
                terminalClocks[3].setTimeZoneString(graphicalClocks[3].getTimeZoneString());
            }
        }
    }
}