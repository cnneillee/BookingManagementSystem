package tw.bm;

import org.junit.Test;
import tw.bm.utils.Clock;

import java.util.Date;

/**
 * @author Neil
 * @date 2017/9/9.
 */
public class BookManagerTest {
    @Test
    public void handleBooking() throws Exception {
        BookManager bm = BookManager.getInstance();
        Booking b = new Booking("Neil", "A",
                false, new Date(2017, 9, 9),
                new Clock((short) 20, (short) 0),
                new Clock((short) 22, (short) 0));
        bm.handleBooking(b);
    }

    @Test
    public void settle() throws Exception {

    }
}