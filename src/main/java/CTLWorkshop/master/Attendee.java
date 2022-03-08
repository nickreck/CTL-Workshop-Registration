package CTLWorkshop.master;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "appform")
public class Attendee
{
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String department;
    @Column(nullable = false)
    private String college;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String phonenumber;
    @Column(nullable = false)
    private int workshopnum;
}
