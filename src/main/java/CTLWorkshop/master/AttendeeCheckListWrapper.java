package CTLWorkshop.master;

import java.util.ArrayList;
import java.util.List;

// This wrapper class is used to pass a list to the HTML form and back to the backend
public class AttendeeCheckListWrapper
{
    private List<Attendee> checklist = new ArrayList<>();

    public void addAttendee(Attendee attendee) {
        checklist.add(attendee);
    }
    public List<Attendee> getChecklist() {
        return checklist;
    }
}
