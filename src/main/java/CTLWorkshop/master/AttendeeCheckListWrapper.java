package CTLWorkshop.master;

import java.util.ArrayList;
import java.util.List;

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
