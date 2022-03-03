package CTLWorkshop.master;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
    @GetMapping("/admin")
    public String viewAdminPage() {
        return "admin";
    }
}
