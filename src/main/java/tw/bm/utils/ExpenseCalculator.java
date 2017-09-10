package tw.bm.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tw.bm.Booking;

import java.util.Date;

/**
 * Calculate booking expense.
 * <p>
 * 羽毛球场的收费标准如下：<p>
 * 周一至周五：
 * <ul>
 * <li>09:00 ~ 12:00 <strong> 30 </strong>元/时</li>
 * <li>12:00 ~ 18:00 <strong> 50 </strong>元/时</li>
 * <li>18:00 ~ 20:00 <strong> 80 </strong>元/时</li>
 * <li>20:00 ~ 22:00 <strong> 60 </strong>元/时</li>
 * </ul>
 * 周六及周日
 * <ul>
 * <li>09:00 ~ 12:00 <strong> 40 </strong>元/时</li>
 * <li>12:00 ~ 18:00 <strong> 50 </strong>元/时</li>
 * <li>18:00 ~ 22:00 <strong> 60 </strong>元/时</li>
 * </ul>
 * 违约金的计算规则如下：<ul>
 * <li>周一至周五的预订取消收取全部费用的<strong> 50% </strong>作为违约金</li>
 * <li>周六及周日的预订取消收取全部费用的<strong> 25% </strong>作为违约金</li>
 *
 * @author Neil
 * @date 2017/9/9.
 */
public class ExpenseCalculator {
    private static Logger logger = LogManager.getLogger(ExpenseCalculator.class);

    /**
     * According to the standards to calculate expense of a booking.
     *
     * @param booking booking
     * @return expense of the booking
     */
    public static int calExpense(Booking booking) {
        Date date = booking.getDate();
        Clock from = booking.getFrom();
        Clock to = booking.getTo();
        boolean canceled = booking.isCanceled();

        boolean isWeekend = Utils.isWeekend(date);
        int price = getPrice(isWeekend, from, to);
        float rate = getRate(isWeekend, canceled);
        int lenInHour = from.hour - to.hour;

        int expense = (int) (lenInHour * price * rate);
        logger.debug("Calculate booking %s:\n\t" +
                "isWeekend: %s\n\t" + isWeekend +
                "price: %s\n\t" + price +
                "rate: %s\n\t" + rate +
                "lenInHour: %s\n" + lenInHour
        );

        return expense;
    }

    private static float getRate(boolean isWeekend, boolean canceled) {
        float rate = 1.0f;
        if (canceled) rate = isWeekend ? 0.25f : 0.5f;
        return rate;
    }

    private static int getPrice(boolean isWeekend, Clock from, Clock to) {
        int price = 0;
        if (isWeekend) {
            if (from.compareTo(new Clock(9, 0)) >= 0 &&
                    to.compareTo(new Clock(12, 0)) <= 0) {
                price = 40;
            } else if (from.compareTo(new Clock(12, 0)) >= 0 &&
                    to.compareTo(new Clock(18, 0)) <= 0) {
                price = 50;
            } else if (from.compareTo(new Clock(18, 0)) >= 0 &&
                    to.compareTo(new Clock(22, 0)) <= 0) {
                price = 60;
            }
        } else {
            if (from.compareTo(new Clock(9, 0)) >= 0 &&
                    to.compareTo(new Clock(12, 0)) <= 0) {
                price = 30;
            } else if (from.compareTo(new Clock(12, 0)) >= 0 &&
                    to.compareTo(new Clock(18, 0)) <= 0) {
                price = 50;
            } else if (from.compareTo(new Clock(18, 0)) >= 0 &&
                    to.compareTo(new Clock(20, 0)) <= 0) {
                price = 80;
            } else if (from.compareTo(new Clock(20, 0)) >= 0 &&
                    to.compareTo(new Clock(22, 0)) <= 0) {
                price = 60;
            }
        }
        return price;
    }
}
