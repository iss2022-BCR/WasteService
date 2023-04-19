package unibo.webgui.wasteservicegui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Scheduled(fixedRate = 5000)
    public void generateData() {
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
    }

    @Scheduled(fixedRate = 2000)
    public void changeLed() throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString("ON");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/topic/led", json);
        TimeUnit.SECONDS.sleep(1);
        try {
            json = objectMapper.writeValueAsString("OFF");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}

// Mock class for the data we want to represent
// I know it's redundant, but it's just a mock
class Data {
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
