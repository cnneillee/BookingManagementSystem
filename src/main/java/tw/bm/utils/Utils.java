package tw.bm.utils;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author Neil
 * @date 2017/9/9.
 */
public class Utils {
    /**
     * Judge weather a date is a weekend.
     *
     * @param date a date
     * @return is weekend
     */
    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == SATURDAY || dayOfWeek == SUNDAY;
    }
}
