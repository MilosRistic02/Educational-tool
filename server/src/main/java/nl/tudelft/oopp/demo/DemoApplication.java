package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.logger.FileLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        FileLogger.init();
        SpringApplication.run(DemoApplication.class, args);
    }

}
