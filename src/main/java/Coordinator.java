public class Coordinator {
    public static void main(String[] args) {
        DataAccessLayer dal = new DataAccessLayer();
        BookingRegistry bookingsJSON = dal.loadBookingRegistry();
        System.out.print(bookingsJSON);
    }
}
