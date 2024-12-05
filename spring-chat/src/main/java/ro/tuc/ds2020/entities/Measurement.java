package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "measurement")
public class Measurement implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID device_id;

    @Column(name = "consum", nullable = false)
    private int consum;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public Measurement() {
    }

    public Measurement(UUID id, UUID device_id, int consum, Date timestamp) {
        this.id = id;
        this.device_id = device_id;
        this.consum = consum;
        this.timestamp = timestamp;
    }

    public Measurement(UUID device_id, int consum, Date timestamp) {
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
}
