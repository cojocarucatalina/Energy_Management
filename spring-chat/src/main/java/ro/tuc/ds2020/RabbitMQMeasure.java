package ro.tuc.ds2020;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.MeasurementDetailsDTO;
import ro.tuc.ds2020.entities.DeviceReference;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Message;
import ro.tuc.ds2020.services.DeviceReferenceService;
import ro.tuc.ds2020.services.MeasurementService;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class RabbitMQMeasure {

    private static final String QUEUE_NAME = "measurements";
    private static final String HOST = "rat.rmq2.cloudamqp.com";
    private static final String USERNAME = "qzwqpbut";
    private static final String PASSWORD = "C9czddqHxrh-cLELUuCiiFgesCdbCU-G";
    private static final String VIRTUAL_HOST = "qzwqpbut";

    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;
    private final MeasurementService measurementService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public RabbitMQMeasure(MeasurementService measurementService) throws NoSuchAlgorithmException, KeyManagementException {
        this.measurementService = measurementService;
        this.factory = new ConnectionFactory();
        this.objectMapper = new ObjectMapper();

        factory.setHost(HOST);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setPort(5671);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);
        factory.useSslProtocol(sslContext);
    }

    public void startListening() throws Exception {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println(" [*] Waiting for messages in queue: " + QUEUE_NAME);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("\n[queue: "+ QUEUE_NAME+"] Received: " + message);

            try {
                handleMessage(message);
            } catch (Exception e) {
                System.err.println(" [!] Failed to process message: " + e.getMessage());
                e.printStackTrace();
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

    private Map<Integer, Integer> hourlyConsumptionMap = new HashMap<>();


    private void handleMessage(String message) {
        try {
            System.out.println("Received message: " + message);
            JsonNode rootNode = objectMapper.readTree(message);
            JsonNode valueNode = rootNode.get("measurement_value");
            JsonNode deviceIdNode = rootNode.get("device_id");

            if (valueNode == null || deviceIdNode == null) {
                System.err.println(" [!] Missing 'measurement_value' or 'device_id' in the message: " + message);
                return;
            }

            UUID deviceId = UUID.fromString(deviceIdNode.asText());
            double value = valueNode.asDouble();
            int intValue = (int) value;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date normalizedTimestamp = calendar.getTime();

            MeasurementDetailsDTO existingMeasurement = measurementService.getMeasurementByDeviceIdAndTimestamp(deviceId, normalizedTimestamp);

            if (existingMeasurement != null) {
                int updatedConsumption = existingMeasurement.getConsum() + intValue;

                measurementService.deleteMeasurementByDeviceIdAndTimestamp(deviceId, normalizedTimestamp);
                System.out.println("Deleted existing measurement for device: " + deviceId + " at " + normalizedTimestamp);

                MeasurementDetailsDTO newMeasurement = new MeasurementDetailsDTO(deviceId, updatedConsumption, normalizedTimestamp);
                measurementService.insert(newMeasurement);
                System.out.println("Inserted new measurement for device: " + deviceId + " at " + normalizedTimestamp + " with updated consumption: " + updatedConsumption);

            } else {
                MeasurementDetailsDTO newMeasurement = new MeasurementDetailsDTO(deviceId, intValue, normalizedTimestamp);
                measurementService.insert(newMeasurement);
                System.out.println("Inserted new measurement for device: " + deviceId + " at " + normalizedTimestamp + " with consumption: " + intValue);
            }

            System.out.println("\n[queue: " + QUEUE_NAME + "] \n{\ndeviceId: " + deviceId +"\nvalue: "+ intValue+"\ndata: "+normalizedTimestamp+"\n}");

            int mhec = measurementService.getMhecByDeviceId(deviceId);
//            if (existingMeasurement != null && existingMeasurement.getConsum() > mhec) {
//                Map<String, Object> messageData = new HashMap<>();
//                messageData.put("type", "alert");
//                messageData.put("message", "Consumption exceeds MHEC (" + mhec + ")\nHourly consumption is " + existingMeasurement.getConsum()
//                        +"\nfor device "+existingMeasurement.getDevice_id());
//
//               // Optional<String> userEmail = deviceReferenceService.getUserEmailByDeviceId(deviceId);
//                simpMessagingTemplate.convertAndSend("/topic/notification", messageData);
//               // simpMessagingTemplate.convertAndSendToUser(userEmail, "/queue/notification", messageData);
//
//                System.out.println("EXCEEDED: Hour " + calendar.get(Calendar.HOUR_OF_DAY) + " consumption: " + existingMeasurement.getConsum());
//            }
            if (existingMeasurement != null && existingMeasurement.getConsum() > mhec) {
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("type", "alert");
                messageData.put("message", "Consumption exceeds MHEC (" + mhec + ")\nHourly consumption is " + existingMeasurement.getConsum()
                        +"\nfor device "+existingMeasurement.getDevice_id());

                simpMessagingTemplate.convertAndSend("/topic/notification/"+deviceId, messageData);
                System.out.println("EXCEEDED: Hour " + calendar.get(Calendar.HOUR_OF_DAY) + " consumption: " + existingMeasurement.getConsum());
            }

        } catch (Exception e) {
            System.err.println(" [!] Failed to process message: " + message);
            e.printStackTrace();
        }

        // measurementService.deleteAllMeasurements();
    }


}
