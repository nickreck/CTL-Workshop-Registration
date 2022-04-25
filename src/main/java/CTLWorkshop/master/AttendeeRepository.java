package CTLWorkshop.master;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// Repository is used as to hold our Attendee objects
public interface AttendeeRepository extends JpaRepository<Attendee, String>
{
    List<Attendee> findByWorkshopnum(int workshopnum);
}