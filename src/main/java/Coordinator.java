public class Coordinator {
    public static void main(String[] args) {
        DataAccessLayer dal = new DataAccessLayer();
        BookingRegistry bookingsJSON = dal.loadBookingRegistry();
        System.out.println("read: " + bookingsJSON);

        Booking newBooking = new Booking(4, 4, 2, 1602406766314L, "test");
        bookingsJSON.addBooking(newBooking);
        System.out.println("before write: " + bookingsJSON);

        dal.saveBookings(bookingsJSON);
    }
}
