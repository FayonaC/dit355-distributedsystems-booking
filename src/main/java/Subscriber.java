import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Subscriber implements MqttCallback {

    private final static ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();

    private final static String TOPIC = "BookingRequest";

    private final static String BROKER = "tcp://localhost:1883";

    private final static String USER_ID = "booking-subscriber";

    private final IMqttClient middleware;

    public Subscriber() throws MqttException {
        middleware = new MqttClient(BROKER, USER_ID);
        middleware.connect();
        middleware.setCallback(this);
    }

    void subscribeToMessages() {
        THREAD_POOL.submit(() -> {
            try {
                middleware.subscribe(TOPIC);
            } catch (MqttSecurityException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost!");
        try {
            middleware.disconnect();
            middleware.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // Try to reestablish? Plan B?
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic '" + topic + "': " + message); // Just here for testing

        // Parsing message JSON
        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(message.toString());
        JSONObject parser = (JSONObject) jsonObject;

        // Creating a booking object using the fields from the parsed JSON
        Booking newBooking = new Booking((Long) parser.get("userid"), (Long) parser.get("requestid"),
                (Long) parser.get("dentistid"), (Long) parser.get("issuance"), (String) parser.get("time"));

        System.out.println("newBooking: " + newBooking); // Just here for testing

        DataAccessLayer dal = new DataAccessLayer();
        Coordinator.bookingRegistry.addBooking(newBooking);
        dal.saveBookings(Coordinator.bookingRegistry);
    }
}