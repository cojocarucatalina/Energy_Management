package ro.tuc.ds2020.dtos;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MeasurementDetailsDTO {

    private UUID id;
    @NotNull
    private UUID device_id;
    @NotNull
    private int consum;
    @NotNull
    private Date timestamp;

    public MeasurementDetailsDTO() {
    }

    public MeasurementDetailsDTO(UUID id, UUID device_id, int consum, Date timestamp) {
        this.id = id;
        this.device_id = device_id;
        this.consum = consum;
        this.timestamp = timestamp;
    }

    public MeasurementDetailsDTO(UUID device_id, int consum, Date timestamp) {
        this.device_id = device_id;
        this.consum = consum;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDevice_id() {
        return device_id;
    }

    public void setDevice_id(UUID device_id) {
        this.device_id = device_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getConsum() {
        return consum;
    }

    public void setConsum(int consum) {
        this.consum = consum;
    }

}
