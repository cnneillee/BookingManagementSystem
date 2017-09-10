package tw.bm.utils;

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

        return (int) (price * rate);
    }

    private static float getRate(boolean isWeekend, boolean canceled) {
        float rate = 1.0f;
        if (canceled) rate = isWeekend ? 0.25f : 0.5f;
        return rate;
    }

    private static int getPrice(boolean isWeekend, Clock from, Clock to) {
        int price = 0;
        if (isWeekend) {
            // 计算第一个区段
            if (from.hour >= 9 && from.hour < 12) {
                int len1 = Math.min(12, to.hour) - from.hour;
                price += len1 * 40;
            }
            // 计算第二个区段
            if (from.hour < 18 && to.hour > 12) {
                int len2 = Math.min(to.hour, 18) - Math.max(from.hour, 12);
                price += len2 * 50;
            }

            // 计算第三个区段
            if (to.hour > 18 && from.hour <= 22) {
                int len3 = to.hour - Math.max(from.hour, 18);
                price += len3 * 60;
            }
        } else {
            // 计算第一个区段
            if (from.hour >= 9 && from.hour < 12) {
                int len1 = Math.min(12, to.hour) - from.hour;
                price += len1 * 30;
            }

            // 计算第二个区段
            if (!(to.hour <= 12 || from.hour >= 18)) {
                int len2 = Math.min(to.hour, 18) - Math.max(from.hour, 12);
                price += len2 * 50;
            }

            // 计算第三个区段
            if (!(to.hour <= 18 || from.hour >= 20)) {
                int len2 = Math.min(to.hour, 20) - Math.max(from.hour, 18);
                price += len2 * 80;
            }

            // 计算第四个区段
            if (to.hour > 20 && from.hour <= 22) {
                int len3 = to.hour - Math.max(from.hour, 20);
                price += len3 * 60;
            }
        }
        return price;
    }
}
