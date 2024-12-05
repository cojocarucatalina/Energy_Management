package ro.tuc.ds2020;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabbitMQSender {

    private static final String QUEUE_NAME = "dev_q";
    private static final String HOST = "rat-01.rmq2.cloudamqp.com";
    private static final String VIRTUAL_HOST = "qrstdtmh";
    private static final String USERNAME = "qrstdtmh";
    private static final String PASSWORD = "FeknmX6FPAPx62NuSBWmceW2kVJ6t2SE";


    private ConnectionFactory factory;

    public RabbitMQSender() throws NoSuchAlgorithmException, KeyManagementException {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setPort(5671);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);
        factory.useSslProtocol(sslContext);
    }

    public void sendMessage(String operation, UUID deviceId, String userEmail, int mhec) throws Exception {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("operation", operation);
            messageMap.put("deviceId", deviceId.toString());
            messageMap.put("userEmail", userEmail);
            messageMap.put("mhec", mhec);

            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(messageMap);

            channel.basicPublish("", QUEUE_NAME, null, messageJson.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + messageJson + "'");
        }
    }

    public void sendMessage(String operation, UUID deviceId) throws Exception {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("operation", operation);
            messageMap.put("deviceId", deviceId.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(messageMap);

            channel.basicPublish("", QUEUE_NAME, null, messageJson.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + messageJson + "'");
        }
    }
}
