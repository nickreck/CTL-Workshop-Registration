package CTLWorkshop.master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
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
    private List<Attendee> emailList;

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
        attendee.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", "nicholas.reck@bobcats.gcsu.edu", "Email testing", "Sorry Nick Forgot to Change the address");
        return "registration_submitted";
    }

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        model.addAttribute("workshop", new Workshop());
        return "admin";
    }

    @PostMapping("/adminsubmitted")
    public String viewSubmittedAdminPage(@ModelAttribute("workshop") Workshop workshop) {
        Schedule schedule = new Schedule();
        workshopRepo.save(workshop);
        emailList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        schedule.start();
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
        if (!attendee.getDepartment().equals("0")) {
            for (int i = 0; i < dynamicList.size(); i++) {
                if (!(dynamicList.get(i).getDepartment().equalsIgnoreCase(attendee.getDepartment()))) {
                    dynamicList.remove(i);
                }
            }
        }
        model.addAttribute("attendance", dynamicList);
        AttendeeCheckListWrapper checklist = new AttendeeCheckListWrapper();
        for (int i = 0; i < dynamicList.size(); i++) {
            checklist.addAttendee(dynamicList.get(i));
            if(checklist.getChecklist().get(i).getAttendance() == null)
                checklist.getChecklist().get(i).setAttendance("Unmarked");
        }
        model.addAttribute("checklist", checklist);
        return "attendancedisplay";
    }

    @GetMapping("/editattendance")
    public String viewEditAttendance(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("workshoplist", list);
        model.addAttribute("workshop", new Workshop());
        return "editattendance";
    }

    @PostMapping("/attendancesubmission")
    public String viewAttendanceSubmission(@ModelAttribute("workshop") Workshop workshop, Model model) {
        if (workshop.getWorkshopnum() == 0)
            dynamicList = attendeeRepo.findAll();
        else
            dynamicList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        model.addAttribute("attendance", dynamicList);
        AttendeeCheckListWrapper checklist = new AttendeeCheckListWrapper();
        for (int i = 0; i < dynamicList.size(); i++) {
            checklist.addAttendee(dynamicList.get(i));
        }
        model.addAttribute("checklist", checklist);
        return "attendancesubmission";
    }

    @PostMapping("/attendancesubmitted")
    public String viewAttendanceSubmission(@ModelAttribute("checklist") AttendeeCheckListWrapper checklist, Model model) {
        List<Attendee> tempList = checklist.getChecklist();
        for (int i = 0; i < tempList.size(); i++) {
            attendeeRepo.delete(dynamicList.get(i));
            dynamicList.get(i).setAttendance(tempList.get(i).getAttendance());
            if (dynamicList.get(i).getAttendance() == null)
                dynamicList.get(i).setAttendance("No");
            attendeeRepo.save(dynamicList.get(i));
        }
        return "attendancesubmitted";
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

    @GetMapping("/deleteworkshop")
    public String viewDeletePage(Model model) {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("workshop", new Workshop());
        return "deleteworkshop";
    }

    @PostMapping("/deleteworkshop_submitted")
    public String deleteworkshop_submitted(@ModelAttribute("workshop") Workshop workshop) {
        List<Workshop> list = workshopRepo.findByWorkshopnum(workshop.getWorkshopnum());
        workshopRepo.delete(list.get(0));
        return "deleteworkshop_submitted";
    }

    public class Schedule extends Thread{
        public void run(){
                List<Workshop> w = workshopRepo.findAll();
                String strDate = w.get(w.size() - 1).getWorkshopdate();
                LocalDate date = LocalDate.parse(strDate);
                int day = date.getDayOfMonth();
                Month month = date.getMonth();
                int year = date.getYear();

                long currentMillis = System.currentTimeMillis();
                long givenDateMillis = LocalDateTime.of(year, month, day, 16, 22, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                try {
                    Thread.sleep(givenDateMillis - currentMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Attendee a : emailList) {
                a.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", a.getId(), "Email testing", "doing some thread testing");
            }
        }
    }
}
