package CTLWorkshop.master;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AttendeeRepositoryTest {
    @Autowired
    private AttendeeRepository repo;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testAttendeeCreation()
    {
        Attendee attendee = new Attendee();
        attendee.setId("nick@gmail.com");
        attendee.setFirstname("Nick");
        attendee.setLastname("Reck");
        attendee.setDepartment("Computer Science");
        attendee.setCollege("Business");
        attendee.setPosition("Student");
        attendee.setPhonenumber("6784630569");
        attendee.setWorkshopdate("2/25/22");
        attendee.setWorkshopname("Learning to teach");

        Attendee saved = repo.save(attendee);
        Attendee exists = entityManager.find(Attendee.class, saved.getId());
        assertThat(exists.getId()).isEqualTo(attendee.getId());
    }
}
