import org.eclipse.paho.client.mqttv3.MqttException;

public class Coordinator {

    public static BookingRegistry bookingRegistry = new BookingRegistry();

    public static void main(String[] args) throws MqttException {
        DataAccessLayer dal = new DataAccessLayer();
        BookingRegistry bookingsJson = dal.loadBookingRegistry();

        Subscriber s = new Subscriber();
        s.subscribeToMessages();

        System.out.print(bookingsJson);

        Publisher p = new Publisher();
        p.sendMessage(bookingsJson);
        p.close();

    }
}
