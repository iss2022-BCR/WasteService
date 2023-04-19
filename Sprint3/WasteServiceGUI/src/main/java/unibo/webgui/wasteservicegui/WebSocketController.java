package unibo.webgui.wasteservicegui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebSocketController {

    @Value("${webgui.addr}")
    String addr;
    @Value("${container.led_state}")
    String led_state;
    @Value("${container.plastic_current}")
    String plastic_current;
    @Value("${container.plastic_max}")
    String plastic_max;
    @Value("${container.glass_current}")
    String glass_current;
    @Value("${container.glass_max}")
    String glass_max;
    @Value("${container.trolley_state}")
    String trolley_state;
    @Value("${container.trolley_position}")
    String trolley_position;
    @Value("${room.home}")
    int [][]  room_home;
    @Value("${room.indoor}")
    int [][]  room_indoor;
    @Value("${room.plastic}")
    int [][] room_plastic;
    @Value("${room.glass}")
    int [][]  room_glass;

    @GetMapping("/")
    public String getDashboard(Model viewmodel) {
        // Leggi numRows, numCols e altri parametri di configurazione da qualche file
        int numRows = 8;
        int numCols = 10;
        // Aggiungili al modello
        viewmodel.addAttribute("addr", addr);
        viewmodel.addAttribute("led_state", led_state);
        viewmodel.addAttribute("plastic_current", plastic_current);
        viewmodel.addAttribute("plastic_max", plastic_max);
        viewmodel.addAttribute("glass_current", glass_current);
        viewmodel.addAttribute("glass_max", glass_max);
        viewmodel.addAttribute("trolley_state", trolley_state);
        viewmodel.addAttribute("trolley_position", trolley_position);
        viewmodel.addAttribute("room_indoor", room_indoor);
        viewmodel.addAttribute("room_home", room_home);
        viewmodel.addAttribute("room_plastic", room_plastic);
        viewmodel.addAttribute("room_glass", room_glass);
        // Restituisci la view (index.html)
        return "index";
    }


    @PostMapping("/update")
    public String update(Model viewmodel, @RequestParam String ipaddr){
        addr = ipaddr;
        viewmodel.addAttribute("addr", addr);
        //UtilsGUI.connectWithUtilsUsingCoap(ipaddr).observeResource(new UtilsCoapObserver());
        //UtilsGUI.connectWithUtilsUsingTcp(ipaddr);
        //UtilsGUI.sendMsg();
        //return buildTheUpdatePage(viewmodel);
        return "test";
    }

}

class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
class WordCount {
    private String content;
    public WordCount(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}