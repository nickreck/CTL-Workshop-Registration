package CTLWorkshop.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MasterController {
    @Autowired
    private WorkshopRepository workshopRepo;
    @Autowired
    private AttendeeRepository attendeeRepo;
    @GetMapping("/home")
    public String viewHomePage(){
        return "home";
    }
    @GetMapping("/registration")
    public String viewRegistrationPage(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("attendee", new Attendee());
        return "registration";
    }
    @PostMapping("/registrationsubmitted")
    public String viewSubmittedRegistrationPage(@ModelAttribute("attendee") Attendee attendee) {
        attendeeRepo.save(attendee);
        return "registration_submitted";
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
