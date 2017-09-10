package tw.bm.utils;

import org.junit.Test;
import tw.bm.Booking;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Neil
 * @date 2017/9/11.
 */
public class ExpenseCalculatorTest {
    @Test
    public void calExpense() throws Exception {
        Booking b = new Booking("Neil", "A",
                false, new Date(2016 - 1900, 6 - 1, 2),
                new Clock((short) 10, (short) 0),
                new Clock((short) 12, (short) 0));
        Booking b1 = new Booking("Neil", "A",
                false, new Date(2016 - 1900, 6 - 1, 2),
                new Clock((short) 9, (short) 0),
                new Clock((short) 10, (short) 0));
        assertEquals(60, ExpenseCalculator.calExpense(b));
        b1.cancel();
        assertEquals(15, ExpenseCalculator.calExpense(b1));
    }

}