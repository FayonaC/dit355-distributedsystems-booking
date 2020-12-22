import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class Publisher {

    private final static String TOPIC = "BookingRegistry";
    private final static String TOPIC2 = "BookingResponse";


    private final static String BROKER = "tcp://localhost:1883";

    private final static String USER_ID = "booking-publisher";

    private final IMqttClient middleware;

    public Publisher() throws MqttException {
        middleware = new MqttClient(BROKER, USER_ID);
        middleware.connect();
    }

    void close() throws MqttException {
        middleware.unsubscribe(new String[]{"BookingRegistry", "BookingResponse"});
        middleware.disconnect();
        middleware.close();
    }

    // This method sends the whole registry to components listening to "BookingRegistry" (Availability)
    void sendMessage(BookingRegistry messageTest) throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage();
        message.setRetained(true);
        String msg = messageTest.toString();
        message.setQos(1);
        message.setPayload(msg.getBytes());
        middleware.publish(TOPIC, message);
    }

    /**
     * Sends a booking response following this format:
     * https://raw.githubusercontent.com/feldob/dit355_2020/master/bookingresponse.json
     *
     * @param response
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    // This method sends the successful booking response to components listening to "BookingResponse" (frontend)
    void sendBookingResponse(String response) throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage();
        String msg = response.toString();
        message.setQos(1);
        message.setPayload(msg.getBytes());
        middleware.publish(TOPIC2, message);
        System.out.println(message.toString());
    }
}
