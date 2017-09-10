package tw.bm;

import tw.bm.exception.BookingException;
import tw.bm.exception.IllegalInputException;

import java.io.*;
import java.util.Scanner;

/**
 * @author Neil
 * @date 2017/9/8.
 */
public class Badminton {
    public static void main(String[] args) {
        Scanner sc = null;
        File file = null;
        if (args.length == 2) {
            try {
                sc = new Scanner(new FileInputStream(args[0]));
                file = new File(args[1]);
            } catch (FileNotFoundException e) {
                System.out.printf("File with path %s not exist\n", args[0]);
                return;
            }
        } else {
            sc = new Scanner(System.in);
            file = new File("output.txt");
        }

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
        String outPut = BookManager.getInstance().settle();
//        System.out.println(outPut);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(outPut, 0, outPut.length());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
