package unibo.webgui.wasteservicegui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebSocketController {

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

    @Value("${service.ip}")
    private String ip;

    @Value("${service.port}")
    private String port;

    @GetMapping("/")
    public String getLogin() {
        return "login";
    }

    public void initializeDashboard(Model viewmodel, String ip, String port) {
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
        viewmodel.addAttribute("ip", ip);
        viewmodel.addAttribute("port", port);
    }


    @PostMapping("/dashboard")
    public String update(Model viewmodel, @RequestParam String ip, @RequestParam String port) {
        initializeDashboard(viewmodel, ip, port);
        this.ip = ip;
        this.port = port;
        // fai qualcosa con ip e porta

        // COAP, potrebbe servirci più tardi, solo ip
        // UtilsGUI.connectWithUtilsUsingCoap(ipaddr).observeResource(new UtilsCoapObserver());
        // Oppure con la porta
        //UtilsGUI.connectWithUtilsUsingTcp(ipaddr, port).observeResource(new UtilsTcpObserver());
        //UtilsGUI.sendMsg();
        //return buildTheUpdatePage(viewmodel);

        // Restituisci la view (dashboard.html)
        return "dashboard";
    }
}