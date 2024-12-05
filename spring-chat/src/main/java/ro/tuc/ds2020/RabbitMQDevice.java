package ro.tuc.ds2020;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.DeviceReferenceDetailsDTO;
import ro.tuc.ds2020.services.DeviceReferenceService;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Component
public class RabbitMQDevice {

    private static final String QUEUE_NAME = "dev_q";
    private static final String HOST = "rat-01.rmq2.cloudamqp.com";
    private static final String VIRTUAL_HOST = "qrstdtmh";
    private static final String USERNAME = "qrstdtmh";
    private static final String PASSWORD = "FeknmX6FPAPx62NuSBWmceW2kVJ6t2SE";

    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;
    private final DeviceReferenceService deviceReferenceService;

    public RabbitMQDevice(DeviceReferenceService deviceReferenceService) throws NoSuchAlgorithmException, KeyManagementException {
        this.deviceReferenceService = deviceReferenceService;
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
            System.out.println(" [queue: "+ QUEUE_NAME+"] Received: " + message + "'");

            try {
                handleMessage(message);
            } catch (Exception e) {
                System.err.println(" [!] Failed to process message: " + e.getMessage());
                e.printStackTrace();
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    private void handleMessage(String message) throws Exception {
        JsonNode rootNode = objectMapper.readTree(message);
        String operation = rootNode.get("operation").asText();
        UUID deviceId = UUID.fromString(rootNode.get("deviceId").asText());

        if ("insert".equalsIgnoreCase(operation)) {
            String userEmail = rootNode.get("userEmail").asText();
            int mhec = rootNode.get("mhec").asInt();
            DeviceReferenceDetailsDTO deviceReferenceDTO = new DeviceReferenceDetailsDTO(deviceId, userEmail, mhec);

            deviceReferenceService.insertDevice(deviceReferenceDTO);
            System.out.println(" [*] Inserted device with ID: " + deviceId);

        } else if ("delete".equalsIgnoreCase(operation)) {

           // System.out.println("[DEBUG LOG: 1] RABBITMQDEVICE"+ deviceId);

            deviceReferenceService.deleteDeviceVoid(deviceId);
            System.out.println(" [*] Deleted device with ID: " + deviceId);

        } else if ("update".equalsIgnoreCase(operation)) {
            String userEmail = rootNode.get("userEmail").asText();
            int mhec = rootNode.get("mhec").asInt();

            DeviceReferenceDetailsDTO deviceReferenceDTO = new DeviceReferenceDetailsDTO(deviceId, userEmail, mhec);
            Optional<DeviceReferenceDetailsDTO> updatedDevice = deviceReferenceService.updateDevice(deviceId, deviceReferenceDTO);

            if (updatedDevice.isEmpty()) {
                System.err.println(" [!] Update failed: Device with ID " + deviceId + " not found.");
            } else {
                System.out.println(" [*] Updated device with ID: " + deviceId);
            }

        }
    }


}
