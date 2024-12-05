package ro.tuc.ds2020.dtos;

public class DeviceUpdateDTO {

    private String description;
    private String address;
    private int mhec;
    private String userEmail;

    public DeviceUpdateDTO() {
    }

    public DeviceUpdateDTO(String userEmail, String address, int mhec, String description) {
        this.userEmail = userEmail;
        this.address = address;
        this.mhec = mhec;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
