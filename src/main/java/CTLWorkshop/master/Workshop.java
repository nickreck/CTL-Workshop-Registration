package CTLWorkshop.master;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "workshops")
public class Workshop
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(nullable = false)
    private String workShopName;
    @Column(nullable = false)
    private String workShopDate;
    @Column(nullable = false)
    private String workShopTime;
    @Column(nullable = false)
    private String workShopLocation;
}