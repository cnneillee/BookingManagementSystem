package tw.bm.exception;

/**
 * @author Neil
 * @date 2017/9/10.
 */
public class BookingConflictException extends Exception {
    public BookingConflictException(String message) {
        super(message);
    }
}
