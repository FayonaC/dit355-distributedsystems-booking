import java.util.Date;
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

    private final static String TOPIC = "SuccessfulBooking";

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
                middleware.subscribe(TOPIC, 1);
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
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (middleware.isConnected() == false && elapsedTime < 60 * 1000) {
            // reestablish lost connection
            try {
                Thread.sleep(3000);
                System.out.println("Reconnecting..");
                middleware.reconnect();
                elapsedTime = (new Date()).getTime() - startTime;

            } catch (Exception e) {

            }
        }
        if (middleware.isConnected() == false) {
            try {
                System.out.println("Tried reconnecting for 1 minute, now disconnecting..");
                middleware.unsubscribe("Successful Booking");
                middleware.disconnect();
                middleware.close();
                System.out.println("Booking RIP :(");
                System.out.println("Please restart broker and component");

            } catch (
                    MqttException mqttException) {
                throwable.getMessage();
            }
        }
        if (middleware.isConnected() == true) {
            try {
                middleware.subscribe("Sucessful Booking");
                System.out.println("Connection to broker reestablished!");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * When a message arrives from the above subscription, create a booking and save it
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic '" + topic + "': " + message);

        makeBooking(message);
    }

    /**
     * Creates a booking from the message and saves it
     *
     * @param message
     * @throws Exception
     */
    private void makeBooking(MqttMessage message) throws Exception {
        // Parsing message JSON
        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(message.toString());
        JSONObject parser = (JSONObject) jsonObject;

        long userid = (Long) parser.get("userid");
        long requestid = (Long) parser.get("requestid");
        long dentistid = (Long) parser.get("dentistid"); // Long.parseLong((Long) parser.get("dentistid"));
        long issuance = (Long) parser.get("issuance");
        String time = (String) parser.get("time");

        // Creating a booking object using the fields from the parsed JSON
        Booking newBooking = new Booking(userid, requestid, dentistid, issuance, time);

        System.out.println("New booking object: " + newBooking);

        // Saving the new booking in the booking registry
        DataAccessLayer dal = new DataAccessLayer();
        Coordinator.bookingRegistry.addBooking(newBooking);
        dal.saveBookings(Coordinator.bookingRegistry);

        System.out.println("Booking has been saved in bookings.json!");

        String responseJSON = "\n{\n" +
                "\"userid\": " + userid +
                ",\n\"requestid\": " + requestid +
                ",\n\"time\": \"" + time + "\"" +
                "\n}\n";

        Publisher p = new Publisher();
        p.sendBookingResponse(responseJSON);
        p.sendMessage(Coordinator.bookingRegistry);
        p.close();
    }
}