package tw.bm;

import tw.bm.exception.BookingException;
import tw.bm.exception.IllegalInputException;

import java.util.Scanner;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class Badminton {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equals("")) break;
            try {
                Booking booking = decodeFromStrArr(line);
                BookManager.getInstance().handleBooking(booking);
                System.out.println("Success: the booking is accepted!");
            } catch (IllegalInputException | BookingException e) {
                System.out.println(e.getMessage());
            } catch (IllegalStateException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        System.out.println(BookManager.getInstance().settle());
    }

    /**
     * {⽤户ID} {预订⽇期 yyyy-MM-dd} {预订时间段 HH:mm~HH:mm} {场地} {取消标记} ，
     * U123 2016-06-02 20:00~22:00 A C ，
     *
     * @param line input
     * @return decoded book record
     */
    private static Booking decodeFromStrArr(String line) throws IllegalInputException {
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
