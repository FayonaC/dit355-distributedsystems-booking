import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookingRegistry {

    private static ArrayList<Booking> Bookings = new ArrayList<>();

    public BookingRegistry(ArrayList<Booking> bookings)  {
        Bookings = bookings;
    }

    public static ArrayList<Booking> getBookings() {
        return Bookings;
    }

    public static void setBookings(ArrayList<Booking> bookings) {
        Bookings = bookings;
    }

    public void addBooking(Booking booking) {
        Bookings.add(booking);
    }

    public String toString() {
        return "{\n" +
                "\"bookings\" : " + Bookings.toString() + "\n}";
    }
}
