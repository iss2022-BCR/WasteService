package unibo.wasteservice_gui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.wasteservice_gui.utils.UtilsStatusGUI;

/*
 * StatusController is needed to act as a middleman
 * between the information emitted by the CoapResources
 * and the Client (Browser).
 * That's because Browser doesn't support JavaScript APIs
 * for CoAP for security reasons.
 */
@Controller
public class StatusController {
    @Value("${service.ip}")
    private String ip;
    @Value("${service.port}")
    private String port;

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


    private void setConfigParams(Model viewmodel)
    {
        viewmodel.addAttribute("ip", ip);
        viewmodel.addAttribute("port", port);

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
    }

    public String buildHomePage(Model viewmodel)
    {
        setConfigParams(viewmodel);
        return "home";
    }

    public String buildDashboardPage(Model viewmodel)
    {
        setConfigParams(viewmodel);
        return "dashboard";
    }

    @GetMapping("/")
    public String home(Model viewmodel)
    {
        return buildHomePage(viewmodel);
    }

    @PostMapping("/dashboard")
    public String updateGUI(Model viewmodel, @RequestParam String ip, @RequestParam String port)
    {
        this.ip = ip;
        this.port = port;

        viewmodel.addAttribute("ip", this.ip);
        viewmodel.addAttribute("port", this.port);

        UtilsStatusGUI.connectWithUtilsUsingCoap(this.ip, Integer.parseInt(port)).observeResource(new StatusCoapObserver());
        UtilsStatusGUI.connectWithUtilsUsingTcp(this.ip, Integer.parseInt(port));
        UtilsStatusGUI.sendMsg();

        return buildDashboardPage(viewmodel);
    }
}
