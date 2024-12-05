package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MeasurementDTO extends RepresentationModel<MeasurementDTO> {
    private UUID id;
    private UUID device_id;
    private int consum;
    private Date timestamp;

    public MeasurementDTO() {
    }

    public MeasurementDTO(UUID id, UUID device_id, int consum, Date timestamp) {
        this.id = id;
        this.device_id = device_id;
        this.consum = consum;
        this.timestamp = timestamp;
    }

    public MeasurementDTO(UUID device_id, int consum, Date timestamp) {
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

    public int getConsum() {
        return consum;
    }

    public void setConsum(int consum) {
        this.consum = consum;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MeasurementDTO measurementDTO = (MeasurementDTO) o;
//        return isAdmin == measurementDTO.isAdmin &&
//                Objects.equals(name, measurementDTO.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, isAdmin);
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public boolean isAdmin() {
//        return isAdmin;
//    }
}
