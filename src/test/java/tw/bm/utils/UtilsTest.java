package tw.bm.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author Neil
 * @date 2017/9/11.
 */
public class UtilsTest {
    @Test
    public void isWeekend() throws Exception {
        // 2017.09.08 周五
        // 2017.09.10 周日
        assertFalse(Utils.isWeekend(new Date(2017 - 1900, 9 - 1, 8)));
        assertTrue(Utils.isWeekend(new Date(2017 - 1900, 9 - 1, 10)));
    }
}