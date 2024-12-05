package ro.tuc.ds2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;

import java.util.TimeZone;

@SpringBootApplication
@Validated
public class Ds2020Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

//    public static void main(String[] args) {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        SpringApplication.run(Ds2020Application.class, args);
//        try {
//            RabbitMQReceiver receiver = new RabbitMQReceiver();
//            receiver.startListening();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    ApplicationContext context = SpringApplication.run(Ds2020Application.class, args);

    new Thread(() -> {
        try {
            RabbitMQDevice receiver = context.getBean(RabbitMQDevice.class);
            receiver.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
    new Thread(() -> {
        try {
            RabbitMQMeasure receiver = context.getBean(RabbitMQMeasure.class);
            receiver.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
}
}
