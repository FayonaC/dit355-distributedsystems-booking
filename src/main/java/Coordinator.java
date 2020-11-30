import org.eclipse.paho.client.mqttv3.MqttException;

public class Coordinator {
    public static void main(String[] args) throws MqttException {

        Subscriber s = new Subscriber();
        s.subscribeToMessages();

        DataAccessLayer dal = new DataAccessLayer();
        BookingRegistry bookingsJson = dal.loadBookingRegistry();
        System.out.print(bookingsJson);

        Publisher p = new Publisher();
        p.sendMessage(bookingsJson);
        p.close();
    }
}
