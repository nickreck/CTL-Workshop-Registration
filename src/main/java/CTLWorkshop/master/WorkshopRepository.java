package CTLWorkshop.master;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, String>
{
    List<Workshop> findByWorkshopnum(int workshopnum);
}
