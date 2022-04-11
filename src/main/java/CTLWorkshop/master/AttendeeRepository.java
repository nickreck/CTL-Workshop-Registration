package CTLWorkshop.master;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    List<Attendee> findByWorkshopnum(int workshopnum);
}