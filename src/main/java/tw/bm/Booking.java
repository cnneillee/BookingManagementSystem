package tw.bm;

import tw.bm.exception.IllegalInputException;
import tw.bm.utils.Clock;
import tw.bm.utils.ExpenseCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Booking
 * {@link #income} to cache calculate result.
 * {@link #cancel()}try to cancel the booking, if has been canceled, throws {@link IllegalStateException}.
 * Implements {@link Overlapping} ,{@link Comparable}. Overwrite {@link #equals(Object)} method.
 */
public class Booking implements Overlapping, Comparable<Booking> {
    private String user;
    private Date date;
    private Clock from;
    private Clock to;
    private String place;
    private boolean isCanceled;
    private int income = -1;

    public Booking(String user, String place, boolean isCanceled,
                   Date date, Clock from, Clock to) {
        this.user = user;
        this.date = date;
        this.from = from;
        this.to = to;
        this.place = place;
        this.isCanceled = isCanceled;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public Clock getFrom() {
        return from;
    }

    public Clock getTo() {
        return to;
    }

    public String getPlace() {
        return place;
    }

    public int getExpense() {
        if (income < 0)
            income = ExpenseCalculator.calExpense(this);
        return income;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel() throws IllegalStateException {
        if (isCanceled) {
            throw new IllegalStateException("IllegalOperate: cannot cancel duplicately");
        }
        this.isCanceled = true;
        income = -1;
    }

    @Override
    public boolean isOverlapping(Booking br) {
        if (this.getDate().compareTo(br.getDate()) != 0) return false;
        else if (this.getFrom().compareTo(br.to) >= 0
                || this.getTo().compareTo(br.getFrom()) <= 0) {
            return false;
        } else return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Booking)) return false;
        Booking o = (Booking) obj;
        return o.getUser().equals(this.getUser())
                && o.getPlace().equals(this.getPlace())
                && o.getDate().compareTo(this.getDate()) == 0
                && o.getFrom().compareTo(this.getFrom()) == 0
                && o.getTo().compareTo(this.getTo()) == 0;
    }

    @Override
    public int compareTo(Booking o) {
        int dateComp = this.date.compareTo(o.getDate());
        int fromComp = this.from.compareTo(o.from);
        int toComp = this.to.compareTo(o.to);
        if (dateComp == 0 && fromComp == 0 && toComp == 0) return 0;
        if (dateComp > 0 || (dateComp == 0 && fromComp > 0)
                || (dateComp == 0 && fromComp == 0 && toComp > 0)) return 1;
        else return -1;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(this.getDate());
        StringBuilder sb = new StringBuilder();
        sb.append(date).append(" ");
        sb.append(from).append("~").append(to).append(" ");
        if (this.isCanceled) sb.append("违约金").append(" ");
        int expense = getExpense();
        sb.append(expense);
        return sb.toString();
    }

    /**
     * Builder of booking.
     */
    public static class BookingBuilder {
        String user = null;
        Date date = null;
        Clock from = null;
        Clock to = null;
        String place = null;
        boolean isCancel = false;

        public BookingBuilder() {
        }

        public BookingBuilder user(String user) {
            this.user = user;
            return this;
        }

        public BookingBuilder bookDate(String bookDate) throws IllegalInputException {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(bookDate);
            } catch (ParseException e) {
                throw new IllegalInputException("Error: the booking is invalid!");
            }
            return this;
        }

        public BookingBuilder period(String period) throws IllegalInputException {
            String[] fromAndTo = period.split("~");
            if (fromAndTo.length != 2) {
                throw new IllegalInputException("Error: the booking is invalid!");
            }
            String toStr = fromAndTo[1];
            String fromStr = fromAndTo[0];

            to = Clock.fromStr(toStr);
            from = Clock.fromStr(fromStr);

            if (to.compareTo(from) > 0) {
                return this;
            } else {
                throw new IllegalInputException("Error: the booking is invalid!");
            }
        }

        public BookingBuilder cancel(String flag) throws IllegalInputException {
            if (flag == null || flag.equals("")) isCancel = false;
            else if (flag.equals("C")) isCancel = true;
            else throw new IllegalInputException("Error: the booking is invalid!");
            return this;
        }

        public BookingBuilder place(String place) {
            this.place = place;
            return this;
        }

        public Booking create() {
            if (user == null || place == null || date == null || from == null || to == null) {
                throw new IllegalArgumentException("USER is null!");
            }
            return new Booking(user, place, isCancel, date, from, to);
        }
    }
}

/**
 * Determine weather two booking overlapping
 */
interface Overlapping {
    boolean isOverlapping(Booking br);
}