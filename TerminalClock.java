import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class TerminalClock implements Runnable {
    private Thread terminalThread = new Thread(this);
    private String timeZoneString;
    private int s, m, h;
    private int clockNumber;

    /**
     * Initializes a clock with a specific font color. The time zone is set to match
     * the default time zone of the computer.
     *
     * @param fontColor the color of the clock font.
     */
    public TerminalClock(int clockNumber, String timeZoneString) {
        this.clockNumber = clockNumber;
        // timeZoneString = TimeZone.getDefault().getID();
        this.h = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.HOUR);
        this.m = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.MINUTE);
        this.s = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString)).get(Calendar.SECOND);
        setup();
    }

    /**
     * Initializes certain settings for the JLabel and starts the thread.
     */
    private void setup() {
        terminalThread.start();
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

                System.out.println("Thread Priority: " + this.terminalThread.getPriority() + ", #Clock: "
                        + this.clockNumber + ", TimeZone: " + this.timeZoneString + ", " + hh + ":" + mm + ":" + ss);

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
        this.terminalThread.setPriority(priority);
    }
}