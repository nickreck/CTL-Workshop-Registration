package CTLWorkshop.master;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class MasterController
{
    // Autowired repository to hold objects from our database
    @Autowired
    private WorkshopRepository workshopRepo;
    @Autowired
    private AttendeeRepository attendeeRepo;

    // These lists allow us to maintain objects throughout multiple HTTP routes
    private List<Attendee> dynamicList;
    private List<Attendee> emailList;

    private int historicAttendanceCount = 0;

    @GetMapping("/")
    public String viewHomePage()
    {
        return "home";
    }

    @GetMapping("/registration")
    public String viewRegistrationPage(Model model)
    {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("attendee", new Attendee());
        return "registration";
    }

    @PostMapping("/registrationsubmitted")
    public String viewSubmittedRegistrationPage(@ModelAttribute("attendee") Attendee attendee)
    {
        historicAttendanceCount++;
        attendee.setId(String.valueOf(historicAttendanceCount));
        attendeeRepo.save(attendee);
        List<Workshop> listOfWorkshops = workshopRepo.findByWorkshopnum(attendee.getWorkshopnum());
        Workshop workshop = listOfWorkshops.get(0);
        attendee.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", attendee.getEmail(), "CTL Workshop Registration", "Thank you " + attendee.getFirstname() + " for registering for an upcoming workshop: " + workshop.getId() + ". A reminder email will show up the morning of the workshop as well as a survey the day following the workshop.");
        return "redirect:registration?success";
    }

    @GetMapping("/admin")
    public String viewAdminPage(Model model)
    {
        model.addAttribute("workshop", new Workshop());
        return "admin";
    }

    @PostMapping("/adminsubmitted")
    public String viewSubmittedAdminPage(@ModelAttribute("workshop") Workshop workshop)
    {
        Schedule schedule = new Schedule();
        workshopRepo.save(workshop);
        emailList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        schedule.start();
        return "redirect:?workshopcreatedsuccess";
    }

    @GetMapping("/adminLogin")
    public String viewLoginPage(Model model)
    {
        return "adminLogin";
    }

    // The following methods all work together to slowly narrow down as to what attendees the user wants to see
    @GetMapping("/attendance")
    public String viewAttendance(Model model)
    {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("workshoplist", list);
        model.addAttribute("workshop", new Workshop());
        return "attendance";
    }

    @PostMapping("/attendancecollege")
    public String viewAttendanceCollege(@ModelAttribute("workshop") Workshop workshop, Model model)
    {
        if (workshop.getWorkshopnum() == 0)
            dynamicList = attendeeRepo.findAll();
        else
            dynamicList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        List<String> colleges = new ArrayList<>();
        boolean val = false;
        for (int i = 0; i < dynamicList.size(); i++)
        {
            for (int j = 0; j < colleges.size(); j++)
            {
                if (dynamicList.get(i).getCollege().equalsIgnoreCase(colleges.get(j)))
                {
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
    public String viewAttendanceDepartment(@ModelAttribute("attendee") Attendee attendee, Model model)
    {
        List<String> departments = new ArrayList<>();
        boolean val = false;
        if (!attendee.getCollege().equals("0"))
        {
            for (int i = 0; i < dynamicList.size(); i++)
            {
                if (!(dynamicList.get(i).getCollege().equalsIgnoreCase(attendee.getCollege())))
                {
                    dynamicList.remove(i);
                }
            }
        }
        for (int i = 0; i < dynamicList.size(); i++)
        {
            for (int j = 0; j < departments.size(); j++)
            {
                if (dynamicList.get(i).getDepartment().equalsIgnoreCase(departments.get(j)))
                {
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
        if (!attendee.getDepartment().equals("0"))
        {
            for (int i = 0; i < dynamicList.size(); i++)
            {
                if (!(dynamicList.get(i).getDepartment().equalsIgnoreCase(attendee.getDepartment())))
                {
                    dynamicList.remove(i);
                }
            }
        }
        model.addAttribute("attendance", dynamicList);
        AttendeeCheckListWrapper checklist = new AttendeeCheckListWrapper();
        for (int i = 0; i < dynamicList.size(); i++)
        {
            checklist.addAttendee(dynamicList.get(i));
            if(checklist.getChecklist().get(i).getAttendance() == null)
                checklist.getChecklist().get(i).setAttendance("Unmarked");
        }
        model.addAttribute("checklist", checklist);
        return "attendancedisplay";
    }

    @PostMapping("/attendanceexport")
    public String viewExportAttendance(Model model) throws Exception
    {
        Workbook book = new Workbook();
        Worksheet sheet = book.getWorksheets().get(0);
        sheet.getCells().importCustomObjects((Collection) dynamicList, new String[] { "Email", "Firstname", "Lastname", "Department", "College", "Position", "Workshopnum", "Attendance"}, true,0,0, dynamicList.size(), true, null, false);
        String homeFolder = System.getProperty("user.home");
        book.save(homeFolder + "/Desktop/Output.xlsx");
        return "redirect:?attendanceexportsuccess";
    }

    @GetMapping("/editattendance")
    public String viewEditAttendance(Model model)
    {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("workshoplist", list);
        model.addAttribute("workshop", new Workshop());
        return "editattendance";
    }

    @PostMapping("/attendancesubmission")
    public String viewAttendanceSubmission(@ModelAttribute("workshop") Workshop workshop, Model model)
    {
        if (workshop.getWorkshopnum() == 0)
            dynamicList = attendeeRepo.findAll();
        else
            dynamicList = attendeeRepo.findByWorkshopnum(workshop.getWorkshopnum());
        model.addAttribute("attendance", dynamicList);
        AttendeeCheckListWrapper checklist = new AttendeeCheckListWrapper();
        for (int i = 0; i < dynamicList.size(); i++)
        {
            checklist.addAttendee(dynamicList.get(i));
        }
        model.addAttribute("checklist", checklist);
        return "attendancesubmission";
    }

    @PostMapping("/attendancesubmitted")
    public String viewAttendanceSubmission(@ModelAttribute("checklist") AttendeeCheckListWrapper checklist, Model model)
    {
        List<Attendee> tempList = checklist.getChecklist();
        for (int i = 0; i < tempList.size(); i++)
        {
            attendeeRepo.delete(dynamicList.get(i));
            dynamicList.get(i).setAttendance(tempList.get(i).getAttendance());
            if (dynamicList.get(i).getAttendance() == null)
                dynamicList.get(i).setAttendance("No");
            attendeeRepo.save(dynamicList.get(i));
        }
        return "redirect:?attendancesubmittedsuccess";
    }

    @GetMapping("/workshopedit")
    public String viewEditPage(Model model)
    {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("workshop", new Workshop());
        return "workshopedit";
    }

    @PostMapping("/workshopedit_submitted")
    public String workshopedit_submitted(@ModelAttribute("workshop") Workshop workshop)
    {
        List<Workshop> list = workshopRepo.findByWorkshopnum(workshop.getWorkshopnum());
        workshopRepo.delete(list.get(0));
        workshopRepo.save(workshop);
        return "redirect:?workshopeditsuccess";
    }

    @GetMapping("/deleteworkshop")
    public String viewDeletePage(Model model)
    {
        List<Workshop> list = workshopRepo.findAll();
        model.addAttribute("list", list);
        model.addAttribute("workshop", new Workshop());
        return "deleteworkshop";
    }

    @PostMapping("/deleteworkshop_submitted")
    public String deleteworkshop_submitted(@ModelAttribute("workshop") Workshop workshop)
    {
        List<Workshop> list = workshopRepo.findByWorkshopnum(workshop.getWorkshopnum());
        workshopRepo.delete(list.get(0));
        return "redirect:?deletesuccess";
    }
    //email scheduling is done here
    //email code is in Attendee.java
    public class Schedule extends Thread{
        public void run()
        {
                //grabs list of workshops
                List<Workshop> w = workshopRepo.findAll();
                //grabs specific workshop
                Workshop workshop = w.get(w.size()-1);
                //gets workshop number
                int workshopNum = workshop.getWorkshopnum();
                //gets workshop date
                String strDate = workshop.getWorkshopdate();
                //changes string date to local date
                LocalDate date = LocalDate.parse(strDate);
                //gets current time in ms
                long currentMillis = System.currentTimeMillis();
                //gets scheduled date ms
                long givenDateMillis = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 16, 16, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                //gets day after scheduled date in ms
                long givenDateMillis2 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 16, 16, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                //sleeps thread until scheduled date
                try
                {
                    Thread.sleep(givenDateMillis - currentMillis);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //pulls attendee list from database
                emailList = attendeeRepo.findByWorkshopnum(workshopNum);
                //sends out emails
                for (Attendee a : emailList)
                {
                a.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", a.getEmail(), "Workshop reminder", "Thanks again for signing up for today’s workshop, " + workshop.getId() + ". We're looking forward to working with you. As a reminder, the workshop will be taking place at " + workshop.getWorkshoptime() + " today at " + workshop.getWorkshoplocation());
                }
                //thread sleeps again
                try {
                    Thread.sleep(givenDateMillis2 - currentMillis);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //pulls attendee list from database
                emailList = attendeeRepo.findByWorkshopnum(workshopNum);
                //sends out survey emails
                for (Attendee a : emailList)
                {
                    a.send("testingjavaemail36@gmail.com", "Pineapplessuck010!", a.getEmail(), "Workshop Survey", "Thank you for attending our latest workshop! We are glad to have you.  We would like to gather feedback on our session and would like to ask that you complete a survey about the session.  If you would, please take a " +
                            "moment to click on the link below and complete the survey. It will be completely anonymous and we will not be able to track who has completed the form. This will help us as we continue to improve our workshops and offerings at the GCSU Center for Teaching and Learning.\n https://gcsu.co1.qualtrics.com/jfe/form/SV_dj7uuqIEFS0rLTv ");
                }
        }
    }
}
