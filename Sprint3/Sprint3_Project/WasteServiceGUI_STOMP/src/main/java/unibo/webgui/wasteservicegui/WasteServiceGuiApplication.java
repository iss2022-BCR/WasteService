package unibo.webgui.wasteservicegui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WasteServiceGuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WasteServiceGuiApplication.class, args);
    }

}
