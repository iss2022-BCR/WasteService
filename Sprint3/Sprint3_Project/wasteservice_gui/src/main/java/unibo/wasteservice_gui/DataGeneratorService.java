package unibo.wasteservice_gui;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class DataGeneratorService {

    private final SimpMessagingTemplate messagingTemplate;

    public DataGeneratorService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Plastic server Mock
    @Scheduled(fixedRate = 5000)
    public void generatePlastic() {
        String msg = "100.0";
        messagingTemplate.convertAndSend("/topic/plastic", msg);
    }

    // Plastic server Mock
    @Scheduled(fixedRate = 5000)
    public void generateGlass() {
        String msg = "100.0";
        messagingTemplate.convertAndSend("/topic/glass", msg);
    }


    // Led server Mock
    // Blink led: change status every 2 seconds from ON to OFF
    @Scheduled(fixedRate = 10000)
    public void changeLed() throws InterruptedException {
        String msg = "ON";
        messagingTemplate.convertAndSend("/topic/led", msg);
        TimeUnit.SECONDS.sleep(5);
        msg = "OFF";
        messagingTemplate.convertAndSend("/topic/led", msg);
    }
    @Scheduled(fixedRate = 10000)
    public void pathExecutor() throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            String msg = "0,"+i;
            messagingTemplate.convertAndSend("/topic/grid", msg);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}

/*    public void generatePlastic() {
        ObjectMapper objectMapper = new ObjectMapper();
        Data data = new Data();
        data.setValue(100.0);
        String json = "";
        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/topic/plastic", json);
    }*/

// Mock class for the data we want to represent
// I know it's redundant, but it's just a mock
/*
class Data {
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
*/
