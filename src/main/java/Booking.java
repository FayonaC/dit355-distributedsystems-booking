import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {

    private long userid;
    private long requestid;
    private long dentistid;
    private long issuance;
    private String time;

    public Booking(long userid, long requestid, long dentistid, long issuance, String time) {
        setUserid(userid);
        setRequestid(requestid);
        setDentistid(dentistid);
        setIssuance(issuance);
        setTime(time);
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        String userValidation = String.valueOf(userid); // Coverts the long userid to a String to be used for validation
        if (userValidation.matches("[0-9]{1,5}")) { // This regex might have to change if we change the format for user id's
            this.userid = userid;
        }
        else {
            throw new IllegalArgumentException("User id has to  be between one and five digits long");
        }
    }

    public long getRequestid() {
        return requestid;
    }

    public void setRequestid(long requestid) {
        String requestValidation = String.valueOf(requestid); // Coverts the long requestid to a String to be used for validation
        if (requestValidation.matches("[0-9]{1,5}")) { // This allows there to be up to 99999 requests
            this.requestid = requestid;
        }
        else {
            throw new IllegalArgumentException("Request id has to be between one and five digits long");
        }
    }

    public long getDentistid() {
        return dentistid;
    }

    public void setDentistid(long dentistid) {
        String dentistValidation = String.valueOf(dentistid); // Coverts the long dentistid to a String to be used for validation
        if (dentistValidation.matches("[0-9]{1,2}")) {
            this.dentistid = dentistid;
        }
        else {
            throw new IllegalArgumentException("Dentist id has to be between one and two digits long");
        }
    }

    public long getIssuance() {
        return issuance;
    }

    public void setIssuance(long issuance) {
        String issuanceValidation = String.valueOf(issuance); // Coverts the long issuance to a String to be used for validation
        if (issuanceValidation.matches("[0-9]{13}")) {
            this.issuance = issuance;
        }
        else {
            throw new IllegalArgumentException("Issuance has to be thirteen digits long");
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        try {
            LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.time = time;
        }
        catch (IllegalArgumentException e) {
            System.out.println("Time has to be in the format YYYY-MM-DD 00:00 (16 characters long including spaces, dashes, and colons)");
        }
    }

    @Override
    public String toString() {
        return "\n{\n" +
                "\"userid\": " + userid +
                ",\n\"requestid\": " + requestid +
                ",\n\"dentistid\": " + dentistid +
                ",\n\"issuance\": " + issuance +
                ",\n\"time\": \"" + time + "\"" +
                "\n}\n";
    }
}
