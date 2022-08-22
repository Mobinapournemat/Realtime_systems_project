import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GraphicalClock extends JLabel implements Runnable {
    private Thread graphicalThread = new Thread(this);
    private String timeZoneString;
    private int s, m, h;
    private File fontFile = new File("./fonts/digital-7.ttf");
    private Font clockFont;
    private FontMetrics clockFontMetrics;
    private Dimension clockSize;
    private Color fontColor;
    private int clockNumber;

    /**
     * Initializes a clock with a specific font color. The time zone is set to match
     * the default time zone of the computer.
     *
     * @param fontColor the color of the clock font.
     */
    public GraphicalClock(Color fontColor, int clockNumber) {
        this.fontColor = fontColor;
        this.clockNumber = clockNumber;
        timeZoneString = TimeZone.getDefault().getID();
        setup();
    }

    /**
     * Allows you to specify a starting time zone for the clock as well as
     * a specific font color.
     *
     * @param fontColor      the color of the clock font.
     * @param timeZoneString a string representing the time zone of the clock.
     */
    public GraphicalClock(Color fontColor, int clockNumber, String timeZoneString) {
        this.fontColor = fontColor;
        this.clockNumber = clockNumber;
        this.timeZoneString = timeZoneString;
        this.h = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.HOUR);
        this.m = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.MINUTE);
        this.s = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.SECOND);
        setup();
    }

    /**
     * Initializes certain settings for the JLabel and starts the thread.
     */
    private void setup() {
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        setClockFont();
        // graphicalThread.setPriority(5 - this.clockNumber);
        graphicalThread.start();
    }

    /**
     * Sets the font to a large, green, digital font with
     * a black background and sets the size of the component.
     */
    private void setClockFont() {
        try {
            if (fontFile.exists()) {
                clockFont = Font.createFont(Font.TRUETYPE_FONT, fontFile)
                        .deriveFont(Font.PLAIN, 80f);
                this.setFont(clockFont);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        this.setForeground(fontColor);
        this.setBackground(Color.BLACK);

        // Figure out JLabel dimensions using font metrics
        String exampleText = "00:00:00"; // Represents max width of any clock string
        int maxWidth = 0;
        clockFontMetrics = getFontMetrics(clockFont);
        maxWidth = clockFontMetrics.stringWidth(exampleText);
        Insets inset = this.getInsets();
        clockSize = new Dimension(maxWidth + inset.left + inset.right,
                clockFontMetrics.getHeight() + 20 + inset.top + inset.bottom);
        this.setPreferredSize(clockSize);
        this.setHorizontalAlignment(SwingConstants.CENTER);

        // This empty border is to vertically center the JLabel text
        // It's a bit hacky, but it should be okay for this
        this.setBorder(BorderFactory.createEmptyBorder(-20, 0, 0, 0));
    }

    /**
     * The overridden run method updates the time displayed on the clock.
     */
    @Override
    public void run() {
        while (true) {
            try {
                s += 1;
                if (s == 60) {
                    m += 1;
                    s = 0;
                }
                if (m == 60) {
                    h += 1;
                    m = 0;
                }
                if (h == 24) {
                    h = 0;
                    m = 0;
                    s = 0;
                }
                String hh = Integer.toString(h);
                String mm = Integer.toString(m);
                String ss = Integer.toString(s);
                // Apply any necessary leading 0s
                if (hh.length() == 1) {
                    hh = "0" + hh;
                }
                if (mm.length() == 1) {
                    mm = "0" + mm;
                }
                if (ss.length() == 1) {
                    ss = "0" + ss;
                }

                this.setText(hh + ":" + mm + ":" + ss);

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mutator for the time zone of the clock.
     *
     * @param timeZoneString a string representing the time zone.
     */
    public void setTimeZoneString(String timeZoneString) {
        this.timeZoneString = timeZoneString;
    }

    /**
     * Accessor for the time zone of the clock.
     *
     * @return a string representing the time zone.
     */
    public String getTimeZoneString() {
        return timeZoneString;
    }

    public void setThreadPriority(int priority) {
        this.graphicalThread.setPriority(priority);
    }
}