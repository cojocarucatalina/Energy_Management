package ro.tuc.ds2020.dtos;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DeviceUpdateDTO {

    @NotNull
    private UUID deviceId;

    private String userEmail;

    private int mhec;

    public DeviceUpdateDTO() {
    }

    public DeviceUpdateDTO(UUID deviceId, String userEmail, int mhec) {
        this.deviceId = deviceId;
        this.userEmail = userEmail;
        this.mhec = mhec;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }

    @Override
    public String toString() {
        return "DeviceUpdateDTO{" +
                "deviceId=" + deviceId +
                ", userEmail='" + userEmail + '\'' +
                ", mhec=" + mhec +
                '}';
    }
}
