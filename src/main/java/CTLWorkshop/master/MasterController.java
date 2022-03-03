package CTLWorkshop.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MasterController {
    @Autowired
    private WorkshopRepository workshopRepo;
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
    @PostMapping("/adminsubmitted")
    public String viewSubmittedAdminPage(@ModelAttribute("workshop") Workshop workshop){
        workshopRepo.save(workshop);
        return "admin_submitted";
    }
}
