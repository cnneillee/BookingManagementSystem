package tw.bm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tw.bm.exception.BookingConflictException;
import tw.bm.exception.DecodeException;
import tw.bm.exception.IllegalInputException;
import tw.bm.utils.PrintUtil;

import java.util.Scanner;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class Badminton {
    private static Logger logger = LogManager.getLogger(Badminton.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                Booking booking = decodeFromStrArr(line);
                BookManager.getInstance().handleBooking(booking);
                PrintUtil.info("Success: the booking is accepted!");
            } catch (IllegalInputException | BookingConflictException e) {
                PrintUtil.error(e.getMessage());
                logger.info("Error: booking error due to\n\t" + e.getMessage());
            } catch (DecodeException | IllegalStateException | IllegalArgumentException e) {
                logger.error(e.getMessage());
            }
        }
        PrintUtil.out(BookManager.getInstance().settle());
    }

    /**
     * {⽤户ID} {预订⽇期 yyyy-MM-dd} {预订时间段 HH:mm~HH:mm} {场地} {取消标记} ，
     * U123 2016-06-02 20:00~22:00 A C ，
     *
     * @param line input
     * @return decoded book record
     */
    private static Booking decodeFromStrArr(String line) throws DecodeException, IllegalInputException {
        String[] lineArr = line.split(" ");
        int len = lineArr.length;
        boolean cancel = false;
        if (len == 4) cancel = false;
        else if (len == 5) cancel = true;
        else throw new IllegalInputException("Error: the booking is invalid!");

        String cancelFlag = cancel ? lineArr[4] : "";
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        return builder.user(lineArr[0])
                .bookDate(lineArr[1])
                .period(lineArr[2])
                .place(lineArr[3])
                .cancel(cancelFlag)
                .create();
    }
}
