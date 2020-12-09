import java.math.BigInteger;

public class Booking {

    private long userid;
    private long requestid;
    private long dentistid;
    private long issuance;
    private String time;

    public Booking(long userid, long requestid, long dentistid, long issuance, String time) {
        this.userid = userid;
        this.requestid = requestid;
        this.dentistid = dentistid;
        this.issuance = issuance;
        this.time = time;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        String userValidation = String.valueOf(userid); // Coverts the long userid to a String to be used for validation
        if (userValidation.matches("[0-9]{5}")) {
            this.userid = userid;
        }
        else {
            throw new IllegalArgumentException("User id has to  be five digits long");
        }
    }

    public long getRequestid() {
        return requestid;
    }

    public void setRequestid(long requestid) {
        String requestValidation = String.valueOf(requestid); // Coverts the long requestid to a String to be used for validation
        if (requestValidation.matches("[0-9]{1,2}")) {
            this.requestid = requestid;
        }
        else {
            throw new IllegalArgumentException("Request id has to be between one and two digits long");
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
        if(time.matches(".*[0-9][-][:]{16}")) {
            throw new IllegalArgumentException("Time has to be in the format YYYY-MM-DD 00:00 (16 characters long including spaces, dashes, and colons)");
        }
        this.time = time;
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
