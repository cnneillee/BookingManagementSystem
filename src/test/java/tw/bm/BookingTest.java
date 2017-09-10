package tw.bm;

import org.junit.Test;
import tw.bm.utils.Clock;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Neil
 * @date 2017/9/9.
 */
public class BookingTest {
    /**
     * 周⼀到周五：
     * 9:00 ~ 12:00 30元/时
     * 12:00 ~ 18:00 50元/时
     * 18:00 ~ 20:00 80元/时
     * 20:00 ~ 22:00 60元/时
     * 周六及周⽇
     * 9:00 ~ 12:00 40元/时
     * 12:00 ~ 18:00 50元/时
     * 18:00 ~ 22:00 60元/时
     *
     * @throws Exception
     */
    @Test
    public void getExpense() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        float calExpense = b.getExpense();
        assertTrue((22 - 20) * 60 == calExpense);
    }

    @Test
    public void cancel() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        boolean oldVal = b.isCanceled();
        b.cancel();
        boolean newVal = b.isCanceled();
        assertNotEquals(oldVal, newVal);
    }

    @Test
    public void isOverlapping() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        Booking b1 = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        Booking b2 = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 10 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        assertTrue(b.isOverlapping(b1));
        assertFalse(b1.isOverlapping(b2));
    }

    @Test
    public void equals() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        Booking b1 = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        assertTrue(b.equals(b1));
    }

    @Test
    public void compareTo() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        Booking b1 = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 10 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        assertTrue(b.compareTo(b1) < 0);
    }

    @Test
    public void testToString() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2017 - 1900, 9 - 1, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        assertEquals("2017-09-09 20:00~22:00 120", b.toString());
    }
}