package tw.bm.exception;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class IllegalInputException extends Exception {
    public IllegalInputException(String due) {
        super(due);
    }
}
