package ro.tuc.ds2020.entities;

public class Notification {
    private String message;

    // Default constructor for serialization/deserialization
    public Notification() {}

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
