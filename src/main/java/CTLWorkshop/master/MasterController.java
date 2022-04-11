package CTLWorkshop.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class MasterController {

    @Autowired
    private LoginRepository loginRepo;
    @Autowired
    private WorkshopRepository workshopRepo;
    @Autowired
    private AttendeeRepository attendeeRepo;
    @GetMapping("/home")
    public String viewHomePage() {
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
    @GetMapping("/adminLogin")
    public String viewLoginPage(Model model) {
        model.addAttribute("login", new Login());
        return "adminLogin";
    }
    @PostMapping("/loggedin")
    public String viewLoggedIn(@ModelAttribute("login") Login login) {
        List<Login> list2 = loginRepo.findAll();
        for (Login i:list2) {
            if(login.checkLogin(i.getUsername(),i.getPassword())){
                return "redirect:home";
            }
        }
        return "adminLogin";
    }
    @GetMapping("/attendance")
    public String viewAttendance(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("workshoplist", list);
        model.addAttribute("workshop", new Workshop());
        return "attendance";
    }
    @PostMapping("/attendancesubmitted")
    public String viewAttendanceSubmitted(@ModelAttribute("workshop") Workshop workshop, Model model) {
        List<Attendee> list;
        if(workshop.getWorkshopnum() == 0)
            list = attendeeRepo.findAll();
        else
            list = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        model.addAttribute("attendance", list);
        return "attendance_submitted";
    }
    @GetMapping("/deleteworkshop")
    public String viewDeletePage(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("workshop", new Workshop());
        return "deleteWorkshop";
    }
    @PostMapping("/deleteworkshop_submitted")
    public String deleteworkshop_submitted(@ModelAttribute("workshop") Workshop workshop) {
        List<Workshop> list = workshopRepo.findByWorkshopnum(workshop.getWorkshopnum());
        workshopRepo.delete(list.get(0));
        return "attendance_submitted";
    }

}
