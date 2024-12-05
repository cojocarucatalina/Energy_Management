package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementDetailsDTO;
import ro.tuc.ds2020.entities.Measurement;

public class MeasurementBuilder {

    private MeasurementBuilder() {
    }

    public static MeasurementDTO toMeasurementDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getId(), measurement.getDevice_id(), measurement.getConsum(), measurement.getTimestamp());
    }

    public static MeasurementDetailsDTO toMeasurementDetailsDTO(Measurement measurement) {
        return new MeasurementDetailsDTO(measurement.getId(), measurement.getDevice_id(), measurement.getConsum(), measurement.getTimestamp());
    }

    public static Measurement toEntity(MeasurementDetailsDTO measurementDetailsDTO) {
        return new Measurement(
                measurementDetailsDTO.getDevice_id(),
                measurementDetailsDTO.getConsum(),
                measurementDetailsDTO.getTimestamp()
        );
    }

}

