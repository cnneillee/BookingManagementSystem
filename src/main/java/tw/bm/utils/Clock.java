package tw.bm.utils;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class Clock implements Comparable<Clock> {
    public short hour;
    public short minute;

    public Clock(short hour, short minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Clock(int hour, int minute) {
        this.hour = (short) hour;
        this.minute = (short) minute;
    }

    public static Clock fromStr(String line) {
        try {
            String[] lineArr = line.split(":");
            short hour = Short.valueOf(lineArr[0]);
            short minute = Short.valueOf(lineArr[1]);
            return new Clock(hour, minute);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int compareTo(Clock o) {
        if (this.hour == o.hour && this.minute == o.minute) return 0;
        else if (this.hour > o.hour || (this.hour == o.hour && this.minute > o.minute)) return 1;
        return -1;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}

