package ro.tuc.ds2020.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.MeasurementRepository;
import ro.tuc.ds2020.services.MeasurementService;
import org.slf4j.Logger;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurement")
public class MeasurementController {

    @Autowired
    private final MeasurementService measurementService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final MeasurementRepository measurementRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public MeasurementController(MeasurementService MeasurementService, MeasurementRepository MeasurementRepository, RestTemplate restTemplate) {
        this.measurementService = MeasurementService;
        this.measurementRepository = MeasurementRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/details")
    public ResponseEntity<List<MeasurementDetailsDTO>> getAllDeviceDetails() {
        List<MeasurementDetailsDTO> deviceDetails = measurementService.findAllMeasurementDetails();
        return new ResponseEntity<>(deviceDetails, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertMeasurement(@Valid @RequestBody MeasurementDetailsDTO measurementDTO) {
        LOGGER.info("Received MeasurementDetailsDTO with device_id: {}", measurementDTO.getDevice_id());

        UUID measurementID = measurementService.insert(measurementDTO);
        return new ResponseEntity<>(measurementID, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllMeasurements() {
        measurementService.deleteAllMeasurements();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/from-date")
    public ResponseEntity<List<MeasurementDetailsDTO>> getMeasurementsFromDateAndDevice(
            @RequestParam("device_id") UUID deviceId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) {

        LOGGER.info("Fetching measurements for device: {} from date: {}", deviceId, startDate);

        List<MeasurementDetailsDTO> measurements = measurementService.findMeasurementsByDeviceAndDate(deviceId, startDate);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }


}

