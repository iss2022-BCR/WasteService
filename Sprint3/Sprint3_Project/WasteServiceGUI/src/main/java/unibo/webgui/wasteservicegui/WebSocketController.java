package unibo.webgui.wasteservicegui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebSocketController {

    @Value("${webgui.addr}")
    private String addr;
    @Value("${container.led_state}")
    private String ledState;
    @Value("${container.plastic_current}")
    private String plasticCurrent;
    @Value("${container.plastic_max}")
    private String plasticMax;
    @Value("${container.glass_current}")
    private String glassCurrent;
    @Value("${container.glass_max}")
    private String glassMax;
    @Value("${container.trolley_state}")
    private String trolleyState;
    @Value("${container.trolley_position}")
    private String trolleyPosition;
    @Value("${room.home}")
    private int[] roomHome;
    @Value("${room.indoor}")
    private int[] roomIndoor;
    @Value("${room.plastic}")
    private int[] roomPlastic;
    @Value("${room.glass}")
    private int[] roomGlass;

    @Value("${room.rows}")
    private int numRows;
    @Value("${room.cols}")
    private int numCols;

    @GetMapping("/")
    public String getDashboard(Model viewmodel) {
        // Aggiungili al modello
        viewmodel.addAttribute("addr", addr);
        viewmodel.addAttribute("led_state", ledState);
        viewmodel.addAttribute("plastic_current", plasticCurrent);
        viewmodel.addAttribute("plastic_max", plasticMax);
        viewmodel.addAttribute("glass_current", glassCurrent);
        viewmodel.addAttribute("glass_max", glassMax);
        viewmodel.addAttribute("trolley_state", trolleyState);
        viewmodel.addAttribute("trolley_position", trolleyPosition);
        viewmodel.addAttribute("room_indoor", roomIndoor);
        viewmodel.addAttribute("room_home", roomHome);
        viewmodel.addAttribute("room_plastic", roomPlastic);
        viewmodel.addAttribute("room_glass", roomGlass);
        viewmodel.addAttribute("num_rows", numRows);
        viewmodel.addAttribute("num_cols", numCols);
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