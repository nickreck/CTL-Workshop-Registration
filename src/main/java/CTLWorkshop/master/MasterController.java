package CTLWorkshop.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MasterController {

    @Autowired
    private LoginRepository loginRepo;
    @Autowired
    private WorkshopRepository workshopRepo;
    @Autowired
    private AttendeeRepository attendeeRepo;
    private List<Attendee> dynamicList;

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
        attendee.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", attendee.getId(), "Email testing", "Thank you, " + attendee.getFirstname() + " for signing up for an upcoming workshop.");
        return "registration_submitted";
    }

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        model.addAttribute("workshop", new Workshop());
        return "admin";
    }

    @PostMapping("/adminsubmitted")
    public String viewSubmittedAdminPage(@ModelAttribute("workshop") Workshop workshop) {
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
        for (Login i : list2) {
            if (login.checkLogin(i.getUsername(), i.getPassword())) {
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

    @PostMapping("/attendancecollege")
    public String viewAttendanceCollege(@ModelAttribute("workshop") Workshop workshop, Model model) {
        if (workshop.getWorkshopnum() == 0)
            dynamicList = attendeeRepo.findAll();
        else
            dynamicList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        List<String> colleges = new ArrayList<>();
        boolean val = false;
        for (int i = 0; i < dynamicList.size(); i++) {
            for (int j = 0; j < colleges.size(); j++) {
                if (dynamicList.get(i).getCollege().equalsIgnoreCase(colleges.get(j))) {
                    val = true;
                }
            }
            if (!val)
                colleges.add(dynamicList.get(i).getCollege());
        }
        model.addAttribute("attendance", colleges);
        model.addAttribute("attendee", new Attendee());
        return "attendancecollege";
    }

    @PostMapping("/attendancedepartment")
    public String viewAttendanceDepartment(@ModelAttribute("attendee") Attendee attendee, Model model) {
        List<String> departments = new ArrayList<>();
        boolean val = false;
        if (!attendee.getCollege().equals("0")) {
            for (int i = 0; i < dynamicList.size(); i++) {
                if (!(dynamicList.get(i).getCollege().equalsIgnoreCase(attendee.getCollege()))) {
                    dynamicList.remove(i);
                }
            }
        }
        for (int i = 0; i < dynamicList.size(); i++) {
            for (int j = 0; j < departments.size(); j++) {
                if (dynamicList.get(i).getDepartment().equalsIgnoreCase(departments.get(j))) {
                    val = true;
                }
            }
            if (!val)
                departments.add(dynamicList.get(i).getDepartment());
        }
        model.addAttribute("attendance", departments);
        model.addAttribute("attendee", new Attendee());
        return "attendancedepartment";
    }

    @PostMapping("/attendancedisplay")
    public String viewAttendanceDisplay(@ModelAttribute("attendee") Attendee attendee, Model model) {
        if(!attendee.getDepartment().equals("0")) {
            for (int i = 0; i < dynamicList.size(); i++) {
                if (!(dynamicList.get(i).getDepartment().equalsIgnoreCase(attendee.getDepartment()))) {
                    dynamicList.remove(i);
                }
            }
        }
        model.addAttribute("attendance", dynamicList);
//        model.addAttribute("attendee", new Attendee());
        return "attendancedisplay";
    }

    @GetMapping("/workshopedit")
    public String viewEditPage(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("workshop", new Workshop());
        return "workshopedit";
    }
    @PostMapping("/workshopedit_submitted")
    public String workshopedit_submitted(@ModelAttribute("workshop") Workshop workshop) {
        List<Workshop> list = workshopRepo.findByWorkshopnum(workshop.getWorkshopnum());
        workshopRepo.delete(list.get(0));
        workshopRepo.save(workshop);
        return "workshopedit_submitted";
    }

//    @PostMapping("/attendancesubmission")
//    public String viewAttendanceSubmission(@ModelAttribute("attendee") Attendee attendee, Model model) {
//        model.addAttribute("attendance", dynamicList);
//        return "attendancedisplay";
//    }
}
