package CTLWorkshop.master;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {
    @GetMapping("")
    public String viewHomePage(Model model) {
        model.addAttribute("attendee", new Attendee());
        return "index";
    }
    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        model.addAttribute("workshop", new Workshop());
        return "admin";
    }
}
