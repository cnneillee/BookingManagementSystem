package tw.bm;

import tw.bm.exception.BookingException;
import tw.bm.exception.IllegalInputException;

import java.util.*;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class BookingManager {
    /**
     * Store all of the booking records.
     * KEY represents the place, reflect to VALUE which represents booking list in this place.
     */
    private HashMap<String, List<Booking>> bookingInfoBase = null;

    /**
     * singleton mode
     */
    private static BookingManager ourInstance = new BookingManager();

    public static BookingManager getInstance() {
        return ourInstance;
    }

    private BookingManager() {
        // Suppose that there are four places(A,B,C,D).
        bookingInfoBase = new HashMap<>();
        bookingInfoBase.put("A", new ArrayList<Booking>());
        bookingInfoBase.put("B", new ArrayList<Booking>());
        bookingInfoBase.put("C", new ArrayList<Booking>());
        bookingInfoBase.put("D", new ArrayList<Booking>());
    }

    /**
     * Deal with every standardized booking.
     *
     * @param booking a standardized booking
     */
    public void handleBooking(Booking booking) throws IllegalStateException, BookingException, IllegalInputException {
        if (booking == null) return;

        String place = booking.getPlace();
        boolean cancel = booking.isCanceled();
        List<Booking> bookingList = this.bookingInfoBase.get(place);

        if (cancel && bookingList == null) {// Cancel from a null list is illegal.
            throw new IllegalStateException("IllegalState: cannot cancel from a null list");
        } else if (bookingList == null) {
            throw new IllegalInputException("Error: MainBooking stadium not offer place " + booking.getPlace());
        } else if (!cancel) {// add booking
            addBooking(booking, bookingList);
        } else {// cancel booking
            cancelBooking(booking, bookingList);
        }
    }

    /**
     * Add booking to certain booking list.
     *
     * @param booking     booking to be added
     * @param bookingList booking list
     */
    private void addBooking(Booking booking, List<Booking> bookingList) throws BookingException {
        // check conflict
        for (Booking br : bookingList) {
            // will not conflict with canceled booking
            if (br.isCanceled()) continue;
            // will not conflict with not overlapping booking
            if (!booking.isOverlapping(br)) continue;

            throw new BookingException("Error: the booking conflicts with existing bookings!");
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
    private void cancelBooking(Booking booking, List<Booking> bookingList) throws BookingException {
        Booking toCancel = null;
        // match the booking
        for (Booking br : bookingList) {
            if (br.isCanceled() || !br.equals(booking)) continue;

            toCancel = br;
            break;
        }

        // cancel the booking
        if (toCancel != null) toCancel.cancel();
        else throw new BookingException("Error: the booking being cancelled does not exist!");
    }

    /**
     * Settle the income in detail.
     */
    public String settle() {
        if (bookingInfoBase == null) return "";
        StringBuilder output = new StringBuilder();
        output.append("收⼊汇总\n").append("---\n");
        double incomeTotal = 0;

        // iterate all of booking records
        Set set = bookingInfoBase.entrySet();
        Iterator<Map.Entry<String, List<Booking>>> it = set.iterator();
        int N = set.size();
        for (int i = 0; it.hasNext(); i++) {
            Map.Entry<String, List<Booking>> e = it.next();
            String place = e.getKey();
            output.append("场地:").append(place).append("\n");
            List<Booking> bookings = e.getValue();
            // sort booking list
            Collections.sort(bookings);
            float income = 0;
            for (Booking b : bookings) {
                // append each booking record in detail
                output.append(b).append("\n");
                // calculate the income in certain place
                income += b.getExpense();
            }
            // calculate the total income in this stadium
            incomeTotal += income;
            output.append("小计：").append(income).append("元").append("\n");
            if (i + 1 < N) output.append("\n");
        }
        output.append("---").append("\n");
        output.append("总计：").append(incomeTotal).append("元");
        return output.toString();
    }
}
