package tw.bm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tw.bm.exception.BookingConflictException;

import java.util.*;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class BookManager {
    private static Logger logger = LogManager.getLogger(BookManager.class);
    /**
     * Store all of the booking records.
     * KEY represents the place, reflect to VALUE which represents booking list in this place.
     */
    private HashMap<String, List<Booking>> bookingInfoBase = null;

    /**
     * singleton mode
     */
    private static BookManager ourInstance = new BookManager();

    public static BookManager getInstance() {
        return ourInstance;
    }

    private BookManager() {
    }

    /**
     * Deal with every standardized booking.
     *
     * @param booking a standardized booking
     */
    public void handleBooking(Booking booking) throws IllegalStateException, BookingConflictException {
        if (booking == null) return;
        if (this.bookingInfoBase == null)
            this.bookingInfoBase = new HashMap<>();

        String place = booking.getPlace();
        boolean cancel = booking.isCanceled();
        List<Booking> bookingList = this.bookingInfoBase.get(place);

        if (cancel && bookingList == null) {// Cancel from a null list is illegal.
            logger.error("IllegalState: cannot cancel from a null list");
            throw new IllegalStateException("IllegalState: cannot cancel from a null list");
        } else if (bookingList == null) {
            bookingList = new ArrayList<>();
            this.bookingInfoBase.put(place, bookingList);
            bookingList.add(booking);
//          addBooking(booking, bookingList);
            logger.info("Create list of place %s, and add booking", place, booking);
        } else if (!cancel) {// add booking
            addBooking(booking, bookingList);
            logger.info("Add booking %s to place %s", booking, place);
        } else {// cancel booking
            cancelBooking(booking, bookingList);
            logger.info("Successfully cancel booking %s", booking);
        }
    }

    /**
     * Add booking to certain booking list.
     *
     * @param booking     booking to be added
     * @param bookingList booking list
     */
    private void addBooking(Booking booking, List<Booking> bookingList) throws BookingConflictException {
        // check conflict
        for (Booking br : bookingList) {
            // will not conflict with canceled booking
            if (br.isCanceled()) continue;
            // will not conflict with not overlapping booking
            if (!booking.isOverlapping(br)) continue;

            throw new BookingConflictException("Error: the booking conflicts with existing bookings!");
        }

        // add booking without conflicts
        bookingList.add(booking);
    }

    /**
     * Cancel a booking in certain booking list.
     * Using the overwrite {@link Booking#equals(Object)} method to match the booking.
     *
     * @param booking     booking to be canceled
     * @param bookingList booking list
     */
    private void cancelBooking(Booking booking, List<Booking> bookingList) throws IllegalStateException {
        Booking toCancel = null;
        // match the booking
        for (Booking br : bookingList) {
            if (!br.equals(booking)) continue;

            toCancel = br;
            break;
        }

        // cancel the booking
        if (toCancel != null) {
            toCancel.cancel();
        } else {
            logger.debug("Fail to cancel booking %s, due to not match", booking);
        }
    }

    /**
     * Settle the income in detail.
     */
    public String settle() {
        StringBuilder output = new StringBuilder();
        output.append("收⼊汇总\n").append("---");
        float incomeTotal = 0;

        // iterate all of booking records
        for (Map.Entry<String, List<Booking>> e : bookingInfoBase.entrySet()) {
            String place = e.getKey();
            output.append("场地：").append(place);
            List<Booking> bookings = e.getValue();
            // sort booking list
            Collections.sort(bookings);
            float income = 0;
            for (Booking b : bookings) {
                // append each booking record in detail
                output.append(b);
                // calculate the income in certain place
                income += b.getExpense();
            }
            // calculate the total income in this stadium
            incomeTotal += income;
            output.append("小计：").append(income).append("元").append("\n");
        }
        output.append("---").append("\n");
        output.append("总计：").append(incomeTotal).append("元").append("\n");
        System.out.println(output.toString());
        logger.info("Settle the general journal:\n%s", output.toString());
        return output.toString();
    }
}
