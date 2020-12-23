import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.*;

public class DataAccessLayer {

    /**
     * Change the filepath variable if you want to load a different JSON file.
     */
    private final String filepath = "././bookings.json";

    /**
     * Loads bookings from a JSON file.
     * Returns null if no booking could be loaded/created from JSON file.
     * @return BookingRegistry or null
     */
    public BookingRegistry loadBookingRegistry() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filepath)) {
            Object jsonObject = jsonParser.parse(reader);
            JSONObject parser = (JSONObject) jsonObject;
            //Retrieves JSON for bookings
            JSONArray bookingsJSON = (JSONArray) parser.get("bookings");
            BookingRegistry registry = loadBookings(bookingsJSON);

            return registry;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an ArrayList with Bookings from JSONArray
     * @param arr JSONArray
     * @return a BookingRegistry
     */
    private BookingRegistry loadBookings(JSONArray arr) {
        ArrayList<Booking> bookings = new ArrayList<Booking>();

        for (Object booking : arr) {
            JSONObject bookingObj = (JSONObject) booking;

            long userid = (Long) bookingObj.get("userid");
            long requestid = (Long) bookingObj.get("requestid");
            long dentistid = (Long) bookingObj.get("dentistid");
            long issuance = (Long) bookingObj.get("issuance");
            String time = (String) bookingObj.get("time");

            try {
                bookings.add(new Booking(userid, requestid, dentistid, issuance, time));
            } catch (IllegalArgumentException e) {
                System.err.println("Error when creating new Booking: " + e.getMessage());
            }
        }
        BookingRegistry registry = new BookingRegistry(bookings);
        return registry;
    }

    public void saveBookings(BookingRegistry bookings) {
        try (FileWriter file = new FileWriter(filepath)) {
            JSONArray bookingsJSON = new JSONArray();
            List<? extends Booking> collection = new ArrayList<>(bookings.getBookings());
            bookingsJSON.addAll(collection);
            file.write("{\n\"bookings\": " + bookingsJSON.toJSONString() + "\n}");
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
