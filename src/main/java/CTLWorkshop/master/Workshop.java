package CTLWorkshop.master;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "workshop")
public class Workshop
{
    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String workshopdate;
    @Column(nullable = false)
    private String workshoptime;
    @Column(nullable = false)
    private String workshoplocation;
    @Column
    private int workshopnum;
}