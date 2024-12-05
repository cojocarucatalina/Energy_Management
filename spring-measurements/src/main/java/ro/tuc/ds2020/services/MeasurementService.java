package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementDetailsDTO;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.entities.Measurement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import ro.tuc.ds2020.repositories.MeasurementRepository;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final MeasurementRepository measurementRepository;
    private final DeviceReferenceService deviceReferenceService;

    @Autowired
    public MeasurementService(DeviceReferenceService deviceReferenceService, MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
        this.deviceReferenceService = deviceReferenceService;
    }

    public List<MeasurementDTO> findMeasurements() {
        List<Measurement> MeasurementList = measurementRepository.findAll();
        return MeasurementList.stream()
                .map(MeasurementBuilder::toMeasurementDTO)
                .collect(Collectors.toList());
    }

    public MeasurementDetailsDTO findMeasurementById(UUID id) {
        Optional<Measurement> prosumerOptional = measurementRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Measurement with id {} was not found in db", id);
            throw new ResourceNotFoundException(Measurement.class.getSimpleName() + " with id: " + id);
        }
        return MeasurementBuilder.toMeasurementDetailsDTO(prosumerOptional.get());
    }

    public List<MeasurementDetailsDTO> findAllMeasurementDetails() {
        List<Measurement> deviceList = measurementRepository.findAll();
        return deviceList.stream()
                .map(MeasurementBuilder::toMeasurementDetailsDTO)
                .collect(Collectors.toList());
    }

    public UUID insert(MeasurementDetailsDTO measurementDTO) {

        Measurement measurement = MeasurementBuilder.toEntity(measurementDTO);
        LOGGER.info("SERVICE: 1 Received MeasurementDetailsDTO with device_id: {}", measurement.getDevice_id());

        measurement = measurementRepository.save(measurement);

        LOGGER.debug("SERVICE: 2 Measurement with id {} was inserted in db", measurement.getId());
        return measurement.getId();
    }

    public void deleteAllMeasurements() {
        try {
            LOGGER.info("SERVICE: Deleting all measurements from the database.");
            measurementRepository.deleteAll();
            LOGGER.debug("SERVICE: All measurements were successfully deleted.");
        } catch (Exception e) {
            LOGGER.error("SERVICE: Failed to delete all measurements.", e);
            throw new RuntimeException("Failed to delete all measurements", e);
        }
    }


    public List<MeasurementDetailsDTO> findMeasurementsByDeviceAndDate(UUID deviceId, Date startDate) {
        List<Measurement> measurements = measurementRepository.findByDeviceAndDate(deviceId, startDate);
        return measurements.stream()
                .map(this::convertToMeasurementDetailsDTO)
                .collect(Collectors.toList());
    }

    private MeasurementDetailsDTO convertToMeasurementDetailsDTO(Measurement measurement) {
        return new MeasurementDetailsDTO(measurement.getDevice_id(), measurement.getConsum(), measurement.getTimestamp());
    }

    public int getMhecByDeviceId(UUID deviceId) {
        return deviceReferenceService.getMhecByDeviceId(deviceId);
    }

    //new added

    public MeasurementDetailsDTO getMeasurementByDeviceIdAndTimestamp(UUID deviceId, Date timestamp) {
        Optional<Measurement> measurementOpt = measurementRepository.findByDeviceIdAndTimestamp(deviceId, timestamp);

        if (measurementOpt.isPresent()) {
            Measurement measurement = measurementOpt.get();
            return new MeasurementDetailsDTO(measurement.getDevice_id(), measurement.getConsum(), measurement.getTimestamp());
        } else {
            return null;
        }
    }

    public void updateMeasurement(MeasurementDetailsDTO measurementDTO) {
        Measurement measurement = new Measurement();
        measurement.setDevice_id(measurementDTO.getDevice_id());
        measurement.setConsum(measurementDTO.getConsum());
        measurement.setTimestamp(measurementDTO.getTimestamp());

        measurementRepository.save(measurement);
    }

    public void deleteMeasurementByDeviceIdAndTimestamp(UUID deviceId, Date normalizedTimestamp) {
        measurementRepository.deleteMeasurementByDeviceIdAndTimestamp(deviceId, normalizedTimestamp);
    }

//    public Optional<String> getUserEmailByDeviceId(UUID deviceId) {
//
//        return DeviceReferenceService.getUserEmailByDeviceId(deviceId);
//    }
}
